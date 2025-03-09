using Newtonsoft.Json;
using NSRetailAPI.Models;
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

        //public const string Path_StockCountingDetail = "D:\\Telemetry\\StockCounting\\{0}\\SaveCountingDetail.txt"; 
        //public const string Action_StockCounting_SaveStockCountingDetail = "SaveStockCountingDetail";        

        //public const string Path_SQLConn = "D:\\Telemetry\\SQLConn.txt";
        //public const string Action_SQLConn_WHConn = "WHConn";
        //public const string Action_SQLConn_CloudConn = "CloudConn";

        //public static void LogTelemetry(string path, string action, params object[] parameters)
        //{
        //    string dir = Path.GetDirectoryName(path);
        //    if (!Directory.Exists(dir))
        //    {
        //        _ = Directory.CreateDirectory(dir);
        //    }

        //    if(!File.Exists(path)) { File.Create(path); }
        //    while (!IsFileReady(path)) { }

        //    string message = $"{DateTime.Now:yyyy-MM-dd hh:mm:ss tt} : {action} , {string.Join(",",  parameters)} {Environment.NewLine}";
        //    File.AppendAllText(path, message);
        //}

        //private static bool IsFileReady(string filename)
        //{
        //    // If the file can be opened for exclusive access it means that the file
        //    // is no longer locked by another process.
        //    try
        //    {
        //        using (FileStream inputStream = File.Open(filename, FileMode.Open, FileAccess.Read, FileShare.None))
        //            return inputStream.Length >= 0;
        //    }
        //    catch (Exception)
        //    {
        //        return false;
        //    }
        //}

        public static string GetJsonString(DataSet ds, Dictionary<string, string>? columnNames, bool multilevel = true)
        {
            try
            {
                ds.DataSetName = "Holder";
                if (columnNames != null)
                {
                    int i = 0;
                    foreach (var columnName in columnNames)
                    {
                        if (multilevel)
                        {
                            DataRelation dataRelation = ds.Relations.Add(ds.Tables[i].Columns[columnName.Key], ds.Tables[i + 1].Columns[columnName.Value]);
                            ds.Tables[i + 1].TableName = ds.Tables[i + 1].TableName + "List";
                            dataRelation.Nested = true;
                        }
                        else
                        {
                            for (int j = 0; j < ds.Tables.Count - 1; j++)
                            {
                                DataRelation dataRelation = ds.Relations.Add(ds.Tables[0].Columns[columnName.Key], ds.Tables[j + 1].Columns[columnName.Value]);
                                ds.Tables[j + 1].TableName = ds.Tables[j + 1].TableName + "List";
                                dataRelation.Nested = true;
                            }
                        }
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

        public static string GetJsonString(DataSet ds)
        {
            try
            {
                ds.DataSetName = "Holder";
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

        public static string GetJsonFromClassObject(DataSet ds)
        {
            RootClass rootClass = new RootClass();
            rootClass.Holder = new HolderClass();
            rootClass.Holder.dayClosure = new DayClosure();
            rootClass.Holder.dayClosure.DayClosureID = Convert.ToInt32(ds.Tables[0].Rows[0]["DAYCLOSUREID"]);
            rootClass.Holder.dayClosure.ClosureDate = Convert.ToDateTime(ds.Tables[0].Rows[0]["CLOSUREDATE"]);
            rootClass.Holder.dayClosure.OpeningBalance = Convert.ToDecimal(ds.Tables[0].Rows[0]["OPENINGBALANCE"]);
            rootClass.Holder.dayClosure.ClosingBalance = Convert.ToDecimal(ds.Tables[0].Rows[0]["CLOSINGBALANCE"]);
            rootClass.Holder.dayClosure.ClosingDifference = Convert.ToDecimal(ds.Tables[0].Rows[0]["CLOSINGDIFFERENCE"]);
            rootClass.Holder.dayClosure.ClosedBy = Convert.ToInt32(ds.Tables[0].Rows[0]["CLOSEDBY"]);
            rootClass.Holder.dayClosure.RefundAmount = Convert.ToDecimal(ds.Tables[0].Rows[0]["REFUNDAMOUNT"]);
            rootClass.Holder.dayClosure.CreatedDate = Convert.ToDateTime(ds.Tables[0].Rows[0]["CREATEDDATE"]);
            rootClass.Holder.dayClosure.CompletedBills = Convert.ToInt32(ds.Tables[0].Rows[0]["COMPLETEDBILLS"]);
            rootClass.Holder.dayClosure.DraftBills = Convert.ToInt32(ds.Tables[0].Rows[0]["DRAFTBILLS"]);
            rootClass.Holder.dayClosure.VoidItems = Convert.ToInt32(ds.Tables[0].Rows[0]["VOIDITEMS"]);
            rootClass.Holder.dayClosure.Address = Convert.ToString(ds.Tables[0].Rows[0]["Address"]);
            rootClass.Holder.dayClosure.PhoneNo = Convert.ToString(ds.Tables[0].Rows[0]["PhoneNo"]);
            rootClass.Holder.dayClosure.BranchName = Convert.ToString(ds.Tables[0].Rows[0]["BranchName"]);
            rootClass.Holder.dayClosure.CounterName = Convert.ToString(ds.Tables[0].Rows[0]["CounterName"]);
            rootClass.Holder.dayClosure.UserName = Convert.ToString(ds.Tables[0].Rows[0]["UserName"]);

            rootClass.Holder.dayClosure.DenominationsList = new List<Denomination>();
            foreach (DataRow row in ds.Tables[1].Rows)
            {
                rootClass.Holder.dayClosure.DenominationsList.Add(new Denomination()
                {
                    DenominationId = Convert.ToInt32(row["DENOMINATIONID"]),
                    DisplayValue = Convert.ToString(row["DISPLAYVALUE"]),
                    ClosureValue = Convert.ToDecimal(row["CLOSUREVALUE"]),
                    Multiplier = Convert.ToDecimal(row["MULTIPLIER"])
                });
            }

            rootClass.Holder.dayClosure.MopValuesList = new List<MOP>();

            foreach (DataRow row in ds.Tables[2].Rows)
            {
                rootClass.Holder.dayClosure.MopValuesList.Add(new MOP()
                {
                    MOPId = Convert.ToInt32(row["MOPID"]),
                    MOPName = Convert.ToString(row["MOPNAME"]),
                    MOPValue = Convert.ToDecimal(row["MOPVALUE"])
                });
            }

            rootClass.Holder.dayClosure.UserWiseMopBreakDownList = new List<UserMOPBreakDown>();

            foreach (DataRow row in ds.Tables[3].Rows)
            {
                rootClass.Holder.dayClosure.UserWiseMopBreakDownList.Add(new UserMOPBreakDown()
                {
                    UserName = Convert.ToString(row["USERNAME"]),
                    MopName = Convert.ToString(row["MOPNAME"]),
                    MopValue = Convert.ToDecimal(row["MOPVALUE"])
                });
            }
            return JsonConvert.SerializeObject(rootClass);

        }
    }
}
