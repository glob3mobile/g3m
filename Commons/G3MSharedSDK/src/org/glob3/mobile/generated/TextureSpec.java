package org.glob3.mobile.generated;import java.util.*;

//
//  TexturesHandler.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//

//
//  TexturesHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureHolder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;

public class TextureSpec
{
  private final String _id;

  private final int _width;
  private final int _height;
  private final boolean _generateMipmap;
  private final int _wrapping;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TextureSpec operator =(TextureSpec that);

  public TextureSpec(String id, int width, int height, boolean generateMipmap, int wrapping)
  {
	  _id = id;
	  _width = width;
	  _height = height;
	  _generateMipmap = generateMipmap;
	  _wrapping = wrapping;

  }

  public TextureSpec()
  {
	  _id = "";
	  _width = 0;
	  _height = 0;
	  _generateMipmap = false;
	  _wrapping = GLTextureParameterValue.clampToEdge();
  }

  public TextureSpec(TextureSpec that)
  {
	  _id = that._id;
	  _width = that._width;
	  _height = that._height;
	  _generateMipmap = that._generateMipmap;
	  _wrapping = that._wrapping;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean generateMipmap() const
  public final boolean generateMipmap()
  {
	return _generateMipmap;
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
//ORIGINAL LINE: int getWrapping() const
  public final int getWrapping()
  {
	return _wrapping;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean equalsTo(const TextureSpec& that) const
  public final boolean equalsTo(TextureSpec that)
  {
	return ((_id.compareTo(that._id) == 0) && (_width == that._width) && (_height == that._height) && (_wrapping == that._wrapping));
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean operator <(const TextureSpec& that) const
//C++ TO JAVA CONVERTER TODO TASK: Operators cannot be overloaded in Java:
  boolean operator <(TextureSpec that)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return lowerThan(that);
	return lowerThan(new TextureSpec(that));
  }
//#endif

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public int hashCode()
  {
		final int prime = 31;
		int result = 1;
		result = prime * result + _height;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + _width;
		return result;
	}

	public final Override public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextureSpec other = (TextureSpec) obj;
		if (_height != other._height)
			return false;
		if (_id == null)
		{
			if (other._id != null)
				return false;
		}
		else if (!_id.equals(other._id))
			return false;
		if (_width != other._width)
			return false;
		return true;
	}
//#endif
}
