package org.glob3.mobile.generated;import java.util.*;

//
//  TextureIDReference.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

//
//  TextureIDReference.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TexturesHandler;

public class TextureIDReference
{
  private final IGLTextureId _id;
  private final boolean _isPremultiplied;
  private TexturesHandler _texHandler;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TextureIDReference(TextureIDReference that);


  public TextureIDReference(IGLTextureId id, boolean isPremultiplied, TexturesHandler texHandler)
  {
	  _id = id;
	  _isPremultiplied = isPremultiplied;
	  _texHandler = texHandler;
  }

  public void dispose()
  {
	_texHandler.releaseGLTextureId(_id);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TextureIDReference* createCopy() const
  public final TextureIDReference createCopy()
  {
	_texHandler.retainGLTextureId(_id);
	return new TextureIDReference(_id, _isPremultiplied, _texHandler);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IGLTextureId* getID() const
  public final IGLTextureId getID()
  {
	return _id;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isPremultiplied() const
  public final boolean isPremultiplied()
  {
	return _isPremultiplied;
  }

}
