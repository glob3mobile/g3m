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
//class GLState;


public class GL
{
  private final INativeGL _nativeGL;


  /////////////////////////////////////////////////
  //CURRENT GL STATUS
  private GLGlobalState _currentGLGlobalState = new GLGlobalState();
  private GPUProgram _currentGPUProgram;
  /////////////////////////////////////////////////

  private final java.util.LinkedList<IGLTextureId> _texturesIdBag = new java.util.LinkedList<IGLTextureId>();
  private int _texturesIdAllocationCounter;
<<<<<<< HEAD

  //  GLGlobalState *_currentState;
  //  GPUProgram* _currentGPUProgram;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void loadModelView();
=======
  //  long                        _texturesIdGetCounter;
  //  long                        _texturesIdTakeCounter;

  // state handling
  private boolean _enableDepthTest;
  private boolean _enableBlend;
  private boolean _enableTextures;
  private boolean _enableTexture2D;
  private boolean _enableVertexColor;
  private boolean _enableVerticesPosition;
  private boolean _enableFlatColor;
  private boolean _enableCullFace;

  private int _cullFace_face;

  private float _scaleX;
  private float _scaleY;
  private float _translationX;
  private float _translationY;

  private IFloatBuffer _vertices;
  private int _verticesTimestamp;
  private IFloatBuffer _textureCoordinates;
  private int _textureCoordinatesTimestamp;
  private IFloatBuffer _colors;
  private int _colorsTimestamp;

  private float _flatColorR;
  private float _flatColorG;
  private float _flatColorB;
  private float _flatColorA;
  private float _flatColorIntensity;
  private float _lineWidth;
  private float _pointSize;

  private ShaderProgram _program;

///#ifdef C_CODE
//  const IGLTextureId* _boundTextureId;
///#endif
///#ifdef JAVA_CODE
//  private IGLTextureId _boundTextureId;
///#endif

  private void loadModelView()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::loadModelView()");
  //  }
  
    _nativeGL.uniformMatrix4fv(GlobalMembersGL.Uniforms.Modelview, false, _modelView);
  }
>>>>>>> webgl-port

  private IGLTextureId getGLTextureId()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::getGLTextureId()");
  //  }
  
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

<<<<<<< HEAD
  private final boolean _verbose;
=======
  //Get Locations warning of errors
  private boolean _errorGettingLocationOcurred;
  private int checkedGetAttribLocation(ShaderProgram program, String name)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::checkedGetAttribLocation()");
  //  }
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
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::checkedGetUniformLocation()");
  //  }
  
    IGLUniformID uID = _nativeGL.getUniformLocation(program, name);
    if (!uID.isValid())
    {
      ILogger.instance().logError("Error fetching Uniform, Program=%s, Variable=\"%s\"", program.description(), name);
      _errorGettingLocationOcurred = true;
    }
    return uID;
  }

  private IFloatBuffer _billboardTexCoord;
  private IFloatBuffer getBillboardTexCoord()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::getBillboardTexCoord()");
  //  }
  
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

//  const bool _verbose;
>>>>>>> webgl-port

  //  void setGLGlobalState(const GLGlobalState& state);
  //  void setProgramState(GPUProgramManager& progManager, const GPUProgramState& progState);
  //
  //  void applyGLGlobalStateAndGPUProgramState(const GLGlobalState& state, GPUProgramManager& progManager, const GPUProgramState& progState);



  public GL(INativeGL nativeGL, boolean verbose)
<<<<<<< HEAD
  {
     _nativeGL = nativeGL;
     _verbose = verbose;
=======
//  _verbose(verbose),
  //  _enableFlatColor(false),
  //  _texturesIdGetCounter(0),
  //  _texturesIdTakeCounter(0),
//  _boundTextureId(NULL)
  {
     _nativeGL = nativeGL;
     _enableTextures = false;
     _enableTexture2D = false;
     _enableVertexColor = false;
     _enableVerticesPosition = false;
     _enableBlend = false;
     _enableDepthTest = false;
     _enableCullFace = false;
     _cullFace_face = GLCullFace.back();
>>>>>>> webgl-port
     _texturesIdAllocationCounter = 0;
     _currentGPUProgram = null;
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

<<<<<<< HEAD
    //    _currentState = GLGlobalState::newDefault(); //Init after constants
  }


  ///#include "GPUProgramState.hpp"
  
=======
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void verticesColors(boolean v);

  public final void clearScreen(float r, float g, float b, float a)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::clearScreen()");
  //  }
  
    _nativeGL.clearColor(r, g, b, a);
    _nativeGL.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
  }

  public final void color(float r, float g, float b, float a)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::color()");
  //  }
  
    if ((_flatColorR != r) || (_flatColorG != g) || (_flatColorB != b) || (_flatColorA != a))
    {
      _nativeGL.uniform4f(GlobalMembersGL.Uniforms.FlatColor, r, g, b, a);
  
      _flatColorR = r;
      _flatColorG = g;
      _flatColorB = b;
      _flatColorA = a;
    }
  }

  public final void pushMatrix()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::pushMatrix()");
  //  }
  
    _matrixStack.addLast(_modelView);
  }

  public final void popMatrix()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::popMatrix()");
  //  }
  
    _modelView = _matrixStack.getLast();
    _matrixStack.removeLast();
>>>>>>> webgl-port
  
  public final void clearScreen(Color color)
  {
<<<<<<< HEAD
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::clearScreen()");
    }
=======
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::loadMatrixf()");
  //  }
>>>>>>> webgl-port
  
  //  int ASK_JM;
  //  GLState glState((GLGlobalState*)&state, NULL);
  //  glState.applyGlobalStateOnGPU(this);
  
<<<<<<< HEAD
=======
    loadModelView();
  }

  public final void multMatrixf(MutableMatrix44D m)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::multMatrixf()");
  //  }
>>>>>>> webgl-port
  
    GLGlobalState state = new GLGlobalState();
    state.setClearColor(color);
    state.applyChanges(this, _currentGLGlobalState);
  
<<<<<<< HEAD
    //setGLGlobalState(state);
    _nativeGL.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
=======
    loadModelView();
  }

  public final void vertexPointer(int size, int stride, IFloatBuffer vertices)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::vertexPointer(size=%d, stride=%d, vertices=%s)",
  //                                 size,
  //                                 stride,
  //                                 vertices->description().c_str());
  //  }
  
    if ((_vertices != vertices) || (_verticesTimestamp != vertices.timestamp()))
    {
      _nativeGL.vertexAttribPointer(GlobalMembersGL.Attributes.Position, size, false, stride, vertices);
      _vertices = vertices;
      _verticesTimestamp = _vertices.timestamp();
    }
>>>>>>> webgl-port
  }

//  void drawElements(int mode,
//                    IShortBuffer* indices, const GLGlobalState& state,
//                    GPUProgramManager& progManager,
//                    const GPUProgramState* gpuState);


  //void GL::drawElements(int mode,
  //                      IShortBuffer* indices, const GLGlobalState& state,
  //                      GPUProgramManager& progManager,
  //                      const GPUProgramState* gpuState) {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawElements(%d, %s)",
  //                                 mode,
  //                                 indices->description().c_str());
  //  }
  //  
  ////  GLState glState((GLGlobalState*)&state, (GPUProgramState*)gpuState);
  ////  glState.applyOnGPU(this, progManager);
  ////  
  ////  //applyGLGlobalStateAndGPUProgramState(state, progManager, *gpuState);
  ////  
  ////  _nativeGL->drawElements(mode,
  ////                          indices->size(),
  ////                          indices);
  ////  
  //  //TODO: CHECKING GPU STATUS BY DELETING ALL
  //  //progManager.getProgram(*gpuState)->onUnused();
  //}
  
  public final void drawElements(int mode, IShortBuffer indices, GLState state, GPUProgramManager progManager)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawElements(%d, %s)",
  //                                 mode,
  //                                 indices->description().c_str());
  //  }
  
    state.applyOnGPU(this, progManager);
  
    _nativeGL.drawElements(mode, indices.size(), indices);
  }

<<<<<<< HEAD
//  void drawArrays(int mode,
//                  int first,
//                  int count, const GLGlobalState& state,
//                  GPUProgramManager& progManager,
//                  const GPUProgramState* gpuState);


  //void GL::drawArrays(int mode,
  //                    int first,
  //                    int count, const GLGlobalState& state,
  //                    GPUProgramManager& progManager,
  //                    const GPUProgramState* gpuState) {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawArrays(%d, %d, %d)",
  //                                 mode,
  //                                 first,
  //                                 count);
  //  }
  //  
  ////  GLState glState((GLGlobalState*)&state, (GPUProgramState*)gpuState);
  ////  glState.applyOnGPU(this, progManager);
  ////  
  //////  applyGLGlobalStateAndGPUProgramState(state, progManager, *gpuState);
  ////  
  ////  
  ////  _nativeGL->drawArrays(mode,
  ////                        first,
  ////                        count);
  ////  
  //  //TODO: CHECKING GPU STATUS BY DELETING ALL
  //  //progManager.getProgram(*gpuState)->onUnused();
  //}
=======
  public final void drawArrays(int mode, int first, int count)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawArrays(%d, %d, %d)",
  //                                 mode,
  //                                 first,
  //                                 count);
  //  }
  
    _nativeGL.drawArrays(mode, first, count);
  }

  public final void setProjection(MutableMatrix44D projection)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::setProjection()");
  //  }
  
    _nativeGL.uniformMatrix4fv(GlobalMembersGL.Uniforms.Projection, false, projection);
  }

  public final boolean useProgram(ShaderProgram program)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::useProgram()");
  //  }
  
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
    _nativeGL.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, _scaleX, _scaleY);
    _nativeGL.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, _translationX, _translationY);
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

  public final void enablePolygonOffset(float factor, float units)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::enablePolygonOffset()");
  //  }
>>>>>>> webgl-port
  
  public final void drawArrays(int mode, int first, int count, GLState state, GPUProgramManager progManager)
  {
<<<<<<< HEAD
    if (_verbose)
    {
      ILogger.instance().logInfo("GL::drawArrays(%d, %d, %d)", mode, first, count);
    }
=======
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::disablePolygonOffset()");
  //  }
>>>>>>> webgl-port
  
    state.applyOnGPU(this, progManager);
    _nativeGL.drawArrays(mode, first, count);
  }

  public final int getError()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::getError()");
  //  }
  
    return _nativeGL.getError();
  }

  public final IGLTextureId uploadTexture(IImage image, int format, boolean generateMipmap)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::uploadTexture()");
  //  }
  
    final IGLTextureId texId = getGLTextureId();
    if (texId != null)
    {
      int texture2D = GLTextureType.texture2D();
  
  //    GLGlobalState state(*GLState::getCurrentGLGlobalState());
  //    GLGlobalState* state = GLState::createCopyOfCurrentGLGlobalState();
  
      GLGlobalState newState = new GLGlobalState();
  
      newState.setPixelStoreIAlignmentUnpack(1);
      newState.bindTexture(texId);
  
      newState.applyChanges(this, _currentGLGlobalState);
  
  //    GLState glState(state, NULL);
  //    glState.applyGlobalStateOnGPU(this);
  //    delete state;
  
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

<<<<<<< HEAD
=======
  public final void setTextureCoordinates(int size, int stride, IFloatBuffer textureCoordinates)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::setTextureCoordinates(size=%d, stride=%d, textureCoordinates=%s)",
  //                                 size,
  //                                 stride,
  //                                 textureCoordinates->description().c_str());
  //  }
  
    if ((_textureCoordinates != textureCoordinates) || (_textureCoordinatesTimestamp != textureCoordinates.timestamp()))
    {
      _nativeGL.vertexAttribPointer(GlobalMembersGL.Attributes.TextureCoord, size, false, stride, textureCoordinates);
      _textureCoordinates = textureCoordinates;
      _textureCoordinatesTimestamp = _textureCoordinates.timestamp();
    }
  }

  public final void bindTexture(IGLTextureId textureId)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::bindTexture()");
  //  }
  
    if (textureId == null)
    {
      ILogger.instance().logError("Can't bind a NULL texture");
    }
    else
    {
  //    if ((_boundTextureId == NULL) || !_boundTextureId->isEqualsTo(textureId)) {
        _nativeGL.bindTexture(GLTextureType.texture2D(), textureId);
  //      _boundTextureId = textureId;
  //    }
  //    else {
  //      ILogger::instance()->logInfo("TextureId %s already bound", textureId->description().c_str());
  //    }
    }
  }

  public final void startBillBoardDrawing(int viewPortWidth, int viewPortHeight)
  {
    _nativeGL.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 1);
    _nativeGL.uniform2f(GlobalMembersGL.Uniforms.ViewPortExtent, viewPortWidth, viewPortHeight);
  
    color(1, 1, 1, 1);
  
    setTextureCoordinates(2, 0, getBillboardTexCoord());
  }
  public final void stopBillBoardDrawing()
  {
    _nativeGL.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0);
  }

  public final void drawBillBoard(IGLTextureId textureId, IFloatBuffer vertices, int textureWidth, int textureHeight)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawBillBoard()");
  //  }
  
    int TODO_refactor_billboard;
  
    _nativeGL.uniform2f(GlobalMembersGL.Uniforms.TextureExtent, textureWidth, textureHeight);
  
    bindTexture(textureId);
  
    vertexPointer(3, 0, vertices);
  
    _nativeGL.drawArrays(GLPrimitive.triangleStrip(), 0, vertices.size() / 3);
  }

>>>>>>> webgl-port
  public final void deleteTexture(IGLTextureId textureId)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::deleteTexture()");
  //  }
  
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
  
      if (_currentGLGlobalState.getBoundTexture() == textureId)
      {
         _currentGLGlobalState.bindTexture(null);
      }
  
  //    GLState::textureHasBeenDeleted(textureId);
  
  //    if (GLState::getCurrentGLGlobalState()->getBoundTexture() == textureId){
  //      GLState::getCurrentGLGlobalState()->bindTexture(NULL);
  //    }
  
      //ILogger::instance()->logInfo("  = delete textureId=%s", texture->description().c_str());
    }
  }

  public final void getViewport(int[] v)
  {
<<<<<<< HEAD
    if (_verbose)
       ILogger.instance().logInfo("GL::getViewport()");

    _nativeGL.getIntegerv(GLVariable.viewport(), v);
=======
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::transformTexCoords()");
  //  }
  
    if ((_scaleX != scaleX) || (_scaleY != scaleY))
    {
      _nativeGL.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, scaleX, scaleY);
      _scaleX = scaleX;
      _scaleY = scaleY;
    }
  
    if ((_translationX != translationX) || (_translationY != translationY))
    {
      _nativeGL.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, translationX, translationY);
      _translationX = translationX;
      _translationY = translationY;
    }
>>>>>>> webgl-port
  }

  public void dispose()
  {
<<<<<<< HEAD
    _nativeGL.dispose();
=======
//    if (_verbose) ILogger::instance()->logInfo("GL::transformTexCoords()");

    transformTexCoords((float) scaleX, (float) scaleY, (float) translationX, (float) translationY);
>>>>>>> webgl-port
  }

  public final int createProgram()
  {
<<<<<<< HEAD
    return _nativeGL.createProgram();
=======
//    if (_verbose) ILogger::instance()->logInfo("GL::transformTexCoords()");

    transformTexCoords((float) scale._x, (float) scale._y, (float) translation._x, (float) translation._y);
>>>>>>> webgl-port
  }

  public final void attachShader(int program, int shader)
  {
<<<<<<< HEAD
    _nativeGL.attachShader(program, shader);
=======
//    if (_verbose) ILogger::instance()->logInfo("GL::transformTexCoords()");

    transformTexCoords((float) scale.x(), (float) scale.y(), (float) translation.x(), (float) translation.y());
>>>>>>> webgl-port
  }

  public final int createShader(ShaderType type)
  {
<<<<<<< HEAD
    return _nativeGL.createShader(type);
=======
//    if (_verbose) ILogger::instance()->logInfo("GL::color()");

    color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
>>>>>>> webgl-port
  }

  public final boolean compileShader(int shader, String source)
  {
<<<<<<< HEAD
    return _nativeGL.compileShader(shader, source);
=======
//    if (_verbose) ILogger::instance()->logInfo("GL::clearScreen()");

    clearScreen(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
>>>>>>> webgl-port
  }

  public final boolean deleteShader(int shader)
  {
    return _nativeGL.deleteShader(shader);
  }

  public final void printShaderInfoLog(int shader)
  {
<<<<<<< HEAD
    _nativeGL.printShaderInfoLog(shader);
=======
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::setBlendFuncSrcAlpha()");
  //  }
  
    _nativeGL.blendFunc(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
>>>>>>> webgl-port
  }

  public final boolean linkProgram(int program)
  {
<<<<<<< HEAD
    return _nativeGL.linkProgram(program);
  }
=======
//    if (_verbose) ILogger::instance()->logInfo("GL::getViewport()");
>>>>>>> webgl-port

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

  public final void uniformMatrix4fv(IGLUniformID location, boolean transpose, Matrix44D matrix)
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

  public final void useProgram(GPUProgram program)
  {
    if (program != null && _currentGPUProgram != program)
    {
  
      if (_currentGPUProgram != null)
      {
        _currentGPUProgram.onUnused(this);
      }
  
      _nativeGL.useProgram(program);
      program.onUsed();
      _currentGPUProgram = program;
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


}
//void GL::applyGLGlobalStateAndGPUProgramState(const GLGlobalState& state, GPUProgramManager& progManager, const GPUProgramState& progState){
//  state.applyChanges(this, *_currentState);
//  setProgramState(progManager, progState);
//}
