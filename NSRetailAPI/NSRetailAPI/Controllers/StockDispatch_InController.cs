using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Data;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class StockDispatch_InController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public StockDispatch_InController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getdispatchlist")]
        public IActionResult GetDispatchList([FromQuery] int BranchID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BranchID", BranchID }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_STOCKDISPATCH_v2", true, parameters);

                string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                if (int.TryParse(str, out int Ivalue))
                {
                    if (ds != null && ds.Tables.Count > 0 && ds.Tables[1].Rows.Count > 0)
                    {
                        ds.Tables[0].TableName = "Holder";
                        ds.Tables[1].TableName = "DISPATCH";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, true));
                    }
                    else
                        return NotFound("Data not found");
                }
                else
                    return BadRequest(str);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("getdispatchdetailbytraynumber")]
        public IActionResult GetDispatchDetailByTrayNumber([FromQuery] int BranchID, [FromQuery] int TrayNumber)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BranchID", BranchID },
                        { "TRAYNUMBER",TrayNumber}
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_STOCKDISPATCHDETAIL_v2", true, parameters);
                string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                if (int.TryParse(str, out int Ivalue))
                {
                    if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                    {
                        ds.Tables[0].TableName = "DISPATCHRECEIVE";
                        ds.Tables[1].TableName = "DISPATCHRECEIVEDETAIL";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, true));
                    }
                    else
                        return NotFound("Data not found");
                }
                else
                    return BadRequest(str);

            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("updatedispatchquantity")]
        public IActionResult UpdateDispatchQuantity([FromQuery] int StockDispatchDetailID, [FromQuery] int ReceivedQuantity, [FromQuery] decimal WeightInKgs)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHDETAILID", StockDispatchDetailID},
                        { "RECEIVEDQUANTITY", ReceivedQuantity},
                        { "WEIGHTINKGS", WeightInKgs}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "POS_USP_U_STOCKDISPATCHDETAIL", true, parameters, true);

                if (rowsaffected == 0)
                    throw new Exception("Error while updating received quantity");
                else
                    return Ok(rowsaffected);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("updatetrayinfo")]
        public IActionResult UpdateTrayInfo([FromQuery] int StockDispatchID, [FromQuery] int TrayNumber, [FromQuery] int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHID", StockDispatchID},
                        { "TRAYNUMBER", TrayNumber},
                        { "USERID", UserID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "POS_USP_U_TRAYINFO", true, parameters, true);

                if (rowsaffected == 0)
                    throw new Exception("Error while updating tray info");
                else
                    return Ok(rowsaffected);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("finishstockin")]
        public IActionResult FinishStockin([FromQuery] int StockDispatchID, [FromQuery] int CounterID, [FromQuery] int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "StockDispatchID", StockDispatchID},
                        { "UserID", UserID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "POS_USP_FINISH_STOCKIN_v2", true, parameters, true);

                if (rowsaffected == 0)
                    throw new Exception("Error while finishing stock in");
                else
                    return Ok(rowsaffected);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
