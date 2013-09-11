

package org.glob3.mobile.specific;


import org.glob3.mobile.generated.ITileVisitor;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Petition;
import org.glob3.mobile.generated.Tile;


public class TileVisitorCache_JavaDesktop
         implements
            ITileVisitor {

   private final LayerSet _layerSet;

   private final int      _tileWidth;
   private final int      _tileHeight;


   //private final G3MRenderContext _g3mrc;


   public TileVisitorCache_JavaDesktop(final LayerSet layerSet,
                                       final int width,
                                       final int height) {
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
         //TODO: OJO WITH THIS NULL
         final java.util.ArrayList<Petition> pet = layer.createTileMapPetitions(null, tile);

         // Storing petitions
         for (int j = 0; j < pet.size(); j++) {
            System.out.println(pet.get(j).getURL().description());
         }
      }


   }
}
