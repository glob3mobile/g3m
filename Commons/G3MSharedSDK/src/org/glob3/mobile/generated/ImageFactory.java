package org.glob3.mobile.generated; 
//
//  ImageFactory.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

//
//  ImageFactory.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//


//class G3MRenderContext;
//class IImageListener;

public interface ImageFactory
{
  void dispose();

  void create(G3MRenderContext rc, int width, int height, IImageListener listener, boolean deleteListener);

}