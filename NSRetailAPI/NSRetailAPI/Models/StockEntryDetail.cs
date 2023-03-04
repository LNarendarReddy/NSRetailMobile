namespace NSRetailAPI.Models
{
    public class StockEntryDetail
    {
        public int STOCKENTRYDETAILID { get; set; }
        public int STOCKENTRYID { get; set; }
        public int ITEMCODEID { get; set; }
        public decimal COSTPRICEWT { get; set; }
        public decimal COSTPRICEWOT { get; set; }
        public decimal MRP { get; set; }
        public decimal SALEPRICE { get; set; }
        public int QUANTITY { get; set; }
        public decimal WEIGHTINKGS { get; set; }
        public int UserID { get; set; }
        public int GSTID { get; set; }
        public int FreeQuantity { get; set; }
        public decimal DiscountFlat { get; set; }
        public decimal DiscountPercentage { get; set; }
        public decimal SchemePercentage { get; set; }
        public decimal SchemeFlat { get; set; }
        public decimal TotalPriceWT { get; set; }
        public decimal TotalPriceWOT { get; set; }
        public decimal AppliedDiscount { get; set; }
        public decimal AppliedScheme { get; set; }
        public decimal AppliedGST { get; set; }
        public decimal FinalPrice { get; set; }
        public decimal CGST { get; set; }
        public decimal SGST { get; set; }
        public decimal IGST { get; set; }
        public decimal CESS { get; set; }
        public string? HSNCODE { get; set; }
    }
}
