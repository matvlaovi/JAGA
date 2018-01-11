using JAGA.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace JAGA.Api
{
    public class SongController : ApiController
    {
        

        public List<song> Get()
        {
            using (var context = new jaga_dbEntities())
            {
                return context.song.ToList();
            }
        }

        public song Get(int id)
        {
            using (var context = new jaga_dbEntities())
            {
                return context.song.FirstOrDefault(x => x.id == id);
            }
        }
        
        public void Post([FromBody]song song)
        {
            using (var context = new jaga_dbEntities())
            {
                context.song.Add(song);
                context.SaveChanges();
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