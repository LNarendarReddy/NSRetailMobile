using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Formatters;
using Newtonsoft.Json;
using NSRetailAPI.Models;
using NSRetailAPI.Utilities;
using System.Data;
using System.Data.SqlClient;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Text;
using System.Xml;

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
        public IActionResult GetUserLogin(string UserName, string Password, string AppVersion, bool isNested = false)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERNAME", UserName }
                        , { "PASSWORD", Utility.Encrypt(Password) }
                        , { "APPVERSION", AppVersion }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, isNested ? "USP_R_USERLOGIN_Mobile_v2" : "USP_R_USERLOGIN_Mobile",  false,parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "User";
                    if (ds.Tables.Count > 1)
                        ds.Tables[1].TableName = isNested ? "FeatureAccesses" : "FeatureAccess";
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (int.TryParse(str, out Ivalue))
                        if (isNested)
                            return Ok(Utilities.Utility.GetJsonString(ds, new Dictionary<string, string>() { { "UserId", "UserId" } }));
                        else
                            return Ok(JsonConvert.SerializeObject(ds));
                    else
                        return BadRequest(str);

                }
                else
                    return NotFound("User does not exists");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        [HttpGet]
        [Route("changepassword")]
        public IActionResult ChangePassword([FromQuery] int UserID, [FromQuery] string OldPassword, [FromQuery] string NewPassword)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "UserID", UserID},
                        { "OldPassword", OldPassword},
                        { "NewPassword", NewPassword}

                    };
                object objReturn = new DataRepository().ExecuteScalar(configuration, "USP_U_CHANGEPASSWORD", false, parameters);
                if (int.TryParse(Convert.ToString(objReturn), out int userid) && userid > 0)
                    return Ok(userid);
                else
                    throw new Exception(Convert.ToString(objReturn));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
