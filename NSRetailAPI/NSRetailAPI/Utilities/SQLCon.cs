using System.Data.SqlClient;
using System.Data;

namespace NSRetailAPI.Utilities
{
    public class SQLCon
    {
        private static SqlConnection? ObjCloudCon = null;
        private static SqlConnection? ObjWHCon = null;
        public static SqlConnection SqlCloudconn(IConfiguration configuration)
        {
            if (ObjCloudCon?.State == ConnectionState.Open)
            {
                return ObjCloudCon;
            }
            ObjCloudCon = new SqlConnection();
            try
            {
                ObjCloudCon.ConnectionString = configuration.GetConnectionString("Cloudcon").ToString();
                ObjCloudCon.Open();
            }
            catch (Exception) { }
            return ObjCloudCon;
        }

        public static SqlConnection SqlWHconn(IConfiguration configuration)
        {
            if (ObjWHCon?.State == ConnectionState.Open)
            {
                return ObjWHCon;
            }
            ObjWHCon = new SqlConnection();
            try
            {
                ObjWHCon.ConnectionString = configuration.GetConnectionString("WHcon").ToString();
                ObjWHCon.Open();
            }
            catch (Exception) { }
            return ObjWHCon;
        }
    }
}
