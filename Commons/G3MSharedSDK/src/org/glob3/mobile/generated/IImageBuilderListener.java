package org.glob3.mobile.generated; 
//
//  IImageBuilderListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

//
//  IImageBuilderListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//


//class IImage;


public abstract class IImageBuilderListener extends IImageListener
{
  public void dispose()
  {
  }

  public abstract void imageCreated(IImage image);

  public abstract void onError(String error);

}