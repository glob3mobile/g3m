//
//  GLGlobalState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GLGlobalState
#define G3MiOSSDK_GLGlobalState

class IFloatBuffer;

#include "Color.hpp"
#include "GLConstants.hpp"
#include "IFloatBuffer.hpp"
#include "MutableVector2D.hpp"
#include "MutableMatrix44D.hpp"
#include "Vector2D.hpp"

#include <list>


class GL;
struct AttributesStruct;
class UniformsStruct;
class GPUProgram;

#define MAX_N_TEXTURES 4

class GLGlobalState {
private:

  static bool _initializationAvailable;

  bool _depthTest;
  bool _blend;
  bool _cullFace;
  int  _culledFace;
  
#ifdef C_CODE
  const IGLTextureId* _boundTextureId[MAX_N_TEXTURES];
#endif
#ifdef JAVA_CODE
  private final IGLTextureId[] _boundTextureId = new IGLTextureId[DefineConstants.MAX_N_TEXTURES];
#endif

  float _lineWidth;
  
  //Polygon Offset
  bool  _polygonOffsetFill;
  float _polygonOffsetFactor;
  float _polygonOffsetUnits;
  
  //Blending Factors
  int _blendSFactor;
  int _blendDFactor;
  
  //Texture Parameters
  int _pixelStoreIAlignmentUnpack;
  
  //Clear color
  float _clearColorR;
  float _clearColorG;
  float _clearColorB;
  float _clearColorA;

  GLGlobalState(const GLGlobalState& parentState) :
  _depthTest(parentState._depthTest),
  _blend(parentState._blend),
  _cullFace(parentState._cullFace),
  _culledFace(parentState._culledFace),
  _lineWidth(parentState._lineWidth),
  _polygonOffsetFactor(parentState._polygonOffsetFactor),
  _polygonOffsetUnits(parentState._polygonOffsetUnits),
  _polygonOffsetFill(parentState._polygonOffsetFill),
  _blendDFactor(parentState._blendDFactor),
  _blendSFactor(parentState._blendSFactor),
  _pixelStoreIAlignmentUnpack(parentState._pixelStoreIAlignmentUnpack),
  _clearColorR(parentState._clearColorR),
  _clearColorG(parentState._clearColorG),
  _clearColorB(parentState._clearColorB),
  _clearColorA(parentState._clearColorA)
  {

    for (int i = 0; i < MAX_N_TEXTURES; i++) {
      _boundTextureId[i] = parentState._boundTextureId[i];
    }

  }
  
public:

  static void initializationAvailable() {
    _initializationAvailable = true;
  }
  
  GLGlobalState() :
  _depthTest(false),
  _blend(false),
  _cullFace(true),
  _culledFace(GLCullFace::back()),
  _lineWidth(1),
  _polygonOffsetFactor(0),
  _polygonOffsetUnits(0),
  _polygonOffsetFill(false),
  _blendDFactor(GLBlendFactor::zero()),
  _blendSFactor(GLBlendFactor::one()),
  _pixelStoreIAlignmentUnpack(-1),
  _clearColorR(0.0),
  _clearColorG(0.0),
  _clearColorB(0.0),
  _clearColorA(0.0)
  {

    if (!_initializationAvailable) {
      ILogger::instance()->logError("GLGlobalState creation before it is available.");
    }

    for (int i = 0; i < MAX_N_TEXTURES; i++) {
      _boundTextureId[i] = NULL;
    }

  }

  static GLGlobalState* newDefault() {
    return new GLGlobalState();
  }
  
  GLGlobalState* createCopy() {
    return new GLGlobalState(*this);
  }

  ~GLGlobalState() {
  }
  
  void enableDepthTest() {
      _depthTest = true;
  }
  void disableDepthTest() {
      _depthTest = false;
  }
  bool isEnabledDepthTest() const { return _depthTest; }
  
  void enableBlend() {
      _blend = true;
  }
  void disableBlend() {
      _blend = false;
  }
  bool isEnabledBlend() const { return _blend; }
  
  void enableCullFace(int face) {
    _cullFace   = true;
    _culledFace = face;
  }
  void disableCullFace() {
    _cullFace = false;
  }
  bool isEnabledCullFace() const { return _cullFace; }
  int getCulledFace() const { return _culledFace; }
  
  void setLineWidth(float lineWidth) {
    _lineWidth = lineWidth;
  }
  float lineWidth() const { return _lineWidth; }
  
  void enablePolygonOffsetFill(float factor, float units) {
    _polygonOffsetFill = true;
    _polygonOffsetFactor = factor;
    _polygonOffsetUnits = units;
  }
  void disPolygonOffsetFill() {
    _polygonOffsetFill = false;
  }
  
  bool getPolygonOffsetFill() const { return _polygonOffsetFill;}
  float getPolygonOffsetUnits() const { return _polygonOffsetUnits;}
  float getPolygonOffsetFactor() const { return _polygonOffsetFactor;}
  
  void setBlendFactors(int sFactor, int dFactor) {
    _blendSFactor = sFactor;
    _blendDFactor = dFactor;
  }
  
  void bindTexture(const IGLTextureId* textureId) {
    _boundTextureId[0] = textureId;
  }
  
  const IGLTextureId* getBoundTexture() const{
    return _boundTextureId[0];
  }

  void bindTexture(int target, const IGLTextureId* textureId) {


    if (target > MAX_N_TEXTURES) {
      ILogger::instance()->logError("WRONG TARGET FOR TEXTURE");
      return;
    }

    _boundTextureId[target] = textureId;
  }

  const IGLTextureId* getBoundTexture(int target) const{
    return _boundTextureId[0];
  }

  void setPixelStoreIAlignmentUnpack(int p) {
    _pixelStoreIAlignmentUnpack = p;
  }
  
  void setClearColor(const Color& color) {
    _clearColorR = color._red;
    _clearColorG = color._green;
    _clearColorB = color._blue;
    _clearColorA = color._alpha;
  }
  
  void applyChanges(GL* gl, GLGlobalState& currentState) const;
};

#endif
