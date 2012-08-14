package org.glob3.mobile.generated; 
/*
 *  Camera.cpp
 *  Prueba Opengl iPad
 *
 *  Created by AgustÃ­n Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */



/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustín Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;


public class CameraDirtyFlags
{
  public boolean _frustumData;
  public boolean _projectionMatrix;
  public boolean _modelMatrix;
  public boolean _modelViewMatrix;
  public boolean _XYZCenterOfView;
  public boolean _geodeticCenterOfView;
  public boolean _frustum;
  public boolean _frustumMC;
  public boolean _halfFrustum;
  public boolean _halfFrustumMC;


  public CameraDirtyFlags()
  {
	setAll(true);
  }

  public CameraDirtyFlags(CameraDirtyFlags other)
  {
	  _frustumData = other._frustumData;
	  _projectionMatrix = other._projectionMatrix;
	  _modelMatrix = other._modelMatrix;
	  _modelViewMatrix = other._modelViewMatrix;
	  _XYZCenterOfView = other._XYZCenterOfView;
	  _geodeticCenterOfView = other._geodeticCenterOfView;
	  _frustum = other._frustum;
	  _frustumMC = other._frustumMC;
	  _halfFrustum = other._halfFrustum;
	  _halfFrustumMC = other._halfFrustumMC;
  }

  public final void setAll(boolean value)
  {
	_frustumData = value;
	_projectionMatrix = value;
	_modelMatrix = value;
	_modelViewMatrix = value;
	_XYZCenterOfView = value;
	_geodeticCenterOfView = value;
	_frustum = value;
	_frustumMC = value;
	_halfFrustum = value;
	_halfFrustumMC = value;
  }

}