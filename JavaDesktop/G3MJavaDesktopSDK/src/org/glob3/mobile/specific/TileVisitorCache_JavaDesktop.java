

package org.glob3.mobile.specific;


import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.ITileVisitor;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.Petition;
import org.glob3.mobile.generated.Tile;


public class TileVisitorCache_JavaDesktop
         implements
            ITileVisitor {

   private long          _numVisits;
   private long          _numPetitions;

   private final ILogger _logger = ILogger.instance();


   public TileVisitorCache_JavaDesktop() {
      _numVisits = 0;
      _numPetitions = 0;
   }


   @Override
   public void dispose() {

   }


   @Override
   public final void visitTile(final java.util.ArrayList<Layer> layers,
                               final Tile tile) {
      for (int i = 0; i < layers.size(); i++) {
         _numVisits++;
         final Layer layer = layers.get(i);

         final java.util.ArrayList<Petition> petitions = layer.createTileMapPetitions(null,
                  layer.getLayerTilesRenderParameters(), tile);
         for (int j = 0; j < petitions.size(); j++) {
            _numPetitions++;

            final Petition petition = petitions.get(i);

            _logger.logInfo(petition.getURL().description());

            //            final IImageDownloadListenerTileCache listener = new IImageDownloadListenerTileCache(_numVisits, tile,
            //                     layer.getTitle());
            //            final IDownloader downloader = _context.getDownloader();
            //
            //            final long requestId = downloader.requestImage(new URL(petition.getURL()), 1, petition.getTimeToCache(), true,
            //                     listener, true);
            //            
            //            if (requestId == -1) {
            //               _logger.logInfo("This request has been cached early (z: %d, x: %d, y: %d)", tile._level,
            //                        tile._column, tile._row);
            //            }
            //            else {
            //               _logger.logInfo("Petition %d has been request (z: %d, x: %d, y: %d)", requestId, tile._level,
            //                        tile._column, tile._row);
            //            }
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
