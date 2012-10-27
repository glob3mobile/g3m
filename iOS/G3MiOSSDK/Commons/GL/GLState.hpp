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
  _depthTest(true) 
  {}
  
  void enableDepthTest() { _depthTest = true; }
  void disableDepthTest() { _depthTest = false; }
  bool isEnabledDepthTest() { return _depthTest; }
  
  
private:
  bool _depthTest;
};


#endif
