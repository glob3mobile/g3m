package org.glob3.mobile.generated; 
/*
 *  Camera.cpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */


/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */




//class ILogger;
//class GPUProgramState;

public class CameraDirtyFlags
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  CameraDirtyFlags operator =(CameraDirtyFlags that);

  public boolean _frustumDataDirty;
  public boolean _projectionMatrixDirty;
  public boolean _modelMatrixDirty;
  public boolean _modelViewMatrixDirty;
  public boolean _cartesianCenterOfViewDirty;
  public boolean _geodeticCenterOfViewDirty;
  public boolean _frustumDirty;
  public boolean _frustumMCDirty;

  public CameraDirtyFlags()
  {
    setAllDirty();
  }

  public final void copyFrom(CameraDirtyFlags other)
  {
    _frustumDataDirty = other._frustumDataDirty;
    _projectionMatrixDirty = other._projectionMatrixDirty;
    _modelMatrixDirty = other._modelMatrixDirty;
    _modelViewMatrixDirty = other._modelViewMatrixDirty;
    _cartesianCenterOfViewDirty = other._cartesianCenterOfViewDirty;
    _geodeticCenterOfViewDirty = other._geodeticCenterOfViewDirty;
    _frustumDirty = other._frustumDirty;
    _frustumMCDirty = other._frustumMCDirty;
  }

  public CameraDirtyFlags(CameraDirtyFlags other)
  {
    _frustumDataDirty = other._frustumDataDirty;
    _projectionMatrixDirty = other._projectionMatrixDirty;
    _modelMatrixDirty = other._modelMatrixDirty;
    _modelViewMatrixDirty = other._modelViewMatrixDirty;
    _cartesianCenterOfViewDirty = other._cartesianCenterOfViewDirty;
    _geodeticCenterOfViewDirty = other._geodeticCenterOfViewDirty;
    _frustumDirty = other._frustumDirty;
    _frustumMCDirty = other._frustumMCDirty;
  }

  public final String description()
  {
    String d = "";
    if (_frustumDataDirty)
       d += "FD ";
    if (_projectionMatrixDirty)
       d += "PM ";
    if (_modelMatrixDirty)
       d += "MM ";
    if (_modelViewMatrixDirty)
       d += "MVM ";
    if (_cartesianCenterOfViewDirty)
       d += "CCV ";
    if (_geodeticCenterOfViewDirty)
       d += "GCV ";
    if (_frustumDirty)
       d += "F ";
    if (_frustumMCDirty)
       d += "FMC ";
    return d;
  }

  @Override
  public String toString() {
    return description();
  }

  public final void setAllDirty()
  {
    _frustumDataDirty = true;
    _projectionMatrixDirty = true;
    _modelMatrixDirty = true;
    _modelViewMatrixDirty = true;
    _cartesianCenterOfViewDirty = true;
    _geodeticCenterOfViewDirty = true;
    _frustumDirty = true;
    _frustumMCDirty = true;
  }

}