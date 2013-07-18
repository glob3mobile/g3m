package org.glob3.mobile.generated; 
//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//


//
//  GL.hpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 14/06/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//





//class IGLProgramId;
//class IGLUniformID;


public class GL
{
  private final INativeGL _nativeGL;

  private MutableMatrix44D _modelView = new MutableMatrix44D();

  // stack of ModelView matrices
  private java.util.LinkedList<MutableMatrix44D> _matrixStack = new java.util.LinkedList<MutableMatrix44D>();

  private final java.util.LinkedList<IGLTextureId> _texturesIdBag = new java.util.LinkedList<IGLTextureId>();
  private int _texturesIdAllocationCounter;
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



  public GL(INativeGL nativeGL, boolean verbose)
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
     _texturesIdAllocationCounter = 0;
     _scaleX = 1F;
     _scaleY = 1F;
     _translationX = 0F;
     _translationY = 0F;
     _vertices = null;
     _verticesTimestamp = 0;
     _textureCoordinates = null;
     _textureCoordinatesTimestamp = 0;
     _colors = null;
     _colorsTimestamp = 0;
     _flatColorR = 0F;
     _flatColorG = 0F;
     _flatColorB = 0F;
     _flatColorA = 0F;
     _flatColorIntensity = 0F;
     _billboardTexCoord = null;
     _lineWidth = 1F;
     _pointSize = 1F;
     _program = null;
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
  }

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
  
    loadModelView();
  }

  public final void loadMatrixf(MutableMatrix44D modelView)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::loadMatrixf()");
  //  }
  
    _modelView = modelView;
  
    loadModelView();
  }

  public final void multMatrixf(MutableMatrix44D m)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::multMatrixf()");
  //  }
  
    _modelView = _modelView.multiply(m);
  
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
  }

//  void drawElements(int mode,
//                    IIntBuffer* indices);

  //void GL::drawElements(int mode,
  //                      IIntBuffer* indices) {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawElements(%d, %s)",
  //                                 mode,
  //                                 indices->description().c_str());
  //  }
  //
  //  _nativeGL->drawElements(mode,
  //                          indices->size(),
  //                          indices);
  //}
  public final void drawElements(int mode, IShortBuffer indices)
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawElements(%d, %s)",
  //                                 mode,
  //                                 indices->description().c_str());
  //  }
  
    _nativeGL.drawElements(mode, indices.size(), indices);
  }

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
  
    _nativeGL.enable(GLFeature.polygonOffsetFill());
    _nativeGL.polygonOffset(factor, units);
  }

  public final void disablePolygonOffset()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::disablePolygonOffset()");
  //  }
  
    _nativeGL.disable(GLFeature.polygonOffsetFill());
  }

  //  void lineWidth(float width);
  //
  //  void pointSize(float size);

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
      //_nativeGL->blendFunc(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
      _nativeGL.pixelStorei(GLAlignment.unpack(), 1);
  
      _nativeGL.bindTexture(GLTextureType.texture2D(), texId);
      _nativeGL.texParameteri(GLTextureType.texture2D(), GLTextureParameter.minFilter(), GLTextureParameterValue.linear());
      _nativeGL.texParameteri(GLTextureType.texture2D(), GLTextureParameter.magFilter(), GLTextureParameterValue.linear());
      _nativeGL.texParameteri(GLTextureType.texture2D(), GLTextureParameter.wrapS(), GLTextureParameterValue.clampToEdge());
      _nativeGL.texParameteri(GLTextureType.texture2D(), GLTextureParameter.wrapT(), GLTextureParameterValue.clampToEdge());
      _nativeGL.texImage2D(image, format);
  
      if (generateMipmap)
      {
        _nativeGL.generateMipmap(GLTextureType.texture2D());
      }
    }
    else
    {
      ILogger.instance().logError("can't get a valid texture id\n");
      return null;
    }
  
    return texId;
  }

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
  
  //    if (_boundTextureId != NULL) {
  //      if (_boundTextureId->isEqualsTo(textureId)) {
  //        _boundTextureId = NULL;
  //      }
  //    }
  
      //ILogger::instance()->logInfo("  = delete textureId=%s", texture->description().c_str());
    }
  }

  public final void transformTexCoords(float scaleX, float scaleY, float translationX, float translationY)
  {
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
  }

  public final void transformTexCoords(double scaleX, double scaleY, double translationX, double translationY)
  {
//    if (_verbose) ILogger::instance()->logInfo("GL::transformTexCoords()");

    transformTexCoords((float) scaleX, (float) scaleY, (float) translationX, (float) translationY);
  }

  public final void transformTexCoords(Vector2D scale, Vector2D translation)
  {
//    if (_verbose) ILogger::instance()->logInfo("GL::transformTexCoords()");

    transformTexCoords((float) scale._x, (float) scale._y, (float) translation._x, (float) translation._y);
  }

  public final void transformTexCoords(MutableVector2D scale, MutableVector2D translation)
  {
//    if (_verbose) ILogger::instance()->logInfo("GL::transformTexCoords()");

    transformTexCoords((float) scale.x(), (float) scale.y(), (float) translation.x(), (float) translation.y());
  }


  public final void color(Color col)
  {
//    if (_verbose) ILogger::instance()->logInfo("GL::color()");

    color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public final void clearScreen(Color col)
  {
//    if (_verbose) ILogger::instance()->logInfo("GL::clearScreen()");

    clearScreen(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  /*void enableVertexFlatColor(const Color& c, float intensity) {
   if (_verbose) ILogger::instance()->logInfo("GL::enableVertexFlatColor()");
   enableVertexFlatColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), intensity);
   }*/

  public final void setBlendFuncSrcAlpha()
  {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::setBlendFuncSrcAlpha()");
  //  }
  
    _nativeGL.blendFunc(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  }

  public final void getViewport(int[] v)
  {
//    if (_verbose) ILogger::instance()->logInfo("GL::getViewport()");

    _nativeGL.getIntegerv(GLVariable.viewport(), v);
  }

  public void dispose()
  {
    _nativeGL.dispose();

    // GL is not owner of these buffers, it keeps a reference only for state-change-testing. NO DELETE THEM.
    // delete _vertices;
    // delete _textureCoordinates;
    // delete _colors;
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

  public final void setState(GLState state)
  {
  
    // Depth Testh
    if (_enableDepthTest != state.isEnabledDepthTest())
    {
      _enableDepthTest = state.isEnabledDepthTest();
      if (_enableDepthTest)
      {
        _nativeGL.enable(GLFeature.depthTest());
      }
      else
      {
        _nativeGL.disable(GLFeature.depthTest());
      }
    }
  
    // Blending
    if (_enableBlend != state.isEnabledBlend())
    {
      _enableBlend = state.isEnabledBlend();
      if (_enableBlend)
      {
        _nativeGL.enable(GLFeature.blend());
      }
      else
      {
        _nativeGL.disable(GLFeature.blend());
      }
    }
  
    // Textures
    if (_enableTextures != state.isEnabledTextures())
    {
      _enableTextures = state.isEnabledTextures();
      if (_enableTextures)
      {
        _nativeGL.enableVertexAttribArray(GlobalMembersGL.Attributes.TextureCoord);
      }
      else
      {
        _nativeGL.disableVertexAttribArray(GlobalMembersGL.Attributes.TextureCoord);
      }
    }
  
    // Texture2D
    if (_enableTexture2D != state.isEnabledTexture2D())
    {
      _enableTexture2D = state.isEnabledTexture2D();
      if (_enableTexture2D)
      {
        _nativeGL.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, 1);
      }
      else
      {
        _nativeGL.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, 0);
      }
    }
  
    // VertexColor
    if (_enableVertexColor != state.isEnabledVertexColor())
    {
      _enableVertexColor = state.isEnabledVertexColor();
      if (_enableVertexColor)
      {
        _nativeGL.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 1);
        _nativeGL.enableVertexAttribArray(GlobalMembersGL.Attributes.Color);
      }
      else
      {
        _nativeGL.disableVertexAttribArray(GlobalMembersGL.Attributes.Color);
        _nativeGL.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 0);
      }
    }
    IFloatBuffer colors = state.getColors();
    if (colors != null && ((_colors != colors) || (_colorsTimestamp != colors.timestamp())))
    {
      _nativeGL.vertexAttribPointer(GlobalMembersGL.Attributes.Color, 4, false, 0, colors);
      _colors = colors;
      _colorsTimestamp = _colors.timestamp();
    }
  
    // Vertices Position
    if (_enableVerticesPosition != state.isEnabledVerticesPosition())
    {
      _enableVerticesPosition = state.isEnabledVerticesPosition();
      if (_enableVerticesPosition)
      {
        _nativeGL.enableVertexAttribArray(GlobalMembersGL.Attributes.Position);
      }
      else
      {
        _nativeGL.disableVertexAttribArray(GlobalMembersGL.Attributes.Position);
      }
    }
  
    // Flat Color
    if (_enableFlatColor != state.isEnabledFlatColor())
    {
      _enableFlatColor = state.isEnabledFlatColor();
      if (_enableFlatColor)
      {
        _nativeGL.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, 1);
      }
      else
      {
        _nativeGL.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, 0);
      }
    }
  
    if (_enableFlatColor)
    {
      color(state.getFlatColor());
      final float intensity = state.getIntensity();
      if (_flatColorIntensity != intensity)
      {
        _nativeGL.uniform1f(GlobalMembersGL.Uniforms.FlatColorIntensity, intensity);
        _flatColorIntensity = intensity;
      }
    }
  
    // Cull Face
    if (_enableCullFace != state.isEnabledCullFace())
    {
      _enableCullFace = state.isEnabledCullFace();
      if (_enableCullFace)
      {
        _nativeGL.enable(GLFeature.cullFace());
      }
      else
      {
        _nativeGL.disable(GLFeature.cullFace());
      }
    }
    final int face = state.getCulledFace();
    if (_cullFace_face != face)
    {
      _nativeGL.cullFace(face);
      _cullFace_face = face;
    }
  
    final float lineWidth = state.lineWidth();
    if (_lineWidth != lineWidth)
    {
      _nativeGL.lineWidth(lineWidth);
      _lineWidth = lineWidth;
    }
  
    final float pointSize = state.pointSize();
    if (_pointSize != pointSize)
    {
      _nativeGL.uniform1f(GlobalMembersGL.Uniforms.PointSize, pointSize);
      _pointSize = pointSize;
    }
  }

}