using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using NSRetailAPI.Utilities;
using System.Data;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class masterController : ControllerBase
    {
        public readonly IConfiguration configuration;
        public masterController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }
        [HttpGet]
        [Route("getbranch")]
        public IActionResult GetBranch(int Userid)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", Userid }
                    };
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_BRANCH", false, parameters);
                if (dt != null && dt.Rows.Count > 0)
                {
                    dt.TableName = "Branch";
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }

        [HttpGet]
        [Route("getcategory")]
        public IActionResult GetCategory(int Userid)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "USERID", Userid }
                    };
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_CATEGORY", false, parameters);
                if (dt != null && dt.Rows.Count > 0)
                {
                    dt.TableName = "Category";
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }

        [HttpGet]
        [Route("getsupplier")]
        public IActionResult GetSupplier(bool UseWHConnection)
        {
            try
            {
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_DEALER", UseWHConnection, null);
                if (dt != null && dt.Rows.Count > 0)
                {
                    dt.TableName = "Supplier";
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }

        [HttpGet]
        [Route("getgst")]
        public IActionResult GetGST(bool UseWHConnection)
        {
            try
            {
                DataTable dt = new DataRepository().GetDataTable(configuration, "USP_R_GSTLIST", UseWHConnection, null);
                if (dt != null && dt.Rows.Count > 0)
                {
                    dt.TableName = "GST";
                    int Ivalue = 0;
                    string str = Convert.ToString(dt.Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(dt));
                }
                else
                    return NotFound("Data not found");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }
    }
}
