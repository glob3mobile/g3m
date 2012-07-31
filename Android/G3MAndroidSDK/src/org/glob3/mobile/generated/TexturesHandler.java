package org.glob3.mobile.generated; 
//C++ TO JAVA CONVERTER WARNING: The declaration of the following method implementation was not found:
//ORIGINAL LINE: int TexturesHandler::getTextureIdFromFileName(const String &filename, int textureWidth, int textureHeight)

//C++ TO JAVA CONVERTER WARNING: The declaration of the following method implementation was not found:
//ORIGINAL LINE: int TexturesHandler::getTextureIdIfAvailable(const String &textureId, int textureWidth, int textureHeight)

//C++ TO JAVA CONVERTER WARNING: The declaration of the following method implementation was not found:
//ORIGINAL LINE: int TexturesHandler::getTextureId(const java.util.ArrayList<const IImage*>& images, const String& textureId, int textureWidth, int textureHeight)

//C++ TO JAVA CONVERTER WARNING: The declaration of the following method implementation was not found:
//ORIGINAL LINE: int TexturesHandler::getTextureId(const java.util.ArrayList<const IImage*>& images, const java.util.ArrayList<const Rectangle*>& rectangles, const String& textureId, int textureWidth, int textureHeight)

//C++ TO JAVA CONVERTER WARNING: The declaration of the following method implementation was not found:
//ORIGINAL LINE: int TexturesHandler::getTextureId(const IImage *image, const String &textureId, int textureWidth, int textureHeight)

//C++ TO JAVA CONVERTER WARNING: The declaration of the following method implementation was not found:
//ORIGINAL LINE: void TexturesHandler::takeTexture(int glTextureId)

//C++ TO JAVA CONVERTER WARNING: The declaration of the following method implementation was not found:
//ORIGINAL LINE: TexturesHandler::~TexturesHandler()


public class TexturesHandler
{
	public int getTextureIdFromFileName(String filename, int textureWidth, int textureHeight)
	{
	  final IImage image = _factory.createImageFromFileName(filename);
    
	  final int texId = getTextureId(image, filename, textureWidth, textureHeight); // filename as the textureId
    
	  if (image != null)
		  image.dispose();
    
	  return texId;
	}
	public int getTextureIdIfAvailable(String textureId, int textureWidth, int textureHeight)
	{
	  for (int i = 0; i < _textureHolders.size(); i++)
	  {
		TextureHolder holder = _textureHolders[i];
		if (holder.hasKey(textureId, textureWidth, textureHeight))
		{
		  holder.retain();
    
		  return holder._glTextureId;
		}
	  }
    
	  return -1;
	}
	public int getTextureId(java.util.ArrayList<const IImage> images, String textureId, int textureWidth, int textureHeight)
	{
	  int previousId = getTextureIdIfAvailable(textureId, textureWidth, textureHeight);
	  if (previousId >= 0)
	  {
		return previousId;
	  }
    
	  TextureHolder holder = new TextureHolder(textureId, textureWidth, textureHeight);
	  holder._glTextureId = _texBuilder.createTextureFromImages(_gl, images, textureWidth, textureHeight);
    
	  if (_verbose)
	  {
		ILogger.instance().logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d", textureId, textureWidth, textureHeight, holder._glTextureId);
	  }
    
	  _textureHolders.push_back(holder);
    
	  return holder._glTextureId;
	}
	public int getTextureId(java.util.ArrayList<const IImage> images, java.util.ArrayList<const Rectangle> rectangles, String textureId, int textureWidth, int textureHeight)
	{
	  int previousId = getTextureIdIfAvailable(textureId, textureWidth, textureHeight);
	  if (previousId >= 0)
	  {
		return previousId;
	  }
    
	  TextureHolder holder = new TextureHolder(textureId, textureWidth, textureHeight);
	  holder._glTextureId = _texBuilder.createTextureFromImages(_gl, _factory, images, rectangles, textureWidth, textureHeight);
    
	  if (_verbose)
	  {
		ILogger.instance().logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d", textureId, textureWidth, textureHeight, holder._glTextureId);
	  }
    
	  _textureHolders.push_back(holder);
    
	  return holder._glTextureId;
	}
	public int getTextureId(IImage image, String textureId, int textureWidth, int textureHeight)
	{
    
	  final java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
	  images.add(image);
	  return getTextureId(images, textureId, textureWidth, textureHeight);
	}
	public void takeTexture(int glTextureId)
	{
	  for (int i = 0; i < _textureHolders.size(); i++)
	  {
		TextureHolder holder = _textureHolders[i];
    
		if (holder._glTextureId == glTextureId)
		{
		  holder.release();
    
		  if (!holder.isRetained())
		  {
			_textureHolders.remove(i);
    
			_gl.deleteTexture(holder._glTextureId);
    
			if (holder != null)
				holder.dispose();
		  }
    
		  return;
		}
	  }
	}
	public void dispose()
	{
	  if (_textureHolders.size() > 0)
	  {
		System.out.print("WARNING: The TexturesHandler is destroyed, but the inner textures were not released.\n");
	  }
	}
}