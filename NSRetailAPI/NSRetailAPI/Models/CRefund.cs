namespace NSRetailAPI.Models
{
    public class CRefund
    {
        public class CRefundDetail
        {
            public int BILLDETAILID { get; set; }
            public int REFUNDQUANTITY { get; set; }
            public double REFUNDWEIGHTINKGS { get; set; }
            public double REFUNDAMOUNT { get; set; }
        }

        public class SaveCRefund
        {
            public int UserID { get; set; }
            public int BillID { get; set; }
            public string CustomerName { get; set; }
            public string CustomerPhone { get; set; }
            public int BranchCounterID { get; set; }
            public List<CRefundDetail> CRefundDetail { get; set; }
        }
    }
}
