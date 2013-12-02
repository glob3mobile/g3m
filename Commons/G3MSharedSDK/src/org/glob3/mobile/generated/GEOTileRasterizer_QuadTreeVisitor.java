package org.glob3.mobile.generated; 
//class GEOTileRasterizer_QuadTreeVisitor : public QuadTreeVisitor {
//private:
//  ICanvas*                   _canvas;
//  const GEORasterProjection* _projection;
//  const int                  _tileLevel;
//
//public:
//  GEOTileRasterizer_QuadTreeVisitor(ICanvas* canvas,
//                                    const GEORasterProjection* projection,
//                                    int tileLevel) :
//  _canvas(canvas),
//  _projection(projection),
//  _tileLevel(tileLevel)
//  {
//  }
//
//  bool visitElement(const Sector&           sector,
//                    const QuadTree_Content* content) const {
//    GEORasterSymbol* symbol = (GEORasterSymbol*) content;
//
//    symbol->rasterize(_canvas, _projection, _tileLevel);
//
//    return false;
//  }
//
//  void endVisit(bool aborted) const {
//  }
//};

public class GEOTileRasterizer_QuadTreeVisitor extends QuadTreeVisitor
{
  private final GEOTileRasterizer _geoTileRasterizer;
  private final IImage _originalImage;
  private final TileRasterizerContext _trc;
  private IImageListener _listener;
  private boolean _autodelete;

  private final int _tileLevel;

  private ICanvas _canvas;
  private GEORasterProjection _projection;

  public GEOTileRasterizer_QuadTreeVisitor(GEOTileRasterizer geoTileRasterizer, IImage originalImage, TileRasterizerContext trc, IImageListener listener, boolean autodelete)
  {
     _geoTileRasterizer = geoTileRasterizer;
     _originalImage = originalImage;
     _trc = trc;
     _listener = listener;
     _autodelete = autodelete;
     _tileLevel = trc._tile._level;
     _canvas = null;
     _projection = null;
  }

  public final boolean visitElement(Sector sector, QuadTree_Content content)
  {
    GEORasterSymbol symbol = (GEORasterSymbol) content;

    if (_canvas == null)
    {
      final int width = _originalImage.getWidth();
      final int height = _originalImage.getHeight();

      final Tile tile = _trc._tile;
      final boolean mercator = _trc._mercator;

      _canvas = _geoTileRasterizer.getCanvas(width, height);

      _canvas.drawImage(_originalImage, 0, 0);

      _projection = new GEORasterProjection(tile._sector, mercator, width, height);
    }

    symbol.rasterize(_canvas, _projection, _tileLevel);

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

      if (_originalImage != null)
         _originalImage.dispose();
      _originalImage = null;

      if (_projection != null)
         _projection.dispose();
    }
  }
}