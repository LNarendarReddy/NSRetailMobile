namespace NSRetailAPI.Models
{
    public class BharatPePaymentPostingModel
    {
        public string name { get; set; }
        public string merchantId { get; set; }
        public string businessName { get; set; }
        public string addressLine1 { get; set; }
        public string addressLine2 { get; set; }
        public string transactionDate { get; set; }
        public string transactionTime { get; set; }
        public string transactionRRN { get; set; }
        public string transactionAmount { get; set; }
        public string transactionAuthCode { get; set; }
        public string transactionCardNumber { get; set; }
        public string transactionTerminalId { get; set; }
        public string invoiceNumber { get; set; }
        public string transactionStatus { get; set; }
        public string transactionID { get; set; }
        public string billNumber { get; set; }
        public string cardType { get; set; }
        public string responseCode { get; set; }
        public string narration { get; set; }
        public string transactionTypeName { get; set; }
        public string checksum { get; set; }
    }
}