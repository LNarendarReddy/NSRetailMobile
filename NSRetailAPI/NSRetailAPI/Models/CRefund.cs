namespace NSRetailAPI.Models
{
    public class CRefund
    {
        public class CRefundDetail
        {
            public int BillDetailId { get; set; }
            public int RefundQuantity { get; set; }
            public double REFUNDWEIGHTINKGS { get; set; }
            public double RefundAmount { get; set; }
        }

        public class SaveCRefund
        {
            public int UserId { get; set; }
            public int BillId { get; set; }
            public string CustomerName { get; set; }
            public string CustomerMobile { get; set; }
            public int BranchCounterId { get; set; }
            public List<CRefundDetail> BillDetailList { get; set; }
        }
    }
}
