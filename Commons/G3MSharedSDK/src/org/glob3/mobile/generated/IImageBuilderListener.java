package org.glob3.mobile.generated; 
//
//  IImageBuilderListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//


//class IImage;


public interface IImageBuilderListener
{
  void dispose();

  void imageCreated(IImage image, String imageName);

  void onError(String error);

}