package org.glob3.mobile.generated; 
//
//  GLGlobalState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

//
//  GLGlobalState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//class IFloatBuffer;




//class GL;
//struct AttributesStruct;
//class UniformsStruct;
//class GPUProgram;

//#define MAX_N_TEXTURES 4

public class GLGlobalState
{

  private static boolean _initializationAvailable = false;

  private boolean _depthTest;
  private boolean _blend;
  private boolean _cullFace;
  private int _culledFace;

  private final IGLTextureId[] _boundTextureId = new IGLTextureId[DefineConstants.MAX_N_TEXTURES];

  private float _lineWidth;

  //Polygon Offset
  private boolean _polygonOffsetFill;
  private float _polygonOffsetFactor;
  private float _polygonOffsetUnits;

  //Blending Factors
  private int _blendSFactor;
  private int _blendDFactor;

  //Texture Parameters
  private int _pixelStoreIAlignmentUnpack;

  //Clear color
  private float _clearColorR;
  private float _clearColorG;
  private float _clearColorB;
  private float _clearColorA;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLGlobalState(GLGlobalState parentState);


  public static void initializationAvailable()
  {
    _initializationAvailable = true;
  }

  public GLGlobalState()
  {
     _depthTest = false;
     _blend = false;
     _cullFace = true;
     _culledFace = GLCullFace.back();
     _lineWidth = 1F;
     _polygonOffsetFactor = 0F;
     _polygonOffsetUnits = 0F;
     _polygonOffsetFill = false;
     _blendDFactor = GLBlendFactor.zero();
     _blendSFactor = GLBlendFactor.one();
     _pixelStoreIAlignmentUnpack = -1;
     _clearColorR = 0.0F;
     _clearColorG = 0.0F;
     _clearColorB = 0.0F;
     _clearColorA = 0.0F;

    if (!_initializationAvailable)
    {
      ILogger.instance().logError("GLGlobalState creation before it is available.");
    }

    for (int i = 0; i < DefineConstants.MAX_N_TEXTURES; i++)
    {
      _boundTextureId[i] = null;
    }

  }

  public static GLGlobalState newDefault()
  {
    return new GLGlobalState();
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

  public final void enablePolygonOffsetFill(float factor, float units)
  {
    _polygonOffsetFill = true;
    _polygonOffsetFactor = factor;
    _polygonOffsetUnits = units;
  }
  public final void disablePolygonOffsetFill()
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

  public final void bindTexture(int target, IGLTextureId textureId)
  {
    if (target > DefineConstants.MAX_N_TEXTURES)
    {
      ILogger.instance().logError("WRONG TARGET FOR TEXTURE");
      return;
    }

    _boundTextureId[target] = textureId;
  }

  public final void onTextureDelete(IGLTextureId textureId)
  {
    for (int i = 0; i < DefineConstants.MAX_N_TEXTURES; i++)
    {
      if (_boundTextureId[i] == textureId)
      {
        _boundTextureId[i] = null;
      }
    }
  }

  public final void setPixelStoreIAlignmentUnpack(int p)
  {
    _pixelStoreIAlignmentUnpack = p;
  }

  public final void setClearColor(Color color)
  {
    _clearColorR = color._red;
    _clearColorG = color._green;
    _clearColorB = color._blue;
    _clearColorA = color._alpha;
  }

  public final void applyChanges(GL gl, GLGlobalState currentState)
  {
  
    INativeGL nativeGL = gl.getNative();
  
    // Depth Test
    if (_depthTest != currentState._depthTest)
    {
      if (_depthTest)
      {
        nativeGL.enable(GLStage.depthTest());
      }
      else
      {
        nativeGL.disable(GLStage.depthTest());
      }
      currentState._depthTest = _depthTest;
    }
  
    // Blending
    if (_blend != currentState._blend)
    {
      if (_blend)
      {
        nativeGL.enable(GLStage.blend());
      }
      else
      {
        nativeGL.disable(GLStage.blend());
      }
      currentState._blend = _blend;
    }
  
    // Cull Face
    if (_cullFace != currentState._cullFace)
    {
      currentState._cullFace = _cullFace;
      if (_cullFace)
      {
        nativeGL.enable(GLStage.cullFace());
        if (_culledFace != currentState._culledFace)
        {
          nativeGL.cullFace(_culledFace);
          currentState._culledFace = _culledFace;
        }
      }
      else
      {
        nativeGL.disable(GLStage.cullFace());
      }
    }
  
    if (_lineWidth != currentState._lineWidth)
    {
      nativeGL.lineWidth(_lineWidth);
      currentState._lineWidth = _lineWidth;
    }
  
    //Polygon Offset
    if (_polygonOffsetFill != currentState._polygonOffsetFill)
    {
      currentState._polygonOffsetFill = _polygonOffsetFill;
      if (_polygonOffsetFill)
      {
        nativeGL.enable(GLStage.polygonOffsetFill());
  
        if (_polygonOffsetFactor != currentState._polygonOffsetFactor || _polygonOffsetUnits != currentState._polygonOffsetUnits)
        {
          nativeGL.polygonOffset(_polygonOffsetFactor, _polygonOffsetUnits);
  
          currentState._polygonOffsetUnits = _polygonOffsetUnits;
          currentState._polygonOffsetFactor = _polygonOffsetFactor;
        }
      }
      else
      {
        nativeGL.disable(GLStage.polygonOffsetFill());
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
  
    for (int i = 0; i < DefineConstants.MAX_N_TEXTURES; i++)
    {
  
      if (_boundTextureId[i] != null)
      {
        if (currentState._boundTextureId[i] == null || !_boundTextureId[i].isEquals(currentState._boundTextureId[i]))
        {
          nativeGL.setActiveTexture(i);
          nativeGL.bindTexture(GLTextureType.texture2D(), _boundTextureId[i]);
  
          currentState._boundTextureId[i] = _boundTextureId[i];
        }
        //else {
        //  ILogger::instance()->logInfo("Texture already bound.\n");
        //}
      }
    }
  
    if (_pixelStoreIAlignmentUnpack != -1 && _pixelStoreIAlignmentUnpack != currentState._pixelStoreIAlignmentUnpack)
    {
      nativeGL.pixelStorei(GLAlignment.unpack(), _pixelStoreIAlignmentUnpack);
      currentState._pixelStoreIAlignmentUnpack = _pixelStoreIAlignmentUnpack;
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
  
  }
}