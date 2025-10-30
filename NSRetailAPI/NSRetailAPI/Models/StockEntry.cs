namespace NSRetailAPI.Models
{
    public class StockEntry
    {
        public int STOCKENTRYID { get; set; }
        public int SUPPLIERID { get; set; }
        public string? SUPPLIERINVOICENO { get; set; }
        public bool TAXINCLUSIVE { get; set; }
        public DateTime InvoiceDate { get; set; }
        public decimal TCS { get; set; }
        public decimal DISCOUNTPER { get; set; }
        public decimal DISCOUNTFLAT { get; set; }
        public decimal EXPENSES { get; set; }
        public decimal TRANSPORT { get; set; }
        public int CATEGORYID { get; set; }
        public int UserID { get; set; }
        public int SupplierIndentId { get; set; }
    }
}
