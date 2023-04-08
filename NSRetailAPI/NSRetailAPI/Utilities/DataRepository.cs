using System.Data.SqlClient;
using System.Data;
using System.Runtime;

namespace NSRetailAPI.Utilities
{
    public class DataRepository
    {
        public DataTable GetDataTable(IConfiguration configuration, string procedureName, bool useWHConn, Dictionary<string, object>? parameters = null)
        {
            DataTable dtReportData = new DataTable();
            try
            {
                using (SqlCommand cmd = new SqlCommand())
                {
                    cmd.Connection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration);
                    cmd.CommandTimeout = 1800;
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.CommandText = procedureName;
                    ProcessParameters(cmd, parameters);

                    using (SqlDataAdapter da = new SqlDataAdapter(cmd))
                    {
                        da.Fill(dtReportData);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName}", ex);
            }
            return dtReportData;
        }
        public DataSet GetDataset(IConfiguration configuration, string procedureName, bool useWHConn, Dictionary<string, object>? parameters = null)
        {
            DataSet dsReportData = new DataSet();
            try
            {
                using (SqlCommand cmd = new SqlCommand())
                {
                    cmd.Connection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration);
                    cmd.CommandTimeout = 1800;
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.CommandText = procedureName;
                    ProcessParameters(cmd, parameters);

                    using (SqlDataAdapter da = new SqlDataAdapter(cmd))
                    {
                        da.Fill(dsReportData);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName}", ex);
            }
            return dsReportData;
        }
        public object ExecuteScalar(IConfiguration configuration, string procedureName, bool useWHConn, Dictionary<string, object>? parameters = null)
        {
            object? obj = null;
            try
            {
                using (SqlCommand cmd = new SqlCommand())
                {
                    cmd.Connection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration);
                    cmd.CommandTimeout = 1800;
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.CommandText = procedureName;
                    ProcessParameters(cmd, parameters);
                    obj = cmd.ExecuteScalar();
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName}", ex);
            }
            return obj;
        }
        public int ExecuteNonQuery(IConfiguration configuration, string procedureName, bool useWHConn, Dictionary<string, object>? parameters = null)
        {
            int rowcount = 0;
            try
            {
                using (SqlCommand cmd = new SqlCommand())
                {
                    cmd.Connection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration);
                    cmd.CommandTimeout = 1800;
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.CommandText = procedureName;
                    ProcessParameters(cmd, parameters);
                    rowcount = cmd.ExecuteNonQuery();
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName}", ex);
            }
            return rowcount;
        }
        private void ProcessParameters(SqlCommand sqlCommand, Dictionary<string, object>? parameters)
        {
            if (parameters == null) return;

            foreach (var param in parameters)
            {
                string paramName = param.Key;
                paramName = paramName.StartsWith("@") ? paramName : $"@{paramName}";
                sqlCommand.Parameters.AddWithValue(paramName, param.Value);
            }
        }
    }
}
