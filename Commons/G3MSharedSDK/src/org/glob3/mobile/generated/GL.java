package org.glob3.mobile.generated; 
//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//


//
//  GL.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 14/06/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//





//class IGLProgramId;
//class IGLUniformID;


//class GPUProgramManager;
//class GPUProgramState;
//class GPUProgram;
//class GLState;

public class GL
{
  private final INativeGL _nativeGL;

  private final java.util.LinkedList<IGLTextureId> _texturesIdBag = new java.util.LinkedList<IGLTextureId>();
  private int _texturesIdAllocationCounter;

//  GLGlobalState *_currentState;
//  GPUProgram* _currentGPUProgram;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void loadModelView();

  private IGLTextureId getGLTextureId()
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::getGLTextureId()");
    }
  
    if (_texturesIdBag.size() == 0)
    {
      //const int bugdetSize = 256;
      final int bugdetSize = 1024;
      //const int bugdetSize = 10240;
  
      final java.util.ArrayList<IGLTextureId> ids = _nativeGL.genTextures(bugdetSize);
      final int idsCount = ids.size();
      for (int i = 0; i < idsCount; i++)
      {
        // ILogger::instance()->logInfo("  = Created textureId=%s", ids[i]->description().c_str());
        _texturesIdBag.addFirst(ids.get(i));
      }
  
      _texturesIdAllocationCounter += idsCount;
  
      ILogger.instance().logInfo("= Created %d texturesIds (accumulated %d).", idsCount, _texturesIdAllocationCounter);
    }
  
    //  _texturesIdGetCounter++;
  
    if (_texturesIdBag.size() == 0)
    {
      ILogger.instance().logError("TextureIds bag exhausted");
      return null;
    }
  
    final IGLTextureId result = _texturesIdBag.getLast();
    _texturesIdBag.removeLast();
  
    //  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
    //         result.getGLTextureId(),
    //         _texturesIdBag.size(),
    //         _texturesIdGetCounter,
    //         _texturesIdTakeCounter,
    //         _texturesIdGetCounter - _texturesIdTakeCounter);
  
    return result;
  }

  private final boolean _verbose;

//  void setGLGlobalState(const GLGlobalState& state);
//  void setProgramState(GPUProgramManager& progManager, const GPUProgramState& progState);
//  
//  void applyGLGlobalStateAndGPUProgramState(const GLGlobalState& state, GPUProgramManager& progManager, const GPUProgramState& progState);



  public GL(INativeGL nativeGL, boolean verbose)
//  _program(NULL),
//  _currentState(NULL),
//  _currentGPUProgram(NULL)
  {
     _nativeGL = nativeGL;
     _verbose = verbose;
     _texturesIdAllocationCounter = 0;
    //Init Constants
    GLCullFace.init(_nativeGL);
    GLBufferType.init(_nativeGL);
    GLFeature.init(_nativeGL);
    GLType.init(_nativeGL);
    GLPrimitive.init(_nativeGL);
    GLBlendFactor.init(_nativeGL);
    GLTextureType.init(_nativeGL);
    GLTextureParameter.init(_nativeGL);
    GLTextureParameterValue.init(_nativeGL);
    GLAlignment.init(_nativeGL);
    GLFormat.init(_nativeGL);
    GLVariable.init(_nativeGL);
    GLError.init(_nativeGL);

//    _currentState = GLGlobalState::newDefault(); //Init after constants
  }

  public final void clearScreen(GLGlobalState state)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::clearScreen()");
    }
  
    GLState glState = new GLState((GLGlobalState) state, null);
    glState.applyGlobalStateOnGPU(this);
  
    //setGLGlobalState(state);
    _nativeGL.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void drawElements(int mode, IShortBuffer indices, GLGlobalState state, GPUProgramManager progManager, GPUProgramState gpuState);

  public final void drawElements(int mode, IShortBuffer indices, GLState state, GPUProgramManager progManager)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::drawElements(%d, %s)", mode, indices.description());
    }
  
    state.applyOnGPU(this, progManager);
  
    _nativeGL.drawElements(mode, indices.size(), indices);
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void drawArrays(int mode, int first, int count, GLGlobalState state, GPUProgramManager progManager, GPUProgramState gpuState);

  public final void drawArrays(int mode, int first, int count, GLState state, GPUProgramManager progManager)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::drawArrays(%d, %d, %d)", mode, first, count);
    }
  
    state.applyOnGPU(this, progManager);
    _nativeGL.drawArrays(mode, first, count);
  }

  public final int getError()
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::getError()");
    }
  
    return _nativeGL.getError();
  }

  public final IGLTextureId uploadTexture(IImage image, int format, boolean generateMipmap)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::uploadTexture()");
    }
  
    final IGLTextureId texId = getGLTextureId();
    if (texId != null)
    {
      int texture2D = GLTextureType.texture2D();
  
  //    GLGlobalState state(*GLState::getCurrentGLGlobalState());
      GLGlobalState state = GLState.createCopyOfCurrentGLGlobalState();
      state.setPixelStoreIAlignmentUnpack(1);
      state.bindTexture(texId);
  
      GLState glState = new GLState((GLGlobalState) state, null);
      glState.applyGlobalStateOnGPU(this);
  
  //    setGLGlobalState(state);
  
      int linear = GLTextureParameterValue.linear();
      int clampToEdge = GLTextureParameterValue.clampToEdge();
      _nativeGL.texParameteri(texture2D, GLTextureParameter.minFilter(), linear);
      _nativeGL.texParameteri(texture2D, GLTextureParameter.magFilter(),linear);
      _nativeGL.texParameteri(texture2D, GLTextureParameter.wrapS(),clampToEdge);
      _nativeGL.texParameteri(texture2D, GLTextureParameter.wrapT(),clampToEdge);
      _nativeGL.texImage2D(image, format);
  
      if (generateMipmap)
      {
        _nativeGL.generateMipmap(texture2D);
      }
  
    }
    else
    {
      ILogger.instance().logError("can't get a valid texture id\n");
      return null;
    }
  
    return texId;
  }

  public final void deleteTexture(IGLTextureId textureId)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::deleteTexture()");
    }
  
    if (textureId != null)
    {
      if (_nativeGL.deleteTexture(textureId))
      {
        _texturesIdBag.addLast(textureId);
      }
      else
      {
        if (textureId != null)
           textureId.dispose();
      }
  
      GLState.textureHasBeenDeleted(textureId);
  
  //    if (GLState::getCurrentGLGlobalState()->getBoundTexture() == textureId){
  //      GLState::getCurrentGLGlobalState()->bindTexture(NULL);
  //    }
  
      //ILogger::instance()->logInfo("  = delete textureId=%s", texture->description().c_str());
    }
  }

  public final void getViewport(int[] v)
  {
    if (_verbose)
       ILogger.instance().logInfo("GL::getViewport()");

    _nativeGL.getIntegerv(GLVariable.viewport(), v);
  }

  public void dispose()
  {
    _nativeGL.dispose();
  }

  public final int createProgram()
  {
    return _nativeGL.createProgram();
  }

  public final void attachShader(int program, int shader)
  {
    _nativeGL.attachShader(program, shader);
  }

  public final int createShader(ShaderType type)
  {
    return _nativeGL.createShader(type);
  }

  public final boolean compileShader(int shader, String source)
  {
    return _nativeGL.compileShader(shader, source);
  }

  public final boolean deleteShader(int shader)
  {
    return _nativeGL.deleteShader(shader);
  }

  public final void printShaderInfoLog(int shader)
  {
    _nativeGL.printShaderInfoLog(shader);
  }

  public final boolean linkProgram(int program)
  {
    return _nativeGL.linkProgram(program);
  }

  public final void printProgramInfoLog(int program)
  {
    _nativeGL.linkProgram(program);
  }

  public final boolean deleteProgram(int program)
  {
    return _nativeGL.deleteProgram(program);
  }

  public final INativeGL getNative()
  {
    return _nativeGL;
  }

  public final void uniform2f(IGLUniformID loc, float x, float y)
  {
     _nativeGL.uniform2f(loc, x, y);
  }

  public final void uniform1f(IGLUniformID loc, float x)
  {
     _nativeGL.uniform1f(loc, x);
  }
  public final void uniform1i(IGLUniformID loc, int v)
  {
     _nativeGL.uniform1i(loc, v);
  }

  public final void uniformMatrix4fv(IGLUniformID location, boolean transpose, MutableMatrix44D matrix)
  {
     _nativeGL.uniformMatrix4fv(location, transpose, matrix);
  }

  public final void uniform4f(IGLUniformID location, float v0, float v1, float v2, float v3)
  {
     _nativeGL.uniform4f(location, v0, v1, v2, v3);
  }

  public final void vertexAttribPointer(int index, int size, boolean normalized, int stride, IFloatBuffer buffer)
  {
    _nativeGL.vertexAttribPointer(index, size, normalized, stride, buffer);
  }

  public final void bindAttribLocation(GPUProgram program, int loc, String name)
  {
    _nativeGL.bindAttribLocation(program, loc, name);
  }

  public final int getProgramiv(GPUProgram program, int pname)
  {
    return _nativeGL.getProgramiv(program, pname);
  }

  public final GPUUniform getActiveUniform(GPUProgram program, int i)
  {
    return _nativeGL.getActiveUniform(program, i);
  }

  public final GPUAttribute getActiveAttribute(GPUProgram program, int i)
  {
    return _nativeGL.getActiveAttribute(program, i);
  }

//  GLGlobalState* getCurrentState() const{ return _currentState;}


  //void GL::setGLGlobalState(const GLGlobalState& state) {
  //  state.applyChanges(this, *_currentState);
  //}
  //
  //void GL::setProgramState(GPUProgramManager& progManager, const GPUProgramState& progState) {
  //  GPUProgram* prog = NULL;
  //  if (!progState.isLinkedToProgram()) {
  //    prog = progManager.getProgram(progState);
  //    progState.linkToProgram(*prog);
  //  } else{
  //    prog = progState.getLinkedProgram();
  //  }
  //  if (prog != NULL){
  //    if (prog != _currentGPUProgram){
  //      if (_currentGPUProgram != NULL){
  //        _currentGPUProgram->onUnused();
  //      }
  //      _currentGPUProgram = prog;
  //      useProgram(prog);
  //    }
  //    
  //    progState.applyChanges(this);
  //  } else{
  //    ILogger::instance()->logError("No available GPUProgram for this state.");
  //  }
  //}
  
  public final void useProgram(GPUProgram program)
  {
    _nativeGL.useProgram(program);
    program.onUsed();
  }

  public final void enableVertexAttribArray(int location)
  {
    _nativeGL.enableVertexAttribArray(location);
  }

  public final void disableVertexAttribArray(int location)
  {
    _nativeGL.disableVertexAttribArray(location);
  }


}