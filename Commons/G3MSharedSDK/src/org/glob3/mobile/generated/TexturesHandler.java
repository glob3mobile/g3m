package org.glob3.mobile.generated;import java.util.*;

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
  
  private IGLTextureId getGLTextureIdIfAvailable(TextureSpec textureSpec)
  {
	final int _textureHoldersSize = _textureHolders.size();
	for (int i = 0; i < _textureHoldersSize; i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (holder->hasSpec(textureSpec))
	  if (holder.hasSpec(new TextureSpec(textureSpec)))
	  {
		holder.retain();
  
		//showHolders("getGLTextureIdIfAvailable(): retained " + holder->description());
  
		return holder._glTextureId;
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
	  return getTextureIDReference(image, format, name, generateMipmap, GLTextureParameterValue.clampToEdge());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: const TextureIDReference* getTextureIDReference(const IImage* image, int format, const String& name, boolean generateMipmap, int wrapping = GLTextureParameterValue::clampToEdge())
  public final TextureIDReference getTextureIDReference(IImage image, int format, String name, boolean generateMipmap, int wrapping)
  {
  
	final TextureSpec textureSpec = new TextureSpec(name, image.getWidth(), image.getHeight(), generateMipmap, wrapping);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const IGLTextureId* previousId = getGLTextureIdIfAvailable(textureSpec);
	final IGLTextureId previousId = getGLTextureIdIfAvailable(new TextureSpec(textureSpec));
	if (previousId != null)
	{
	  return new TextureIDReference(previousId, image.isPremultiplied(), this);
	}
  
	TextureHolder holder = new TextureHolder(textureSpec);
	holder._glTextureId = _gl.uploadTexture(image, format, textureSpec.generateMipmap(), textureSpec.getWrapping());
  
  
	if (_verbose)
	{
	  ILogger.instance().logInfo("Uploaded texture \"%s\" to GPU with texId=%s", textureSpec.description().c_str(), holder._glTextureId.description().c_str());
	}
  
	_textureHolders.add(holder);
  
	//showHolders("getGLTextureId(): created holder " + holder->description());
  
	return new TextureIDReference(holder._glTextureId, image.isPremultiplied(), this);
  }


  //This two methods are supposed to be accessed only by TextureIDReference class
  public final void releaseGLTextureId(IGLTextureId glTextureId)
  {
	if (glTextureId == null)
	{
	  return;
	}
  
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
  
	  if (holder._glTextureId.isEquals(glTextureId))
	  {
		holder.release();
  
		//showHolders("releaseGLTextureId(  ): released holder " + holder->description());
  
		if (!holder.isRetained())
		{
		  _gl.deleteTexture(holder._glTextureId);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		  _textureHolders.erase(_textureHolders.iterator() + i);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		  _textureHolders.remove(i);
//#endif
  
		  if (holder != null)
			  holder.dispose();
		}
  
		return;
	  }
	}
  }
  public final void retainGLTextureId(IGLTextureId glTextureId)
  {
	if (glTextureId == null)
	{
	  return;
	}
  
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
  
	  if (holder._glTextureId.isEquals(glTextureId))
	  {
		holder.retain();
  
		//showHolders("retainGLTextureId(): retained holder " + holder->description());
  
		return;
	  }
	}
  
	ILogger.instance().logInfo("break (point) on me 6\n");
  }
}
