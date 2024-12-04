using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Data;
using System.Data.SqlClient;
using static NSRetailAPI.Models.CRefund;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CRefundController : ControllerBase
    {
        public readonly IConfiguration configuration;

        public CRefundController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getbillbynumber")]
        public IActionResult GetBillByNumber([FromQuery] string billNumber, [FromQuery] bool isAdminRole, [FromQuery] int branchCounterID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BILLNUMBER", billNumber },
                        { "IsAdminRole", isAdminRole },
                        { "BranchCounterID", branchCounterID },

                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_BILLBYNUMBER", false, parameters);
                if (ds != null && ds.Tables[0].Rows.Count > 0
                       && int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                {
                    ds.Tables[0].TableName = "BILL";
                    ds.Tables[1].TableName = "BILLDETAIL";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "BILLID", "BILLID" } }));
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpPost]
        [Route("savecrefund")]
        public IActionResult SaveCRefund([FromBody] string jsonString)
        {
            try
            {
                SaveCRefund crefund = JsonConvert.DeserializeObject<SaveCRefund>(jsonString);

                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "dtRefund",  crefund.CRefundDetail},
                        { "UserID", crefund.UserID},
                        { "BillID", crefund.BillID },
                        { "CustomerName", crefund.CustomerName},
                        { "CustomerPhone", crefund.CustomerPhone },
                        { "BranchCounterID", crefund.BranchCounterID },

                    };
                int rowsaffacted = new DataRepository().ExecuteNonQuery(configuration, "POS_USP_CU_CREFUND", false, parameters);
                if (rowsaffacted > 0)
                    return Ok($"Rows inserted : {rowsaffacted}");
                else
                    return BadRequest("Something went wrong!");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }
    }
}
