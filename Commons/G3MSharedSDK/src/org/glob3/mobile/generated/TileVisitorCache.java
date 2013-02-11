package org.glob3.mobile.generated; 
public class TileVisitorCache implements ITileVisitor
{

    private MultiLayerTileTexturizer _texturizer;
    private final TilesRenderParameters _parameters;
    private final G3MRenderContext _rc;
    private final LayerSet _layerSet;

    public TileVisitorCache(MultiLayerTileTexturizer texturizer, TilesRenderParameters parameters, G3MRenderContext rc, LayerSet layerSet)
    {
       _texturizer = texturizer;
       _parameters = parameters;
       _rc = rc;
       _layerSet = layerSet;
    }

    public void dispose()
    {

    }

    public final void visitTile(Tile tile)
    {

        TileTextureBuilder ttb = new TileTextureBuilder(_texturizer, _rc, _layerSet, _parameters, _rc.getDownloader(), tile, null, null);

        ttb.start();
    }
}