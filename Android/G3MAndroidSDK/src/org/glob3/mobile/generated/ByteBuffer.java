package org.glob3.mobile.generated; 
//
//  ByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//THIS CLASS RECEIVES A REFERENCE TO A BYTE ARRAY PREVIOUSLY ALLOCATED
//TO DELETE THE ARRAY CALL release()
public class ByteBuffer
{
  private final String _data;
  private final int _dataLength;

  private boolean _hasBeenReleased;

  public ByteBuffer(ByteBuffer bb)
  {
	  _data = bb._data;
	  _dataLength = bb._dataLength;
	  _hasBeenReleased = bb._hasBeenReleased;
  }
  public ByteBuffer()
  {
	  _data = null;
	  _dataLength = 0;
	  _hasBeenReleased = true;
  }
  public ByteBuffer(String data, int dataLength)
  {
	  _data = data;
	  _dataLength = dataLength;
	  _hasBeenReleased = false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getData() const
  public final String getData()
  {
	  if (!_hasBeenReleased)
		  return _data;
		  else
			  return null;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getDataLength() const
  public final int getDataLength()
  {
	  if (!_hasBeenReleased)
		  return _dataLength;
		  else
			  return 0;
  }

  public final void release()
  {
	if (!_hasBeenReleased)
	{
	  _hasBeenReleased = true;
	}
  }
}