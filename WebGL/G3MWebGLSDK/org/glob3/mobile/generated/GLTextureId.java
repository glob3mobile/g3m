package org.glob3.mobile.generated; 
//
//  GLTextureId.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

//
//  GLTextureId.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//




public class GLTextureId
{
  private int _textureId;

  public GLTextureId()
  {
	_textureId = -1;
  }

  public static GLTextureId invalid()
  {
	return new GLTextureId(-1);
  }

  public GLTextureId(GLTextureId that)
  {
	  _textureId = that._textureId;

  }

  public GLTextureId(int textureId)
  {
	  _textureId = textureId;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isValid() const
  public final boolean isValid()
  {
	return (_textureId > 0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getGLTextureId() const
  public final int getGLTextureId()
  {
	return _textureId;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("GLTextureId #").add(_textureId);
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEqualsTo(const GLTextureId& that) const
  public final boolean isEqualsTo(GLTextureId that)
  {
	return (_textureId == that._textureId);
  }
}