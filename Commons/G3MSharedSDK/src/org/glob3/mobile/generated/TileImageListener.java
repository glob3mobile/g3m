package org.glob3.mobile.generated; 
//
//  TileImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//


//class IImage;

public abstract class TileImageListener
{
  public void dispose()
  {
  }

  public abstract void imageCreated(String tileId, IImage image, String imageId, TileImageContribution contribution);

  public abstract void imageCreationError(String tileId, String error);

  public abstract void imageCreationCanceled(String tileId);

}