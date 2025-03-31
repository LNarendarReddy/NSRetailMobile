using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Data;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class StockEntry_v2Controller : ControllerBase
    {
        public readonly IConfiguration configuration;
        public StockEntry_v2Controller(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpGet]
        [Route("getsupplier")]
        public IActionResult GetSupplier()
        {
            try
            {
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_DEALER_v2", true, null);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "Holder";
                    ds.Tables[1].TableName = "SUPPLIER";
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
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("getgst")]
        public IActionResult GetGST()
        {
            try
            {
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_GSTLIST_v2", true, null);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "Holder";
                    ds.Tables[1].TableName = "GST";
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
                return BadRequest(ex.Message);
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
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMDATAFORSTOCKENTRY_v2", true, parameters);
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
                        return BadRequest(str);
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
        [Route("getinvoice")]
        public IActionResult GetInvoice(int CategoryID, int StockEntryID, int UserID, bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "CATEGORYID",CategoryID }
                    ,{ "STOCKENTRYID",StockEntryID }
                    ,{ "USERID",UserID }
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_STOCKENTRYDTAFT", UseWHConnection, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (!int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                        return NotFound("No stock entry draft found!");
                    else
                    {
                        ds.Tables[0].TableName = "StockEntry";
                        if (ds.Tables.Count > 1)
                            ds.Tables[1].TableName = "StockEntryDetail";
                        return Ok(JsonConvert.SerializeObject(ds));
                    }
                }
                else
                {
                    return NotFound("No stock entry draft found!");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("saveinvoice")]
        public IActionResult SaveInvoice(string jsonstring, bool UseWHConnection)
        {
            try
            {

                StockEntry stockEntry = JsonConvert.DeserializeObject<StockEntry>(jsonstring);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYID", stockEntry.STOCKENTRYID }
                        ,{ "SUPPLIERID", stockEntry.SUPPLIERID }
                        ,{ "SUPPLIERINVOICENO", stockEntry.SUPPLIERINVOICENO }
                        ,{ "TAXINCLUSIVE", stockEntry.TAXINCLUSIVE }
                        ,{ "INVOICEDATE", stockEntry.InvoiceDate }
                        ,{ "TCS", stockEntry.TCS }
                        ,{ "DISCOUNTPER", stockEntry.DISCOUNTPER }
                        ,{ "DISCOUNT", stockEntry.DISCOUNTFLAT }
                        ,{ "EXPENSES", stockEntry.EXPENSES }
                        ,{ "TRANSPORT", stockEntry.TRANSPORT}
                        ,{ "CATEGORYID", stockEntry.CATEGORYID }
                        ,{ "USERID", stockEntry.UserID }
                };
                object obj = new DataRepository().ExecuteScalar(configuration, "USP_CU_STOCKENTRY", UseWHConnection, parameters);
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
        [Route("saveinvoicedetail")]
        public IActionResult saveinvoicedetail(string jsonstring, bool UseWHConnection)
        {
            try
            {
                StockEntryDetail stockEntryDetail = JsonConvert.DeserializeObject<StockEntryDetail>(jsonstring);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYDETAILID", stockEntryDetail.STOCKENTRYDETAILID }
                    , { "STOCKENTRYID", stockEntryDetail.STOCKENTRYID }
                    , { "ITEMCODEID", stockEntryDetail.ITEMCODEID }
                    , { "COSTPRICEWT", stockEntryDetail.COSTPRICEWT }
                    , { "COSTPRICEWOT", stockEntryDetail.COSTPRICEWOT }
                    , { "MRP", stockEntryDetail.MRP }
                    , { "SALEPRICE", stockEntryDetail.SALEPRICE }
                    , { "QUANTITY", stockEntryDetail.QUANTITY }
                    , { "WEIGHTINKGS", stockEntryDetail.WEIGHTINKGS }
                    , { "USERID", stockEntryDetail.UserID }
                    , { "GSTID", stockEntryDetail.GSTID }
                    , { "FREEQUANTITY", stockEntryDetail.FreeQuantity}
                    , { "DISCOUNTFLAT", stockEntryDetail.DiscountFlat }
                    , { "DISCOUNTPERCENTAGE", stockEntryDetail.DiscountPercentage }
                    , { "SCHEMEPERCENTAGE", stockEntryDetail.SchemePercentage }
                    , { "SCHEMEFLAT", stockEntryDetail.SchemeFlat }
                    , { "TOTALPRICEWT", stockEntryDetail.TotalPriceWT }
                    , { "TOTALPRICEWOT", stockEntryDetail.TotalPriceWOT }
                    , { "APPLIEDDISCOUNT", stockEntryDetail.AppliedDiscount }
                    , { "APPLIEDSCHEME", stockEntryDetail.AppliedScheme }
                    , { "APPLIEDDGST", stockEntryDetail.AppliedGST }
                    , { "FINALPRICE", stockEntryDetail.FinalPrice }
                    , { "CGST", stockEntryDetail.CGST }
                    , { "SGST", stockEntryDetail.SGST }
                    , { "IGST", stockEntryDetail.IGST }
                    , { "CESS", stockEntryDetail.CESS }
                    , { "HSNCODE", stockEntryDetail.HSNCODE }
                };
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_CU_STOCKENTRYDETAIL", UseWHConnection, parameters);

                if (dt != null && dt.Rows.Count > 0)
                {
                    dt.TableName = "stockentrydetail";
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        throw new Exception(str);
                    else
                        return Ok("Successfully saved");
                }
                else
                    throw new Exception("Data not found");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("deleteinvoicedetail")]
        public IActionResult DeleteInvoiceDetail(int StockEntryDetailID, int UserID, bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYDETAILID", StockEntryDetailID}
                    ,{ "UserID", UserID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_STOCKENTRYDETAIL", UseWHConnection, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while deleting item!");
                else
                    return Ok("Item successfully deleted");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("updateinvoice")]
        public IActionResult UpdateInvoice(string jsonstring, bool UseWHConnection)
        {
            try
            {
                StockEntry stockEntry = JsonConvert.DeserializeObject<StockEntry>(jsonstring);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYID", stockEntry.STOCKENTRYID }
                        ,{ "TCS", stockEntry.TCS }
                        ,{ "DISCOUNTPER", stockEntry.DISCOUNTPER }
                        ,{ "DISCOUNTFLAT", stockEntry.DISCOUNTFLAT }
                        ,{ "EXPENSES", stockEntry.EXPENSES }
                        ,{ "TRANSPORT", stockEntry.TRANSPORT}
                };

                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_U_STOCKENTRY", UseWHConnection, parameters, true);

                if (rowsaffected == 0)
                    throw new Exception("Error while submitting stock entry!");
                else
                    return Ok("Stock entry submitted successfully");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost]
        [Route("discardinvoice")]
        public IActionResult DiscardInvoice(int StockEntryID, int UserID, bool UseWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYID", StockEntryID}
                        ,{ "UserID", UserID}
                };

                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_DISCARDSTOCKENTRY", UseWHConnection, parameters);

                if (rowsaffected == 0)
                    throw new Exception("Error while discarding invoice!");
                else
                    return Ok("Stock entry discarded successfully!");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

    }
}
