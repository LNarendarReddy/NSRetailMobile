using System.Runtime.CompilerServices;

namespace NSRetailAPI.Models
{
    public class BillingBase
    {
        int userID;
        public int UserID { get { return userID; } set { userID = value; } }

        int billID;
        public int BillID { get { return billID; } set { billID = value; } }

        int billDetailID;
        public int BillDetailID { get { return billDetailID; } set { billDetailID = value; } }

        int branchCounterID;
        public int BranchCounterID { get { return branchCounterID; } set { branchCounterID = value; } }

        int branchID;
        public int BranchID { get { return branchID; } set { branchID = value; } }
    }
    public class SaveBillDetail: BillingBase
    {

        int itemPriceID;
        public int ItemPriceID { get { return itemPriceID; } set { itemPriceID = value; } }

        int quantity;
        public int Quantity { get { return quantity; } set { quantity = value; } }

        double weightInKGS;
        public double WeightInKGS { get { return weightInKGS; } set { weightInKGS = value; } }


        bool isBillOfferItem;
        public bool IsBillOfferItem { get { return isBillOfferItem; } set { isBillOfferItem = value; } }

        double? billOfferPrice;
        public double? BillOfferPrice { get { return billOfferPrice; } set { billOfferPrice = value; } }
    }

    public class DeleteBillDetail : BillingBase
    {
        List<Sno> snos;
        public List<Sno> Snos { get { return snos; } set { snos = value; } }
    }
    public class Sno
    {
        public int id;
    }

    public class FinishBill : BillingBase
    {
        int daySequenceID;
        public int DaySequenceID { get { return daySequenceID; } set { daySequenceID = value; } }

        string customerName;
        public string CustomerName { get { return customerName; } set { customerName = value; } }

        string customerNumber;
        public string CustomerNumber { get { return customerNumber; } set { customerNumber = value; } }

        string customerGST;
        public string CustomerGST { get { return customerGST; } set { CustomerGST = value; } }

        double rounding;
        public double Rounding { get { return rounding; } set { rounding = value; } }

        bool isDoorDelivery;
        public bool IsDoorDelivery { get { return isDoorDelivery; } set { isDoorDelivery = value; } }

        double tenderedCash;
        public double TenderedCash { get { return tenderedCash; } set { tenderedCash = value; } }

        double tenderedChange;
        public double TenderedChange { get { return tenderedChange; } set { tenderedCash = value; } }

        List<MOPValues> mOPValues;
        public List<MOPValues> MopValues { get { return mOPValues; } set { mOPValues = value; } }
    }
    public class MOPValues
    {
        int mopID;
        public int MopID { get {return mopID; } set { mopID = value; } }

        decimal mopValue;
        public decimal MopValue { get { return mopValue; } set {  mopValue = value; } }
    }

    public class SaveDayClosure : BillingBase
    {
        double refundAmount;
        public double RefundAmount { get { return refundAmount; } set { refundAmount = value; } }

        int daySequenceID;
        public int DaySequenceID { get { return daySequenceID; } set { daySequenceID = value; } }

        List<MOPValues> mOPValues;
        public List<MOPValues> MopValues { get { return mOPValues; } set { mOPValues = value; } }

        List<MOPValues> denominations;
        public List<MOPValues> Denominations { get { return denominations; } set { denominations = value; } }
    }
}
