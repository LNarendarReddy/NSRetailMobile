using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Data;
using System.Text;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class Stockdispatch_v2Controller : ControllerBase
    {
        public readonly IConfiguration configuration;

        public Stockdispatch_v2Controller(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getbranch")]
        public IActionResult GetBranch( [FromQuery] int Userid, [FromQuery] bool IsManualDispatch)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", Userid },
                        { "IsManualDispatch",IsManualDispatch  }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_BRANCHFORDISPATCH_v2", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "Holder";
                    ds.Tables[1].TableName = "BRANCH";
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, true));
                    else
                        return BadRequest(str);
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
        [Route("getbranchindent")]
        public IActionResult GetBranchIndent([FromQuery] int BranchID, [FromQuery] int CategoryID, 
            [FromQuery] int NoOfDays, [FromQuery] int SubCategoryID, [FromQuery] bool ISMobileCall)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BranchID", BranchID },
                        { "CategoryID", CategoryID },
                        { "NoOfDays", NoOfDays },
                        { "ISMobileCall", ISMobileCall },
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_RPT_BRANCHINDENT_AVG", true, parameters);
                if (ds != null && ds.Tables.Count > 0)
                {
                    ds.Tables[0].TableName = "STOCKDISPATCH";
                    ds.Tables[1].TableName = "BRANCHINDENTDETAIL";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, true));
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
        [Route("savebranchindent")]
        public async Task<IActionResult> SaveBranchIndent([FromBody] BranchIndent branchIndent)
        {
            try
            {
                DataTable dt = new();
                dt.Columns.Add("ITEMID", typeof(int));
                dt.Columns.Add("BRANCHSTOCK", typeof(decimal));
                dt.Columns.Add("AVGSALES", typeof(decimal));
                dt.Columns.Add("NOOFDAYSSALES", typeof(decimal));
                dt.Columns.Add("INDENTQUANTITY", typeof(decimal));
                dt.Columns.Add("LASTDISPATCHDATE", typeof(DateTime));
                dt.Columns.Add("SUBCATEGORYID", typeof(int));

                branchIndent.branchIndentDetailList.ForEach(x => dt.Rows.Add(x.ITEMID, x.BRANCHSTOCK, x.AVGSALES, 
                    x.NOOFDAYSSALES, x.INDENTQUANTITY, x.LASTDISPATCHDATE,x.SUBCATEGORYID ));

                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "FROMBRANCHID", branchIndent.FROMBRANCHID },
                        { "TOBRANCHID", branchIndent.TOBRANCHID},
                        { "CATEGORYID",branchIndent. CATEGORYID},
                        { "NOOFDAYS", branchIndent.NOOFDAYS},
                        { "USERID", branchIndent.USERID },
                        { "dtDetail", dt}
                    };
                object obj = new DataRepository().ExecuteScalar(configuration, "USP_CU_BRANCHINDENT_v2", true, parameters);
                string str = Convert.ToString(obj);
                if (int.TryParse(str, out int ivalue))
                    return Ok(ivalue);
                else
                    return BadRequest(str);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("Getbranchindentlist")]
        public IActionResult GetBranchIndentList([FromQuery] int CategoryID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "CATEGORYID", CategoryID }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_RPT_BRANCHINDENTLIST", true, parameters);
                if (ds != null && ds.Tables.Count > 0)
                {
                    ds.Tables[0].TableName = "Holder";
                    ds.Tables[1].TableName = "BRANCHINDENT";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, true));
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
        [Route("discardbranchindent")]
        public IActionResult DiscardBranchIndent([FromQuery] int BranchIndentID, [FromQuery] int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BRANCHINDENTID", BranchIndentID },
                        { "UserID", UserID },
                    };
                int rowsAffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_BRANCHINDENT", true, parameters);
                if (rowsAffected > 0)
                    return Ok(rowsAffected);
                else
                    return BadRequest("Something went wrong");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getdispatchwithbi")]
        public IActionResult GetDispatchDraftWithBI([FromQuery] int UserID, [FromQuery]  bool IsManualDispatch)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "USERID",UserID },
                    { "IsManualDispatch", IsManualDispatch}
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_DISPATCHDRAFT_v2", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (!int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                    {
                        return NotFound(Convert.ToString(ds.Tables[0].Rows[0][0]));
                    }
                    GetDispatchDataset(ds);
                    return Ok(Utility.GetJsonString(ds));
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

        private DataSet GetDispatchDataset(DataSet ds)
        {
            ds.Tables[0].TableName = "STOCKDISPATCH";
            ds.Tables[1].TableName = "BRANCHINDENTDETAILList";
            ds.Tables[2].TableName = "STOCKDISPATCHDETAILINDENTList";
            ds.Tables[3].TableName = "STOCKDISPATCHDETAILMANUALList";
            ds.Tables[4].TableName = "TRAYINFOLIST";

            DataRelation dataRelation = ds.Relations.Add(ds.Tables[0].Columns["BRANCHINDENTID"], ds.Tables[1].Columns["BRANCHINDENTID"]);
            dataRelation.Nested = true;

            DataRelation dataRelation1 = ds.Relations.Add(ds.Tables[1].Columns["BRANCHINDENTDETAILID"], ds.Tables[2].Columns["BRANCHINDENTDETAILID"]);
            dataRelation1.Nested = true;

            DataRelation dataRelation2 = ds.Relations.Add(ds.Tables[0].Columns["STOCKDISPATCHID"], ds.Tables[3].Columns["STOCKDISPATCHID"]);
            dataRelation2.Nested = true;

            DataRelation dataRelation3 = ds.Relations.Add(ds.Tables[0].Columns["STOCKDISPATCHID"], ds.Tables[4].Columns["STOCKDISPATCHID"]);
            dataRelation3.Nested = true;

            return ds;
        }

        [HttpPost]
        [Route("savedispatch")]
        public IActionResult SaveDispatch([FromQuery] int StockDispatchID, [FromQuery] int FromBranchID, 
            [FromQuery] int ToBranchID, [FromQuery] int CategoryID, [FromQuery] int SubCategoryID, 
            [FromQuery] int BranchIndentID, [FromQuery] int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHID", StockDispatchID }
                        ,{ "FROMBRANCHID", FromBranchID }
                        ,{ "TOBRANCHID", ToBranchID }
                        ,{ "CATEGORYID", CategoryID }
                        ,{ "SUBCATEGORYID", SubCategoryID }
                        ,{ "BRANCHINDENTID", BranchIndentID}
                        ,{ "USERID", UserID}
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_CU_STOCKDISPATCH_v2", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (!int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                    {
                        return NotFound(Convert.ToString(ds.Tables[0].Rows[0][0]));
                    }
                    GetDispatchDataset(ds);
                    return Ok(Utility.GetJsonString(ds));
                }
                else
                {
                    return NotFound("Dispatch does not exists");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("getitem")]
        public IActionResult GetItem([FromQuery] string ItemCode, [FromQuery] int CategoryID, [FromQuery] int SubcategoryID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODE", ItemCode },
                        { "CATEGORYID", CategoryID },
                        { "SUBCATEGORYID", SubcategoryID }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMDATAFORDISPATCH_v2", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                    {
                        ds.Tables[0].TableName = "ITEM";
                        ds.Tables[1].TableName = "ITEMCODE";
                        ds.Tables[2].TableName = "ITEMPRICE";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "ITEMID", "ITEMID" }, { "ITEMCODEID", "ITEMCODEID" } }, true));
                    }
                    else
                        throw new Exception(str);
                }
                else
                    throw new Exception("Itemcode does not exists");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("savedispatchdetail")]
        public IActionResult SaveDispatchDetail([FromQuery] int StockDispatchID, [FromQuery] int StockDispatchDetailID,
            [FromQuery] int ItemPriceID, [FromQuery] int TrayNumber, [FromQuery] int DispatchQuantity, 
            [FromQuery] decimal WeightinKGs, [FromQuery] int UserID, [FromQuery] int BranchIndentDetailID, [FromQuery] int TrayInfoID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "STOCKDISPATCHID", StockDispatchID}
                    , { "STOCKDISPATCHDETAILID", StockDispatchDetailID }
                    , { "ITEMPRICEID", ItemPriceID }
                    , { "TRAYNUMBER", TrayNumber }
                    , { "DISPATCHQUANTITY", DispatchQuantity}
                    , { "WEIGHTINKGS", WeightinKGs }
                    , { "USERID", UserID }
                    , { "BRANCHINDENTDETAILID", BranchIndentDetailID }
                    , { "TRAYINFOID", TrayInfoID}
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "USP_CU_STOCKDISPATCHDETAIL", true, parameters);
                string str = Convert.ToString(obj);
                if (int.TryParse(str, out int Ivalue))
                    return Ok(Ivalue);
                else
                    throw new Exception(str);

            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("deletedispatchdetail")]
        public IActionResult DeleteDispatchDetail([FromQuery] int StockDispatchDetailID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHDETAILID", StockDispatchDetailID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_STOCKDISPATCHDETAILS", true, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while deleting item");
                else
                    return Ok(rowsaffected);
            }
            catch (Exception ex)
            {
                return BadRequest("Error while deleting item : " + ex.Message);
            }
        }

        [HttpPost]
        [Route("updatedispatch")]
        public IActionResult UpdateDispatch([FromQuery] int StockDispatchID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHID", StockDispatchID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_U_STOCKDISPATCH", true, parameters, true);

                if (rowsaffected == 0)
                    throw new Exception("Error while submitting dispatch");
                else
                    return Ok(rowsaffected);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("discarddispatch")] 
        public IActionResult DiscardDispatch([FromQuery] int StockDispatchID, [FromQuery] int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "StockDispatchID", StockDispatchID}
                    ,{ "UserID", UserID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_DISPATCH", true, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while discarding dispatch");
                else
                    return Ok(rowsaffected);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("savetrayinfo")]
        public IActionResult SaveTrayInfo([FromQuery] int StockDispatchID, [FromQuery] int TrayNumber, [FromQuery] int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKDISPATCHID", StockDispatchID }
                        ,{ "TRAYNUMBER", TrayNumber }
                        ,{ "USERID", UserID}
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "USP_CU_TRAYINFO", true, parameters);
                string str = Convert.ToString(obj);
                if (int.TryParse(str, out int Ivalue))
                    return Ok(Ivalue);
                else
                    throw new Exception(str);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("gettrayinfo")]
        public IActionResult GetTrayInfo([FromQuery] int StockDispatchID, [FromQuery] bool IsMobileCall )
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "STOCKDISPATCHID", StockDispatchID },
                        { "IsMobileCall", IsMobileCall }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_TRAYINFO", true, parameters);
                if (ds != null && ds.Tables.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                    {
                        ds.Tables[0].TableName = "Holder";
                        ds.Tables[1].TableName = "TRAYINFO";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }, false));
                    }
                    else
                        throw new Exception(str);
                }
                else
                    return NotFound("No tray numbers exists");

            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("deletetrayinfo")]
        public IActionResult DeleteTrayInfo([FromQuery] int TrayInfoID, [FromQuery] int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                     { "TRAYINFOID", TrayInfoID}
                    ,{ "USERID", UserID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_TRAYINFO", true, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while discarding dispatch");
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
