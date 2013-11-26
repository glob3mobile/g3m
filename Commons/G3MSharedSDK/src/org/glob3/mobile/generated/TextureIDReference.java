package org.glob3.mobile.generated; 
//class TexturesHandler;

public class TextureIDReference
{
  private final IGLTextureId _id;
  private TexturesHandler _texHandler;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TextureIDReference(TextureIDReference that);


  public TextureIDReference(IGLTextureId id, TexturesHandler texHandler)
  {
     _texHandler = texHandler;
     _id = id;
  }

  public void dispose()
  {
    _texHandler.releaseGLTextureId(_id);
  }

  public final TextureIDReference createCopy()
  {
    _texHandler.retainGLTextureId(_id);
    return new TextureIDReference(_id, _texHandler);
  }

  public final IGLTextureId getID()
  {
    return _id;
  }

}