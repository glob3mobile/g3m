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

  public GEOTileRasterizer()
  {
  }

  public final String getId()
  {
    return "GEOTileRasterizer";
  }

  public final void initialize(G3MContext context)
  {
  }

  public final void rawRasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodelete)
  {
    //  if (_quadTree.isEmpty()) {
    //    listener->imageCreated(image);
    //    if (autodelete) {
    //      delete listener;
    //    }
    //  }
    //  else {
    //    const Tile*   tile     = trc._tile;
    //    const bool    mercator = trc._mercator;
    //
    //    const int width  = image->getWidth();
    //    const int height = image->getHeight();
    //
    //    GEORasterProjection* projection = new GEORasterProjection(tile->_sector, mercator,
    //                                                              width, height);
    //
    //    ICanvas* canvas = getCanvas(width, height);
    //
    //    canvas->drawImage(image, 0, 0);
    //
    //    _quadTree.acceptVisitor(tile->_sector,
    //                            GEOTileRasterizer_QuadTreeVisitor(canvas, projection, tile->_level));
    //
    //    canvas->createImage(listener, autodelete);
    //
    //    delete image;
    //
    //    delete projection;
    //  }
  
    final Tile tile = trc._tile;
    _quadTree.acceptVisitor(tile._sector, new GEOTileRasterizer_QuadTreeVisitor(this, image, trc, listener, autodelete));
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

  public final void clear()
  {
    _quadTree.clear();
    notifyChanges();
  }

  public final ICanvas getCanvas(int width, int height)
  {
    return super.getCanvas(width, height);
  }
}