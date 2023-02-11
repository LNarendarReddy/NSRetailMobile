using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using NSRetailAPI.Utilities;
using System.Data;
using System.Security.Cryptography.Xml;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MasterController : ControllerBase
    {
        public readonly IConfiguration configuration;

        public MasterController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }
        [HttpGet]
        [Route("GetBranch")]
        public IActionResult GetBranch(int UserID)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", UserID}
                    };
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_BRANCH", parameters);
                if (dt != null && dt.Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Branches does not exists");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }

        [HttpGet]
        [Route("GetCategory")]
        public IActionResult GetCategory()
        {
            try
            {
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_CATEGORY");
                if (dt != null && dt.Rows.Count > 0)
                {
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Categories does not exists");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }
    }
}
