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
                    ds.Tables[1].TableName = "Counting";
                    if (ds.Tables.Count > 1)
                        ds.Tables[1].TableName = "CountingDetail";
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
                return StatusCode(500, ex.ToString());
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
