package org.glob3.mobile.generated;
//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 02/05/11.
//

///#include <list>

//
//  GL.hpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 14/06/11.
//





//class IGLUniformID;
//class GPUProgramManager;
//class GPUProgramState;
//class GLState;


public class GL
{
  private final INativeGL _nativeGL;


  //CURRENT GL STATUS
  private GLGlobalState _currentGLGlobalState;
  private GPUProgram _currentGPUProgram;


  private final java.util.LinkedList<IGLTextureID> _texturesIDBag = new java.util.LinkedList<IGLTextureID>();
  private int _texturesIDAllocationCounter;

  private IGLTextureID getGLTextureID()
  {
    if (_texturesIDBag.size() == 0)
    {
      final int bugdetSize = 1024;
  
      final java.util.ArrayList<IGLTextureID> ids = _nativeGL.genTextures(bugdetSize);
      final int idsCount = ids.size();
      for (int i = 0; i < idsCount; i++)
      {
        _texturesIDBag.addFirst(ids.get(i));
      }
  
      _texturesIDAllocationCounter += idsCount;
  
      ILogger.instance().logInfo("= Created %d texturesIDs (accumulated %d).", idsCount, _texturesIDAllocationCounter);
    }
  
    if (_texturesIDBag.size() == 0)
    {
      ILogger.instance().logError("TextureIDs bag exhausted");
      return null;
    }
  
    final IGLTextureID result = _texturesIDBag.getLast();
    _texturesIDBag.removeLast();
  
    return result;
  }

  private GLGlobalState _clearScreenState; //State used to clear screen with certain color

  private static boolean isPowerOfTwo(int x)
  {
    return ((x >= 0) && ((x == 1) || (x == 2) || (x == 4) || (x == 8) || (x == 16) || (x == 32) || (x == 64) || (x == 128) || (x == 256) || (x == 512) || (x == 1024) || (x == 2048) || (x == 4096) || (x == 8192) || (x == 16384) || (x == 32768) || (x == 65536) || (x == 131072) || (x == 262144) || (x == 524288) || (x == 1048576) || (x == 2097152) || (x == 4194304) || (x == 8388608) || (x == 16777216) || (x == 33554432) || (x == 67108864) || (x == 134217728) || (x == 268435456) || (x == 536870912) || (x == 1073741824)));
  }




  public GL(INativeGL nativeGL)
  {
     _nativeGL = nativeGL;
     _currentGPUProgram = null;
     _texturesIDAllocationCounter = 0;
     _clearScreenState = null;
    //Init Constants
    GLCullFace.init(_nativeGL);
    GLBufferType.init(_nativeGL);
    GLStage.init(_nativeGL);
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

    GLGlobalState.initializationAvailable();

    _currentGLGlobalState = new GLGlobalState();
    _clearScreenState = new GLGlobalState();
  }

  public final void clearScreen(Color color)
  {
    _clearScreenState.setClearColor(color);
    _clearScreenState.applyChanges(this, _currentGLGlobalState);
  
    _nativeGL.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
  }

  public final void drawElements(int mode, IShortBuffer indices, GLState state, GPUProgramManager progManager)
  {
    drawElements(mode, indices, (int) indices.size(), state, progManager);
  }

  public final void drawElements(int mode, IShortBuffer indices, int count, GLState state, GPUProgramManager progManager)
  {
    state.applyOnGPU(this, progManager);
  
    _nativeGL.drawElements(mode, count, indices);
  }

  public final void drawArrays(int mode, int first, int count, GLState state, GPUProgramManager progManager)
  {
    state.applyOnGPU(this, progManager);
  
    _nativeGL.drawArrays(mode, first, count);
  }

  public final int getError()
  {
    return _nativeGL.getError();
  }

  public final IGLTextureID uploadTexture(IImage image, int format, boolean generateMipmap, int wrapS, int wrapT)
  {
  
    final IGLTextureID texID = getGLTextureID();
    if (texID != null)
    {
      GLGlobalState newState = new GLGlobalState();
  
      newState.setPixelStoreIAlignmentUnpack(1);
      newState.bindTexture(0, texID);
  
      newState.applyChanges(this, _currentGLGlobalState);
  
      final int texture2D = GLTextureType.texture2D();
      final int linear = GLTextureParameterValue.linear();
  
      if (generateMipmap)
      {
        _nativeGL.texParameteri(texture2D, GLTextureParameter.minFilter(), GLTextureParameterValue.linearMipmapNearest());
      }
      else
      {
        _nativeGL.texParameteri(texture2D, GLTextureParameter.minFilter(), linear);
      }
  
      _nativeGL.texParameteri(texture2D, GLTextureParameter.magFilter(), linear);
  
      _nativeGL.texParameteri(texture2D, GLTextureParameter.wrapS(), wrapS);
      _nativeGL.texParameteri(texture2D, GLTextureParameter.wrapT(), wrapT);
  
      _nativeGL.texImage2D(image, format);
  
      if (generateMipmap)
      {
        if (isPowerOfTwo(image.getWidth()) && isPowerOfTwo(image.getHeight()))
        {
          _nativeGL.generateMipmap(texture2D);
        }
        else
        {
          ILogger.instance().logError("Can't generate mipmap. Texture dimensions are not power of two.");
        }
      }
    }
    else
    {
      ILogger.instance().logError("can't get a valid texture id\n");
      return null;
    }
  
    return texID;
  }

  public final void deleteTexture(IGLTextureID textureID)
  {
    if (textureID != null)
    {
      _currentGLGlobalState.onTextureDelete(textureID);
  
      if (_nativeGL.deleteTexture(textureID))
      {
        _texturesIDBag.addLast(textureID);
      }
      else
      {
        if (textureID != null)
           textureID.dispose();
      }
    }
  }

  public void dispose()
  {
    _nativeGL.dispose();
    _clearScreenState.dispose();
    _currentGLGlobalState.dispose();
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

  public final boolean deleteProgram(GPUProgram program)
  {
    if (program == null)
    {
      return false;
    }

    if (_currentGPUProgram == program) //In case of deleting active program
    {
      _currentGPUProgram.removeReference();
      _currentGPUProgram = null;
    }

    return _nativeGL.deleteProgram(program.getProgramID());
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

  public final void uniformMatrix4fv(IGLUniformID location, boolean transpose, Matrix44D matrix)
  {
    _nativeGL.uniformMatrix4fv(location, transpose, matrix);
  }

  public final void uniform4f(IGLUniformID location, float v0, float v1, float v2, float v3)
  {
    _nativeGL.uniform4f(location, v0, v1, v2, v3);
  }

  public final void uniform3f(IGLUniformID location, float v0, float v1, float v2)
  {
    _nativeGL.uniform3f(location, v0, v1, v2);
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

  public final void useProgram(GPUProgram program)
  {
    if (program != null)
    {
      if (_currentGPUProgram != program)
      {
  
        if (_currentGPUProgram != null)
        {
          _currentGPUProgram.onUnused(this);
          _currentGPUProgram.removeReference();
        }
  
        _nativeGL.useProgram(program);
        program.onUsed();
        _currentGPUProgram = program;
        _currentGPUProgram.addReference();
      }
    }
  
  }

  public final void enableVertexAttribArray(int location)
  {
    _nativeGL.enableVertexAttribArray(location);
  }

  public final void disableVertexAttribArray(int location)
  {
    _nativeGL.disableVertexAttribArray(location);
  }

  public final GLGlobalState getCurrentGLGlobalState()
  {
    return _currentGLGlobalState;
  }

  public final void viewport(int x, int y, int width, int height)
  {
    _nativeGL.viewport(x, y, width, height);
  }

  public final void clearDepthBuffer()
  {
    _nativeGL.clear(GLBufferType.depthBuffer());
  }

}