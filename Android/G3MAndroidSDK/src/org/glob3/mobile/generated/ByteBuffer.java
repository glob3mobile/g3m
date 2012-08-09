package org.glob3.mobile.generated; 
//
//  ByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


public class ByteBuffer
{

  byte[] _data;

  private final int _length;

  public ByteBuffer(byte[] data, int dataLength)
  {
	  _data = data;
	  _length = dataLength;
  }

  public ByteBuffer(ByteBuffer bb)
  {
	  _length = bb._length;
	  _data = new byte[bb._length];
	  System.arraycopy(bb._data, 0, _data, 0, _length);
  }

  public void dispose()
  {
  }


  byte[] getData() { return _data;}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getLength() const
  public final int getLength()
  {
	return _length;
  }

}