using Microsoft.AspNetCore.Mvc;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using Newtonsoft.Json;
using System.Security.Cryptography;
using System.Text;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class GatewayController : ControllerBase
    {
        public readonly IConfiguration configuration;

        public GatewayController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        [HttpPost]
        [Route("bharatpepaymentposting")]
        public IActionResult BharatPePaymentPosting([FromBody] BharatPePaymentPostingModel model)
        {
            try
            {
                if (model == null)
                    return BadRequest("Invalid request");

                string saltValue =
                    AppCache.PaymentGatewaySecrets[
                        PaymentGatewayConstants.BharatPe];

                string generatedChecksum = GenerateChecksum(
                    model.transactionID,
                    model.merchantId,
                    model.transactionRRN,
                    saltValue
                );

                if (!generatedChecksum.Equals(
                        model.checksum,
                        StringComparison.OrdinalIgnoreCase))
                {
                    return Unauthorized("Checksum validation failed");
                }

                Dictionary<string, object> parameters = new()
                {
                    { "PAYMENTGATEWAYINFOID", PaymentGatewayConstants.BharatPe },
                    { "BILLNUMBER", model.billNumber },
                    { "RAWDATA", JsonConvert.SerializeObject(model) }
                };

                int rowsaffected = new DataRepository().ExecuteNonQuery(
                    configuration,
                    "POS_USP_I_GATEWAYPAYMENTPOSTING",
                    true,
                    parameters,
                    true);

                if (rowsaffected == 0)
                    throw new Exception(
                        "Error while saving BharatPe transaction");

                return Ok(new
                {
                    status = 200,
                    message = "success",
                    merchant_refTxnId = model.billNumber
                });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        private static string GenerateChecksum(
            string transactionId,
            string merchantId,
            string transactionRRN,
            string saltValue)
        {
            string rawString =
                transactionId +
                merchantId +
                transactionRRN +
                saltValue;

            using (SHA512 sha512 = SHA512.Create())
            {
                byte[] bytes = Encoding.UTF8.GetBytes(rawString);
                byte[] hash = sha512.ComputeHash(bytes);

                StringBuilder builder = new StringBuilder();

                foreach (byte b in hash)
                {
                    builder.Append(b.ToString("x2"));
                }

                return builder.ToString();
            }
        }
    }
}