//
//  GLGlobalState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GLGlobalState_hpp
#define G3MiOSSDK_GLGlobalState_hpp

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

class GLGlobalState {
private:
  bool _depthTest;
  bool _blend;
  bool _cullFace;
  int  _culledFace;
  
#ifdef C_CODE
  const IGLTextureId* _boundTextureId;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _boundTextureId;
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
  
  //Marks of state changed
  bool _depthTestChanged;
  bool _blendChanged;
  bool _cullFaceChanged;
  bool _lineWidthChanged;
  bool _polygonOffsetChanged;
  bool _blendFactorsChanged;
  bool _boundTextureChanged;
  bool _pixelStoreIChanged;
  bool _clearColorChanged;

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
  _boundTextureId(parentState._boundTextureId),
  _pixelStoreIAlignmentUnpack(parentState._pixelStoreIAlignmentUnpack),
  _clearColorR(parentState._clearColorR),
  _clearColorG(parentState._clearColorG),
  _clearColorB(parentState._clearColorB),
  _clearColorA(parentState._clearColorA),
  _depthTestChanged(parentState._depthTestChanged),
  _blendChanged(parentState._blendChanged),
  _cullFaceChanged(parentState._cullFaceChanged),
  _lineWidthChanged(parentState._lineWidthChanged),
  _polygonOffsetChanged(parentState._polygonOffsetChanged),
  _blendFactorsChanged(parentState._blendFactorsChanged),
  _boundTextureChanged(parentState._boundTextureChanged),
  _pixelStoreIChanged(parentState._pixelStoreIChanged),
  _clearColorChanged(parentState._clearColorChanged)
  {
  }
  
public:
  
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
  _boundTextureId(NULL),
  _pixelStoreIAlignmentUnpack(-1),
  _clearColorR(0.0),
  _clearColorG(0.0),
  _clearColorB(0.0),
  _clearColorA(0.0),
  _depthTestChanged(false),
  _blendChanged(false),
  _cullFaceChanged(false),
  _lineWidthChanged(false),
  _polygonOffsetChanged(false),
  _blendFactorsChanged(false),
  _boundTextureChanged(false),
  _pixelStoreIChanged(false),
  _clearColorChanged(false)
  {
  }

  //GLGlobalState* createCombinationWithMorePriorityGLGlobalState(const GLGlobalState* that);
  
  static GLGlobalState* newDefault() {
    return new GLGlobalState();
  }
  
  GLGlobalState* createCopy() {
    return new GLGlobalState(*this);
  }
  
  
  
  //  explicit GLGlobalState(const GLGlobalState& parentState) :
  //  _depthTest(parentState._depthTest),
  //  _blend(parentState._blend),
  //  _cullFace(parentState._cullFace),
  //  _culledFace(parentState._culledFace),
  //  _lineWidth(parentState._lineWidth),
  //  _polygonOffsetFactor(parentState._polygonOffsetFactor),
  //  _polygonOffsetUnits(parentState._polygonOffsetUnits),
  //  _polygonOffsetFill(parentState._polygonOffsetFill),
  //  _blendDFactor(parentState._blendDFactor),
  //  _blendSFactor(parentState._blendSFactor),
  //  _boundTextureId(parentState._boundTextureId),
  //  _pixelStoreIAlignmentUnpack(parentState._pixelStoreIAlignmentUnpack),
  //  _clearColorR(parentState._clearColorR),
  //  _clearColorG(parentState._clearColorG),
  //  _clearColorB(parentState._clearColorB),
  //  _clearColorA(parentState._clearColorA)
  //  {
  //  }
  
  ~GLGlobalState() {
  }
  
  void enableDepthTest() {
      _depthTest = true;
    _depthTestChanged = true;
  }
  void disableDepthTest() {
      _depthTest = false;
    _depthTestChanged = true;
  }
  bool isEnabledDepthTest() const { return _depthTest; }
  
  void enableBlend() {
      _blend = true;
    _blendChanged = true;
  }
  void disableBlend() {
      _blend = false;
    _blendChanged = true;
  }
  bool isEnabledBlend() const { return _blend; }
  
  void enableCullFace(int face) {
    _cullFace   = true;
    _culledFace = face;
    _cullFaceChanged = true;
  }
  void disableCullFace() {
    _cullFace = false;
    _cullFaceChanged = true;
  }
  bool isEnabledCullFace() const { return _cullFace; }
  int getCulledFace() const { return _culledFace; }
  
  void setLineWidth(float lineWidth) {
    _lineWidth = lineWidth;
    _lineWidthChanged = true;
  }
  float lineWidth() const { return _lineWidth; }
  
  void enablePolygonOffsetFill(float factor, float units) {
    _polygonOffsetFill = true;
    _polygonOffsetFactor = factor;
    _polygonOffsetUnits = units;
    
    _polygonOffsetChanged = true;
  }
  void disPolygonOffsetFill() {
    _polygonOffsetFill = false;
    
    _polygonOffsetChanged = true;
  }
  
  bool getPolygonOffsetFill() const { return _polygonOffsetFill;}
  float getPolygonOffsetUnits() const { return _polygonOffsetUnits;}
  float getPolygonOffsetFactor() const { return _polygonOffsetFactor;}
  
  void setBlendFactors(int sFactor, int dFactor) {
    _blendSFactor = sFactor;
    _blendDFactor = dFactor;
    
    _blendFactorsChanged = true;
  }
  
  void bindTexture(const IGLTextureId* textureId) {
    _boundTextureId = textureId;
    
    _boundTextureChanged = true;
  }
  
  const IGLTextureId* getBoundTexture() const{
    return _boundTextureId;
  }
  
  void setPixelStoreIAlignmentUnpack(int p) {
    _pixelStoreIAlignmentUnpack = p;
    
    _pixelStoreIChanged = true;
  }
  
  void setClearColor(const Color& color) {
    _clearColorR = color.getRed();
    _clearColorG = color.getGreen();
    _clearColorB = color.getBlue();
    _clearColorA = color.getAlpha();
    
    _clearColorChanged = true;
  }
  
  void applyChanges(GL* gl, GLGlobalState& currentState) const;
};

#endif
