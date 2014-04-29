package org.glob3.mobile.generated; 
//
//  IImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/12.
//
//


//class IImage;


public interface IImageListener
{
  void dispose();

  /**
   Callback method for image-creation.  The image has to be deleted in C++ / and disposed() en Java
   */
  void imageCreated(IImage image);


}