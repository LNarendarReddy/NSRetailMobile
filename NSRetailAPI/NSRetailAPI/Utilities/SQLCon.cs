using System.Data.SqlClient;
using System.Data;

namespace NSRetailAPI.Utilities
{
    public class SQLCon
    {
        private static SqlConnection? ObjCloudCon = null;
        public static SqlConnection SqlCloudconn(IConfiguration configuration)
        {
            if (ObjCloudCon?.State == ConnectionState.Open)
            {
                return ObjCloudCon;
            }
            ObjCloudCon = new SqlConnection();
            try
            {
                ObjCloudCon.ConnectionString = configuration.GetConnectionString("mobilecon").ToString();
                ObjCloudCon.Open();
            }
            catch (Exception) { }
            return ObjCloudCon;
        }
    }
}
