package org.glob3.mobile.generated; 
//
//  TileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

//
//  TileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//



//class IImage;
//class Tile;
//class IImageListener;

public abstract class TileRasterizer
{

  public void dispose()
  {

  }

  public abstract String getId();

  public abstract void rasterize(IImage image, Tile tile, IImageListener listener, boolean autodelete);

}