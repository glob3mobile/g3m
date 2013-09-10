

package org.glob3.mobile.specific;


import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.ITileVisitor;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Petition;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.URL;


public class TileVisitorCache_Android
         implements
            ITileVisitor {

   private long           _debugCounter = 0;
   final G3MContext       _context;
   private final LayerSet _layerSet;

   private final int      _tileWidth;
   private final int      _tileHeight;


   public TileVisitorCache_Android(final G3MContext context,
                                   final LayerSet layerSet,
                                   final int width,
                                   final int height) {
      _context = context;
      _layerSet = layerSet;
      _tileWidth = width;
      _tileHeight = height;
   }


   @Override
   public void dispose() {

   }


   // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
   // Java:
   // ORIGINAL LINE: void visitTile(Tile* tile) const
   @Override
   public final void visitTile(final Tile tile) {
      for (int i = 0; i < _layerSet.size(); i++) {
         final Layer layer = _layerSet.getLayer(i);
         final java.util.ArrayList<Petition> pet = layer.createTileMapPetitions(null, tile);
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


   public long getDebugCounter() {
      return _debugCounter;
   }
}
