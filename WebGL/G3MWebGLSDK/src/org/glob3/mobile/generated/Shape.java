package org.glob3.mobile.generated; 
//
//  Shape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  Shape.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//



public abstract class Shape
{
  protected final Geodetic3D _position ;

  public Shape(Geodetic3D position)
  {
	  _position = new Geodetic3D(position);

  }

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D getPosition() const
  public final Geodetic3D getPosition()
  {
	return _position;
  }

  public abstract void render(RenderContext rc);
}