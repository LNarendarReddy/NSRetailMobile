using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using NSRetailAPI.Utilities;
using System.Data;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PickListController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public PickListController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getbranchlist")]
        public IActionResult GetBranchList([FromQuery] int CategoryID)
        {
            try
            {
                Dictionary<string, object> parameters = new() 
                {
                    { "CategoryID", CategoryID }
                };

                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_PICKLIST_BRANCH_FOR_TRAYDATA", true, parameters);

                if (ds != null && ds.Tables.Count > 1 && ds.Tables[1].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[1].Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        throw new Exception(str);
                    else
                    {
                        ds.Tables[0].TableName = "Holder";
                        ds.Tables[1].TableName = "BRANCH";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
                    }
                }
                else
                    return NotFound("No branch found for picklist");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("getitemdata")]
        public IActionResult GetItemData([FromQuery] int PickListID)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                    { "PickListID", PickListID }
                };

                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_PICKLIST_ITEMDATA", true, parameters);

                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[1].Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        throw new Exception(str);
                    else
                    {
                        ds.Tables[0].TableName = "Holder";
                        ds.Tables[1].TableName = "PICKLISTITEM";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
                    }
                }
                else
                    return NotFound("No branch found for picklist");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("addtray")]
        public IActionResult AddTray([FromQuery] int pickListID, [FromQuery] int pickListItemID
            , [FromQuery] int quantity, [FromQuery] string trayNumber, [FromQuery] int userID)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                    { "pickListID", pickListID },
                    { "pickListItemID", pickListItemID },
                    { "quantity", quantity },
                    { "trayNumber", trayNumber },
                    { "userID", userID },
                };

                object obj = new DataRepository().ExecuteScalarWithTransaction(configuration, "USP_CU_PICKLIST_TRAYDATA", true, parameters);
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

        [HttpGet]
        [Route("gettraywisedata")]
        public IActionResult GetTrayWiseData([FromQuery] int PickListID)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                    { "PickListID", PickListID }
                };

                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_PICKLIST_TRAYDATA", true, parameters);

                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[1].Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        throw new Exception(str);
                    else
                    {
                        ds.Tables[0].TableName = "Holder";
                        ds.Tables[1].TableName = "PICKLISTTRAY";
                        ds.Tables[2].TableName = "PICKLISTITEM";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" }, { "PICKLISTTRAYID", "PICKLISTTRAYID" } }));
                    }
                }
                else
                    return NotFound("No branch found for picklist");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpDelete]
        [Route("deleteitemdetail")]
        public IActionResult DeleteItemDetail([FromQuery] int pickListItemDetailID, [FromQuery] int userID)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                    { "pickListItemDetailID", pickListItemDetailID },
                    { "userID", userID }
                };

                new DataRepository().ExecuteNonQuery(configuration, "USP_D_PICKLIST_ITEMDATA", true, parameters, true);

                return Ok(pickListItemDetailID);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpDelete]
        [Route("deletetray")]
        public IActionResult DeleteTray([FromQuery] int pickListTrayID, [FromQuery] int userID)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                    { "pickListTrayID", pickListTrayID },
                    { "userID", userID }
                };

                new DataRepository().ExecuteNonQuery(configuration, "USP_D_PICKLIST_TRAY", true, parameters, true);

                return Ok(pickListTrayID);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
