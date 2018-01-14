using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace JAGA.Models
{
    public class Song
    {
        
        public int Id { get; set; }
        [Required]
        public string Artist { get; set; }
        [Required]
        public string Title { get; set; }
        [Required]
        public string Tabs { get; set; }
        [Required]
        public int Difficulty { get; set; }
    }
}