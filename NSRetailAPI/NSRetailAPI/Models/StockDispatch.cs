namespace NSRetailAPI.Models
{
    public class StockDispatch
    {
        public int STOCKDISPATCHID { get; set; }
        public int FROMBRANCHID { get; set; }
        public int TOBRANCHID { get; set; }
        public int CATEGORYID { get; set; }
        public int SUBCATEGORYID { get; set; }
        public int USERID { get; set; }
    }
}
