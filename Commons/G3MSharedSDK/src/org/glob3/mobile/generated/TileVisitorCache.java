

package org.glob3.mobile.generated;

//
//  TileVisitorCache.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//

//
//  TileVisitorCache.h
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//


public class TileVisitorCache
         implements
            ITileVisitor {
   private final G3MContext _context;
   private long             _numVisits;
   private long             _numPetitions;


   @Override
   public void dispose() {
   }


   public TileVisitorCache(final G3MContext context) {
      _context = context;
      _numVisits = 0;
      _numPetitions = 0;
   }


   @Override
   public final void visitTile(final java.util.ArrayList<Layer> layers,
                               final Tile tile) {
      for (int i = 0; i < layers.size(); i++) {
         _numVisits++;
         final Layer layer = layers.get(i);
         final java.util.ArrayList<Petition> petitions = layer.createTileMapPetitions(null, tile);
         for (int j = 0; j < petitions.size(); j++) {
            _numPetitions++;

            final Petition petition = petitions.get(i);

            final IImageDownloadListenerTileCache listener = new IImageDownloadListenerTileCache(_numVisits, tile,
                     layer.getTitle());
            final IDownloader downloader = _context.getDownloader();

            final long requestId = downloader.requestImage(new URL(petition.getURL()), 1, petition.getTimeToCache(), true,
                     listener, true);
            if (requestId == -1) {
               _context.getLogger().logInfo("This request has been cached (z: %d, x: %d, y: %d)", tile._level, tile._column,
                        tile._row);
            }
            else {
               _context.getLogger().logInfo("Petition %d has been request (z: %d, x: %d, y: %d)", requestId, tile._level,
                        tile._column, tile._row);
            }
         }
      }
   }


   @Override
   public final long getNumVisits() {
      return _numVisits;
   }


   @Override
   public final long getNumPetitions() {
      return _numPetitions;
   }
}
