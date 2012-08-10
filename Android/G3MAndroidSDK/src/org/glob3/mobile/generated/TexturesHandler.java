package org.glob3.mobile.generated; 
//
//  TexturesHandler.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  TexturesHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureHolder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Rectangle;

public class TexturesHandler
{
  private java.util.ArrayList<TextureHolder> _textureHolders = new java.util.ArrayList<TextureHolder>();

  private final GL _gl;
  private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE
  private final TextureBuilder _texBuilder;

  private final boolean _verbose;


  public TexturesHandler(GL gl, IFactory factory, TextureBuilder texBuilder, boolean verbose)
  {
	  _gl = gl;
	  _factory = factory;
	  _texBuilder = texBuilder;
	  _verbose = verbose;
  }

  public void dispose()
  {
	if (_textureHolders.size() > 0)
	{
	  System.out.print("WARNING: The TexturesHandler is destroyed, but the inner textures were not released.\n");
	}
  }

  public final int getTextureIdFromFileName(String filename, int textureWidth, int textureHeight)
  {
	IImage image = _factory.createImageFromFileName(filename);
  
	final int texId = getTextureId(image, filename, textureWidth, textureHeight); // filename as the textureId
  
	if (image != null)
		image.dispose();
  
	return texId;
  }

  public final int getTextureId(java.util.ArrayList<IImage> images, String textureId, int textureWidth, int textureHeight)
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
  
	_textureHolders.add(holder);
  
	return holder._glTextureId;
  }

  public final int getTextureId(java.util.ArrayList<IImage> images, java.util.ArrayList<Rectangle> rectangles, String textureId, int textureWidth, int textureHeight)
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
  
	_textureHolders.add(holder);
  
	return holder._glTextureId;
  }

  public final int getTextureId(IImage image, String textureId, int textureWidth, int textureHeight)
  {
	final java.util.ArrayList<IImage> images = new java.util.ArrayList<IImage>();
	images.add(image);
	return getTextureId(images, textureId, textureWidth, textureHeight);
  }


  public final int getTextureIdIfAvailable(String textureId, int textureWidth, int textureHeight)
  {
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
	  if (holder.hasKey(textureId, textureWidth, textureHeight))
	  {
		holder.retain();
  
		return holder._glTextureId;
	  }
	}
  
	return -1;
  }


  public final void takeTexture(int glTextureId)
  {
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
  
	  if (holder._glTextureId == glTextureId)
	  {
		holder.release();
  
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

}