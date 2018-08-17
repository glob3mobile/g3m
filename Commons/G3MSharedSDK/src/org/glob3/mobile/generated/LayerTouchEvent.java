package org.glob3.mobile.generated;//
//  LayerTouchEventListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 14/08/12.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Layer;

public class LayerTouchEvent
{
  private final Geodetic3D _position = new Geodetic3D();
  private final Sector _sector = new Sector();
  private final Layer _layer;

  public LayerTouchEvent(Geodetic3D position, Sector sector, Layer layer)
  {
	  _position = new Geodetic3D(position);
	  _sector = new Sector(sector);
	  _layer = layer;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic3D getPosition() const
  public final Geodetic3D getPosition()
  {
	return _position;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getSector() const
  public final Sector getSector()
  {
	return _sector;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Layer* getLayer() const
  public final Layer getLayer()
  {
	return _layer;
  }

}
