package org.glob3.mobile.generated; 
//
//  TextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;


public class TextureMapping
{
  private final int _textureId;
  private final float[] _texCoords;
  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();
  private final TexturesHandler _texHandler;
  private final int _width;
  private final int _height;
  private final String _texID;


  public TextureMapping(int textureId, float[] texCoords, TexturesHandler texHandler, String texID, int width, int height)
  {
	  _textureId = textureId;
	  _texCoords = texCoords;
	  _texHandler = texHandler;
	  _width = width;
	  _height = height;
	  _texID = texID;
	_translation = new MutableVector2D(0, 0);
	_scale = new MutableVector2D(1, 1);
  }

  public TextureMapping(int textureId, java.util.ArrayList<MutableVector2D> texCoords, TexturesHandler texHandler, String texID, int width, int height)
  {
	  _textureId = textureId;
	  _texHandler = texHandler;
	  _width = width;
	  _height = height;
	  _texID = texID;
	float[] texCoordsA = new float[2 * texCoords.size()];
	int p = 0;
	for (int i = 0; i < texCoords.size(); i++)
	{
	  texCoordsA[p++] = (float) texCoords.get(i).x();
	  texCoordsA[p++] = (float) texCoords.get(i).y();
	}
	_texCoords = texCoordsA;
  
	_translation = new MutableVector2D(0.0, 0.0);
	_scale = new MutableVector2D(1.0, 1.0);
  }

  public final void setTranslationAndScale(Vector2D translation, Vector2D scale)
  {
	_translation = translation.asMutableVector2D();
	_scale = scale.asMutableVector2D();
  }

  public void dispose()
  {

	if (_texHandler != null)
	{
	  _texHandler.takeTexture(_textureId);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getTextureId() const
  public final int getTextureId()
  {
	return _textureId;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getStringTexID() const
  public final String getStringTexID()
  {
	return _texID;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getWidth() const
  public final int getWidth()
  {
	return _width;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getHeight() const
  public final int getHeight()
  {
	return _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const float[] getTexCoords() const
  public final float[] getTexCoords()
  {
	  return _texCoords;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void bind(const RenderContext* rc) const
  public final void bind(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.transformTexCoords(_scale, _translation);
  
	gl.bindTexture(_textureId);
	gl.setTextureCoordinates(2, 0, _texCoords);
  }
}