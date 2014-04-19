package org.glob3.mobile.generated; 
//
//  TileImageListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

//
//  TileImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//


//class Tile;
//class IImage;
//class Sector;
//class RectangleF;

public abstract class TileImageListener
{
  public void dispose()
  {
  }

  public abstract void imageCreated(Tile tile, IImage image, String imageId, Sector imageSector, RectangleF imageRectangle, float alpha);

  public abstract void imageCreationError(Tile tile, String error);

  public abstract void imageCreationCanceled(Tile tile);

}