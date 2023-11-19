using System.Security.Cryptography;
using System.Text;

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
    }
}
