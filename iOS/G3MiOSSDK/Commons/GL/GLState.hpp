//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
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
  
  IFloatBuffer* _colors;
  float         _intensity;
  float         _flatColorR;
  float         _flatColorG; 
  float         _flatColorB;
  float         _flatColorA; 
  int           _culledFace;

  
public:
  GLState():
  _depthTest(true),
  _blend(false),
  _textures(false),
  _texture2D(false),
  _vertexColor(false),
  _verticesPosition(false),
  _flatColor(false),
  _cullFace(true),
  _culledFace(GLCullFace::back())
  {}
  
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
  
  void enableVertexColor(IFloatBuffer* colors, float intensity) { 
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
  
  void enableFlatColor(const Color& c, float intensity) { 
    _flatColor = true; 
    _flatColorR = c.getRed();
    _flatColorG = c.getGreen();
    _flatColorB = c.getBlue();
    _flatColorA= c.getAlpha();
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
  
};


#endif
