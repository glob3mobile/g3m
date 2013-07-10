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


public class GEOTileRasterizer extends TileRasterizer
{
  private QuadTree _quadTree = new QuadTree();

  public final String getId()
  {
    return "GEOTileRasterizer";
  }

  public final void rasterize(IImage image, Tile tile, IImageListener listener, boolean autodelete)
  {
    _quadTree.visitElements(tile.getSector(), new GEOTileRasterizer_QuadTreeVisitor());
  }

  public final void addSymbol(GEORasterSymbol symbol)
  {
    _quadTree.add(symbol.getSector(), symbol);
  }

}