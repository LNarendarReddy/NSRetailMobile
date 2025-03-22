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
    public class stockcountingController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public stockcountingController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getbranch")]
        public IActionResult GetBranch(int Userid,bool isNested = false)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", Userid }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_BRANCHFORCOUNTING", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "Holder";
                    ds.Tables[1].TableName = "BRANCH";
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                        if (isNested)
                            return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }));
                        else
                            return Ok(JsonConvert.SerializeObject(ds.Tables[0]));
                    else
                        return BadRequest(str);
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("getitem")]
        public IActionResult GetItem(string ItemCode,bool isNested = false)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODE", ItemCode }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMDATAFORCOUNTING", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                    {
                        ds.Tables[0].TableName = "ITEM";
                        ds.Tables[1].TableName = "ITEMCODE";
                        ds.Tables[2].TableName = "ITEMPRICE";
                        if (isNested)
                            return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "ITEMID", "ITEMID" }, { "ITEMCODEID", "ITEMCODEID" } }));
                        else
                            return Ok(JsonConvert.SerializeObject(ds));
                    }
                    else
                    {
                        throw new Exception(str);
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
        [Route("getcounting")]
        public IActionResult GetCounting(int UserID, bool isNested = false)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "USERID",UserID }
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "CLOUD_USP_R_STOCKCOUNTING", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int countingid) && countingid > 0)
                    {
                        ds.Tables[0].TableName = "Counting";
                        if (ds.Tables.Count > 1)
                            ds.Tables[1].TableName = "CountingDetail";
                        if (isNested)
                            return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "STOCKCOUNTINGID", "STOCKCOUNTINGID" } }));
                        else
                            return Ok(JsonConvert.SerializeObject(ds));
                    }
                    else
                    {
                        return NotFound("No counting data found");
                    }
                }
                else
                {
                    return NotFound("No counting data found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("savecounting")]
        public IActionResult SaveCounting(int StockCountingID, int BranchID, int UserID, string StockLocationName)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                    { "STOCKCOUNTINGID", StockCountingID}
                    , { "BranchID", BranchID}
                    , { "USERID",UserID }
                    , { "CREATEDDATE", DateTime.Now }
                    , { "STOCKLOCATIONNAME", StockLocationName }
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "CLOUD_USP_CU_STOCKCOUNTING", false, parameters);
                string str = Convert.ToString(obj);
                if (!int.TryParse(str, out int ivalue))
                    return BadRequest(str);
                else
                    return Ok(ivalue);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("savecountingdetail")]
        public IActionResult SaveCountingDetail(int StockCountingDetailID, int StockCountingID, int ItemPriceID, int Quantity, decimal WeightInKgs)
        {
            try
            {
                //Utility.LogTelemetry(string.Format(Utility.Path_StockCountingDetail, StockCountingID), Utility.Action_StockCounting_SaveStockCountingDetail,
                //           StockCountingDetailID, StockCountingID, ItemPriceID, Quantity, WeightInKgs, string.Empty, "Request Received");

                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKCOUNTINGDETAILID",StockCountingDetailID }
                    , { "STOCKCOUNTINGID", StockCountingID }
                    , { "ITEMPRICEID", ItemPriceID}
                    , { "QUANTITY", Quantity }
                    , { "WEIGHTINKGS", WeightInKgs }
                    , { "CREATEDDATE", DateTime.Now }
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "CLOUD_USP_CU_STOCKCOUNTINGDETAIL", false, parameters);
                string str = Convert.ToString(obj);

                if (!int.TryParse(str, out int Ivalue) || Ivalue <= 0)
                {
                    //Utility.LogTelemetry(string.Format(Utility.Path_StockCountingDetail, StockCountingID), Utility.Action_StockCounting_SaveStockCountingDetail,
                    //    StockCountingDetailID, StockCountingID, ItemPriceID, Quantity, WeightInKgs, str, "Bad Request");
                    return BadRequest(str);
                }
                else
                {
                    //Utility.LogTelemetry(string.Format(Utility.Path_StockCountingDetail, StockCountingID), Utility.Action_StockCounting_SaveStockCountingDetail,
                    //    StockCountingDetailID, StockCountingID, ItemPriceID, Quantity, WeightInKgs, str, "OK");
                    return Ok("Successfully saved");
                }
            }
            catch (Exception ex)
            {
                //Utility.LogTelemetry(string.Format(Utility.Path_StockCountingDetail, StockCountingID), Utility.Action_StockCounting_SaveStockCountingDetail,
                //        StockCountingDetailID, StockCountingID, ItemPriceID, Quantity, WeightInKgs, ex.Message, "Bad Request");
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("deletecountingdetail")]
        public IActionResult DeleteCountingDetail(int StockCountingDetailID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKCOUNTINGDETAILID", StockCountingDetailID}
                };
                object obj = new DataRepository(). ExecuteScalar(configuration, "CLOUD_USP_D_STOCKCOUNTINGDETAIL1", false, parameters);

                string str = Convert.ToString(obj);
                if (int.TryParse(str, out int ivalue))
                    return Ok("Item deleted successfully");
                else
                    throw new Exception(str);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("updatecounting")]
        public IActionResult UpdateCounting(int StockCountingID, int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKCOUNTINGID", StockCountingID}
                        ,{ "USERID", UserID}
                        ,{ "UPDATEDDATE", DateTime.Now}
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "CLOUD_USP_U_STOCKCOUNTING", false, parameters);
                string str = Convert.ToString(obj);
                if (int.TryParse(str, out int ivalue))
                    return Ok("Stock counting submitted successfully");
                else
                    throw new Exception(str);

            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("discardcounting")]
        public IActionResult DiscardCounting(int StockCountingID, int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKCOUNTINGID", StockCountingID}
                    ,{ "USERID", UserID}
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "CLOUD_USP_D_STOCKCOUNTING", false, parameters);
                
                string str = Convert.ToString(obj);
                if (int.TryParse(str, out int ivalue))
                    return Ok("Stock counting discarded successfully");
                else
                    throw new Exception(str);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
