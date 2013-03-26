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


public class GL
{
  private final INativeGL _nativeGL;

  private final java.util.LinkedList<IGLTextureId> _texturesIdBag = new java.util.LinkedList<IGLTextureId>();
  private int _texturesIdAllocationCounter;

  private GLState _currentState;

  private ShaderProgram _program;

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

  //Get Locations warning of errors
  private boolean _errorGettingLocationOcurred;
  private int checkedGetAttribLocation(ShaderProgram program, String name)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::checkedGetAttribLocation()");
    }
    int l = _nativeGL.getAttribLocation(program, name);
    if (l == -1)
    {
      ILogger.instance().logError("Error fetching Attribute, Program=%s, Variable=\"%s\"", program.description(), name);
      _errorGettingLocationOcurred = true;
    }
    return l;
  }
  private IGLUniformID checkedGetUniformLocation(ShaderProgram program, String name)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::checkedGetUniformLocation()");
    }
    IGLUniformID uID = _nativeGL.getUniformLocation(program, name);
    if (!uID.isValid())
    {
      ILogger.instance().logError("Error fetching Uniform, Program=%s, Variable=\"%s\"", program.description(), name);
      _errorGettingLocationOcurred = true;
    }
    return uID;
  }

  private IFloatBuffer _billboardTexCoord;

  private void setState(GLState state)
  {
    //Changes current State and calls OpenGL API
    state.applyChanges(_nativeGL, _currentState, GlobalMembersGL.Attributes, GlobalMembersGL.Uniforms);
  }

  private final boolean _verbose;



  public GL(INativeGL nativeGL, boolean verbose)
  {
     _nativeGL = nativeGL;
     _verbose = verbose;
     _texturesIdAllocationCounter = 0;
     _billboardTexCoord = null;
     _program = null;
     _currentState = null;
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

    _currentState = GLState.newDefault(); //Init after constants
  }

  public final IFloatBuffer getBillboardTexCoord()
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::getBillboardTexCoord()");
    }
  
    if (_billboardTexCoord == null)
    {
      FloatBufferBuilderFromCartesian2D texCoor = new FloatBufferBuilderFromCartesian2D();
      texCoor.add(1,1);
      texCoor.add(1,0);
      texCoor.add(0,1);
      texCoor.add(0,0);
      _billboardTexCoord = texCoor.create();
    }
  
    return _billboardTexCoord;
  }

  public final void clearScreen(GLState state)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::clearScreen()");
    }
    setState(state);
    _nativeGL.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
  }

  public final void drawElements(int mode, IShortBuffer indices, GLState state)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::drawElements(%d, %s)", mode, indices.description());
    }
  
    setState(state);
  
    _nativeGL.drawElements(mode, indices.size(), indices);
  }

  public final void drawArrays(int mode, int first, int count, GLState state)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::drawArrays(%d, %d, %d)", mode, first, count);
    }
  
    setState(state);
    _nativeGL.drawArrays(mode, first, count);
  }

  public final boolean useProgram(ShaderProgram program)
  {
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::useProgram()");
    }
  
    if (_program == program)
    {
      return true;
    }
    _program = program;
  
    // set shaders
    _nativeGL.useProgram(program);
  
    //Methods checkedGetAttribLocation and checkedGetUniformLocation
    //will turn _errorGettingLocationOcurred to true is that happens
    _errorGettingLocationOcurred = false;
  
    // Extract the handles to attributes
    GlobalMembersGL.Attributes.Position = checkedGetAttribLocation(program, "Position");
    GlobalMembersGL.Attributes.TextureCoord = checkedGetAttribLocation(program, "TextureCoord");
    GlobalMembersGL.Attributes.Color = checkedGetAttribLocation(program, "Color");
  
    GlobalMembersGL.Uniforms.deleteUniformsIDs(); //DELETING
  
    // Extract the handles to uniforms
    GlobalMembersGL.Uniforms.Projection = checkedGetUniformLocation(program, "Projection");
    GlobalMembersGL.Uniforms.Modelview = checkedGetUniformLocation(program, "Modelview");
    GlobalMembersGL.Uniforms.Sampler = checkedGetUniformLocation(program, "Sampler");
    GlobalMembersGL.Uniforms.EnableTexture = checkedGetUniformLocation(program, "EnableTexture");
    GlobalMembersGL.Uniforms.FlatColor = checkedGetUniformLocation(program, "FlatColor");
    GlobalMembersGL.Uniforms.TranslationTexCoord = checkedGetUniformLocation(program, "TranslationTexCoord");
    GlobalMembersGL.Uniforms.ScaleTexCoord = checkedGetUniformLocation(program, "ScaleTexCoord");
    GlobalMembersGL.Uniforms.PointSize = checkedGetUniformLocation(program, "PointSize");
  
    // default values
    _nativeGL.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, (float)1.0, (float)1.0);
    _nativeGL.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, (float)0.0, (float)0.0);
    _nativeGL.uniform1f(GlobalMembersGL.Uniforms.PointSize, 1);
  
    //BILLBOARDS
    GlobalMembersGL.Uniforms.BillBoard = checkedGetUniformLocation(program, "BillBoard");
    GlobalMembersGL.Uniforms.ViewPortExtent = checkedGetUniformLocation(program, "ViewPortExtent");
    GlobalMembersGL.Uniforms.TextureExtent = checkedGetUniformLocation(program, "TextureExtent");
  
    _nativeGL.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0); //NOT DRAWING BILLBOARD
  
    //FOR FLAT COLOR MIXING
    GlobalMembersGL.Uniforms.FlatColorIntensity = checkedGetUniformLocation(program, "FlatColorIntensity");
    GlobalMembersGL.Uniforms.ColorPerVertexIntensity = checkedGetUniformLocation(program, "ColorPerVertexIntensity");
    GlobalMembersGL.Uniforms.EnableColorPerVertex = checkedGetUniformLocation(program, "EnableColorPerVertex");
    GlobalMembersGL.Uniforms.EnableFlatColor = checkedGetUniformLocation(program, "EnableFlatColor");
  
    //Return
    return !_errorGettingLocationOcurred;
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
  
      GLState state = new GLState(_currentState);
      state.setPixelStoreIAlignmentUnpack(1);
      state.bindTexture(texId);
      state.setTextureParameterMinFilter(GLTextureParameterValue.linear());
      state.setTextureParameterMagFilter(GLTextureParameterValue.linear());
      state.setTextureParameterWrapS(GLTextureParameterValue.clampToEdge());
      state.setTextureParameterWrapT(GLTextureParameterValue.clampToEdge());
      setState(state);
  
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
  
      if (_currentState.getBoundTexture() == textureId)
      {
        _currentState.bindTexture(null);
      }
  
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

  public final void deleteShader(int shader)
  {
    _nativeGL.deleteShader(shader);
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

  public final void deleteProgram(int program)
  {
    _nativeGL.deleteProgram(program);
  }
}