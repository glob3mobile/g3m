//
//  GLGlobalState.hpp
//  G3M
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//

#ifndef G3M_GLGlobalState
#define G3M_GLGlobalState

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
  const IGLTextureID* _boundTextureID[MAX_N_TEXTURES];
#endif
#ifdef JAVA_CODE
  private final IGLTextureID[] _boundTextureID = new IGLTextureID[DefineConstants.MAX_N_TEXTURES];
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

  GLGlobalState(const GLGlobalState& parentState);

public:

  static void initializationAvailable() {
    _initializationAvailable = true;
  }

  GLGlobalState();

  static GLGlobalState* newDefault() {
    return new GLGlobalState();
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
  void disablePolygonOffsetFill() {
    _polygonOffsetFill = false;
  }

  bool getPolygonOffsetFill()    const { return _polygonOffsetFill;   }
  float getPolygonOffsetUnits()  const { return _polygonOffsetUnits;  }
  float getPolygonOffsetFactor() const { return _polygonOffsetFactor; }

  void setBlendFactors(int sFactor, int dFactor) {
    _blendSFactor = sFactor;
    _blendDFactor = dFactor;
  }

  void bindTexture(const int target,
                   const IGLTextureID* textureID);

  void onTextureDelete(const IGLTextureID* textureID) {
    for (int i = 0; i < MAX_N_TEXTURES; i++) {
      if (_boundTextureID[i] == textureID) {
        _boundTextureID[i] = NULL;
      }
    }
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
