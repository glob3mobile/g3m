

package org.glob3.mobile.specific;


import java.util.ArrayList;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.ITileVisitor;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.Petition;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.URL;


public class TileVisitorCache_Android
         implements
            ITileVisitor {

   private long             _debugCounter = 0;
   private final G3MContext _context;


   public TileVisitorCache_Android(final G3MContext context) {
      _context = context;
   }


   @Override
   public void dispose() {

   }


   // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
   // Java:
   // ORIGINAL LINE: void visitTile(Tile* tile) const
   @Override
   public final void visitTile(final ArrayList<Layer> layers,
                               final Tile tile) {
      for (int i = 0; i < layers.size(); i++) {
         final Layer layer = layers.get(i);
         final java.util.ArrayList<Petition> pet = layer.createTileMapPetitions(null, null, tile);
         // Storing petitions
         for (int j = 0; j < pet.size(); j++) {
            final Petition petition = pet.get(j);
            _context.getLogger().logInfo(petition.getSector().description());
            _context.getLogger().logInfo(pet.get(j).getURL().description());
            final long requestId = _context.getDownloader().requestImage(new URL(petition.getURL()), 1,
                     petition.getTimeToCache(), true, new IImageDownloadListenerTileCache(_debugCounter), true);
            if (requestId == -1) {
               _context.getLogger().logInfo(
                        "This request is cached (z:" + tile._level + ", x:" + tile._column + ", y:" + tile._row, requestId);
            }
            else {
               _context.getLogger().logInfo(
                        "RequestId " + requestId + " is waiting (z:" + tile._level + ", x:" + tile._column + ", y:" + tile._row,
                        requestId + ")");
            }

            _debugCounter++;
         }
      }
   }


   public long getDebugCounter() {
      return _debugCounter;
   }
}
