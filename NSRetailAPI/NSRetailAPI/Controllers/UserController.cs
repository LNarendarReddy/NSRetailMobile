using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Formatters;
using Newtonsoft.Json;
using NSRetailAPI.Utilities;
using System.Data;
using System.Data.SqlClient;
using System.Text;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class userController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public userController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }
        [HttpPost]
        [Route("getuserlogin")]
        public IActionResult GetUserLogin(string UserName, string Password, string AppVersion)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERNAME", UserName }
                        , { "PASSWORD", Utility.Encrypt(Password) }
                        , { "APPVERSION", AppVersion }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_USERLOGIN_Mobile",  false,parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "User";
                    if (ds.Tables.Count > 1)
                        ds.Tables[1].TableName = "FeatureAccess";
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(ds));
                }
                else
                    return NotFound("User does not exists");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }
    }
}
