using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Data;

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
        [Route("getdispatchwithoutbi")]
        public IActionResult GetDispatchWithoutBI(int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "USERID",UserID }
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_DISPATCHDRAFT", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (!int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                    {
                        return NotFound(Convert.ToString(ds.Tables[0].Rows[0][0]));
                    }
                    ds.Tables[0].TableName = "Dispatch";
                    ds.Tables[1].TableName = "DispatchDetail";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "STOCKDISPATCHID", "STOCKDISPATCHID" } }, true));
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

        [HttpGet]
        [Route("getdispatchwithbi")]
        public IActionResult GetDispatchDraftWithBI(int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "USERID",UserID }
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_DISPATCHDRAFT_v2", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (!int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                    {
                        return NotFound(Convert.ToString(ds.Tables[0].Rows[0][0]));
                    }
                    ds.Tables[0].TableName = "STOCKDISPATCH";
                    ds.Tables[1].TableName = "BRANCHINDENTDETAIL";
                    ds.Tables[2].TableName = "STOCKDISPATCHDETAILWITHBI";
                    ds.Tables[3].TableName = "STOCKDISPATCHDETAILWITHOUTBI";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, false));
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

        [HttpGet]
        [Route("getbranch")]
        public IActionResult GetBranch( [FromQuery] int Userid)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", Userid }
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
                        { "SubCategoryID", SubCategoryID },
                        { "ISMobileCall", ISMobileCall },
                    };
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_RPT_BRANCHINDENT_AVG", true, parameters);
                if (dt != null && dt.Rows.Count > 0)
                {
                    DataTable dtParent = new DataTable();
                    dtParent.Columns.Add("PARENTID", typeof(int));
                    DataRow dataRow = dtParent.NewRow();
                    dataRow["PARENTID"] = 0;
                    dtParent.Rows.Add(dataRow);

                    DataSet ds = new DataSet();
                    ds.Tables.Add(dtParent);
                    ds.Tables.Add(dt);
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
        public IActionResult SaveBranchIndent([FromQuery] string jsonstring)
        {
            try
            {
                BranchIndent branchIndent = JsonConvert.DeserializeObject<BranchIndent>(jsonstring);

                DataTable dt = new();
                dt.Columns.Add("ITEMID", typeof(int));
                dt.Columns.Add("BRANCHSTOCK", typeof(decimal));
                dt.Columns.Add("AVGSALES", typeof(decimal));
                dt.Columns.Add("NOOFDAYSSALES", typeof(decimal));
                dt.Columns.Add("INDENTQUANTITY", typeof(decimal));
                dt.Columns.Add("LASTDISPATCHEDDATE", typeof(DateTime));

                branchIndent.branchIndentDetail.ForEach(x => dt.Rows.Add(x.ITEMID, x.BRANCHSTOCK, x.AVGSALES, 
                    x.NOOFDAYSSALES, x.INDENTQUANTITY, x.LASTDISPATCHEDDATE));

                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "FROMBRANCHID", branchIndent.FROMBRANCHID },
                        { "TOBRANCHID", branchIndent.TOBRANCHID},
                        { "CATEGORYID",branchIndent. CATEGORYID},
                        { "SUBCATEGORYID", branchIndent.SUBCATEGORYID},
                        { "NOOFDAYS", branchIndent.NOOFDAYS},
                        { "USERID", branchIndent.USERID },
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_CU_BRANCHINDENT", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                    {
                        ds.Tables[0].TableName = "STOCKDISPATCH";
                        ds.Tables[1].TableName = "BRANCHINDENTDETAIL";
                        ds.Tables[2].TableName = "STOCKDISPATCHDETAILWITHBI";
                        ds.Tables[3].TableName = "STOCKDISPATCHDETAILWITHOUTBI";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, false));
                    }
                    else
                        throw new Exception(str);

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
        [Route("savedispatch")]
        public IActionResult SaveDispatch(string jsonstring)
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
                        ,{ "SUBCATEGORYID", stockDispatch.SUBCATEGORYID }
                        ,{ "USERID", stockDispatch.USERID }
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "USP_CU_STOCKDISPATCH", true, parameters);
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
        public IActionResult SaveDispatchDetail([FromQuery] string jsonstring)
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
                    , { "BRANCHINDENTDETAILID", stockDispatchDetail.BRANCHINDENTDETAILID }
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
        public IActionResult DiscardDispatch([FromQuery] int StockDispatchID, [FromQuery] int UserID, [FromQuery] int BranchIndentID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "StockDispatchID", StockDispatchID}
                    ,{ "UserID", UserID}
                    ,{ "BRANCHINDENTID", BranchIndentID}
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

        [HttpGet]
        [Route("currentstock")]
        public IActionResult GetCurrentStock([FromQuery] int FROMBRANCHID, [FromQuery] int TOBRANCHID,
           [FromQuery] int ITEMCODEID, [FromQuery] int PARENTITEMID, [FromQuery] int ITEMPRICEID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "FROMBRANCHID",FROMBRANCHID }
                    ,{ "TOBRANCHID",TOBRANCHID }
                    ,{ "ITEMCODEID",ITEMCODEID }
                    ,{ "PARENTITEMID",PARENTITEMID }
                    ,{ "ITEMPRICEID",ITEMPRICEID }
                };

                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_CURRENTSTOCK", true, parameters);
                if (dt != null && dt.Rows.Count > 0)
                {
                    DataTable dtParent = new DataTable();
                    dtParent.Columns.Add("PARENTID", typeof(int));
                    DataRow dataRow = dtParent.NewRow();
                    dataRow["PARENTID"] = 1;
                    dtParent.Rows.Add(dataRow);

                    DataSet ds = new DataSet();
                    ds.Tables.Add(dtParent);

                    dt.Columns.Add("PARENTID", typeof(int));
                    dt.Rows[0]["PARENTID"] = 1;

                    ds.Tables.Add(dt);
                    ds.Tables[0].TableName = "Holder";
                    ds.Tables[1].TableName = "CurrentStock";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, true));
                }
                else
                {
                    throw new Exception("Error while retrieving current stock");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
