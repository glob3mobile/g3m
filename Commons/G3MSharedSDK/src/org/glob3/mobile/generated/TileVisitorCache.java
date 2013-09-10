package org.glob3.mobile.generated; 
public class TileVisitorCache implements ITileVisitor
{

    private MultiLayerTileTexturizer _texturizer;
    private final G3MRenderContext _rc;
    private final PlanetRendererContext _prc;
    private final LayerSet _layerSet;

    public TileVisitorCache(MultiLayerTileTexturizer texturizer, G3MRenderContext rc, PlanetRendererContext prc, LayerSet layerSet)
    {
       _texturizer = texturizer;
       _rc = rc;
       _prc = prc;
       _layerSet = layerSet;
    }

    public void dispose()
    {

    }

    public final void visitTile(Tile tile)
    {

        TileTextureBuilder ttb = new TileTextureBuilder(_texturizer, _prc.getTileRasterizer(), _rc, _layerSet, _rc.getDownloader(), tile, null, null, 0);

        ttb.start();
    }
}