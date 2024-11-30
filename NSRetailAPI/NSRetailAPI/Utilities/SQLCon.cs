using System.Data.SqlClient;
using System.Data;
using System.Xml.Schema;

namespace NSRetailAPI.Utilities
{
    public class SQLCon
    {
        static int noOfCloudConns, noOfWHConns;

        public static SqlConnection SqlCloudconn(IConfiguration configuration)
        {
            SqlConnection ObjCloudCon = new SqlConnection();
            try
            {

                string stwesar1sdfda = Utility.Decrypt(configuration.GetConnectionString("njklgdfrrfddsger").ToString());
                string stdvcxz2q2w3s = Utility.Decrypt(configuration.GetConnectionString("kliufgfhbcvbfvdd").ToString());
                string ssfdatr3sdabd = Utility.Decrypt(configuration.GetConnectionString("blyudsssdfdgfdsa").ToString());
                string stretwr4awqas = Utility.Decrypt(configuration.GetConnectionString("vcxbvcxhgtrysfgd").ToString());

                string njkfgrrtdd = $"Data Source = {stwesar1sdfda}; Initial Catalog = {stdvcxz2q2w3s}; User Id = {ssfdatr3sdabd}; Password = {stretwr4awqas}; Pooling = True; Connect Timeout = 5; Max Pool Size = 2000;MultipleActiveResultSets=true;";

                ObjCloudCon.ConnectionString = njkfgrrtdd;
                ObjCloudCon.Open();
                ObjCloudCon.Disposed += ObjCloudCon_Disposed;
                noOfCloudConns++;
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_CloudConn, noOfCloudConns, "Success");
            }
            catch (Exception ex) 
            {
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_CloudConn, noOfCloudConns, ex.Message);
                throw ex;
            }
            return ObjCloudCon;
        }

        public static SqlConnection SqlCloudTestconn(IConfiguration configuration)
        {
            SqlConnection ObjCloudCon = new SqlConnection();
            try
            {

                string stwesar1sdfda = Utility.Decrypt(configuration.GetConnectionString("f7ea6ebe-d717-4cc6-b45d-410e5f4c13ff").ToString());
                string stdvcxz2q2w3s = Utility.Decrypt(configuration.GetConnectionString("0f7d60c8-b011-422d-a1f7-8fed8eaad5c8").ToString());
                string ssfdatr3sdabd = Utility.Decrypt(configuration.GetConnectionString("bd62557d-d076-4c99-aecd-01f7e0c9990d").ToString());
                string stretwr4awqas = Utility.Decrypt(configuration.GetConnectionString("73a09af2-858d-4b7f-a463-0585ebdeb650").ToString());

                string njkfgrrtdd = $"Data Source = {stwesar1sdfda}; Initial Catalog = {stdvcxz2q2w3s}; User Id = {ssfdatr3sdabd}; Password = {stretwr4awqas}; Pooling = True; Connect Timeout = 5; Max Pool Size = 2000;MultipleActiveResultSets=true;";

                ObjCloudCon.ConnectionString = njkfgrrtdd;
                ObjCloudCon.Open();
                ObjCloudCon.Disposed += ObjCloudCon_Disposed;
                noOfCloudConns++;
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_CloudConn, noOfCloudConns, "Success");
            }
            catch (Exception ex)
            {
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_CloudConn, noOfCloudConns, ex.Message);
                throw ex;
            }
            return ObjCloudCon;
        }

        public static SqlConnection SqlWHconn(IConfiguration configuration)
        {
            SqlConnection ObjWHCon = new SqlConnection();
            try
            {
                string stwegfsdasar1sdfda = Utility.Decrypt(configuration.GetConnectionString("hlkjuuslrfdtregd").ToString());
                string stdvcxz2fdsaaq2w3s = Utility.Decrypt(configuration.GetConnectionString("wetrcvcascvvbfdg").ToString());
                string ssdfsawfdatr3sdabd = Utility.Decrypt(configuration.GetConnectionString("cdxsdedsagfdrzds").ToString());
                string stretwrgfdas4awqas = Utility.Decrypt(configuration.GetConnectionString("kfgtfgtrscxvbccf").ToString());

                string njkfgrrtdklftrsdsd = $"Data Source = {stwegfsdasar1sdfda}; Initial Catalog = {stdvcxz2fdsaaq2w3s}; User Id = {ssdfsawfdatr3sdabd}; Password = {stretwrgfdas4awqas}; Pooling = True; Connect Timeout = 5; Max Pool Size = 2000;MultipleActiveResultSets=true;";
                
                ObjWHCon.ConnectionString = njkfgrrtdklftrsdsd;
                ObjWHCon.Open();
                ObjWHCon.Disposed += ObjWHCon_Disposed;
                noOfWHConns++;
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_WHConn, noOfWHConns, "Success");
            }
            catch (Exception ex) 
            {
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_WHConn, noOfWHConns, ex.Message);
                throw ex;
            }
            return ObjWHCon;
        }

        private static void ObjWHCon_Disposed(object? sender, EventArgs e)
        {
            noOfWHConns--;
            Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_WHConn, noOfWHConns, "Dispose");
        }

        private static void ObjCloudCon_Disposed(object? sender, EventArgs e)
        {
            noOfCloudConns--;
            Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_CloudConn, noOfCloudConns, "Dispose");
        }
    }
}
