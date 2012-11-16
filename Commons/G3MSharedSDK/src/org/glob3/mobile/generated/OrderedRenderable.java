package org.glob3.mobile.generated; 
//
//  OrderedRenderable.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/13/12.
//
//

//
//  OrderedRenderable.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/13/12.
//
//



public interface OrderedRenderable
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double squaredDistanceFromEye() const = 0;
  double squaredDistanceFromEye();

  void render(RenderContext rc);

}