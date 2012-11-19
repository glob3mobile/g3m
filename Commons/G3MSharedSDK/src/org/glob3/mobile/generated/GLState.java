package org.glob3.mobile.generated; 
//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;


public class GLState
{

  private boolean _depthTest;
  private boolean _blend;
  private boolean _textures;
  private boolean _texture2D;
  private boolean _vertexColor;
  private boolean _verticesPosition;
  private boolean _flatColor;
  private boolean _cullFace;

  private IFloatBuffer _colors;
  private float _intensity;
  private float _flatColorR;
  private float _flatColorG;
  private float _flatColorB;
  private float _flatColorA;
  private int _culledFace;


  public GLState()
  {
	  _depthTest = true;
	  _blend = false;
	  _textures = false;
	  _texture2D = false;
	  _vertexColor = false;
	  _verticesPosition = false;
	  _flatColor = false;
	  _cullFace = true;
	  _culledFace = GLCullFace.back();
  }

  public final void enableDepthTest()
  {
	  _depthTest = true;
  }
  public final void disableDepthTest()
  {
	  _depthTest = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledDepthTest() const
  public final boolean isEnabledDepthTest()
  {
	  return _depthTest;
  }

  public final void enableBlend()
  {
	  _blend = true;
  }
  public final void disableBlend()
  {
	  _blend = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledBlend() const
  public final boolean isEnabledBlend()
  {
	  return _blend;
  }

  public final void enableTextures()
  {
	  _textures = true;
  }
  public final void disableTextures()
  {
	  _textures = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledTextures() const
  public final boolean isEnabledTextures()
  {
	  return _textures;
  }

  public final void enableTexture2D()
  {
	  _texture2D = true;
  }
  public final void disableTexture2D()
  {
	  _texture2D = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledTexture2D() const
  public final boolean isEnabledTexture2D()
  {
	  return _texture2D;
  }

  public final void enableVertexColor(IFloatBuffer colors, float intensity)
  {
	_vertexColor = true;
	_colors = colors;
	_intensity = intensity;
  }
  public final void disableVertexColor()
  {
	  _vertexColor = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledVertexColor() const
  public final boolean isEnabledVertexColor()
  {
	  return _vertexColor;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getColors() const
  public final IFloatBuffer getColors()
  {
	  return _colors;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getIntensity() const
  public final float getIntensity()
  {
	  return _intensity;
  }

  public final void enableVerticesPosition()
  {
	  _verticesPosition = true;
  }
  public final void disableVerticesPosition()
  {
	  _verticesPosition = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledVerticesPosition() const
  public final boolean isEnabledVerticesPosition()
  {
	  return _verticesPosition;
  }

  public final void enableFlatColor(Color c, float intensity)
  {
	_flatColor = true;
	_flatColorR = c.getRed();
	_flatColorG = c.getGreen();
	_flatColorB = c.getBlue();
	_flatColorA = c.getAlpha();
	_intensity = intensity;
  }
  public final void disableFlatColor()
  {
	  _flatColor = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledFlatColor() const
  public final boolean isEnabledFlatColor()
  {
	  return _flatColor;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color getFlatColor() const
  public final Color getFlatColor()
  {
	return Color.fromRGBA(_flatColorR, _flatColorG, _flatColorB, _flatColorA);
  }

  public final void enableCullFace(int face)
  {
	_cullFace = true;
	_culledFace = face;
  }
  public final void disableCullFace()
  {
	  _cullFace = false;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabledCullFace() const
  public final boolean isEnabledCullFace()
  {
	  return _cullFace;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getCulledFace() const
  public final int getCulledFace()
  {
	  return _culledFace;
  }

}