using System.Runtime.CompilerServices;

namespace NSRetailAPI.Models
{
    public class BillingBase
    {
        public int UserID { get; set; }

        public int BillID { get; set; }

        public int BillDetailID { get; set; }

        public int BranchCounterID { get; set; }

        public int BranchID { get; set; }
    }
    public class SaveBillDetail : BillingBase
    {

        public int ItemPriceID { get; set; }

        public int Quantity { get; set; }

        public double WeightInKGS { get; set; }

        public bool IsBillOfferItem { get; set; }

        public double? BillOfferPrice { get; set; }
    }

    public class DeleteBillDetail : BillingBase
    {
        public List<Sno> Snos { get; set; }
    }
    public class Sno
    {
        public int id;
    }

    public class FinishBill : BillingBase
    {
        public int DaySequenceID { get; set; }

        public string CustomerName { get; set; }

        public string CustomerNumber { get; set; }

        public string CustomerGST { get; set; }

        public double Rounding { get; set; }

        public bool IsDoorDelivery { get; set; }

        public double TenderedCash { get; set; }

        public double TenderedChange { get; set; }

        public List<MOPValues> MopValues { get; set; }
    }
    public class MOPValues
    {
        public int MopID { get; set; }

        public decimal MopValue { get; set; }
    }

    public class SaveDayClosure : BillingBase
    {
        public double RefundAmount { get; set; }

        public int DaySequenceID { get; set; }

        public List<MOPValues> MopValues { get; set; }

        public List<MOPValues> Denominations { get; set; }
    }
}
