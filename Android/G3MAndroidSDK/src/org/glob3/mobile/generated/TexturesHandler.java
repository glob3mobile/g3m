package org.glob3.mobile.generated; 
public class TexturesHandler
{
  private java.util.ArrayList<TextureHolder> _textureHolders = new java.util.ArrayList<TextureHolder>();

  private final GL _gl;
  private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE
  private final TextureBuilder _textureBuilder;

  private final boolean _verbose;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showHolders(const String message) const
  private void showHolders(String message)
  {
	if (false)
	{
	  String holdersString = ">>>> " + message + ", Holders=(";
	  for (int i = 0; i < _textureHolders.size(); i++)
	  {
		TextureHolder holder = _textureHolders.get(i);
  
		if (i > 0)
		{
		  holdersString += ", ";
		}
		holdersString += holder.description();
	  }
	  holdersString += ")";
  
	  System.out.printf("%s\n", holdersString);
	}
  }


  public TexturesHandler(GL gl, IFactory factory, TextureBuilder texBuilder, boolean verbose)
  {
	  _gl = gl;
	  _factory = factory;
	  _textureBuilder = texBuilder;
	  _verbose = verbose;
  }

  public void dispose()
  {
	if (_textureHolders.size() > 0)
	{
	  System.out.print("WARNING: The TexturesHandler is destroyed, but the inner textures were not released.\n");
	}
  }

  public final GLTextureID getGLTextureIdFromFileName(String filename, int textureWidth, int textureHeight)
  {
	IImage image = _factory.createImageFromFileName(filename);
  
	final GLTextureID texId = getGLTextureId(image, new TextureSpec(filename, textureWidth, textureHeight)); // filename as the id
	_factory.deleteImage(image);
  
	return texId;
  }

  public final GLTextureID getGLTextureId(java.util.ArrayList<IImage> images, TextureSpec textureSpec)
  {
	GLTextureID previousId = getGLTextureIdIfAvailable(textureSpec);
	if (previousId.isValid())
	{
	  return previousId;
	}
  
	TextureHolder holder = new TextureHolder(textureSpec);
	holder._glTextureId = _textureBuilder.createTextureFromImages(_gl, images, textureSpec.getWidth(), textureSpec.getHeight());
  
	if (_verbose)
	{
	  ILogger.instance().logInfo("Uploaded texture \"%s\" to GPU with texId=%s", textureSpec.description(), holder._glTextureId.description());
	}
  
	_textureHolders.add(holder);
  
	showHolders("getGLTextureId(): created holder " + holder.description());
  
	return holder._glTextureId;
  }

  public final GLTextureID getGLTextureId(java.util.ArrayList<IImage> images, java.util.ArrayList<Rectangle> rectangles, TextureSpec textureSpec)
  {
	GLTextureID previousId = getGLTextureIdIfAvailable(textureSpec);
	if (previousId.isValid())
	{
	  return previousId;
	}
  
	TextureHolder holder = new TextureHolder(textureSpec);
	holder._glTextureId = _textureBuilder.createTextureFromImages(_gl, _factory, images, rectangles, textureSpec.getWidth(), textureSpec.getHeight());
  
	if (_verbose)
	{
	  ILogger.instance().logInfo("Uploaded texture \"%s\" to GPU with texId=%s", textureSpec.description(), holder._glTextureId.description());
	}
  
	_textureHolders.add(holder);
  
	showHolders("getGLTextureId(): created holder " + holder.description());
  
	return holder._glTextureId;
  }

  public final GLTextureID getGLTextureId(IImage image, TextureSpec textureSpec)
  {
	final java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
	images.add(image);
	return getGLTextureId(images, textureSpec);
  }

  public final GLTextureID getGLTextureIdIfAvailable(TextureSpec textureSpec)
  {
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
	  if (holder.hasSpec(textureSpec))
	  {
		holder.retain();
  
		showHolders("getGLTextureIdIfAvailable(): retained " + holder.description());
  
		return holder._glTextureId;
	  }
	}
  
	return GLTextureID.invalid();
  }

  public final void releaseGLTextureId(GLTextureID glTextureId)
  {
	if (!glTextureId.isValid())
	{
	  return;
	}
  
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
  
	  if (holder._glTextureId.isEqualsTo(glTextureId))
	  {
		holder.release();
  
		showHolders("releaseGLTextureId(  ): released holder " + holder.description());
  
		if (!holder.isRetained())
		{
		  _gl.deleteTexture(holder._glTextureId);
  
  		_textureHolders.remove(i);
  
		  if (holder != null)
			  holder.dispose();
		}
  
		return;
	  }
	}
  }

  public final void retainGLTextureId(GLTextureID glTextureId)
  {
	if (!glTextureId.isValid())
	{
	  return;
	}
  
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
  
	  if (holder._glTextureId.isEqualsTo(glTextureId))
	  {
		holder.retain();
  
		showHolders("retainGLTextureId(): retained holder " + holder.description());
  
		return;
	  }
	}
  
	System.out.print("break (point) on me 6\n");
  }

}