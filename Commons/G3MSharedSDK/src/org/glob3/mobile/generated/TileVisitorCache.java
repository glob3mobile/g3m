

package org.glob3.mobile.generated;

public class TileVisitorCache
         implements
            ITileVisitor {

   private final MultiLayerTileTexturizer _texturizer;
   private final PlanetRendererContext    _prc;
   private final G3MRenderContext         _rc;
   private final LayerSet                 _layerSet;


   public TileVisitorCache(final MultiLayerTileTexturizer texturizer,
                           final PlanetRendererContext prc,
                           final G3MRenderContext rc,
                           final LayerSet layerSet) {
      _texturizer = texturizer;
      _prc = prc;
      _rc = rc;
      _layerSet = layerSet;
   }


   @Override
   public void dispose() {

   }


   @Override
   public final void visitTile(final Tile tile) {

      final TileTextureBuilder ttb = new TileTextureBuilder(_texturizer, _prc.getTileRasterizer(), _rc, _layerSet,
               _rc.getDownloader(), tile, null, null, 0);

      ttb.start();
   }
}
