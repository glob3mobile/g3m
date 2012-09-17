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

class GLFeature {
  static int _polygonOffsetFill;
  static int _depthTest;
  static int _blend;
  static int _cullFace;
  
public:
  static int polygonOffsetFill(){ return _polygonOffsetFill;}
  static int depthTest(){ return _depthTest;}
  static int blend(){ return _blend;}
  static int cullFace(){ return _cullFace;}

  static void init(const INativeGL* ngl){
    _polygonOffsetFill = ngl->Feature_PolygonOffsetFill();
    _depthTest = ngl->Feature_DepthTest();
    _blend = ngl->Feature_Blend();
    _cullFace = ngl->Feature_CullFace();
  }
};

class GLType {
  static int _float;
  static int _unsignedByte;
  static int _unsignedInt;
  static int _int;
  
public:
  static int glFloat(){ return _float;}
  static int glUnsignedByte(){ return _unsignedByte;}
  static int glUnsignedInt(){ return _unsignedInt;}
  static int glInt(){ return _int;}
  
  static void init(const INativeGL* ngl){
    _float = ngl->Type_Float();
    _unsignedByte = ngl->Type_UnsignedByte();
    _unsignedInt = ngl->Type_UnsignedInt();
    _int = ngl->Type_Int();
  }
};

class GLPrimitive {
  static int _triangleStrip;
  static int _lines;
  static int _lineLoop;
  static int _points;
  
public:
  static int triangleStrip(){ return _triangleStrip;}
  static int lines(){ return _lines;}
  static int lineLoop(){ return _lineLoop;}
  static int points(){ return _points;}
  
  static void init(const INativeGL* ngl){
    _triangleStrip = ngl->Primitive_TriangleStrip();
    _lines = ngl->Primitive_Lines();
    _lineLoop = ngl->Primitive_LineLoop();
    _points = ngl->Primitive_Points();
  }
};

/*


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
