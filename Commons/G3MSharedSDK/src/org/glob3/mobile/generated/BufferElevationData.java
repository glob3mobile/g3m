package org.glob3.mobile.generated;//
//  BufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

//
//  BufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//



public abstract class BufferElevationData extends ElevationData
{
  protected final int _bufferSize;
  protected final double _deltaHeight;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double getValueInBufferAt(int index) const = 0;
  protected abstract double getValueInBufferAt(int index);


  ///#include "Vector2I.hpp"
  
  public BufferElevationData(Sector sector, Vector2I extent, Sector realSector, Vector2I realExtent, int bufferSize, double deltaHeight)
  {
	  super(sector, extent);
	  _bufferSize = bufferSize;
	  _deltaHeight = deltaHeight;
  
  }

  public void dispose()
  {

  if (_sector != null)
	  _sector.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getElevationAt(int x, int y) const
  public final double getElevationAt(int x, int y)
  {
	final int index = ((_height-1-y) * _width) + x;
  
	return getValueInBufferAt(index) + _deltaHeight;
  }

}
