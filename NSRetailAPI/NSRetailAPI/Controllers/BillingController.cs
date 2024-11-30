using Microsoft.AspNetCore.Mvc;
using NSRetailAPI.Utilities;
using System.Data.SqlClient;
using System.Data;
using Newtonsoft.Json;
using System.Diagnostics.Metrics;
using System.Diagnostics.Eventing.Reader;
using NSRetailAPI.Models;

namespace NSRetailAPI.Controllers   
{
    [Route("api/[controller]")]
    [ApiController]
    public class BillingController : ControllerBase
    {
        public readonly IConfiguration configuration;

        public BillingController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getcounters")]
        public IActionResult GetCounters([FromQuery] int BranchID, [FromQuery] bool isMobileCounter = true)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BranchID", BranchID },
                        { "ISMOBILECOUNTER", isMobileCounter}

                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "USP_R_BRANCHCOUNTER", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BRANCHCOUNTER";
                    return Ok(Utility.GetJsonString(ds, null));
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
        public IActionResult GetItem([FromQuery] string ItemCode)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODE", ItemCode }
                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "USP_R_ITEMDATAFORBILLING", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                    {
                        ds.Tables[0].TableName = "ITEM";
                        ds.Tables[1].TableName = "ITEMCODES";
                        ds.Tables[2].TableName = "ITEMPRICES";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "ITEMID", "ITEMID" }, { "ITEMCODEID", "ITEMCODEID" } }));
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
        [Route("getitemcodes")]
        public IActionResult GetItemCodes()
        {
            try
            {
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_ITEMCODES", false, null);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "ITEMCODE";
                    return Ok(Utility.GetJsonString(ds, null));
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
        [Route("getinitialload")]
        public IActionResult GetInitialLoad([FromQuery] int userID, [FromQuery] int branchCounterID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "UserID", userID },
                        { "BranchCounterID", branchCounterID }
                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_LOAD", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "DAYSEQUENCE";
                    if (ds.Tables.Count > 1)
                    {
                        ds.Tables[1].TableName = "BILL";
                        ds.Tables[2].TableName = "BILLDETAILS";
                    }
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getbill")]
        public IActionResult GetBill([FromQuery] int daySequenceID, [FromQuery] int billID, [FromQuery] int branchCounterID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BillID", billID },
                        { "DaySequenceID", daySequenceID },
                        { "BranchCounterID", branchCounterID },

                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_LOAD", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILL";
                    ds.Tables[1].TableName = "BILLDETAILS";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getmrplist")]
        public IActionResult GetMRPList([FromQuery] int itemCodeID, [FromQuery] int branchID, [FromQuery] bool showAllMRP = false)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODEID", itemCodeID},
                        { "FilterMRPByStock", showAllMRP },
                        { "BRANCHID", branchID },

                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_ITEMMRPLIST", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "ITEMPRICE";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getofferlist")]
        public IActionResult GetOfferList([FromQuery] int ItemPriceID, [FromQuery] int branchID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMPRICEID", ItemPriceID},
                        { "BRANCHID", branchID }

                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_GETOFFERS", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "ITEMPRICE";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpPost]
        [Route("deletebilldetail")]
        public IActionResult DeleteBillDetail([FromBody] string jsonString)
        {
            try
            {
                DeleteBillDetail billDetail = JsonConvert.DeserializeObject<DeleteBillDetail>(jsonString);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BillDetailID", billDetail.BillDetailID},
                        { "UserID", billDetail.UserID},
                        { "SNos", billDetail.Snos},
                        { "BranchCounterID", billDetail.BranchCounterID},
                        { "BRANCHID", billDetail.BranchID}

                    };
                DataSet ds = new DataRepository().GetDatasetWithTransaction_test(configuration, "POS_USP_D_BILLDETAIL", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILLDETAIL";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpPost]
        [Route("savebilldetail")]
        public IActionResult SaveBillDetail([FromBody] string jsonString)
        {
            try
            {
                SaveBillDetail billDetail = JsonConvert.DeserializeObject<SaveBillDetail>(jsonString);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BillID", billDetail.BillID},
                        { "ItemPriceID", billDetail.ItemPriceID},
                        { "Quantity", billDetail.Quantity},
                        { "WeightInKgs", billDetail.WeightInKGS},
                        { "UserID", billDetail.UserID },
                        { "BillDetailID", billDetail.BillDetailID},
                        { "IsBillOfferItem", billDetail.IsBillOfferItem},
                        { "BillOfferPrice", billDetail.BillOfferPrice},
                        { "BranchCounterID", billDetail.BranchCounterID },
                        { "BranchID", billDetail.BranchID }
                    };
                DataSet ds = new DataRepository().GetDatasetWithTransaction_test(configuration, "POS_USP_CU_BILLDETAIL", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILLDETAIL";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getbilloffers")]
        public IActionResult GetBillOffers([FromQuery] int billID, [FromQuery] int BranchID, [FromQuery] int CounterID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BillID", billID},
                        { "BRANCHID", BranchID},
                        { "BRANCHCOUNTERID", CounterID},

                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_GETBILLOFFERS", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "OFFER";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getmop")]
        public IActionResult GetMOPs()
        {
            try
            {
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_MOP", false, null);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "MOP";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("finishbill")]
        public IActionResult FinishBill([FromBody] string jsonString)
        {
            try
            {
                FinishBill finishBill = JsonConvert.DeserializeObject<FinishBill>(jsonString);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "UserID", finishBill.UserID},
                        { "BillID", finishBill.BillID},
                        { "DaySequenceID", finishBill.DaySequenceID},
                        { "CustomerName", finishBill.CustomerName},
                        { "CustomerNumber", finishBill.CustomerNumber},
                        { "CustomerGST", finishBill.CustomerGST},
                        { "Rounding", finishBill.Rounding},
                        { "IsDoorDelivery", finishBill.IsDoorDelivery},
                        { "TenderedCash", finishBill.TenderedCash},
                        { "TenderedChange", finishBill.TenderedChange},
                        { "MopValues", finishBill.MopValues},
                        { "BRANCHCOUNTERID", finishBill.BranchCounterID},

                    };
                DataSet ds = new DataRepository().GetDatasetWithTransaction_test(configuration, "POS_USP_FINISH_BILL", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILL";
                    ds.Tables[1].TableName = "BILLDETAILS";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getdayclosure")]
        public IActionResult GetDayClosure([FromQuery] int CounterID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BRANCHCOUNTERID", CounterID}
                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_R_DAYCLOSURE", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (ds.Tables[0].Rows.Count == 1)
                        throw new Exception(Convert.ToString(ds.Tables[0].Rows[0][0]));
                    ds.Tables[0].TableName = "DENOMINATION";
                    ds.Tables[1].TableName = "MOP";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpPost]
        [Route("savedayclosure")]
        public IActionResult SaveDayClosure([FromBody] string jsonString)
        {

            try
            {
                SaveDayClosure dayClosure = JsonConvert.DeserializeObject<SaveDayClosure>(jsonString);
                //dsInput?.Tables[0].Columns.Remove("DISPLAYVALUE");
                //dsInput?.Tables[0].Columns.Remove("MULTIPLIER");
                //dsInput?.Tables[0].Columns.Remove("QUANTITY");
                //dsInput?.Tables[1].Columns.Remove("MOPNAME");

                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BRANCHCOUNTERID", dayClosure.BranchCounterID},
                        { "dtDenomination", dayClosure.Denominations},
                        { "dtMOP", dayClosure.MopValues},
                        { "RefundAmount", dayClosure.RefundAmount},
                        { "DaySequenceID", dayClosure.DaySequenceID},
                        { "USERID", dayClosure.UserID}
                    };
                object objReturn = new DataRepository().ExecuteScalarWithTransaction_test(configuration, "POS_USP_CU_DAYCLOSURE", false, parameters);
                if (int.TryParse(Convert.ToString(objReturn), out int DayClosureID))
                    return Ok(DayClosureID);
                else
                    throw new Exception("Error while saving day closure - " + Convert.ToString(objReturn));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getdayclosureforreport")]
        public IActionResult GetDayClosureForReport([FromQuery] int dayClosureID, [FromQuery] int CounterID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "DAYCLOSUREID", dayClosureID},
                        { "BRANCHCOUNTERID", CounterID}
                    };
                DataSet ds = new DataRepository().GetDataset_test(configuration, "POS_USP_RPT_DAYCLOSURE", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "DAYCLOSURE";
                    ds.Tables[1].TableName = "DENOMINATION";
                    ds.Tables[2].TableName = "MOP";
                    ds.Tables[3].TableName = "BILLS";
                    ds.Tables[4].TableName = "USERWISEMOP";
                    return Ok(Utility.GetJsonString(ds, null));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }
    }
}
