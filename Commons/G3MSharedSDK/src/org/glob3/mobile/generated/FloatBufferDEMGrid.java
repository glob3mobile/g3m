package org.glob3.mobile.generated; 
//
//  FloatBufferDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//

//
//  FloatBufferDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//




public class FloatBufferDEMGrid extends BufferDEMGrid
{
  private float[] _buffer;

  protected final double getValueInBufferAt(int index)
  {
    return _buffer[index];
  }


  public FloatBufferDEMGrid(Sector sector,
                            Vector2I extent,
                            float[] buffer,
                            int bufferSize,
                            double deltaHeight) {
    super(sector, extent, bufferSize, deltaHeight);
    _buffer = buffer;
    if (_bufferSize != (_extent._x * _extent._y)) {
      throw new RuntimeException("Invalid bufferSize");
    }
  }
  
  public void dispose()
  {
    _buffer = null;
    super.dispose();
  }


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3D getMinMaxAverageElevations();

}