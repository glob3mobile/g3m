package org.glob3.mobile.generated; 
public class GEOTileRasterizer_QuadTreeVisitor extends QuadTreeVisitor
{
  private final GEOTileRasterizer _geoTileRasterizer;
  private IImage _originalImage;
  private final TileRasterizerContext _trc;
  private IImageListener _listener;
  private boolean _autodelete;

  private ICanvas _canvas;
  private GEORasterProjection _projection;

  public GEOTileRasterizer_QuadTreeVisitor(GEOTileRasterizer geoTileRasterizer, IImage originalImage, TileRasterizerContext trc, IImageListener listener, boolean autodelete)
  {
     _geoTileRasterizer = geoTileRasterizer;
     _originalImage = originalImage;
     _trc = trc;
     _listener = listener;
     _autodelete = autodelete;
     _canvas = null;
     _projection = null;
  }

  public final boolean visitElement(Sector sector, QuadTree_Content content)
  {
    GEORasterSymbol symbol = (GEORasterSymbol) content;

    final Tile tile = _trc._tile;

    if (_canvas == null)
    {
      final int width = _originalImage.getWidth();
      final int height = _originalImage.getHeight();

      _canvas = _geoTileRasterizer.getCanvas(width, height);

      _canvas.drawImage(_originalImage, 0, 0);

      _projection = new GEORasterProjection(tile._sector, _trc._mercator, width, height);
    }

    symbol.rasterize(_canvas, _projection, tile._level);

    return false;
  }

  public final void endVisit(boolean aborted)
  {
    if (_canvas == null)
    {
      _listener.imageCreated(_originalImage);
      if (_autodelete)
      {
        if (_listener != null)
           _listener.dispose();
      }
    }
    else
    {
      _canvas.createImage(_listener, _autodelete);

      _originalImage = null;
      _originalImage = null;

      if (_projection != null)
         _projection.dispose();
    }
  }
}