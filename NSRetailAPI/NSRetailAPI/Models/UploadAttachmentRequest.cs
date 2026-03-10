namespace NSRetailAPI.Models
{
    public class UploadAttachmentRequest
    {
        public int StockEntryID { get; set; }

        public IFormFile InvoiceAttachment { get; set; }

        public int UserID { get; set; }
    }
}
