﻿using System.Security.Cryptography;
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

        public const string Path_StockCounting = "D:\\Telemetry\\SaveCountingDetail.txt"; 
        public const string Action_StockCounting_SaveStockCountingDetail = "SaveStockCountingDetail";

        public const string Path_SQLConn = "D:\\Telemetry\\SQLConn.txt";
        public const string Action_SQLConn_WHConn = "WHConn";
        public const string Action_SQLConn_CloudConn = "CloudConn";

        public static void LogTelemetry(string path, string action, params object[] parameters)
        {
            string message = $"{DateTime.Now:yyyy-MM-dd hh:mm:ss tt} : {action} , {string.Join(",",  parameters)} {Environment.NewLine}";
            File.AppendAllText(path, message);
        }
    }
}
