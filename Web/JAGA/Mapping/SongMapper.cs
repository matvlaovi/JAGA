using JAGA.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace JAGA.Mapping
{
    public static class SongMapper
    {
        public static Song MapToModel(this song dbResult)
        {
            if (dbResult == null) return null;

            return new Song()
            {
                Artist = dbResult.artist,
                Difficulty = dbResult.difficulty.GetValueOrDefault(),
                Id = dbResult.id,
                Tabs = dbResult.tabs,
                Title = dbResult.title
            };
        }

        public static List<Song> MapToModels(this IEnumerable<song> dbResult)
        {
            List<Song> list = new List<Song>();
            if (dbResult == null) return null;

            foreach (var item in dbResult)
            {
                list.Add(item.MapToModel());
            }

            return list;
            
        }

        public static song MapToResult(this Song model)
        {
            if (model == null) return null;

            return new song()
            {
                artist = model.Artist,
                difficulty = model.Difficulty,
                id = model.Id,
                tabs = model.Tabs,
                title = model.Title,
                
            };
        }
    }
}