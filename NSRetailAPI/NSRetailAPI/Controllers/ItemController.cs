using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using NSRetailAPI.Utilities;
using System.Data;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ItemController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public ItemController(IConfiguration _configuration)
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
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_BRANCHFORITEM", false, parameters);
                if (ds != null)
                {
                    ds.Tables[0].TableName = "ACCESS";
                    ds.Tables[1].TableName = "BRANCH";
                    return Ok(JsonConvert.SerializeObject(ds));
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
        public IActionResult GetItem(string ItemCode, bool useWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODE", ItemCode }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMFORITEMDETAILS", useWHConnection, parameters);
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
        [Route("getitemdata")]
        public IActionResult GetItemData(int ItemCodeID, int BranchID, bool useWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODEID", ItemCodeID},
                        { "BRANCHID",  BranchID}
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMDATAFORITEMDETAILS", useWHConnection, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "ITEMPRICE";
                    ds.Tables[1].TableName = "OFFER";
                    ds.Tables[2].TableName = "ITEMCOSTPRICE";
                    ds.Tables[3].TableName = "BRANCHSTOCK";
                    return Ok(JsonConvert.SerializeObject(ds));
                }
                else
                    throw new Exception("Itemcode does not exists");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
