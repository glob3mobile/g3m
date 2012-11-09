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


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  CameraDirtyFlags operator =(CameraDirtyFlags that);

  public boolean _frustumData;
  public boolean _projectionMatrix;
  public boolean _modelMatrix;
  public boolean _modelViewMatrix;
  public boolean _cartesianCenterOfView;
  public boolean _geodeticCenterOfView;
  public boolean _frustum;
  public boolean _frustumMC;
  public boolean _halfFrustum;
  public boolean _halfFrustumMC;


  public CameraDirtyFlags()
  {
	setAll(true);
  }

  public final void copyFrom(CameraDirtyFlags other)
  {
	_frustumData = other._frustumData;
	_projectionMatrix = other._projectionMatrix;
	_modelMatrix = other._modelMatrix;
	_modelViewMatrix = other._modelViewMatrix;
	_cartesianCenterOfView = other._cartesianCenterOfView;
	_geodeticCenterOfView = other._geodeticCenterOfView;
	_frustum = other._frustum;
	_frustumMC = other._frustumMC;
	_halfFrustum = other._halfFrustum;
	_halfFrustumMC = other._halfFrustumMC;
  }


  public CameraDirtyFlags(CameraDirtyFlags other)
  {
	_frustumData = other._frustumData;
	_projectionMatrix = other._projectionMatrix;
	_modelMatrix = other._modelMatrix;
	_modelViewMatrix = other._modelViewMatrix;
	_cartesianCenterOfView = other._cartesianCenterOfView;
	_geodeticCenterOfView = other._geodeticCenterOfView;
	_frustum = other._frustum;
	_frustumMC = other._frustumMC;
	_halfFrustum = other._halfFrustum;
	_halfFrustumMC = other._halfFrustumMC;

  }

  public final String description()
  {
	String d = "";

	if (_frustumData)
		d+= "FD ";
	if (_projectionMatrix)
		d += "PM ";
	if (_modelMatrix)
		d+= "MM ";

	if (_modelViewMatrix)
		d+= "MVM ";
	if (_cartesianCenterOfView)
		d += "CCV ";
	if (_geodeticCenterOfView)
		d+= "GCV ";

	if (_frustum)
		d+= "F ";
	if (_frustumMC)
		d += "FMC ";
	if (_halfFrustum)
		d+= "HF ";
	if (_halfFrustumMC)
		d+= "HFMC ";
	return d;
  }

  public final void setAll(boolean value)
  {
	_frustumData = value;
	_projectionMatrix = value;
	_modelMatrix = value;
	_modelViewMatrix = value;
	_cartesianCenterOfView = value;
	_geodeticCenterOfView = value;
	_frustum = value;
	_frustumMC = value;
	_halfFrustum = value;
	_halfFrustumMC = value;
  }
}