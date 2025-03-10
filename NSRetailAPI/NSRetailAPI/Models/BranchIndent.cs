namespace NSRetailAPI.Models
{
    public class BranchIndent
    {
        public int BRANCHINDENTID { get; set; }
        public int FROMBRANCHID { get; set; }
        public int TOBRANCHID { get; set; }
        public int CATEGORYID { get; set; }
        public int SUBCATEGORYID { get; set; }
        public int USERID { get; set; }
        public int NOOFDAYS { get; set; }
        public List<BranchIndentDetail> branchIndentDetailList { get; set; }
    }
    public class BranchIndentDetail
    {
        public int ITEMID { get; set; }
        public double BRANCHSTOCK { get; set; }
        public double AVGSALES { get; set; }
        public double NOOFDAYSSALES { get; set; }
        public double INDENTQUANTITY { get; set; }
        public DateTime? LASTDISPATCHDATE { get; set; }
    }
}