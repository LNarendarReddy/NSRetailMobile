using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using NSRetailAPI.Utilities;
using System.Data;

namespace NSRetailAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class itemController : ControllerBase
    {
        private readonly IConfiguration configuration;
        public itemController(IConfiguration _configuration) { configuration = _configuration; }
        [HttpGet]
        [Route("getitem")]
        public IActionResult GetItem(string ItemCode, bool useWHConnection)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>
                    {
                        { "ITEMCODE", ItemCode }
                    };
                DataSet ds = new DataRepository().GetDataset(configuration, "USP_R_ITEMDATA", useWHConnection, parameters);
                if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    ds.Tables[0].TableName = "ITEM";
                    if (ds.Tables.Count > 1)
                    {
                        ds.Tables[1].TableName = "ITEMCODE";
                        ds.Tables[2].TableName = "ITEMPRICE";
                    }
                    int Ivalue = 0;
                    string str = Convert.ToString(ds.Tables[0].Rows[0][0]);
                    if (!int.TryParse(str, out Ivalue))
                        return BadRequest(str);
                    else
                        return Ok(JsonConvert.SerializeObject(ds));
                }
                else
                    return NotFound("Itemcode does not exists");
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.ToString());
            }
        }
    }
}
