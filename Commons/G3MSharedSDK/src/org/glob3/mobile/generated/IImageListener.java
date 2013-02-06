package org.glob3.mobile.generated; 
//
//  IImageListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/12.
//
//

//
//  IImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;

public interface IImageListener
{

  /**
   Callback method for image-creation.  The image has to be deleted in C++ / and disposed() en Java
   */
  void imageCreated(IImage image);

}