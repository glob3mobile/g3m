package org.glob3.mobile.generated; 
//
//  ByteArrayWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 15/08/12.
//
//

//
//  ByteArrayWrapper.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class ByteArrayWrapper
{

  byte[] _data;

  private final int _length;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ByteArrayWrapper(ByteArrayWrapper that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ByteArrayWrapper operator =(ByteArrayWrapper that);

  public ByteArrayWrapper(byte[] data, int dataLength)
  {
	  _data = data;
	  _length = dataLength;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ByteArrayWrapper* copy() const
  public final ByteArrayWrapper copy()
  {
	byte[] newData = new byte[_length];
	System.arraycopy(_data, 0, newData, 0, _length);
	return new ByteArrayWrapper(newData, _length);
  }

  public void dispose()
  {
  }


  public byte[] getData() {
	return _data;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getDataAsString() const
  public final String getDataAsString()
  {
	return IStringUtils.instance().createString(_data, _length);
  }

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
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("ByteArrayWrapper(length=").add(_length).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}