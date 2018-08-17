package org.glob3.mobile.generated;import java.util.*;

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
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//struct AttributesStruct;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class UniformsStruct;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgram;

//#define MAX_N_TEXTURES 4

public class GLGlobalState
{

  private static boolean _initializationAvailable = false;

  private boolean _depthTest;
  private boolean _blend;
  private boolean _cullFace;
  private int _culledFace;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private static final IGLTextureId[] _boundTextureId = new IGLTextureId[DefineConstants.MAX_N_TEXTURES];
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  private internal[] final IGLTextureId _boundTextureId = new IGLTextureId[DefineConstants.MAX_N_TEXTURES];
//#endif

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
	  _cullFace = false;
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

  public final void setLineWidth(float lineWidth)
  {
	_lineWidth = lineWidth;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float lineWidth() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getPolygonOffsetFill() const
  public final boolean getPolygonOffsetFill()
  {
	  return _polygonOffsetFill;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getPolygonOffsetUnits() const
  public final float getPolygonOffsetUnits()
  {
	  return _polygonOffsetUnits;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getPolygonOffsetFactor() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyChanges(GL* gl, GLGlobalState& currentState) const
  public final void applyChanges(GL gl, tangible.RefObject<GLGlobalState> currentState)
  {
  
	INativeGL nativeGL = gl.getNative();
  
	// Depth Test
	if (_depthTest != currentState.argvalue._depthTest)
	{
	  if (_depthTest)
	  {
		nativeGL.enable(GLStage.depthTest());
	  }
	  else
	  {
		nativeGL.disable(GLStage.depthTest());
	  }
	  currentState.argvalue._depthTest = _depthTest;
	}
  
	// Blending
	if (_blend != currentState.argvalue._blend)
	{
	  if (_blend)
	  {
		nativeGL.enable(GLStage.blend());
	  }
	  else
	  {
		nativeGL.disable(GLStage.blend());
	  }
	  currentState.argvalue._blend = _blend;
	}
  
	// Cull Face
	if (_cullFace != currentState.argvalue._cullFace)
	{
	  currentState.argvalue._cullFace = _cullFace;
	  if (_cullFace)
	  {
		nativeGL.enable(GLStage.cullFace());
		if (_culledFace != currentState.argvalue._culledFace)
		{
		  nativeGL.cullFace(_culledFace);
		  currentState.argvalue._culledFace = _culledFace;
		}
	  }
	  else
	  {
		nativeGL.disable(GLStage.cullFace());
	  }
	}
  
	if (_lineWidth != currentState.argvalue._lineWidth)
	{
	  nativeGL.lineWidth(_lineWidth);
	  currentState.argvalue._lineWidth = _lineWidth;
	}
  
	//Polygon Offset
   // if (_polygonOffsetFill != currentState._polygonOffsetFill) {
  //    currentState._polygonOffsetFill = _polygonOffsetFill;
	  if (_polygonOffsetFill)
	  {
		nativeGL.enable(GLStage.polygonOffsetFill());
  
	   /* if (_polygonOffsetFactor != currentState._polygonOffsetFactor ||
			_polygonOffsetUnits != currentState._polygonOffsetUnits) {*/
		  nativeGL.polygonOffset(_polygonOffsetFactor, _polygonOffsetUnits);
  
		  currentState.argvalue._polygonOffsetUnits = _polygonOffsetUnits;
		  currentState.argvalue._polygonOffsetFactor = _polygonOffsetFactor;
	 //   }
	  }
	  else
	  {
		nativeGL.disable(GLStage.polygonOffsetFill());
	  }
  //  }
  
	//Blending Factors
	if (_blendDFactor != currentState.argvalue._blendDFactor || _blendSFactor != currentState.argvalue._blendSFactor)
	{
	  nativeGL.blendFunc(_blendSFactor, _blendDFactor);
	  currentState.argvalue._blendDFactor = _blendDFactor;
	  currentState.argvalue._blendSFactor = _blendSFactor;
	}
  
	//Texture (After blending factors)
  
	for (int i = 0; i < DefineConstants.MAX_N_TEXTURES; i++)
	{
  
	  if (_boundTextureId[i] != null)
	  {
		if (currentState.argvalue._boundTextureId[i] == null || !_boundTextureId[i].isEquals(currentState.argvalue._boundTextureId[i]))
		{
		  nativeGL.setActiveTexture(i);
		  nativeGL.bindTexture(GLTextureType.texture2D(), _boundTextureId[i]);
  
		  currentState.argvalue._boundTextureId[i] = _boundTextureId[i];
		}
		//else {
		//  ILogger::instance()->logInfo("Texture already bound.\n");
		//}
	  }
	}
  
	if (_pixelStoreIAlignmentUnpack != -1 && _pixelStoreIAlignmentUnpack != currentState.argvalue._pixelStoreIAlignmentUnpack)
	{
	  nativeGL.pixelStorei(GLAlignment.unpack(), _pixelStoreIAlignmentUnpack);
	  currentState.argvalue._pixelStoreIAlignmentUnpack = _pixelStoreIAlignmentUnpack;
	}
  
	if (_pixelStoreIAlignmentUnpack != -1 && _pixelStoreIAlignmentUnpack != currentState.argvalue._pixelStoreIAlignmentUnpack)
	{
	  nativeGL.pixelStorei(GLAlignment.unpack(), _pixelStoreIAlignmentUnpack);
	  currentState.argvalue._pixelStoreIAlignmentUnpack = _pixelStoreIAlignmentUnpack;
	}
  
	if (_clearColorR != currentState.argvalue._clearColorR || _clearColorG != currentState.argvalue._clearColorG || _clearColorB != currentState.argvalue._clearColorB || _clearColorA != currentState.argvalue._clearColorA)
	{
	  nativeGL.clearColor(_clearColorR, _clearColorG, _clearColorB, _clearColorA);
	  currentState.argvalue._clearColorR = _clearColorR;
	  currentState.argvalue._clearColorG = _clearColorG;
	  currentState.argvalue._clearColorB = _clearColorB;
	  currentState.argvalue._clearColorA = _clearColorA;
	}
  
  }
}
