using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Data;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class stockdispatchController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public stockdispatchController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getbranch")]
        public IActionResult GetBranch(int Userid)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", Userid }
                    };
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_BRANCHFORDISPATCH", false, parameters);
                if (dt != null && dt.Rows.Count > 0)
                {
                    dt.TableName = "Branch";
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }

        [HttpGet]
        [Route("getitem")]
        public IActionResult GetItem(string ItemCode, int CategoryID, bool useWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODE", ItemCode },
                        { "CATEGORYID", CategoryID }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMDATAFORDISPATCH", useWHConnection, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        throw new Exception(str);
                    else
                    {
                        ds.Tables[0].TableName = "ITEM";
                        ds.Tables[1].TableName = "ITEMCODE";
                        ds.Tables[2].TableName = "ITEMPRICE";
                        return Ok(JsonConvert.SerializeObject(ds));
                    }
                }
                else
                    throw new Exception("Itemcode does not exists");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
        [HttpGet]
        [Route("getdispatch")]
        public IActionResult GetDispatch(int CategoryID, int UserID, bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "CATEGORYID",CategoryID }
                    ,{ "USERID",UserID }
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_DISPATCHDRAFT", UseWHConnection, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (!int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                    {
                        return NotFound(Convert.ToString(ds.Tables[0].Rows[0][0])); 
                    }
                    ds.Tables[0].TableName = "Dispatch";
                    if (ds.Tables.Count > 1)
                        ds.Tables[1].TableName = "DispatchDetail";
                    return Ok(JsonConvert.SerializeObject(ds));
                }
                else
                {
                     return NotFound("Dispatch does not exists");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpPost]
        [Route("savedispatch")]
        public IActionResult SaveDispatch(string jsonstring, bool UseWHConnection)
        {
            try
            {

                StockDispatch stockDispatch = JsonConvert.DeserializeObject<StockDispatch>(jsonstring);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHID", stockDispatch.STOCKDISPATCHID }
                        ,{ "FROMBRANCHID", stockDispatch.FROMBRANCHID }
                        ,{ "TOBRANCHID", stockDispatch.TOBRANCHID }
                        ,{ "CATEGORYID", stockDispatch.CATEGORYID }
                        ,{ "USERID", stockDispatch.USERID }
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "USP_CU_STOCKDISPATCH", UseWHConnection, parameters);
                string str = Convert.ToString(obj);
                if (!int.TryParse(str, out int ivalue))
                    throw new Exception(str);
                else
                    return Ok(ivalue);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("savedispatchdetail")]
        public IActionResult SaveDispatchDetail(string jsonstring, bool UseWHConnection)
        {
            try
            {
                StockDispatchDetail stockDispatchDetail = JsonConvert.DeserializeObject<StockDispatchDetail>(jsonstring);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHDETAILID", stockDispatchDetail.STOCKDISPATCHDETAILID }
                    , { "STOCKDISPATCHID", stockDispatchDetail.STOCKDISPATCHID }
                    , { "ITEMPRICEID", stockDispatchDetail.ITEMPRICEID }
                    , { "TRAYNUMBER", stockDispatchDetail.TRAYNUMBER }
                    , { "DISPATCHQUANTITY", stockDispatchDetail.DISPATCHQUANTITY }
                    , { "WEIGHTINKGS", stockDispatchDetail.WEIGHTINKGS }
                    , { "USERID", stockDispatchDetail.USERID }
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "USP_CU_STOCKDISPATCHDETAIL", UseWHConnection, parameters);
                string str = Convert.ToString(obj);
                if (!int.TryParse(str, out int Ivalue))
                    throw new Exception(str);
                else
                    return Ok("Successfully saved");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("deletedispatchdetail")]
        public IActionResult DeleteDispatchDetail(int StockDispatchDetailID, bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHDETAILID", StockDispatchDetailID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_STOCKDISPATCHDETAILS", UseWHConnection, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while deleting item");
                else
                    return Ok("Item deleted successfully");
            }
            catch (Exception ex)
            {
                return BadRequest("Error while deleting item");
            }
        }

        [HttpPost]
        [Route("updatedispatch")]
        public IActionResult UpdateDispatch(int StockDispatchID, bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHID", StockDispatchID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_U_STOCKDISPATCH", UseWHConnection, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while submitting dispatch");
                else
                    return Ok("Stock dispatch submitted successfully");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("discarddispatch")]
        public IActionResult DiscardDispatch(int StockDispatchID, int UserID,bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "StockDispatchID", StockDispatchID}
                    ,{ "UserID", UserID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_DISPATCH", UseWHConnection, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while discarding dispatch");
                else
                    return Ok("Stock dispatch discarded successfully");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
