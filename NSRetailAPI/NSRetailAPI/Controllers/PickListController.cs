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
        [Route("getlocationdivision")]
        public IActionResult GetLocationDivision([FromQuery] int CategoryID)
        {
            try
            {
                Dictionary<string, object> parameters = new() 
                {
                    { "CategoryID", CategoryID },
                    { "IsMobileCall", true }
                };

                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_LOCATIONDIVISION", true, parameters);

                if (ds != null && ds.Tables.Count > 1 && ds.Tables[1].Rows.Count > 0)
                {
                    string? str = Convert.ToString(ds.Tables[1].Rows[0][0]);
                    if (!int.TryParse(str, out int Ivalue))
                        throw new Exception(str);
                    else
                    {
                        ds.Tables[0].TableName = "Holder";
                        ds.Tables[1].TableName = "LocationDivision";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
                    }
                }
                else
                    return NotFound("No location division found for branch");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("getbranchlistfordispatch")]
        public IActionResult GetBranchListForDispatch([FromQuery] int locationDivisionID)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                    { "LocationDivisionID", locationDivisionID }
                };

                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_PICKLIST_BRANCH_FOR_DISPATCH", true, parameters);

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
                    return NotFound("No branch found for picklist dispatch");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        //[HttpGet]
        //[Route("getsupplierlist")]
        //public IActionResult GetSupplierList([FromQuery] int CategoryID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "CategoryID", CategoryID }
        //        };

        //        DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_PICKLIST_SUPPLIER_FOR_TRAYDATA", true, parameters);

        //        if (ds != null && ds.Tables.Count > 1 && ds.Tables[1].Rows.Count > 0)
        //        {
        //            int Ivalue = 0;
        //            string str = Convert.ToString(ds.Tables[1].Rows[0][0]);
        //            if (!int.TryParse(str, out Ivalue))
        //                throw new Exception(str);
        //            else
        //            {
        //                ds.Tables[0].TableName = "Holder";
        //                ds.Tables[1].TableName = "SUPPLIER";
        //                return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
        //            }
        //        }
        //        else
        //            return NotFound("No supplier found for picklist");
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpGet]
        //[Route("getitemdata")]
        //public IActionResult GetItemData([FromQuery] int PickListID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "PickListID", PickListID }
        //        };

        //        DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_PICKLIST_ITEMDATA", true, parameters);

        //        if (ds != null && ds.Tables.Count > 0 && ds.Tables[1].Rows.Count > 0)
        //        {
        //            int Ivalue = 0;
        //            string str = Convert.ToString(ds.Tables[1].Rows[0][0]);
        //            if (!int.TryParse(str, out Ivalue))
        //                throw new Exception(str);
        //            else
        //            {
        //                ds.Tables[0].TableName = "Holder";
        //                ds.Tables[1].TableName = "PICKLISTITEM";
        //                return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
        //            }
        //        }
        //        else
        //            return NotFound("No branch found for picklist");
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpPost]
        //[Route("addtray")]
        //public IActionResult AddTray([FromQuery] int pickListID, [FromQuery] int pickListItemID
        //    , [FromQuery] int quantity, [FromQuery] string trayNumber, [FromQuery] int userID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "pickListID", pickListID },
        //            { "pickListItemID", pickListItemID },
        //            { "quantity", quantity },
        //            { "trayNumber", trayNumber },
        //            { "userID", userID },
        //        };

        //        object obj = new DataRepository().ExecuteScalarWithTransaction(configuration, "USP_CU_PICKLIST_TRAYDATA", true, parameters);
        //        string str = Convert.ToString(obj);
        //        if (!int.TryParse(str, out int ivalue))
        //            throw new Exception(str);
        //        else
        //            return Ok(ivalue);
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpGet]
        //[Route("gettraywisedata")]
        //public IActionResult GetTrayWiseData([FromQuery] int PickListID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "PickListID", PickListID }
        //        };

        //        DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_PICKLIST_TRAYDATA", true, parameters);

        //        if (ds != null && ds.Tables.Count > 0 && ds.Tables[1].Rows.Count > 0)
        //        {
        //            int Ivalue = 0;
        //            string str = Convert.ToString(ds.Tables[1].Rows[0][0]);
        //            if (!int.TryParse(str, out Ivalue))
        //                throw new Exception(str);
        //            else
        //            {
        //                ds.Tables[0].TableName = "Holder";
        //                ds.Tables[1].TableName = "PICKLISTTRAY";
        //                ds.Tables[2].TableName = "PICKLISTITEM";
        //                return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" }, { "PICKLISTTRAYID", "PICKLISTTRAYID" } }));
        //            }
        //        }
        //        else
        //            return NotFound("No tray wise found for picklist");
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpGet]
        //[Route("gettraysfordispatch")]
        //public IActionResult GetTraysForDispatch([FromQuery] int BranchID, [FromQuery] int CategoryID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "BranchID", BranchID },
        //            { "CategoryID", CategoryID }
        //        };

        //        DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_DISPATCH_TRAYDATA", true, parameters);

        //        if (ds != null && ds.Tables.Count > 0 && ds.Tables[1].Rows.Count > 0)
        //        {
        //            int Ivalue = 0;
        //            string str = Convert.ToString(ds.Tables[1].Rows[0][0]);
        //            if (!int.TryParse(str, out Ivalue))
        //                throw new Exception(str);
        //            else
        //            {
        //                ds.Tables[0].TableName = "Holder";
        //                ds.Tables[1].TableName = "PICKLISTTRAY";
        //                ds.Tables[2].TableName = "PICKLISTITEM";
        //                return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" }, { "PICKLISTTRAYID", "PICKLISTTRAYID" } }));
        //            }
        //        }
        //        else
        //            return NotFound("No trays found for picklist dispatch");
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpDelete]
        //[Route("deleteitemdetail")]
        //public IActionResult DeleteItemDetail([FromQuery] int pickListItemDetailID, [FromQuery] int userID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "pickListItemDetailID", pickListItemDetailID },
        //            { "userID", userID }
        //        };

        //        new DataRepository().ExecuteNonQuery(configuration, "USP_D_PICKLIST_ITEMDATA", true, parameters, true);

        //        return Ok(pickListItemDetailID);
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpDelete]
        //[Route("deletetray")]
        //public IActionResult DeleteTray([FromQuery] int pickListTrayID, [FromQuery] int userID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "pickListTrayID", pickListTrayID },
        //            { "userID", userID }
        //        };

        //        new DataRepository().ExecuteNonQuery(configuration, "USP_D_PICKLIST_TRAY", true, parameters, true);

        //        return Ok(pickListTrayID);
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpPost]
        //[Route("submitpicklist")]
        //public IActionResult SubmitPicklist([FromQuery] int pickListID, [FromQuery] int userID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "pickListID", pickListID },
        //            { "userID", userID }
        //        };

        //        new DataRepository().ExecuteNonQuery(configuration, "USP_U_PICKLIST", true, parameters, true);

        //        return Ok(pickListID);
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpPost]
        //[Route("verifytray")]
        //public IActionResult VerifyTray([FromQuery] int pickListTrayID, [FromQuery] bool IsTrayVerified, [FromQuery] int userID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "pickListTrayID", pickListTrayID },
        //            { "IsTrayVerified", IsTrayVerified },
        //            { "userID", userID }
        //        };

        //        new DataRepository().ExecuteNonQuery(configuration, "USP_V_PICKLISTTRAY", true, parameters, true);

        //        return Ok(pickListTrayID);
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpPost]
        //[Route("updatetray")]
        //public IActionResult UpdateTray([FromQuery] int pickListTrayID, [FromQuery] string trayNumber, [FromQuery] int userID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "pickListTrayID", pickListTrayID },
        //            { "TrayNumber", trayNumber },
        //            { "userID", userID }
        //        };

        //        new DataRepository().ExecuteNonQuery(configuration, "USP_U_PICKLISTTRAY", true, parameters, true);

        //        return Ok(pickListTrayID);
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}

        //[HttpPost]
        //[Route("dispatchbranch")]
        //public IActionResult DispatchBranch([FromQuery] int branchID, [FromQuery] string pickListTrayIDs, [FromQuery] int userID)
        //{
        //    try
        //    {
        //        Dictionary<string, object> parameters = new()
        //        {
        //            { "branchID", branchID },
        //            { "pickListTrayIDs", pickListTrayIDs },
        //            { "userID", userID }
        //        };

        //        new DataRepository().ExecuteNonQuery(configuration, "USP_P_DISPATCHFTBRANCH", true, parameters, true);

        //        return Ok(branchID);
        //    }
        //    catch (Exception ex)
        //    {
        //        return BadRequest(ex.Message);
        //    }
        //}
    }
}
