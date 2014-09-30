

package org.glob3.mobile.specific;


import java.util.ArrayList;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.ITileVisitor;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.Tile;


public class TileVisitorCache_Android
implements
ITileVisitor {

   private final long       _debugCounter = 0;
   private final G3MContext _context;


   public TileVisitorCache_Android(final G3MContext context) {
      _context = context;
   }


   @Override
   public void dispose() {

   }


   public long getDebugCounter() {
      return _debugCounter;
   }


   @Override
   public final void visitTile(final ArrayList<Layer> layers,
                               final Tile tile) {
      for (int i = 0; i < layers.size(); i++) {
         final Layer layer = layers.get(i);
         final java.util.ArrayList<Petition> pet = layer.createTileMapPetitions(null, null, tile);
         // Storing petitions
         for (int j = 0; j < pet.size(); j++) {
            final Petition petition = pet.get(j);
            _context.getLogger().logInfo(pet.get(j).getURL().description());
            _context.getDownloader().requestImage(new URL(petition.getURL()), 1, petition.getTimeToCache(), true,
                     new IImageDownloadListenerTileCache(_debugCounter), true);
            _debugCounter++;
         }
      }
   }


}
