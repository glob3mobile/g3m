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
  _blend(false)
  {}
  
  void enableDepthTest() { _depthTest = true; }
  void disableDepthTest() { _depthTest = false; }
  bool isEnabledDepthTest() { return _depthTest; }
  
  void enableBlend() { _blend = true; }
  void disableBlend() { _blend = false; }
  bool isEnabledBlend() { return _blend; }
  
  
private:
  bool _depthTest;
  bool _blend;
};


#endif
