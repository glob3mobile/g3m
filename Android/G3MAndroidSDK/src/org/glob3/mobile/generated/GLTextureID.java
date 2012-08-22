package org.glob3.mobile.generated; 
//
//  GLTextureID.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

//
//  GLTextureID.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//




public class GLTextureID
{
  private int _textureId;


  public GLTextureID()
  {
	_textureId = -1;
  }


  public static GLTextureID invalid()
  {
	return new GLTextureID(-1);
  }

  public GLTextureID(GLTextureID that)
  {
	  _textureId = that._textureId;

  }

  public GLTextureID(int textureId)
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
//ORIGINAL LINE: int getGLTextureID() const
  public final int getGLTextureID()
  {
	return _textureId;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("GLTextureID #").add(_textureId);
	String s = isb.getString();
	isb = null;
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEqualsTo(const GLTextureID& that) const
  public final boolean isEqualsTo(GLTextureID that)
  {
	return (_textureId == that._textureId);
  }
}