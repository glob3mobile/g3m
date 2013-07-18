package org.glob3.mobile.generated; 
//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


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
  private int _culledFace;

  private IFloatBuffer _colors;
  private float _intensity;
  private float _flatColorR;
  private float _flatColorG;
  private float _flatColorB;
  private float _flatColorA;

  private float _lineWidth;
  private float _pointSize;


  private GLState()
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
     _colors = null;
     _intensity = 0F;
     _flatColorR = 0F;
     _flatColorG = 0F;
     _flatColorB = 0F;
     _flatColorA = 0F;
     _lineWidth = 1F;
     _pointSize = 1F;
  }



  public static GLState newDefault()
  {
    return new GLState();
  }

  public GLState(GLState parentState)
  {
     _depthTest = parentState._depthTest;
     _blend = parentState._blend;
     _textures = parentState._textures;
     _texture2D = parentState._texture2D;
     _vertexColor = parentState._vertexColor;
     _verticesPosition = parentState._verticesPosition;
     _flatColor = parentState._flatColor;
     _cullFace = parentState._cullFace;
     _culledFace = parentState._culledFace;
     _colors = parentState._colors;
     _intensity = parentState._intensity;
     _flatColorR = parentState._flatColorR;
     _flatColorG = parentState._flatColorG;
     _flatColorB = parentState._flatColorB;
     _flatColorA = parentState._flatColorA;
     _lineWidth = parentState._lineWidth;
     _pointSize = parentState._pointSize;
  }

  public void dispose()
  {
  }

  public final void enableDepthTest()
  {
     _depthTest = true;
  }
  public final void disableDepthTest()
  {
     _depthTest = false;
  }
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
  public final boolean isEnabledVertexColor()
  {
     return _vertexColor;
  }
  public final IFloatBuffer getColors()
  {
     return _colors;
  }
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
  public final boolean isEnabledVerticesPosition()
  {
     return _verticesPosition;
  }

  public final void enableFlatColor(Color color, float intensity)
  {
    _flatColor = true;
    _flatColorR = color.getRed();
    _flatColorG = color.getGreen();
    _flatColorB = color.getBlue();
    _flatColorA = color.getAlpha();
    _intensity = intensity;
  }
  public final void disableFlatColor()
  {
     _flatColor = false;
  }
  public final boolean isEnabledFlatColor()
  {
     return _flatColor;
  }
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
  public final boolean isEnabledCullFace()
  {
     return _cullFace;
  }
  public final int getCulledFace()
  {
     return _culledFace;
  }

  public final void setLineWidth(float lineWidth)
  {
     _lineWidth = lineWidth;
  }
  public final float lineWidth()
  {
     return _lineWidth;
  }

  public final void setPointSize(float pointSize)
  {
     _pointSize = pointSize;
  }
  public final float pointSize()
  {
     return _pointSize;
  }

}