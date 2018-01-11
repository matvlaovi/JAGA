using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace JAGA.Models
{
    public class Song
    {
        public int Id { get; set; }
        public string Artist { get; set; }
        public string Title { get; set; }
        public string Tabs { get; set; }
        public int Difficulty { get; set; }
    }
}