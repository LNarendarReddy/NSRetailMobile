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
        public IActionResult GetSupplier([FromQuery] string searchKey)
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
        [Route("getcategory")]
        public IActionResult GetCategory()
        {

            try
            {
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_CATEGORY_v2", true, null);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "Holder";
                    ds.Tables[1].TableName = "Category";
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
        [Route("getindentlist")]
        public IActionResult GetSupplierIndentList([FromQuery] int supplierId, [FromQuery] int categoryId)
        {
            try
            {
                Dictionary<string, object> parameters = new()
                {
                        { "SUPPLIERID", supplierId },
                        { "CATEGORYID", categoryId }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_SUPPLIERINDENTLIST", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out int Ivalue))
                    {
                        ds.Tables[0].TableName = "Holder";
                        ds.Tables[1].TableName = "SupplierIndent";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string> { { "PARENTID", "PARENTID" } }, true));
                    }
                    else
                        return BadRequest(str);
                }
                else
                    throw new Exception("Indent does not exists");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet]
        [Route("getinvoice")]
        public IActionResult GetInvoice(int CategoryID, int StockEntryID, int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                     { "CATEGORYID",CategoryID }
                    ,{ "STOCKENTRYID",StockEntryID }
                    ,{ "USERID",UserID }
                };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_STOCKENTRYDTAFT_v2", true, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    if (!int.TryParse(Convert.ToString(ds.Tables[0].Rows[0][0]), out int ivalue))
                        return NotFound("No stock entry draft found!");
                    else
                    {
                        ds.Tables[0].TableName = "StockEntry";
                        if (ds.Tables.Count > 1)
                            ds.Tables[1].TableName = "StockEntryDetail";
                        return Ok(Utility.GetJsonString(ds, new Dictionary<string, string>() { { "STOCKENTRYID", "STOCKENTRYID" } }));
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
        [Route("getitembycode")]
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

        [HttpPost]
        [Route("saveinvoice")]
        public async Task<IActionResult> SaveInvoice([FromBody] StockEntry stockEntry)
        {
            try
            {
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
                        ,{ "SupplierIndentID", stockEntry.SupplierIndentId }
                };
                object obj = await Task.Run(() => new DataRepository().ExecuteScalar(configuration, "USP_CU_STOCKENTRY", true, parameters));
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
        public async Task<IActionResult> SaveInvoiceDetail([FromBody] StockEntryDetail stockEntryDetail)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "STOCKENTRYDETAILID", stockEntryDetail.STOCKENTRYDETAILID },
                    { "STOCKENTRYID", stockEntryDetail.STOCKENTRYID },
                    { "ITEMCODEID", stockEntryDetail.ITEMCODEID },
                    { "QUANTITY", stockEntryDetail.QUANTITY },
                    { "WEIGHTINKGS", stockEntryDetail.WEIGHTINKGS },
                    { "MRP", stockEntryDetail.MRP },
                    { "SALEPRICE", stockEntryDetail.SALEPRICE },
                    { "COSTPRICEWT", stockEntryDetail.COSTPRICEWT },
                    { "COSTPRICEWOT", stockEntryDetail.COSTPRICEWOT },
                    { "INVOICECPWITHTAX", stockEntryDetail.InvoiceCPWithTax },      
                    { "INVOICECPWITHOUTTAX", stockEntryDetail.InvoiceCPWithoutTax },
                    { "GSTID", stockEntryDetail.GSTID },
                    { "DISCOUNTFLAT", stockEntryDetail.DiscountFlat },
                    { "DISCOUNTPERCENTAGE", stockEntryDetail.DiscountPercentage },
                    { "SCHEMEPERCENTAGE", stockEntryDetail.SchemePercentage },
                    { "SCHEMEFLAT", stockEntryDetail.SchemeFlat },
                    { "TOTALPRICEWT", stockEntryDetail.TotalPriceWT },
                    { "TOTALPRICEWOT", stockEntryDetail.TotalPriceWOT },
                    { "APPLIEDDISCOUNT", stockEntryDetail.AppliedDiscount },
                    { "APPLIEDSCHEME", stockEntryDetail.AppliedScheme },
                    { "APPLIEDDGST", stockEntryDetail.AppliedGST },
                    { "FINALPRICE", stockEntryDetail.FinalPrice },
                    { "CGST", stockEntryDetail.CGST },
                    { "SGST", stockEntryDetail.SGST },
                    { "IGST", stockEntryDetail.IGST },
                    { "CESS", stockEntryDetail.CESS },
                    { "HSNCODE", stockEntryDetail.HSNCODE },
                    { "USERID", stockEntryDetail.UserID }
                };
                DataTable dt = await Task.Run(() => new DataRepository().GetDataTable(configuration, "USP_CU_STOCKENTRYDETAIL_v2", true, parameters));

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
        public IActionResult DeleteInvoiceDetail(int StockEntryDetailID, int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "STOCKENTRYDETAILID", StockEntryDetailID},
                    { "UserID", UserID}
                };
                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_STOCKENTRYDETAIL_V2", true, parameters);

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
        public IActionResult UpdateInvoice(string stockEntryId)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYID", stockEntryId }
                };

                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_U_STOCKENTRY_MOBILESUBMISSION", true, parameters);

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
        public IActionResult DiscardInvoice(int StockEntryID, int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                        { "STOCKENTRYID", StockEntryID}
                        ,{ "UserID", UserID}
                };

                int rowsaffected = new DataRepository().ExecuteNonQuery(configuration, "USP_D_DISCARDSTOCKENTRY_v2", true, parameters, true);

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

        [HttpPost]
        [Route("uploadattachment")]
        public async Task<IActionResult> UploadAttachment([FromForm] UploadAttachmentRequest request)
        {
            try
            {
                if (request.InvoiceAttachment == null || request.InvoiceAttachment.Length == 0)
                    throw new Exception("No file uploaded!");

                byte[] fileBytes;
                using (var memoryStream = new MemoryStream())
                {
                    request.InvoiceAttachment.CopyTo(memoryStream);
                    fileBytes = memoryStream.ToArray();
                }

                Dictionary<string, object> parameters = new Dictionary<string, object>
                {
                    { "STOCKENTRYID", request.StockEntryID },
                    { "INVOICEATTACHMENT", fileBytes },
                    { "USERID", request.UserID }
                };

                object obj = await Task.Run(() => new DataRepository().ExecuteScalar(configuration, "USP_CU_STOCKENTRYATTACHMENTS", true, parameters));
                string str = Convert.ToString(obj);
                if (!int.TryParse(str, out int attachmentId))
                    throw new Exception(str);
                else
                    return Ok(new { message = "Attachment uploaded successfully", attachmentId = attachmentId });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
