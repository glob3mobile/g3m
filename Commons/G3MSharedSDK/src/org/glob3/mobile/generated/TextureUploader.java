package org.glob3.mobile.generated; 
public class TextureUploader extends IImageListener
{
  private TileTextureBuilder _builder;
  private final Tile _tile;
  private final boolean _mercator;

  private TileRasterizer _tileRasterizer;

  private final java.util.ArrayList<RectangleF> _srcRects;
  private final java.util.ArrayList<RectangleF> _dstRects;

  private final String _textureId;

  public TextureUploader(TileTextureBuilder builder, Tile tile, boolean mercator, TileRasterizer tileRasterizer, java.util.ArrayList<RectangleF> srcRects, java.util.ArrayList<RectangleF> dstRects, String textureId)
  {
     _builder = builder;
     _tile = tile;
     _mercator = mercator;
     _tileRasterizer = tileRasterizer;
     _srcRects = srcRects;
     _dstRects = dstRects;
     _textureId = textureId;

  }

  public final void imageCreated(IImage image)
  {
    if (_tileRasterizer == null)
    {
      _builder.imageCreated(image, _srcRects, _dstRects, _textureId);
    }
    else
    {
      _tileRasterizer.rasterize(image, _tile, _mercator, new TextureUploader(_builder, _tile, _mercator, null, _srcRects, _dstRects, _textureId), true);
    }
  }
}