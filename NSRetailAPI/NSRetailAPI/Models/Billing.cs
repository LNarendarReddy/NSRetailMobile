using System.Collections.ObjectModel;
using System.Runtime.CompilerServices;

namespace NSRetailAPI.Models
{
    internal class RootClass
    {
        public HolderClass Holder;
    }
    public class HolderClass
    {

        public DayClosure dayClosure;
    }
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
        public List<BillDetailSNo> Snos { get; set; }
    }
    public class BillDetailSNo
    {
        public int billdetailid;

        public int sno;

    }

    public class Bill : BillingBase
    {
        public int DaySequenceID { get; set; }

        public string CustomerName { get; set; }

        public string CustomerNumber { get; set; }

        public string CustomerGST { get; set; }

        public double Rounding { get; set; }

        public bool IsDoorDelivery { get; set; }

        public double TenderedCash { get; set; }

        public double TenderedChange { get; set; }

        public List<MOPValues> MopValueList { get; set; }
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

    public class DayClosure
    {
        public int DayClosureID;

        public DateTime ClosureDate;

        public decimal OpeningBalance;

        public decimal ClosingBalance;

        public decimal ClosingDifference;

        public int ClosedBy;

        public decimal RefundAmount;

        public DateTime CreatedDate;

        public int CompletedBills;

        public int DraftBills;

        public int VoidItems;

        public string Address;

        public string PhoneNo;

        public string BranchName;

        public string CounterName;

        public string UserName;

        public List<Denomination> DenominationsList;

        public List<MOP> MopValuesList;

        public List<UserMOPBreakDown> UserWiseMopBreakDownList;

    }

    public class Denomination
    {
        public int DenominationId;
        
        public string DisplayValue;

        public decimal Multiplier;

        public int Quantity;

        public decimal ClosureValue;
    }

    public class MOP
    {
        public int MOPId;

        public string MOPName;

        public decimal MOPValue;
    }

    public partial class UserMOPBreakDown
    {
        public string UserName;

        public string MopName;

        public decimal MopValue;
    }

}
