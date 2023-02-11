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
    public class UserController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public UserController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }
        [HttpGet]
        [Route("GetUserLogin")]
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
                DataTable dtLogin = new DataRepository().GetDataTable(configuration, "USP_R_USERLOGIN", parameters);
                if (dtLogin != null && dtLogin.Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(dtLogin.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dtLogin));
                }
                else
                    return NotFound("User does not exists");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }
    }
}
