package org.glob3.mobile.generated;
//
//  IImageFactoryListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//


//class IImage;


public interface IImageFactoryListener
{
  void dispose();

  void imageCreated(IImage image, String imageName);

  void onError(String error);

}