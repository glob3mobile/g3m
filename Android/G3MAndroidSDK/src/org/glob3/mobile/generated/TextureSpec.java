package org.glob3.mobile.generated; 
//
//  TexturesHandler.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  TexturesHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureHolder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Rectangle;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;


public class TextureSpec
{
  private final String _id;

  private final int _width;
  private final int _height;
  private final boolean _isMipmap;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TextureSpec operator =(TextureSpec that);

  public TextureSpec(String id, int width, int height, boolean isMipmap)
  {
	  _id = id;
	  _width = width;
	  _height = height;
	  _isMipmap = isMipmap;

  }

  public TextureSpec()
  {
	  _id = "";
	  _width = 0;
	  _height = 0;
	  _isMipmap = false;
  }

  public TextureSpec(TextureSpec that)
  {
	  _id = that._id;
	  _width = that._width;
	  _height = that._height;
	  _isMipmap = that._isMipmap;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isMipmap() const
  public final boolean isMipmap()
  {
	return _isMipmap;
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
//ORIGINAL LINE: boolean equalsTo(const TextureSpec& that) const
  public final boolean equalsTo(TextureSpec that)
  {
	return ((_id.compareTo(that._id) == 0) && (_width == that._width) && (_height == that._height));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean lowerThan(const TextureSpec& that) const
  public final boolean lowerThan(TextureSpec that)
  {
	if (_id.compareTo(that._id) < 0)
	{
	  return true;
	}
	else if (_id.compareTo(that._id) > 0)
	{
	  return false;
	}

	if (_width < that._width)
	{
	  return true;
	}
	else if (_width > that._width)
	{
	  return false;
	}

	return (_height < that._height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("(").add(_id).add(" ").add(_width).add("x").add(_height).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }


  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _height;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + _width;
		return result;
	}
  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextureSpec other = (TextureSpec) obj;
		if (_height != other._height)
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_width != other._width)
			return false;
		return true;
	}
}