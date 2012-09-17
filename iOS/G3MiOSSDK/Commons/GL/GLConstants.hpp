//
//  GLConstants.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_GLConstants_hpp
#define G3MiOSSDK_GLConstants_hpp

#include "INativeGL.hpp"

class GLCullFace {
  static int _front;
  static int _back;
  static int _frontAndBack;
  
public:
  static int front(){ return _front;}
  static int back(){ return _back;}
  static int frontAndBack(){ return _frontAndBack;}
  
  static void init(const INativeGL* ngl){
    _front = ngl->CullFace_Front();
    _back = ngl->CullFace_Back();
    _frontAndBack = ngl->CullFace_FrontAndBack();
  }
};

class GLBufferType {
  static int _colorBuffer;
  static int _depthBuffer;
  
public:
  static int colorBuffer(){ return _colorBuffer;}
  static int depthBuffer(){ return _depthBuffer;}
  
  static void init(const INativeGL* ngl){
    _colorBuffer = ngl->BufferType_ColorBuffer();
    _depthBuffer = ngl->BufferType_DepthBuffer();
  }
};

/*

enum GLFeature {
  PolygonOffsetFill,
  DepthTest,
  Blend,
  CullFacing
};

enum GLType {
  Float,
  UnsignedByte,
  UnsignedInt,
  Int
};

enum GLPrimitive {
  TriangleStrip,
  Lines,
  LineLoop,
  Points
};

enum GLError {
  NoError,
  InvalidEnum,
  InvalidValue,
  InvalidOperation,
  OutOfMemory,
  UnknownError
};

enum GLBlendFactor {
  SrcAlpha,
  OneMinusSrcAlpha
};

enum GLTextureType {
  Texture2D
};

enum GLTextureParameter {
  MinFilter,
  MagFilter,
  WrapS,
  WrapT
};

enum GLTextureParameterValue {
  Linear,
  ClampToEdge
};

enum GLAlignment {
  Unpack,
  Pack
};

enum GLFormat {
  RGBA
};

enum GLVariable {
  Viewport
};

*/

#endif
