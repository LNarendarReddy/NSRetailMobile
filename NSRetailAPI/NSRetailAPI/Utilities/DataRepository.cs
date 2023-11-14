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
                using (SqlConnection connection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration))
                {
                    using (SqlCommand cmd = new SqlCommand())
                    {
                        cmd.Connection = connection;
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
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName} - {ex.Message}", ex);
            }
            return dtReportData;
        }
        public DataSet GetDataset(IConfiguration configuration, string procedureName, bool useWHConn, Dictionary<string, object>? parameters = null)
        {
            DataSet dsReportData = new DataSet();
            try
            {
                using (SqlConnection connection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration))
                {
                    using (SqlCommand cmd = new SqlCommand())
                    {
                        cmd.Connection = connection;
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
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName} - {ex.Message}", ex);
            }
            return dsReportData;
        }
        public object ExecuteScalar(IConfiguration configuration, string procedureName, bool useWHConn, Dictionary<string, object>? parameters = null)
        {
            object? obj = null;
            try
            {
                using (SqlConnection connection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration))
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
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName} - {ex.Message}", ex);
            }
            return obj;
        }
        public int ExecuteNonQuery(IConfiguration configuration, string procedureName, bool useWHConn, Dictionary<string, object>? parameters = null, bool UseTransaction = false)
        {
            int rowcount = 0;
            try
            {
                using (SqlConnection sqlConnection = useWHConn ? SQLCon.SqlWHconn(configuration) : SQLCon.SqlCloudconn(configuration))
                {
                    SqlTransaction sqlTransaction = null;
                    try
                    {
                        using (SqlCommand cmd = new SqlCommand())
                        {
                            if (UseTransaction)
                                sqlTransaction = sqlConnection.BeginTransaction();
                            cmd.Connection = sqlConnection;
                            if (UseTransaction)
                                cmd.Transaction = sqlTransaction;
                            cmd.CommandTimeout = 1800;
                            cmd.CommandType = CommandType.StoredProcedure;
                            cmd.CommandText = procedureName;
                            ProcessParameters(cmd, parameters);
                            rowcount = cmd.ExecuteNonQuery();
                            sqlTransaction?.Commit();
                        }
                    }
                    catch (Exception ex)
                    {
                        sqlTransaction?.Rollback();
                        throw new Exception($"Error while executing {procedureName} - {ex.Message}", ex);
                    }
                    finally { sqlTransaction?.Dispose(); }
                }
            }
            catch (Exception ex)
            {
                throw new Exception($"Error while executing {procedureName} - {ex.Message}", ex);
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
