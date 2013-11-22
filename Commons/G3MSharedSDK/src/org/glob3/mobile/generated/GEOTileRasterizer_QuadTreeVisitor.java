package org.glob3.mobile.generated; 
public class GEOTileRasterizer_QuadTreeVisitor extends QuadTreeVisitor
{
  private ICanvas _canvas;
  private final GEORasterProjection _projection;
  private final int _tileLevel;

  public GEOTileRasterizer_QuadTreeVisitor(ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
     _canvas = canvas;
     _projection = projection;
     _tileLevel = tileLevel;
  }

  public final boolean visitElement(Sector sector, QuadTree_Content content)
  {
    GEORasterSymbol symbol = (GEORasterSymbol) content;

    symbol.rasterize(_canvas, _projection, _tileLevel);

    return false;
  }

  public final void endVisit(boolean aborted)
  {
  }

}