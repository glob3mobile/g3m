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
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureHolder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;


public class TextureSpec
{
  private final String _id;

  private final int _width;
  private final int _height;


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void operator =(TextureSpec that);

  public TextureSpec(String id, int width, int height)
  {
	  _id = id;
	  _width = width;
	  _height = height;

  }

  public TextureSpec(TextureSpec that)
  {
	  _id = that._id;
	  _width = that._width;
	  _height = that._height;

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


  TODO_implements_equals;
  TODO_implements_hashcode;
}