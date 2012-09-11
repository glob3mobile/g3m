package org.glob3.mobile.generated; 
//
//  GLImage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class GLImage
{

  private final GLFormat _format;

  private final IByteBuffer _data;

  private int _width;
  private int _height;


  public GLImage(GLFormat format, IByteBuffer data, int width, int height)
  {
	  _format = format;
	  _data = data;
	  _width = width;
	  _height = height;

  }

  public void dispose()
  {
	if (_data != null)
		_data.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	if (_format == GLFormat.RGBA)
	{
	  return true; //Always true
	}

	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IByteBuffer* getByteBuffer() const
  public final IByteBuffer getByteBuffer()
  {
	return _data;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getWidth() const
  public final int getWidth()
  {
	return _width;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getHeight() const
  public final int getHeight()
  {
	return _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GLFormat getFormat() const
  public final GLFormat getFormat()
  {
	return _format;
  }

}