//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GLState_hpp
#define G3MiOSSDK_GLState_hpp


class GLState {
  
public:
  GLState():
  _depthTest(true),
  _blend(false),
  _textures(false),
  _texture2D(false)
  {}
  
  void enableDepthTest() { _depthTest = true; }
  void disableDepthTest() { _depthTest = false; }
  bool isEnabledDepthTest() { return _depthTest; }
  
  void enableBlend() { _blend = true; }
  void disableBlend() { _blend = false; }
  bool isEnabledBlend() { return _blend; }
  
  void enableTextures() { _textures = true; }
  void disableTextures() { _textures = false; }
  bool isEnabledTextures() { return _textures; }
  
  void enableTexture2D() { _texture2D = true; }
  void disableTexture2D() { _texture2D = false; }
  bool isEnabledTexture2D() { return _texture2D; }
  
  
private:
  bool _depthTest;
  bool _blend;
  bool _textures;
  bool _texture2D;
};


#endif
