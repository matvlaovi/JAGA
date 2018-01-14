using JAGA.Models;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using JAGA.Mapping;
using System.Net.Http;
using System.Net;
using System;

namespace JAGA.Api
{
    public class SongController : ApiController
    {


        public List<Song> Get()
        {
            using (var context = new jaga_dbEntities())
            {
                return context.song.MapToModels();
            }
        }

        public Song Get(int id)
        {
            using (var context = new jaga_dbEntities())
            {
                return context.song.FirstOrDefault(x => x.id == id).MapToModel();
            }
        }

        public void Put(int id, [FromBody]string value)
        {
        }

        public void Delete(int id)
        {
        }

    }

}

    