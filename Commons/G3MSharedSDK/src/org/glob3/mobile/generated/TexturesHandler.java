package org.glob3.mobile.generated;
public class TexturesHandler
{
  private java.util.ArrayList<TextureHolder> _textureHolders = new java.util.ArrayList<TextureHolder>();

  private final GL _gl;
  private final boolean _verbose;
  //void showHolders(const std::string& message) const;


  //void TexturesHandler::showHolders(const std::string& message) const {
  //  if (false) {
  //    std::string holdersString = ">>>> " + message + ", Holders=(";
  //    for (int i = 0; i < _textureHolders.size(); i++) {
  //      TextureHolder* holder = _textureHolders[i];
  //
  //      if (i > 0) {
  //        holdersString += ", ";
  //      }
  //      holdersString += holder->description();
  //    }
  //    holdersString += ")";
  //
  //    ILogger::instance()->logInfo("%s\n", holdersString.c_str() );
  //  }
  //}
  
  private IGLTextureID getGLTextureIDIfAvailable(TextureSpec textureSpec)
  {
    final int _textureHoldersSize = _textureHolders.size();
    for (int i = 0; i < _textureHoldersSize; i++)
    {
      TextureHolder holder = _textureHolders.get(i);
      if (holder.hasSpec(textureSpec))
      {
        holder.retain();
  
        //showHolders("getGLTextureIDIfAvailable(): retained " + holder->description());
  
        return holder._glTextureID;
      }
    }
  
    return null;
  }


  public TexturesHandler(GL gl, boolean verbose)
  {
     _gl = gl;
     _verbose = verbose;
  }

  public void dispose()
  {
    if (_textureHolders.size() > 0)
    {
      ILogger.instance().logWarning("WARNING: The TexturesHandler is destroyed, but the inner textures were not released.\n");
    }
  }

  public final TextureIDReference getTextureIDReference(IImage image, int format, String name, boolean generateMipmap)
  {
  
    final TextureSpec textureSpec = new TextureSpec(name, image.getWidth(), image.getHeight(), generateMipmap);
  
    final IGLTextureID previousID = getGLTextureIDIfAvailable(textureSpec);
    if (previousID != null)
    {
      return new TextureIDReference(previousID, image.isPremultiplied(), this);
    }
  
    TextureHolder holder = new TextureHolder(textureSpec);
    holder._glTextureID = _gl.uploadTexture(image, format, textureSpec.generateMipmap());
  
  
    if (_verbose)
    {
      ILogger.instance().logInfo("Uploaded texture \"%s\" to GPU with texID=%s", textureSpec.description(), holder._glTextureID.description());
    }
  
    _textureHolders.add(holder);
  
    //showHolders("getGLTextureID(): created holder " + holder->description());
  
    return new TextureIDReference(holder._glTextureID, image.isPremultiplied(), this);
  }


  //This two methods are supposed to be accessed only by TextureIDReference class
  public final void releaseGLTextureID(IGLTextureID glTextureID)
  {
    if (glTextureID == null)
    {
      return;
    }
  
    for (int i = 0; i < _textureHolders.size(); i++)
    {
      TextureHolder holder = _textureHolders.get(i);
  
      if (holder._glTextureID.isEquals(glTextureID))
      {
        holder.release();
  
        //showHolders("releaseGLTextureID(  ): released holder " + holder->description());
  
        if (!holder.isRetained())
        {
          _gl.deleteTexture(holder._glTextureID);
  
          _textureHolders.remove(i);
  
          if (holder != null)
             holder.dispose();
        }
  
        return;
      }
    }
  }
  public final void retainGLTextureID(IGLTextureID glTextureID)
  {
    if (glTextureID == null)
    {
      return;
    }
  
    for (int i = 0; i < _textureHolders.size(); i++)
    {
      TextureHolder holder = _textureHolders.get(i);
  
      if (holder._glTextureID.isEquals(glTextureID))
      {
        holder.retain();
  
        //showHolders("retainGLTextureID(): retained holder " + holder->description());
  
        return;
      }
    }
  
    ILogger.instance().logInfo("break (point) on me 6\n");
  }
}
