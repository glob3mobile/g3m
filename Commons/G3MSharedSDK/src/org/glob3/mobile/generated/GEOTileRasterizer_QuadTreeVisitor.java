package org.glob3.mobile.generated; 
public class GEOTileRasterizer_QuadTreeVisitor extends QuadTreeVisitor
{
  private ICanvas _canvas;
  private final GEORasterProjection _projection;

  public GEOTileRasterizer_QuadTreeVisitor(ICanvas canvas, GEORasterProjection projection)
  {
     _canvas = canvas;
     _projection = projection;
  }

  public final boolean visitElement(Sector sector, Object element)
  {
    GEORasterSymbol symbol = (GEORasterSymbol) element;

//    int __REMOVE_DEBUG_CODE;
//    _canvas->setStrokeWidth(2);
//    _canvas->setStrokeColor(Color::fromRGBA(1, 1, 0, 0.5f));
//    sector.rasterize(_canvas, _projection);

    symbol.rasterize(_canvas, _projection);

    return false;
  }

  public final void endVisit(boolean aborted)
  {
  }

}