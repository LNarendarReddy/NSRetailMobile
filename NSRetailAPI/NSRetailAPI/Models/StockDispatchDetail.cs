namespace NSRetailAPI.Models
{
    public class StockDispatchDetail
    {
        public int STOCKDISPATCHDETAILID { get; set; }
        public int STOCKDISPATCHID { get; set; }
        public int ITEMPRICEID { get; set; }
        public int TRAYNUMBER { get; set; }
        public int DISPATCHQUANTITY { get; set; }
        public decimal WEIGHTINKGS { get; set; }
        public int USERID { get; set; }
    }
}
