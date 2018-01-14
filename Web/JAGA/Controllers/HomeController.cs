using JAGA.Mapping;
using JAGA.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace JAGA.Controllers
{
    public class HomeController : Controller
    {

        public ActionResult Index()
        {
            return View();
        }

        public ActionResult InsertSong(Song song)
        {
            if (ModelState.IsValid && song.Difficulty > 0)
            {
                using (var context = new jaga_dbEntities())
                {
                    context.song.Add(song.MapToResult());
                    context.SaveChanges();
                }

                TempData["SuccessMessage"] = "You successfully added a new song!";

                return RedirectToAction("Index");

            }
            else
            {
                TempData["ErrorMessage"] = "All fields are required!";

                return RedirectToAction("Index");
            }
        }
    }
}