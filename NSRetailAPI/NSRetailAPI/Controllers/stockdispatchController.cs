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
                    ds.Tables[1].TableName = "Dispatch";
                    if (ds.Tables.Count > 1)
                        ds.Tables[1].TableName = "DispatchDetail";
                    return Ok(JsonConvert.SerializeObject(ds));
                }
                else
                {
                    return NotFound();
                }
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
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
                    return BadRequest(str);
                else
                    return Ok(ivalue);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
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
                    return BadRequest(str);
                else
                    return Ok(JsonConvert.SerializeObject(Ivalue));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }

        [HttpPost]
        [Route("deletedispatchdetail")]
        public IActionResult DeleteDispatchDetail(int StockCountingDetailID, bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHDETAILID", StockCountingDetailID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_STOCKDISPATCHDETAILS", UseWHConnection, parameters);

                if (rowsaffected == 0)
                    return BadRequest();
                else
                    return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
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
                    return BadRequest();
                else
                    return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
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
                    return BadRequest();
                else
                    return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }
    }
}
