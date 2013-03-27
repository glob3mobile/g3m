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
     _depthTest = false;
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
     _pixelStoreIAlignmentUnpack = -1;
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
      _colorsTimeStamp = colors.timestamp();
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
  }

  public final IGLTextureId getBoundTexture()
  {
    return _boundTextureId;
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

  public final void applyChanges(INativeGL nativeGL, GLState currentState, AttributesStruct attributes, UniformsStruct uniforms)
  {
  
    // Depth Test
    if (_depthTest != currentState._depthTest)
    {
      if (_depthTest)
      {
        nativeGL.enable(GLFeature.depthTest());
      }
      else
      {
        nativeGL.disable(GLFeature.depthTest());
      }
      currentState._depthTest = _depthTest;
    }
  
    // Blending
    if (_blend != currentState._blend)
    {
      if (_blend)
      {
        nativeGL.enable(GLFeature.blend());
      }
      else
      {
        nativeGL.disable(GLFeature.blend());
      }
      currentState._blend = _blend;
    }
  
    // Textures
    if (_textures != currentState._textures)
    {
      if (_textures)
      {
        nativeGL.enableVertexAttribArray(attributes.TextureCoord);
      }
      else
      {
        nativeGL.disableVertexAttribArray(attributes.TextureCoord);
      }
      currentState._textures = _textures;
    }
  
    // Texture2D
    if (_texture2D != currentState._texture2D)
    {
      if (_texture2D)
      {
        nativeGL.uniform1i(uniforms.EnableTexture, 1);
      }
      else
      {
        nativeGL.uniform1i(uniforms.EnableTexture, 0);
      }
      currentState._texture2D = _texture2D;
    }
  
    // VertexColor
    if (_vertexColor != currentState._vertexColor)
    {
      if (_vertexColor)
      {
        nativeGL.uniform1i(uniforms.EnableColorPerVertex, 1);
        nativeGL.enableVertexAttribArray(attributes.Color);
      }
      else
      {
        nativeGL.disableVertexAttribArray(attributes.Color);
        nativeGL.uniform1i(uniforms.EnableColorPerVertex, 0);
      }
      currentState._vertexColor = _vertexColor;
    }
    if (_vertexColor)
    {
      if ((_colors != currentState._colors) || (_colorsTimeStamp != currentState._colorsTimeStamp))
      {
        nativeGL.vertexAttribPointer(attributes.Color, 4, false, 0, _colors);
        currentState._colors = _colors;
        currentState._colorsTimeStamp = _colorsTimeStamp;
      }
    }
  
  
    // Vertex
    if (_vertices != null)
    {
      if ((_vertices != currentState._vertices) || (_verticesTimestamp != currentState._verticesTimestamp) || (_verticesSize != currentState._verticesSize) || (_verticesStride != currentState._verticesStride))
      {
        nativeGL.vertexAttribPointer(attributes.Position, _verticesSize, false, _verticesStride, _vertices);
        currentState._vertices = _vertices;
        currentState._verticesTimestamp = _verticesTimestamp;
        currentState._verticesSize = _verticesSize;
        currentState._verticesStride = _verticesStride;
      }
    }
  
    // Vertices Position
    if (_verticesPosition != currentState._verticesPosition)
    {
      if (_verticesPosition)
      {
        nativeGL.enableVertexAttribArray(attributes.Position);
      }
      else
      {
        nativeGL.disableVertexAttribArray(attributes.Position);
      }
      currentState._verticesPosition = _verticesPosition;
    }
  
    //Texture Coordinates
    if (_textureCoordinates != null)
    {
      if (_textureCoordinates != currentState._textureCoordinates || _textureCoordinatesTimestamp != currentState._textureCoordinatesTimestamp || _textureCoordinatesSize != currentState._textureCoordinatesSize || _textureCoordinatesStride != currentState._textureCoordinatesStride)
      {
        nativeGL.vertexAttribPointer(attributes.TextureCoord, _textureCoordinatesSize, false, _textureCoordinatesStride, _textureCoordinates);
  
        currentState._textureCoordinates = _textureCoordinates;
        currentState._textureCoordinatesTimestamp = _textureCoordinatesTimestamp;
        currentState._textureCoordinatesSize = _textureCoordinatesSize;
        currentState._textureCoordinatesStride = _textureCoordinatesStride;
      }
    }
  
    if (_textureCoordinatesScaleX != currentState._textureCoordinatesScaleX || _textureCoordinatesScaleY != currentState._textureCoordinatesScaleY)
    {
      nativeGL.uniform2f(uniforms.ScaleTexCoord, _textureCoordinatesScaleX, _textureCoordinatesScaleY);
  
      currentState._textureCoordinatesScaleX = _textureCoordinatesScaleX;
      currentState._textureCoordinatesScaleY = _textureCoordinatesScaleY;
    }
  
    if (_textureCoordinatesTranslationX != currentState._textureCoordinatesTranslationX || _textureCoordinatesTranslationY != currentState._textureCoordinatesTranslationY)
    {
      nativeGL.uniform2f(uniforms.TranslationTexCoord, _textureCoordinatesTranslationX, _textureCoordinatesTranslationY);
  
      currentState._textureCoordinatesTranslationX = _textureCoordinatesTranslationX;
      currentState._textureCoordinatesTranslationY = _textureCoordinatesTranslationY;
    }
  
  
    // Flat Color
    if (_flatColor != currentState._flatColor)
    {
      if (_flatColor)
      {
        nativeGL.uniform1i(uniforms.EnableFlatColor, 1);
      }
      else
      {
        nativeGL.uniform1i(uniforms.EnableFlatColor, 0);
      }
      currentState._flatColor = _flatColor;
    }
  
    if (_flatColor)
    {
      if ((_flatColorR != currentState._flatColorR) || (_flatColorG != currentState._flatColorG) || (_flatColorB != currentState._flatColorB) || (_flatColorA != currentState._flatColorA))
      {
        nativeGL.uniform4f(uniforms.FlatColor, _flatColorR, _flatColorG, _flatColorB,_flatColorA);
  
        currentState._flatColorR = _flatColorR;
        currentState._flatColorG = _flatColorG;
        currentState._flatColorB = _flatColorB;
        currentState._flatColorA = _flatColorA;
      }
  
      if (_intensity != currentState._intensity)
      {
        nativeGL.uniform1f(uniforms.FlatColorIntensity, _intensity);
  
        currentState._intensity = _intensity;
      }
    }
  
    // Cull Face
    if (_cullFace != currentState._cullFace)
    {
      currentState._cullFace = _cullFace;
      if (_cullFace)
      {
        nativeGL.enable(GLFeature.cullFace());
        if (_culledFace != currentState._culledFace)
        {
          nativeGL.cullFace(_culledFace);
          currentState._culledFace = _culledFace;
        }
      }
      else
      {
        nativeGL.disable(GLFeature.cullFace());
      }
    }
  
    if (_lineWidth != currentState._lineWidth)
    {
      nativeGL.lineWidth(_lineWidth);
      currentState._lineWidth = _lineWidth;
    }
  
    if (_pointSize != currentState._pointSize)
    {
      nativeGL.uniform1f(uniforms.PointSize, _pointSize);
      currentState._pointSize = _pointSize;
    }
  
  
    //Polygon Offset
    if (_polygonOffsetFill != currentState._polygonOffsetFill)
    {
      currentState._polygonOffsetFill = _polygonOffsetFill;
      if (_polygonOffsetFill)
      {
        nativeGL.enable(GLFeature.polygonOffsetFill());
  
        if (_polygonOffsetFactor != currentState._polygonOffsetFactor || _polygonOffsetUnits != currentState._polygonOffsetUnits)
        {
          nativeGL.polygonOffset(_polygonOffsetFactor, _polygonOffsetUnits);
  
          currentState._polygonOffsetUnits = _polygonOffsetUnits;
          currentState._polygonOffsetFactor = _polygonOffsetFactor;
        }
  
      }
      else
      {
        nativeGL.disable(GLFeature.polygonOffsetFill());
      }
    }
  
    //Blending Factors
    if (_blendDFactor != currentState._blendDFactor || _blendSFactor != currentState._blendSFactor)
    {
      nativeGL.blendFunc(_blendSFactor, _blendDFactor);
      currentState._blendDFactor = _blendDFactor;
      currentState._blendSFactor = _blendSFactor;
    }
  
    //Texture (After blending factors)
    if (_boundTextureId != null)
    {
      if (currentState._boundTextureId == null || !_boundTextureId.isEqualsTo(currentState._boundTextureId))
      {
        nativeGL.bindTexture(GLTextureType.texture2D(), _boundTextureId);
  
        currentState._boundTextureId = _boundTextureId;
      }
      else
      {
        //ILogger::instance()->logInfo("Texture already bound.\n");
      }
    }
  
    if (_billboarding != currentState._billboarding)
    {
      if (_billboarding)
      {
        nativeGL.uniform1i(uniforms.BillBoard, 1);
      }
      else
      {
        nativeGL.uniform1i(uniforms.BillBoard, 0);
      }
      currentState._billboarding = _billboarding;
    }
  
    //Viewport
    if (_viewportHeight != currentState._viewportHeight || _viewportWidth != currentState._viewportWidth)
    {
      nativeGL.uniform2f(uniforms.ViewPortExtent, _viewportWidth, _viewportHeight);
  
      currentState._viewportWidth = _viewportWidth;
      currentState._viewportHeight = _viewportHeight;
    }
  
    if (_pixelStoreIAlignmentUnpack != -1 && _pixelStoreIAlignmentUnpack != currentState._pixelStoreIAlignmentUnpack)
    {
      nativeGL.pixelStorei(GLAlignment.unpack(), _pixelStoreIAlignmentUnpack);
      currentState._pixelStoreIAlignmentUnpack = _pixelStoreIAlignmentUnpack;
    }
  
    if (_clearColorR != currentState._clearColorR || _clearColorG != currentState._clearColorG || _clearColorB != currentState._clearColorB || _clearColorA != currentState._clearColorA)
    {
      nativeGL.clearColor(_clearColorR, _clearColorG, _clearColorB, _clearColorA);
      currentState._clearColorR = _clearColorR;
      currentState._clearColorG = _clearColorG;
      currentState._clearColorB = _clearColorB;
      currentState._clearColorA = _clearColorA;
    }
  
    //Projection
    if (!_projectionMatrix.isEqualsTo(currentState._projectionMatrix))
    {
      nativeGL.uniformMatrix4fv(uniforms.Projection, false, _projectionMatrix);
      currentState._projectionMatrix = _projectionMatrix;
    }
  
    //Modelview
    if (!_modelViewMatrix.isEqualsTo(currentState._modelViewMatrix))
    {
      nativeGL.uniformMatrix4fv(uniforms.Modelview, false, _modelViewMatrix);
      currentState._modelViewMatrix = _modelViewMatrix;
    }
  
    //Texture Extent
    if (_textureWidth != currentState._textureWidth || _textureHeight != currentState._textureHeight)
    {
      nativeGL.uniform2f(uniforms.TextureExtent, _textureWidth, _textureHeight);
      currentState._textureHeight = _textureHeight;
      currentState._textureWidth = _textureWidth;
    }
  
  }

}