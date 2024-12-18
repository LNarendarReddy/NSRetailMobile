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

        [HttpPost]
        [Route("savecounteridentifier")]
        public IActionResult SaveCounterIdentifier([FromQuery] string Identifier, [FromQuery] int CounterID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "IDENTIFIER", Identifier },
                        { "COUNTERID", CounterID}

                    };
                object objReturn = new DataRepository().ExecuteScalar(configuration, "POS_USP_U_COUNTERIDENTIFIER", false, parameters);
                if (int.TryParse(Convert.ToString(objReturn), out int BranchCounterID) && BranchCounterID > 0)
                    return Ok(BranchCounterID);
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("getcounterbyidentifier")]
        public IActionResult GetCountersByIdentifier([FromQuery] string Identifier, [FromQuery] int BranchID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "IDENTIFIER", Identifier },
                        {"BranchID", BranchID }


                    };
                object objReturn = new DataRepository().ExecuteScalar(configuration, "POS_USP_R_BRANCHCOUNTERIDBYIDENTIFIER", false, parameters);
                if (int.TryParse(Convert.ToString(objReturn), out int CounterID) && CounterID > 0)
                    return Ok(CounterID);
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
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
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_BRANCHCOUNTER", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BRANCH";
                    ds.Tables[1].TableName = "BRANCHCOUNTER";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "BRANCHID", "BRANCHID" } }));
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
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMDATAFORBILLING", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                    {
                        ds.Tables[0].TableName = "ITEM";
                        ds.Tables[1].TableName = "ITEMCODE";
                        ds.Tables[2].TableName = "ITEMPRICE";
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
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_LOAD", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "DAYSEQUENCE";
                    if (ds.Tables.Count > 1)
                    {
                        ds.Tables[1].TableName = "BILL";
                        ds.Tables[2].TableName = "BILLDETAIL";
                    }
                    else if (ds.Tables.Count == 1 && ds.Tables[0].Rows.Count == 1
                    && ds.Tables[0].Columns.Count == 1)
                    {
                        // this means it is a validation message
                        return BadRequest(ds.Tables[0].Rows[0][0].ToString());
                    }

                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "BRANCHCOUNTERID", "BRANCHCOUNTERID" }, { "BILLID", "BILLID" } }));
                }
                else
                {
                    return NotFound("Data not found");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
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
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_BILL", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILL";
                    ds.Tables[1].TableName = "BILLDETAIL";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "BILLID", "BILLID" } }));
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
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_GETOFFERS", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "HOLDER";
                    ds.Tables[1].TableName = "OFFER";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
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
        public IActionResult DeleteBillDetail([FromQuery] string jsonString)
        {
            try
            {
                DeleteBillDetail billDetail = JsonConvert.DeserializeObject<DeleteBillDetail>(jsonString);

                DataTable dtSNos = new DataTable();
                dtSNos.Columns.Add("billdetailid", typeof(int));
                dtSNos.Columns.Add("sno", typeof(int));

                billDetail.Snos.ToList().ForEach(x => dtSNos.Rows.Add(x.billdetailid, x.sno));

                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BillDetailID", billDetail.BillDetailID},
                        { "UserID", billDetail.UserID},
                        { "SNos", dtSNos },
                        { "BranchCounterID", billDetail.BranchCounterID},
                        { "BRANCHID", billDetail.BranchID}

                    };
                DataSet ds = new DataRepository().GetDatasetWithTransaction(configuration, "POS_USP_D_BILLDETAIL", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILL";
                    ds.Tables[1].TableName = "BILLDETAIL";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "BILLID", "BILLID" } }));
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
        public IActionResult SaveBillDetail([FromQuery] string jsonString)
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
                DataSet ds = new DataRepository().GetDatasetWithTransaction(configuration, "POS_USP_CU_BILLDETAIL", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILL";
                    ds.Tables[1].TableName = "BILLDETAIL";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "BILLID", "BILLID" } }));
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
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_GETBILLOFFERS", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "HOLDER";
                    ds.Tables[1].TableName = "OFFER";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
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
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_MOP", false, null);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "HOLDER";
                    ds.Tables[1].TableName = "MOP";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }));
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
        [Route("finishbill")]
        public IActionResult FinishBill([FromQuery] string jsonString)
        {
            try
            {
                Bill finishBill = JsonConvert.DeserializeObject<Bill>(jsonString);
                DataTable dt = new();
                dt.Columns.Add("MOPID", typeof(int));
                dt.Columns.Add("MOPVALUE", typeof(decimal));

                finishBill.MopValueList.ForEach(x => dt.Rows.Add(x.MopID, x.MopValue));

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
                        { "MopValues", dt},
                        { "BRANCHCOUNTERID", finishBill.BranchCounterID},

                    };
                DataSet ds = new DataRepository().GetDatasetWithTransaction(configuration, "POS_USP_FINISH_BILL", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "BILL";
                    ds.Tables[1].TableName = "BILLDETAIL";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "BILLID", "BILLID" } }));
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
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_R_DAYCLOSURE", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out int Ivalue))
                    {
                        ds.Tables[0].TableName = "HOLDER";
                        ds.Tables[1].TableName = "DENOMINATION";
                        ds.Tables[2].TableName = "MOP";
                        ds.Tables[3].TableName = "REFUND";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }, false));
                    }
                    else
                        return BadRequest(str);
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
        public IActionResult SaveDayClosure([FromQuery] string jsonString)
        {

            try
            {
                SaveDayClosure dayClosure = JsonConvert.DeserializeObject<SaveDayClosure>(jsonString);

                DataTable dtDenominations = new();
                dtDenominations.Columns.Add("MOPID", typeof(int));
                dtDenominations.Columns.Add("MOPVALUE", typeof(decimal));

                DataTable dtMOP = new();
                dtMOP.Columns.Add("MOPID", typeof(int));
                dtMOP.Columns.Add("MOPVALUE", typeof(decimal));

                dayClosure.Denominations.ToList().ForEach(x => dtDenominations.Rows.Add(x.MopID, x.MopValue));
                dayClosure.MopValues.ToList().ForEach(x => dtMOP.Rows.Add(x.MopID, x.MopValue));

                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "BRANCHCOUNTERID", dayClosure.BranchCounterID},
                        { "dtDenomination", dtDenominations},
                        { "dtMOP", dtMOP},
                        { "RefundAmount", dayClosure.RefundAmount},
                        { "DaySequenceID", dayClosure.DaySequenceID},
                        { "USERID", dayClosure.UserID}
                    };
                object objReturn = new DataRepository().ExecuteScalarWithTransaction(configuration, "POS_USP_CU_DAYCLOSURE", false, parameters);
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
                DataSet ds = new DataRepository().GetDataset(configuration, "POS_USP_RPT_DAYCLOSURE", false, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "DAYCLOSURE";
                    ds.Tables[1].TableName = "DENOMINATION";
                    ds.Tables[2].TableName = "MOP";
                    ds.Tables[3].TableName = "BILL";
                    ds.Tables[4].TableName = "USERWISEMOP";
                    return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "PARENTID", "PARENTID" } }, false));
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
