package org.glob3.mobile.generated; 
//
//  GEOTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEOTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//



//class GEORasterSymbol;


public class GEOTileRasterizer extends CanvasTileRasterizer
{
  private QuadTree _quadTree = new QuadTree();

  public final String getId()
  {
    return "GEOTileRasterizer";
  }

  public final void rawRasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodelete)
  {
    final Tile tile = trc._tile;
    final boolean mercator = trc._mercator;
  
    final int width = image.getWidth();
    final int height = image.getHeight();
  
    GEORasterProjection projection = new GEORasterProjection(tile.getSector(), mercator, width, height);
  
    ICanvas canvas = getCanvas(width, height);
  
    canvas.drawImage(image, 0, 0);
  
    _quadTree.acceptVisitor(tile.getSector(), new GEOTileRasterizer_QuadTreeVisitor(canvas, projection, tile.getLevel()));
  
    canvas.createImage(listener, autodelete);
  
    if (image != null)
       image.dispose();
  
    if (projection != null)
       projection.dispose();
  }

  public final void addSymbol(GEORasterSymbol symbol)
  {
    final Sector sector = symbol.getSector();
  
    if (sector == null)
    {
      //    ILogger::instance()->logError("Symbol %s has not sector, can't symbolize",
      //                                  symbol->description().c_str());
      if (symbol != null)
         symbol.dispose();
    }
    else
    {
      final boolean added = _quadTree.add(sector, symbol);
      if (added)
      {
        notifyChanges();
      }
      else
      {
        if (symbol != null)
           symbol.dispose();
      }
    }
  }

}