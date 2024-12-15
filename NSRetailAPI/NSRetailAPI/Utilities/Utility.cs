using Newtonsoft.Json;
using System.Data;
using System.Security.Cryptography;
using System.Text;
using System.Xml;

namespace NSRetailAPI.Utilities
{
    public static class Utility
    {
        private static byte[] Encrypt(byte[] input)
        {
            PasswordDeriveBytes pdb = new PasswordDeriveBytes("NSoftSol", new byte[] { 0x43, 0x87, 0x23, 0x72, 0x45, 0x56, 0x68, 0x14, 0x62, 0x84 });
            MemoryStream ms = new MemoryStream();
            Aes aes = new AesManaged();
            aes.Key = pdb.GetBytes(aes.KeySize / 8);
            aes.IV = pdb.GetBytes(aes.BlockSize / 8);
            CryptoStream cs = new CryptoStream(ms, aes.CreateEncryptor(), CryptoStreamMode.Write);
            cs.Write(input, 0, input.Length);
            cs.Close();
            return ms.ToArray();
        }
        private static byte[] Decrypt(byte[] input)
        {
            PasswordDeriveBytes pdb = new PasswordDeriveBytes("NSoftSol", new byte[] { 0x43, 0x87, 0x23, 0x72, 0x45, 0x56, 0x68, 0x14, 0x62, 0x84 });
            MemoryStream ms = new MemoryStream();
            Aes aes = new AesManaged();
            aes.Key = pdb.GetBytes(aes.KeySize / 8);
            aes.IV = pdb.GetBytes(aes.BlockSize / 8);
            CryptoStream cs = new CryptoStream(ms, aes.CreateDecryptor(), CryptoStreamMode.Write);
            cs.Write(input, 0, input.Length);
            cs.Close();
            return ms.ToArray();
        }
        public static string Encrypt(string input)
        {
            return Convert.ToBase64String(Encrypt(Encoding.UTF8.GetBytes(input)));
        }
        public static string Decrypt(string input)
        {
            return Encoding.UTF8.GetString(Decrypt(Convert.FromBase64String(input)));
        }

        public const string Path_StockCountingDetail = "D:\\Telemetry\\StockCounting\\{0}\\SaveCountingDetail.txt"; 
        public const string Action_StockCounting_SaveStockCountingDetail = "SaveStockCountingDetail";        

        public const string Path_SQLConn = "D:\\Telemetry\\SQLConn.txt";
        public const string Action_SQLConn_WHConn = "WHConn";
        public const string Action_SQLConn_CloudConn = "CloudConn";

        public static void LogTelemetry(string path, string action, params object[] parameters)
        {
            string dir = Path.GetDirectoryName(path);
            if (!Directory.Exists(dir))
            {
                _ = Directory.CreateDirectory(dir);
            }

            if(!File.Exists(path)) { File.Create(path); }
            while (!IsFileReady(path)) { }

            string message = $"{DateTime.Now:yyyy-MM-dd hh:mm:ss tt} : {action} , {string.Join(",",  parameters)} {Environment.NewLine}";
            File.AppendAllText(path, message);
        }

        private static bool IsFileReady(string filename)
        {
            // If the file can be opened for exclusive access it means that the file
            // is no longer locked by another process.
            try
            {
                using (FileStream inputStream = File.Open(filename, FileMode.Open, FileAccess.Read, FileShare.None))
                    return inputStream.Length >= 0;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public static string GetJsonString(DataSet ds, Dictionary<string, string>? columnNames, bool multilevel = true)
        {
            try
            {

                if (ds.Tables.Count == 1 && ds.Tables[0].Rows.Count == 1 
                    && ds.Tables[0].Columns.Count == 1 
                    && columnNames != null 
                    && columnNames.Any() && multilevel)
                {
                    // this means it is a validation message
                    throw new Exception(ds.Tables[0].Rows[0][0].ToString());
                }

                ds.DataSetName = "Holder";
                if (columnNames != null)
                {
                    int i = 0;
                    foreach (var columnName in columnNames)
                    {
                        if (multilevel)
                        {
                            ds.Relations.Add(ds.Tables[i].Columns[columnName.Key], ds.Tables[i + 1].Columns[columnName.Value]);
                            ds.Tables[i + 1].TableName = ds.Tables[i + 1].TableName + "List";
                        }
                        else
                        {
                            for (int j = 0; j < ds.Tables.Count - 1; j++)
                            {
                                ds.Relations.Add(ds.Tables[0].Columns[columnName.Key], ds.Tables[j + 1].Columns[columnName.Value]);
                                ds.Tables[j + 1].TableName = ds.Tables[j + 1].TableName + "List";
                            }
                        }
                        ds.Relations[i].Nested = true;
                        i++;
                    }
                }

                StringWriter sw = new StringWriter();
                ds.WriteXml(sw);
                string xmlString = sw.ToString();

                for (int i = 1; i < ds.Tables.Count; i++)
                {
                    xmlString = xmlString.Replace($"<{ds.Tables[i].TableName}>", $"<{ds.Tables[i].TableName} xmlns:json=\"http://james.newtonking.com/projects/json\" json:Array=\"true\">");
                }

                XmlDocument doc = new XmlDocument();
                doc.LoadXml(xmlString);

                return JsonConvert.SerializeXmlNode(doc, Newtonsoft.Json.Formatting.Indented);
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}
