//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GLState_hpp
#define G3MiOSSDK_GLState_hpp

class IFloatBuffer;

#include "Color.hpp"
#include "GLConstants.hpp"

class GLState {
private:
  bool _depthTest;
  bool _blend;
  bool _textures;
  bool _texture2D;
  bool _vertexColor;
  bool _verticesPosition;
  bool _flatColor;
  bool _cullFace;
  int  _culledFace;

  IFloatBuffer* _colors;
  float         _intensity;
  float         _flatColorR;
  float         _flatColorG;
  float         _flatColorB;
  float         _flatColorA;

  float _lineWidth;
  float _pointSize;


  GLState() :
  _depthTest(true),
  _blend(false),
  _textures(false),
  _texture2D(false),
  _vertexColor(false),
  _verticesPosition(false),
  _flatColor(false),
  _cullFace(true),
  _culledFace(GLCullFace::back()),
  _colors(NULL),
  _intensity(0),
  _flatColorR(0),
  _flatColorG(0),
  _flatColorB(0),
  _flatColorA(0),
  _lineWidth(1),
  _pointSize(1)
  {
  }



public:
  static GLState* newDefault() {
    return new GLState();
  }

  explicit GLState(const GLState& parentState) :
  _depthTest(parentState._depthTest),
  _blend(parentState._blend),
  _textures(parentState._textures),
  _texture2D(parentState._texture2D),
  _vertexColor(parentState._vertexColor),
  _verticesPosition(parentState._verticesPosition),
  _flatColor(parentState._flatColor),
  _cullFace(parentState._cullFace),
  _culledFace(parentState._culledFace),
  _colors(parentState._colors),
  _intensity(parentState._intensity),
  _flatColorR(parentState._flatColorR),
  _flatColorG(parentState._flatColorG),
  _flatColorB(parentState._flatColorB),
  _flatColorA(parentState._flatColorA),
  _lineWidth(parentState._lineWidth),
  _pointSize(parentState._pointSize)
  {
  }

  ~GLState() {}

  void enableDepthTest() { _depthTest = true; }
  void disableDepthTest() { _depthTest = false; }
  bool isEnabledDepthTest() const { return _depthTest; }

  void enableBlend() { _blend = true; }
  void disableBlend() { _blend = false; }
  bool isEnabledBlend() const { return _blend; }

  void enableTextures() { _textures = true; }
  void disableTextures() { _textures = false; }
  bool isEnabledTextures() const { return _textures; }

  void enableTexture2D() { _texture2D = true; }
  void disableTexture2D() { _texture2D = false; }
  bool isEnabledTexture2D() const { return _texture2D; }

  void enableVertexColor(IFloatBuffer* colors,
                         float intensity) {
    _vertexColor  = true;
    _colors       = colors;
    _intensity    = intensity;
  }
  void disableVertexColor() { _vertexColor = false; }
  bool isEnabledVertexColor() const { return _vertexColor; }
  IFloatBuffer* getColors() const { return _colors; }
  float getIntensity() const { return _intensity; }

  void enableVerticesPosition() { _verticesPosition = true; }
  void disableVerticesPosition() { _verticesPosition = false; }
  bool isEnabledVerticesPosition() const { return _verticesPosition; }

  void enableFlatColor(const Color& color,
                       float intensity) {
    _flatColor = true;
    _flatColorR = color.getRed();
    _flatColorG = color.getGreen();
    _flatColorB = color.getBlue();
    _flatColorA= color.getAlpha();
    _intensity = intensity;
  }
  void disableFlatColor() { _flatColor = false; }
  bool isEnabledFlatColor() const { return _flatColor; }
  Color getFlatColor() const {
    return Color::fromRGBA(_flatColorR, _flatColorG, _flatColorB, _flatColorA);
  }

  void enableCullFace(int face) {
    _cullFace   = true;
    _culledFace = face;
  }
  void disableCullFace() { _cullFace = false; }
  bool isEnabledCullFace() const { return _cullFace; }
  int getCulledFace() const { return _culledFace; }

  void setLineWidth(float lineWidth) { _lineWidth = lineWidth; }
  float lineWidth() const { return _lineWidth; }

  void setPointSize(float pointSize) { _pointSize = pointSize; }
  float pointSize() const { return _pointSize; }
  
};

#endif
