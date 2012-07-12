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




public class TexturesHandler
{
  private java.util.ArrayList<TextureHolder> _textureHolders = new java.util.ArrayList<TextureHolder>();

  public void dispose()
  {
	if (_textureHolders.size() > 0)
	{
	  System.out.print("WARNING: The TexturesHandler is destroyed, but the inner textures were not released.\n");
	}
  }

  public final int getTextureIdFromFileName(RenderContext rc, String filename, int textureWidth, int textureHeight)
  {
	final IImage image = rc.getFactory().createImageFromFileName(filename);
  
	final int texId = getTextureId(rc, image, filename, textureWidth, textureHeight); // filename as the textureId
  
	if (image != null)
		image.dispose();
  
	return texId;
  }

  public final int getTextureId(RenderContext rc, IImage image, String textureId, int textureWidth, int textureHeight)
  {
  
	TextureHolder candidate = new TextureHolder(textureId, textureWidth, textureHeight);
  
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
	  if (holder.equalsTo(candidate))
	  {
		holder.retain();
		if (candidate != null)
			candidate.dispose();
  
		return holder._glTextureId;
	  }
	}
  
	candidate._glTextureId = rc.getGL().uploadTexture(image, textureWidth, textureHeight);
  
	rc.getLogger().logInfo("Uploaded texture \"%s\" (%dx%d) to GPU with texId=%d", textureId, textureWidth, textureHeight, candidate._glTextureId);
  
	_textureHolders.add(candidate);
  
	return candidate._glTextureId;
  }

  public final void takeTexture(RenderContext rc, int glTextureId)
  {
	for (int i = 0; i < _textureHolders.size(); i++)
	{
	  TextureHolder holder = _textureHolders.get(i);
  
	  if (holder._glTextureId == glTextureId)
	  {
		holder.release();
  
		if (!holder.isRetained())
		{
  		_textureHolders.remove(i);
  
		  rc.getGL().deleteTexture(holder._glTextureId);
  
		  if (holder != null)
			  holder.dispose();
		}
  
		return;
	  }
	}
  }

}