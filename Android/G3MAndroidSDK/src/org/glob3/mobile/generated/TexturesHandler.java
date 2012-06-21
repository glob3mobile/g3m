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
//class TextureKey;




public class TexturesHandler
{
  private java.util.ArrayList<TextureKey> _textures = new java.util.ArrayList<TextureKey>();

  public int __Diego_destructor_must_delete_TextureKeys;
  public void dispose()
  {
  }

  public final int getTextureIdFromFileName(RenderContext rc, String filename, int textureWidth, int textureHeight)
  {
	IImage image = rc.getFactory().createImageFromFileName(filename);
  
	return getTextureId(rc, image, filename, textureWidth, textureHeight); // filename as the textureId
  }

  public final int getTextureId(RenderContext rc, IImage image, String textureId, int textureWidth, int textureHeight)
  {
  
	TextureKey key = new TextureKey(textureId, textureWidth, textureHeight);
  
	for (int i = 0; i < _textures.size(); i++)
	{
	  TextureKey each = _textures.get(i);
	  if (each.equalsTo(key))
	  {
		each.retain();
		return each._glTextureId;
	  }
	}
  
	key._glTextureId = rc.getGL().uploadTexture(image, textureWidth, textureHeight);
  
	rc.getLogger().logInfo("Uploaded texture \"%s\" (%ix%i) to GPU with texId=%i", textureId, textureWidth, textureHeight, key._glTextureId);
  
	_textures.add(key);
  
	return key._glTextureId;
  }

  public final void takeTexture(RenderContext rc, int glTextureId)
  {
	for (int i = 0; i < _textures.size(); i++)
	{
	  TextureKey each = _textures.get(i);
  
	  if (each._glTextureId == glTextureId)
	  {
		each.release();
  
		if (!each.isRetained())
		{
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		  _textures.remove(i);
  
		  rc.getGL().deleteTexture(each._glTextureId);
  
		  if (each != null)
			  each.dispose();
		}
  
		return;
	  }
	}
  
  }

}