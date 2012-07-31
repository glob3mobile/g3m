package org.glob3.mobile.generated; 
//
//  TexturesHandler.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




public class TextureHolder
{
  public final String _textureId;
  public final int _textureWidth;
  public final int _textureHeight;
  public int _glTextureId;

  public int _referenceCounter;

  public TextureHolder(String textureId, int textureWidth, int textureHeight)
  {
	  _textureId = textureId;
	  _textureWidth = textureWidth;
	  _textureHeight = textureHeight;
	_referenceCounter = 1;
	_glTextureId = -1;
  }

  public void dispose()
  {
  }

  public final void retain()
  {
	_referenceCounter++;
  }

  public final void release()
  {
	_referenceCounter--;
  }

  public final boolean isRetained()
  {
	return _referenceCounter > 0;
  }

  public final boolean hasKey(String textureId, int textureWidth, int textureHeight)
  {
	if (_textureWidth != textureWidth)
	{
	  return false;
	}
	if (_textureHeight != textureHeight)
	{
	  return false;
	}

	if (_textureId.compareTo(textureId) != 0)
	{
	  return false;
	}

	return true;
  }
}