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

    symbol.rasterize(_canvas, _projection);

    return false;
  }

  public final void endVisit(boolean aborted)
  {
  }

}