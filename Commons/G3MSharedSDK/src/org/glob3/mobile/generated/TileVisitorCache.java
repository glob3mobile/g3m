

package org.glob3.mobile.generated;

public class TileVisitorCache
         extends
            ITileVisitor {

   private final MultiLayerTileTexturizer _texturizer;
   private final TilesRenderParameters    _parameters;
   private final G3MRenderContext         _rc;
   private final PlanetRendererContext    _prc;
   private final LayerSet                 _layerSet;


   public TileVisitorCache(final MultiLayerTileTexturizer texturizer,
                           final TilesRenderParameters parameters,
                           final G3MRenderContext rc,
                           final PlanetRendererContext prc,
                           final LayerSet layerSet) {
      _texturizer = texturizer;
      _parameters = parameters;
      _rc = rc;
      _prc = prc;
      _layerSet = layerSet;
   }


   @Override
   public void dispose() {

   }


   //C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
   //ORIGINAL LINE: void visitTile(Tile* tile) const
   @Override
   public final void visitTile(final Tile tile) {

      final TileTextureBuilder ttb = new TileTextureBuilder(_texturizer, _prc.getTileRasterizer(), _rc, _layerSet,
               _rc.getDownloader(), tile, null, null, 0);

      ttb.start();
   }
}
