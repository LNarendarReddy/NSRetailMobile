using System.Data;

namespace NSRetailAPI.Utilities
{
    public static class PaymentGatewayLoader
    {
        public static void LoadPaymentGatewaySecrets(
            IConfiguration configuration)
        {
            Dictionary<string, object> parameters =
                new Dictionary<string, object>();

            DataSet ds = new DataRepository().GetDataset(
                configuration,
                "POS_USP_R_PAYMENTGATEWAYINFO",
                true,
                parameters);

            if (ds == null ||
                ds.Tables.Count == 0 ||
                ds.Tables[0].Rows.Count == 0)
                return;

            AppCache.PaymentGatewaySecrets.Clear();

            foreach (DataRow row in ds.Tables[0].Rows)
            {
                int paymentGatewayInfoID =
                    Convert.ToInt32(row["PAYMENTGATEWAYINFOID"]);

                string merchantSecret =
                    Convert.ToString(row["MERCHANTSECRET"]);

                AppCache.PaymentGatewaySecrets[paymentGatewayInfoID]
                    = merchantSecret;
            }
        }
    }
}