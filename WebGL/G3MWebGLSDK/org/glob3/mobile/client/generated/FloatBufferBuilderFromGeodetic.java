package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromGeodetic.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class FloatBufferBuilderFromGeodetic extends FloatBufferBuilder
{

  private final CenterStrategy _centerStrategy;
  private float _cx;
  private float _cy;
  private float _cz;

  private void setCenter(Vector3D center)
  {
	_cx = (float)center.x();
	_cy = (float)center.y();
	_cz = (float)center.z();
  }

  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE


  public FloatBufferBuilderFromGeodetic(CenterStrategy cs, Planet planet, Vector3D center)
  {
	  _planet = planet;
	  _centerStrategy = cs;
	setCenter(center);
  }

  public FloatBufferBuilderFromGeodetic(CenterStrategy cs, Planet planet, Geodetic2D center)
  {
	  _planet = planet;
	  _centerStrategy = cs;
	setCenter(_planet.toCartesian(center));
  }

  public FloatBufferBuilderFromGeodetic(CenterStrategy cs, Planet planet, Geodetic3D center)
  {
	  _planet = planet;
	  _centerStrategy = cs;
	setCenter(_planet.toCartesian(center));
  }

  public final void add(Geodetic3D g)
  {
	final Vector3D vector = _planet.toCartesian(g);

	float x = (float) vector.x();
	float y = (float) vector.y();
	float z = (float) vector.z();

	if (_centerStrategy == CenterStrategy.FirstVertex && _values.size() == 0)
	{
	  setCenter(vector);
	}

	if (_centerStrategy != CenterStrategy.NoCenter)
	{
	  x -= _cx;
	  y -= _cy;
	  z -= _cz;
	}

	_values.add(x);
	_values.add(y);
	_values.add(z);
  }

  public final void add(Geodetic2D g)
  {
	final Vector3D vector = _planet.toCartesian(g);

	float x = (float) vector.x();
	float y = (float) vector.y();
	float z = (float) vector.z();

	if (_centerStrategy == CenterStrategy.FirstVertex && _values.size() == 0)
	{
	  setCenter(vector);
	}

	if (_centerStrategy != CenterStrategy.NoCenter)
	{
	  x -= _cx;
	  y -= _cy;
	  z -= _cz;
	}

	_values.add(x);
	_values.add(y);
	_values.add(z);
  }

  public final Vector3D getCenter()
  {
	return new Vector3D((double)_cx,(double)_cy,(double)_cz);
  }
}