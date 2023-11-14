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
        public IActionResult GetBranch(int Userid)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", Userid }
                    };
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_BRANCHFORCOUNTING", false, parameters);
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
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getitem")]
        public IActionResult GetItem(string ItemCode)
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
        [Route("getcounting")]
        public IActionResult GetCounting(int UserID)
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
        public IActionResult SaveCounting(int StockCountingID, int BranchID, int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKCOUNTINGID", StockCountingID}
                        ,{ "BranchID", BranchID}
                        ,{ "USERID",UserID }
                        ,{ "CREATEDDATE", DateTime.Now }

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
                    Utility.LogTelemetry(Utility.Path_StockCounting, Utility.Action_StockCounting_SaveStockCountingDetail,
                        StockCountingDetailID, StockCountingID, ItemPriceID, Quantity, WeightInKgs, str, "Bad Request");
                    return BadRequest(str);
                }
                else
                {
                    Utility.LogTelemetry(Utility.Path_StockCounting, Utility.Action_StockCounting_SaveStockCountingDetail,
                        StockCountingDetailID, StockCountingID, ItemPriceID, Quantity, WeightInKgs, str, "OK");
                    return Ok("Successfully saved");
                }
            }
            catch (Exception ex)
            {
                Utility.LogTelemetry(Utility.Path_StockCounting, Utility.Action_StockCounting_SaveStockCountingDetail,
                        StockCountingDetailID, StockCountingID, ItemPriceID, Quantity, WeightInKgs, ex.Message, "Bad Request");
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
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "CLOUD_USP_D_STOCKCOUNTINGDETAIL1", false, parameters);
                
                if (rowsaffected == 0)
                    return BadRequest("Error while deleting item");
                else
                    return Ok("Item deleted successfully");
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
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "CLOUD_USP_U_STOCKCOUNTING", false, parameters);

                if (rowsaffected == 0)
                    return BadRequest("Error while submitting stockcounting");
                else
                    return Ok("Stock counting submitted successfully");
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
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "CLOUD_USP_D_STOCKCOUNTING", false, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while discarding counting");
                else
                    return Ok("Stock counting discarded successfully");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
