package org.glob3.mobile.generated; 
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


//class IGLTextureID;
//class TexturesHandler;

public class TextureIDReference
{
  private final IGLTextureID _id;
  private final boolean _isPremultiplied;
  private TexturesHandler _texHandler;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TextureIDReference(TextureIDReference that);


  public TextureIDReference(IGLTextureID id, boolean isPremultiplied, TexturesHandler texHandler)
  {
     _id = id;
     _isPremultiplied = isPremultiplied;
     _texHandler = texHandler;
  }

  public void dispose()
  {
    _texHandler.releaseGLTextureID(_id);
  }

  public final TextureIDReference createCopy()
  {
    _texHandler.retainGLTextureID(_id);
    return new TextureIDReference(_id, _isPremultiplied, _texHandler);
  }

  public final IGLTextureID getID()
  {
    return _id;
  }

  public final boolean isPremultiplied()
  {
    return _isPremultiplied;
  }

}