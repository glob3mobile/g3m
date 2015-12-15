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




//class IImage;
//class G3MRenderContext;
//class TextureHolder;
//class GL;
//class IFactory;
//class IGLTextureId;
//class TextureIDReference;

public class TextureSpec
{
  private final String _id;

  private final int _width;
  private final int _height;
  private final boolean _generateMipmap;
  private final int _wrapMode;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TextureSpec operator =(TextureSpec that);

  public TextureSpec(String id, int width, int height, boolean generateMipmap)
  {
     _id = id;
     _width = width;
     _height = height;
     _generateMipmap = generateMipmap;
     _wrapMode = GLTextureParameterValue.clampToEdge();

  }

  public TextureSpec(String id, int width, int height, boolean generateMipmap, int wrapMode)
  {
     _id = id;
     _width = width;
     _height = height;
     _generateMipmap = generateMipmap;
     _wrapMode = wrapMode;

  }

  public TextureSpec()
  {
     _id = "";
     _width = 0;
     _height = 0;
     _generateMipmap = false;
     _wrapMode = GLTextureParameterValue.clampToEdge();
  }

  public TextureSpec(TextureSpec that)
  {
     _id = that._id;
     _width = that._width;
     _height = that._height;
     _generateMipmap = that._generateMipmap;
     _wrapMode = that._wrapMode;

  }

  public final boolean generateMipmap()
  {
    return _generateMipmap;
  }

  public final int getWidth()
  {
    return _width;
  }

  public final int getHeight()
  {
    return _height;
  }

  public final int getWrapMode()
  {
    return _wrapMode;
  }

  public final boolean equalsTo(TextureSpec that)
  {
    return ((_id.compareTo(that._id) == 0) && (_width == that._width) && (_height == that._height) && (_wrapMode == that._wrapMode));
  }

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

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(");
    isb.addString(_id);
    isb.addString(" ");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }


  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _height;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + _width;
		result = prime * result + _wrapMode;
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
		if (_wrapMode != other._wrapMode)
			return false;
		return true;
	}
}