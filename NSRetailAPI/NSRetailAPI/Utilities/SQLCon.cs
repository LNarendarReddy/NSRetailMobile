using System.Data.SqlClient;
using System.Data;

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
                ObjCloudCon.ConnectionString = configuration.GetConnectionString("Cloudcon").ToString();
                ObjCloudCon.Open();
                ObjCloudCon.Disposed += ObjCloudCon_Disposed;
                noOfCloudConns++;
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_CloudConn, noOfCloudConns, "Success");
            }
            catch (Exception ex) 
            {
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_CloudConn, noOfCloudConns, ex.Message);
            }
            return ObjCloudCon;
        }

        public static SqlConnection SqlWHconn(IConfiguration configuration)
        {
            SqlConnection ObjWHCon = new SqlConnection();
            try
            {
                ObjWHCon.ConnectionString = configuration.GetConnectionString("WHcon").ToString();
                ObjWHCon.Open();
                ObjWHCon.Disposed += ObjWHCon_Disposed;
                noOfWHConns++;
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_WHConn, noOfWHConns, "Success");
            }
            catch (Exception ex) 
            {
                Utility.LogTelemetry(Utility.Path_SQLConn, Utility.Action_SQLConn_WHConn, noOfWHConns, ex.Message);
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

        //private static SqlConnection? ObjCloudCon = null;
        //private static SqlConnection? ObjWHCon = null;
        //public static SqlConnection SqlCloudconn(IConfiguration configuration)
        //{
        //    if (ObjCloudCon?.State == ConnectionState.Open)
        //    {
        //        return ObjCloudCon;
        //    }
        //    ObjCloudCon = new SqlConnection();
        //    try
        //    {
        //        ObjCloudCon.ConnectionString = configuration.GetConnectionString("Cloudcon").ToString();
        //        ObjCloudCon.Open();
        //    }
        //    catch (Exception) { }
        //    return ObjCloudCon;
        //}

        //public static SqlConnection SqlWHconn(IConfiguration configuration)
        //{
        //    if (ObjWHCon?.State == ConnectionState.Open)
        //    {
        //        return ObjWHCon;
        //    }
        //    ObjWHCon = new SqlConnection();
        //    try
        //    {
        //        ObjWHCon.ConnectionString = configuration.GetConnectionString("WHcon").ToString();
        //        ObjWHCon.Open();
        //    }
        //    catch (Exception) { }
        //    return ObjWHCon;
        //}
    }
}
