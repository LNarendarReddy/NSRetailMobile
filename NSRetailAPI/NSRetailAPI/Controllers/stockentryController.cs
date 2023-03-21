using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Collections.Generic;
using System.Data;
using System.Security.Cryptography.Xml;
using System.Text.Json.Nodes;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class stockentryController : ControllerBase
    {
        public readonly IConfiguration configuration;

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
                    ds.Tables[0].TableName = "StockEntry";
                    if (ds.Tables.Count > 1)
                        ds.Tables[1].TableName = "StockEntryDetail";
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

        public stockentryController(IConfiguration _configuration)
        {
            configuration = _configuration;
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
        [Route("saveinvoicedetail")]
        public IActionResult saveinvoicedetail(string jsonstring, bool UseWHConnection)
        {
            try
            {
                StockEntryDetail stockEntryDetail = JsonConvert.DeserializeObject<StockEntryDetail>(jsonstring);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYDETAILID", stockEntryDetail.STOCKENTRYID }
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
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
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
        [Route("updateinvoice")]
        public IActionResult UpdateInvoice(string jsonstring, bool UseWHConnetion)
        {
            try
            {
                StockEntry stockEntry = JsonConvert.DeserializeObject<StockEntry>(jsonstring);
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYID", stockEntry.STOCKENTRYID }
                        ,{ "TCS", stockEntry.TCS }
                        ,{ "DISCOUNTPER", stockEntry.DISCOUNTPER }
                        ,{ "DISCOUNT", stockEntry.DISCOUNTFLAT }
                        ,{ "EXPENSES", stockEntry.EXPENSES }
                        ,{ "TRANSPORT", stockEntry.TRANSPORT}
                };

                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_U_STOCKENTRY", UseWHConnetion, parameters);

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
        [Route("discardinvoice")]
        public IActionResult DiscardInvoice(int StockEntryID, int UserID, bool UseWHConnetion)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYID", StockEntryID}
                        ,{ "UserID", UserID}
                };

                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_DISCARDSTOCKENTRY", UseWHConnetion, parameters);

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
