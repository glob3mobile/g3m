package org.glob3.mobile.generated; 
//
//  ByteBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

//
//  ByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class ByteBuffer
{
  private byte _data;
  byte[] _data;

  private final int _length;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ByteBuffer(ByteBuffer that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void operator =(ByteBuffer that);

  public ByteBuffer(byte[] data, int dataLength)
  {
	  _data = data;
	  _length = dataLength;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ByteBuffer* copy() const
  public final ByteBuffer copy()
  {
	byte[] newData = new byte[_length];
	System.arraycopy(_data, 0, newData, 0, _length);
	return new ByteBuffer(newData, _length);
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	std.ostringstream buffer = new std.ostringstream();
	buffer << "ByteBuffer(length=";
	buffer << _length;
	buffer << ")";
	return buffer.str();
  }

}