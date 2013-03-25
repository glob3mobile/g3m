package org.glob3.mobile.generated; 
//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//class IFloatBuffer;



//class GL;
//struct AttributesStruct;
//class UniformsStruct;

public class GLState
{

  private int _stateTimeStamp;

  private boolean _depthTest;
  private boolean _blend;
  private boolean _textures;
  private boolean _texture2D;
  private boolean _vertexColor;
  private boolean _verticesPosition;
  private boolean _flatColor;
  private boolean _cullFace;
  private int _culledFace;

  private IFloatBuffer _colors; //Vertex colors
  private int _colorsTimeStamp;
  private float _intensity;
  private float _flatColorR;
  private float _flatColorG;
  private float _flatColorB;
  private float _flatColorA;

  private IFloatBuffer _vertices;
  private int _verticesTimestamp;
  private int _verticesSize;
  private int _verticesStride;

  private IFloatBuffer _textureCoordinates;
  private int _textureCoordinatesTimestamp;
  private int _textureCoordinatesSize;
  private int _textureCoordinatesStride;
  private float _textureCoordinatesScaleX;
  private float _textureCoordinatesScaleY;
  private float _textureCoordinatesTranslationX;
  private float _textureCoordinatesTranslationY;

  //Texture Extent
  private float _textureWidth;
  private float _textureHeight;

  private IGLTextureId _boundTextureId;

  private float _lineWidth;
  private float _pointSize;

  //Polygon Offset
  private boolean _polygonOffsetFill;
  private float _polygonOffsetFactor;
  private float _polygonOffsetUnits;

  //Blending Factors
  private int _blendSFactor;
  private int _blendDFactor;

  //Billboarding
  private boolean _billboarding;

  //Viewport
  private int _viewportWidth;
  private int _viewportHeight;

  //Texture Parameters
  private int _texParMinFilter;
  private int _texParMagFilter;
  private int _texParWrapS;
  private int _texParWrapT;
  private int _pixelStoreIAlignmentUnpack;

  //Clear color
  private float _clearColorR;
  private float _clearColorG;
  private float _clearColorB;
  private float _clearColorA;

  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();
  private MutableMatrix44D _modelViewMatrix = new MutableMatrix44D();


  private GLState()
  {
     _stateTimeStamp = 0;
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
     _colorsTimeStamp = 0;
     _intensity = 0F;
     _flatColorR = 0F;
     _flatColorG = 0F;
     _flatColorB = 0F;
     _flatColorA = 0F;
     _lineWidth = 1F;
     _pointSize = 1F;
     _polygonOffsetFactor = 0F;
     _polygonOffsetUnits = 0F;
     _polygonOffsetFill = false;
     _blendDFactor = GLBlendFactor.zero();
     _blendSFactor = GLBlendFactor.one();
     _vertices = null;
     _verticesTimestamp = 0;
     _verticesSize = 0;
     _verticesStride = 0;
     _textureCoordinates = null;
     _textureCoordinatesTimestamp = 0;
     _textureCoordinatesSize = 0;
     _textureCoordinatesStride = 0;
     _textureCoordinatesScaleX = 1.0F;
     _textureCoordinatesScaleY = 1.0F;
     _textureCoordinatesTranslationX = 0.0F;
     _textureCoordinatesTranslationY = 0.0F;
     _boundTextureId = null;
     _billboarding = false;
     _viewportHeight = 0;
     _viewportWidth = 0;
     _texParMinFilter = -1;
     _texParMagFilter = -1;
     _texParWrapS = -1;
     _texParWrapT = -1;
     _pixelStoreIAlignmentUnpack = 0;
     _clearColorR = 0.0F;
     _clearColorG = 0.0F;
     _clearColorB = 0.0F;
     _clearColorA = 0.0F;
     _projectionMatrix = new MutableMatrix44D(MutableMatrix44D.invalid());
     _modelViewMatrix = new MutableMatrix44D(MutableMatrix44D.invalid());
     _textureWidth = 0.0F;
     _textureHeight = 0.0F;
  }



  public static GLState newDefault()
  {
    return new GLState();
  }

  public GLState(GLState parentState)
  {
     _stateTimeStamp = parentState._stateTimeStamp;
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
     _colorsTimeStamp = parentState._colorsTimeStamp;
     _intensity = parentState._intensity;
     _flatColorR = parentState._flatColorR;
     _flatColorG = parentState._flatColorG;
     _flatColorB = parentState._flatColorB;
     _flatColorA = parentState._flatColorA;
     _lineWidth = parentState._lineWidth;
     _pointSize = parentState._pointSize;
     _polygonOffsetFactor = parentState._polygonOffsetFactor;
     _polygonOffsetUnits = parentState._polygonOffsetUnits;
     _polygonOffsetFill = parentState._polygonOffsetFill;
     _blendDFactor = parentState._blendDFactor;
     _blendSFactor = parentState._blendSFactor;
     _vertices = parentState._vertices;
     _verticesTimestamp = parentState._verticesTimestamp;
     _verticesSize = parentState._verticesSize;
     _verticesStride = parentState._verticesStride;
     _textureCoordinates = parentState._textureCoordinates;
     _textureCoordinatesTimestamp = parentState._textureCoordinatesTimestamp;
     _textureCoordinatesSize = parentState._textureCoordinatesSize;
     _textureCoordinatesStride = parentState._textureCoordinatesStride;
     _textureCoordinatesScaleX = parentState._textureCoordinatesScaleX;
     _textureCoordinatesScaleY = parentState._textureCoordinatesScaleY;
     _textureCoordinatesTranslationX = parentState._textureCoordinatesTranslationX;
     _textureCoordinatesTranslationY = parentState._textureCoordinatesTranslationY;
     _boundTextureId = parentState._boundTextureId;
     _billboarding = parentState._billboarding;
     _viewportWidth = parentState._viewportWidth;
     _viewportHeight = parentState._viewportHeight;
     _texParMinFilter = parentState._texParMinFilter;
     _texParMagFilter = parentState._texParMagFilter;
     _texParWrapS = parentState._texParWrapS;
     _texParWrapT = parentState._texParWrapT;
     _pixelStoreIAlignmentUnpack = parentState._pixelStoreIAlignmentUnpack;
     _clearColorR = parentState._clearColorR;
     _clearColorG = parentState._clearColorG;
     _clearColorB = parentState._clearColorB;
     _clearColorA = parentState._clearColorA;
     _projectionMatrix = new MutableMatrix44D(parentState._projectionMatrix);
     _modelViewMatrix = new MutableMatrix44D(parentState._modelViewMatrix);
  }

  public void dispose()
  {
  }

  public final void enableBillboarding()
  {
    _billboarding = true;
  }
  public final void disableBillboarding()
  {
    _billboarding = false;
  }

  public final void setViewportSize(int w, int h)
  {
    _viewportWidth = w;
    _viewportHeight = h;
  }

  public final void enableDepthTest()
  {
    if (_depthTest != true)
    {
      _depthTest = true;
      _stateTimeStamp++;
    }
  }
  public final void disableDepthTest()
  {
    if (_depthTest != false)
    {
      _depthTest = false;
      _stateTimeStamp++;
    }
  }
  public final boolean isEnabledDepthTest()
  {
     return _depthTest;
  }

  public final void enableBlend()
  {
    if (_blend != true)
    {
      _blend = true;
      _stateTimeStamp++;
    }
  }
  public final void disableBlend()
  {
    if (_blend != false)
    {
      _blend = false;
      _stateTimeStamp++;
    }
  }
  public final boolean isEnabledBlend()
  {
     return _blend;
  }

  public final void enableTextures()
  {
    if (_textures != true)
    {
      _textures = true;
      _stateTimeStamp++;
    }
  }
  public final void disableTextures()
  {
    if (_textures != false)
    {
      _textures = false;
      _stateTimeStamp++;
    }
  }
  public final boolean isEnabledTextures()
  {
     return _textures;
  }

  public final void enableTexture2D()
  {
    if (_texture2D != true)
    {
      _stateTimeStamp++;
      _texture2D = true;
    }
  }
  public final void disableTexture2D()
  {
    if (_texture2D != false)
    {
      _texture2D = false;
      _stateTimeStamp++;
    }
  }
  public final boolean isEnabledTexture2D()
  {
     return _texture2D;
  }

  public final void enableVertexColor(IFloatBuffer colors, float intensity)
  {
    if (colors != _colors || _vertexColor != true || _intensity != intensity || _colorsTimeStamp != colors.timestamp())
    {
      _vertexColor = true;
      _colors = colors;
      _intensity = intensity;
      _colorsTimeStamp = colors.timestamp();
      _stateTimeStamp++;
    }
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

  public final void setPointSize(float ps)
  {
     _pointSize = ps;
  }
  public final float pointSize()
  {
     return _pointSize;
  }

  public final void enablePolygonOffsetFill(float factor, float units)
  {
    _polygonOffsetFill = true;
    _polygonOffsetFactor = factor;
    _polygonOffsetUnits = units;
  }
  public final void disPolygonOffsetFill()
  {
    _polygonOffsetFill = false;
  }

  public final boolean getPolygonOffsetFill()
  {
     return _polygonOffsetFill;
  }
  public final float getPolygonOffsetUnits()
  {
     return _polygonOffsetUnits;
  }
  public final float getPolygonOffsetFactor()
  {
     return _polygonOffsetFactor;
  }

  public final void setBlendFactors(int sFactor, int dFactor)
  {
    _blendSFactor = sFactor;
    _blendDFactor = dFactor;
  }

  public final void setVertices(IFloatBuffer vertices, int size, int stride)
  {
    _vertices = vertices;
    _verticesTimestamp = vertices.timestamp();
    _verticesSize = size;
    _verticesStride = stride;
  }

  public final void setTextureCoordinates(IFloatBuffer texCoors, int size, int stride)
  {
    _textureCoordinates = texCoors;
    _textureCoordinatesTimestamp = texCoors.timestamp();
    _textureCoordinatesSize = size;
    _textureCoordinatesStride = stride;
  }

  public final void scaleTextureCoordinates(float x, float y)
  {
    _textureCoordinatesScaleX = x;
    _textureCoordinatesScaleY = y;
  }

  public final void scaleTextureCoordinates(MutableVector2D scale)
  {
    _textureCoordinatesScaleX = (float) scale.x();
    _textureCoordinatesScaleY = (float) scale.y();
  }

  public final void scaleTextureCoordinates(Vector2D scale)
  {
    _textureCoordinatesScaleX = (float) scale.x();
    _textureCoordinatesScaleY = (float) scale.y();
  }

  public final void translateTextureCoordinates(float x, float y)
  {
    _textureCoordinatesTranslationX = x;
    _textureCoordinatesTranslationY = y;
  }

  public final void translateTextureCoordinates(MutableVector2D translation)
  {
    _textureCoordinatesTranslationX = (float) translation.x();
    _textureCoordinatesTranslationY = (float) translation.y();
  }

  public final void translateTextureCoordinates(Vector2D translation)
  {
    _textureCoordinatesTranslationX = (float) translation.x();
    _textureCoordinatesTranslationY = (float) translation.y();
  }

  public final void bindTexture(IGLTextureId textureId)
  {
    _boundTextureId = textureId;

    _texParMinFilter = -1;
  }

  public final IGLTextureId getBoundTexture()
  {
    return _boundTextureId;
  }

  public final void setTextureParameterMinFilter(int p)
  {
    _texParMinFilter = p;
  }

  public final void setTextureParameterMagFilter(int p)
  {
    _texParMagFilter = p;
  }

  public final void setTextureParameterWrapS(int p)
  {
    _texParWrapS = p;
  }

  public final void setTextureParameterWrapT(int p)
  {
    _texParWrapT = p;
  }

  public final void setPixelStoreIAlignmentUnpack(int p)
  {
    _pixelStoreIAlignmentUnpack = p;
  }

  public final void setClearColor(Color color)
  {
    _clearColorR = color.getRed();
    _clearColorG = color.getGreen();
    _clearColorB = color.getBlue();
    _clearColorA = color.getAlpha();
  }

  public final void setProjectionMatrix(MutableMatrix44D projection)
  {
    _projectionMatrix = projection;
  }

  public final void setModelViewMatrix(MutableMatrix44D mv)
  {
    _modelViewMatrix = mv;
  }

  public final void multiplyModelViewMatrix(MutableMatrix44D mv)
  {
    _modelViewMatrix = _modelViewMatrix.multiply(mv);
  }

  public final void setTextureExtent(float w, float h)
  {
    _textureHeight = h;
    _textureWidth = w;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void applyChanges(INativeGL nativeGL, GLState currentState, AttributesStruct attributes, UniformsStruct uniforms);

}