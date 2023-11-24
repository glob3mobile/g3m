package org.glob3.mobile.generated;
//
//  TexturesHandler.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 19/06/12.
//

//
//  TexturesHandler.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 19/06/12.
//



//class IImage;
//class G3MRenderContext;
//class TextureHolder;
//class GL;
//class IFactory;
//class IGLTextureID;
//class TextureIDReference;

public class TextureSpec
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TextureSpec operator =(TextureSpec that);

  public final String _id;
  public final int _width;
  public final int _height;
  public final boolean _generateMipmap;
  public final int _wrapS;
  public final int _wrapT;

  public TextureSpec(String id, int width, int height, boolean generateMipmap, int wrapS, int wrapT)
  {
     _id = id;
     _width = width;
     _height = height;
     _generateMipmap = generateMipmap;
     _wrapS = wrapS;
     _wrapT = wrapT;

  }

  //  TextureSpec():_id(""), _width(0),_height(0), _generateMipmap(false) {}

  public TextureSpec(TextureSpec that)
  {
     _id = that._id;
     _width = that._width;
     _height = that._height;
     _generateMipmap = that._generateMipmap;
     _wrapS = that._wrapS;
     _wrapT = that._wrapT;

  }

  public final boolean isEquals(TextureSpec that)
  {
    return ((_id.equals(that._id)) && (_width == that._width) && (_height == that._height) && (_generateMipmap == that._generateMipmap) && (_wrapS == that._wrapS) && (_wrapT == that._wrapT));
  }

  //  bool lowerThan(const TextureSpec& that) const {
  //    if (_id < that._id) {
  //      return true;
  //    }
  //    else if (_id > that._id) {
  //      return false;
  //    }
  //
  //    if (_width < that._width) {
  //      return true;
  //    }
  //    else if (_width > that._width) {
  //      return false;
  //    }
  //
  //    return (_height < that._height);
  //  }

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

  ///#ifdef C_CODE
  //  bool operator<(const TextureSpec& that) const {
  //    return lowerThan(that);
  //  }
  ///#endif

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_id == null) ? 0 : _id.hashCode());
    result = prime * result + _width;
    result = prime * result + _height;
    result = prime * result + (_generateMipmap ? 1 : 3);
    result = prime * result + _wrapS;
    result = prime * result + _wrapT;
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
    return isEquals(other);
  }
}