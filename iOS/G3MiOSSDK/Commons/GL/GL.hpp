//
//  GL.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 14/06/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GL_hpp
#define G3MiOSSDK_GL_hpp

#include "INativeGL.hpp"

#include "IImage.hpp"
#include "MutableMatrix44D.hpp"
#include "Color.hpp"
#include "MutableVector2D.hpp"
#include "IFloatBuffer.hpp"
#include "GLConstants.hpp"
#include "GLState.hpp"

#include <list>

class IGLProgramId;
class IGLUniformID;

#include "IGLTextureId.hpp"

class GL {
private:
  INativeGL* const _nativeGL;

  std::list<const IGLTextureId*> _texturesIdBag;
  long                           _texturesIdAllocationCounter;

  GLState *_currentState;

  ShaderProgram* _program;

  inline void loadModelView();

  const IGLTextureId* getGLTextureId();

  //Get Locations warning of errors
  bool _errorGettingLocationOcurred;
  int checkedGetAttribLocation(ShaderProgram* program,
                               const std::string& name);
  IGLUniformID* checkedGetUniformLocation(ShaderProgram* program,
                                          const std::string& name);

  IFloatBuffer* _billboardTexCoord;

  void setState(const GLState& state);

  const bool _verbose;

public:


  GL(INativeGL* const nativeGL,
     bool verbose) :
  _nativeGL(nativeGL),
  _verbose(verbose),
  _texturesIdAllocationCounter(0),
  _billboardTexCoord(NULL),
  _program(NULL),
  _currentState(NULL)
  {
    //Init Constants
    GLCullFace::init(_nativeGL);
    GLBufferType::init(_nativeGL);
    GLFeature::init(_nativeGL);
    GLType::init(_nativeGL);
    GLPrimitive::init(_nativeGL);
    GLBlendFactor::init(_nativeGL);
    GLTextureType::init(_nativeGL);
    GLTextureParameter::init(_nativeGL);
    GLTextureParameterValue::init(_nativeGL);
    GLAlignment::init(_nativeGL);
    GLFormat::init(_nativeGL);
    GLVariable::init(_nativeGL);
    GLError::init(_nativeGL);
    
    _currentState = GLState::newDefault(); //Init after constants
  }
  
  IFloatBuffer* getBillboardTexCoord();

  void clearScreen(const GLState& state);

  void drawElements(int mode,
                    IShortBuffer* indices, const GLState& state);

  void drawArrays(int mode,
                  int first,
                  int count, const GLState& state);

  bool useProgram(ShaderProgram* program);

  int getError();

  const IGLTextureId* uploadTexture(const IImage* image,
                                    int format,
                                    bool generateMipmap);

  void deleteTexture(const IGLTextureId* textureId);

  void getViewport(int v[]) {
    if (_verbose) ILogger::instance()->logInfo("GL::getViewport()");

    _nativeGL->getIntegerv(GLVariable::viewport(), v);
  }

  ~GL() {
#ifdef C_CODE
    delete _nativeGL;
#endif
#ifdef JAVA_CODE
    _nativeGL.dispose();
#endif
  }

  int createProgram() const {
    return _nativeGL->createProgram();
  }

  void attachShader(int program, int shader) const {
    _nativeGL->attachShader(program, shader);
  }

  int createShader(ShaderType type) const {
    return _nativeGL->createShader(type);
  }

  bool compileShader(int shader, const std::string& source) const {
    return _nativeGL->compileShader(shader, source);
  }

  void deleteShader(int shader) const {
    _nativeGL->deleteShader(shader);
  }

  void printShaderInfoLog(int shader) const {
    _nativeGL->printShaderInfoLog(shader);
  }

  bool linkProgram(int program) const {
    return _nativeGL->linkProgram(program);
  }

  void printProgramInfoLog(int program) const {
    _nativeGL->linkProgram(program);
  }

  void deleteProgram(int program) const  {
    _nativeGL->deleteProgram(program);
  }
};

#endif
