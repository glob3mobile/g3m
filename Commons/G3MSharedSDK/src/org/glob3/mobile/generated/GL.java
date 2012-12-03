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









//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLProgramId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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

  private void loadModelView()
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::loadModelView()");
	}
  
	_nativeGL.uniformMatrix4fv(GlobalMembersGL.Uniforms.Modelview, false, _modelView);
  }


  /*void GL::enableCullFace(int face) {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableCullFace()");
   }
   if (!_enableCullFace) {
   _nativeGL->enable(GLFeature::cullFace());
   _enableCullFace = true;
   }
  
   if (_cullFace_face != face) {
   _nativeGL->cullFace(face);
   _cullFace_face = face;
   }
   }
  
   void GL::disableCullFace() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableCullFace()");
   }
  
   if (_enableCullFace) {
   _nativeGL->disable(GLFeature::cullFace());
   _enableCullFace = false;
   }
   }*/
  
  private IGLTextureId getGLTextureId()
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::getGLTextureId()");
	}
  
	if (_texturesIdBag.size() == 0)
	{
	  final int bugdetSize = 256;
  
	  ILogger.instance().logInfo("= Creating %d texturesIds...", bugdetSize);
  
	  final java.util.ArrayList<IGLTextureId> ids = _nativeGL.genTextures(bugdetSize);
  
	  for (int i = 0; i < bugdetSize; i++)
	  {
		//      _texturesIdBag.push_back(ids[i]);
		_texturesIdBag.addFirst(ids.get(i));
	  }
  
	  _texturesIdAllocationCounter += bugdetSize;
  
	  ILogger.instance().logInfo("= Created %d texturesIds (accumulated %d).", bugdetSize, _texturesIdAllocationCounter);
	}
  
	//  _texturesIdGetCounter++;
  
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

  //  int _lastTextureWidth;
  //  int _lastTextureHeight;
  ///#ifdef C_CODE
  //  unsigned char* _lastImageData;
  ///#endif
  ///#ifdef JAVA_CODE
  //  byte[] _lastImageData;
  ///#endif

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
	  ILogger.instance().logError("Error fetching Attribute, Program = %d, Variable = %s", program, name);
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
	  ILogger.instance().logError("Error fetching Uniform, Program = %d, Variable = %s", program, name);
	  _errorGettingLocationOcurred = true;
	}
	return uID;
  }

  private IFloatBuffer _billboardTexCoord;
  private IFloatBuffer getBillboardTexCoord()
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

  //void enableDepthTest();
  //void disableDepthTest();

  private final boolean _verbose;


  public GL(INativeGL nativeGL, boolean verbose)
  //  _enableFlatColor(false),
  //  _texturesIdGetCounter(0),
  //  _texturesIdTakeCounter(0),
  {
	  _nativeGL = nativeGL;
	  _verbose = verbose;
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

  //void enableVerticesPosition();

  //void enableTextures();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void verticesColors(boolean v);

  //void enableTexture2D();

  //void enableVertexFlatColor(float r, float g, float b, float a,float intensity);

  //void disableVertexFlatColor();

  //void disableTexture2D();

  //void disableVerticesPosition();

  //void disableTextures();

  public final void clearScreen(float r, float g, float b, float a)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::clearScreen()");
	}
  
	_nativeGL.clearColor(r, g, b, a);
	_nativeGL.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
  }

  public final void color(float r, float g, float b, float a)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::color()");
	}
  
	if ((_flatColorR != r) || (_flatColorG != g) || (_flatColorB != b) || (_flatColorA != a))
	{
	  _nativeGL.uniform4f(GlobalMembersGL.Uniforms.FlatColor, r, g, b, a);
  
	  _flatColorR = r;
	  _flatColorG = g;
	  _flatColorB = b;
	  _flatColorA = a;
	}
  }

  //void enableVertexColor(IFloatBuffer* colors, float intensity);

  //void disableVertexColor();

  public final void pushMatrix()
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::pushMatrix()");
	}
  
	_matrixStack.addLast(_modelView);
  }

  public final void popMatrix()
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::popMatrix()");
	}
  
	_modelView = _matrixStack.getLast();
	_matrixStack.removeLast();
  
	loadModelView();
  }

  public final void loadMatrixf(MutableMatrix44D modelView)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::loadMatrixf()");
	}
  
	_modelView = modelView;
  
	loadModelView();
  }

  public final void multMatrixf(MutableMatrix44D m)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::multMatrixf()");
	}
  
	_modelView = _modelView.multiply(m);
  
	loadModelView();
  }

  public final void vertexPointer(int size, int stride, IFloatBuffer vertices)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::vertexPointer(size=%d, stride=%d, vertices=%s)", size, stride, vertices.description());
	}
  
	if ((_vertices != vertices) || (_verticesTimestamp != vertices.timestamp()))
	{
	  _nativeGL.vertexAttribPointer(GlobalMembersGL.Attributes.Position, size, false, stride, vertices);
	  _vertices = vertices;
	  _verticesTimestamp = _vertices.timestamp();
	}
  }

  public final void drawElements(int mode, IIntBuffer indices)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::drawElements(%d, %s)", mode, indices.description());
	}
  
	_nativeGL.drawElements(mode, indices.size(), indices);
  }

  public final void drawArrays(int mode, int first, int count)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::drawArrays(%d, %d, %d)", mode, first, count);
	}
  
	_nativeGL.drawArrays(mode, first, count);
  }

  public final void setProjection(MutableMatrix44D projection)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::setProjection()");
	}
  
	_nativeGL.uniformMatrix4fv(GlobalMembersGL.Uniforms.Projection, false, projection);
  }

  public final boolean useProgram(ShaderProgram program)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::useProgram()");
	}
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
	GlobalMembersGL.Uniforms.ViewPortRatio = checkedGetUniformLocation(program, "ViewPortRatio");
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
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::enablePolygonOffset()");
	}
  
	_nativeGL.enable(GLFeature.polygonOffsetFill());
	_nativeGL.polygonOffset(factor, units);
  }

  public final void disablePolygonOffset()
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::disablePolygonOffset()");
	}
  
	_nativeGL.disable(GLFeature.polygonOffsetFill());
  }

  public final void lineWidth(float width)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::lineWidth()");
	}
  
	if (_lineWidth != width)
	{
	  _nativeGL.lineWidth(width);
	  _lineWidth = width;
	}
  }

  public final void pointSize(float size)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::pointSize()");
	}
  
	_nativeGL.uniform1f(GlobalMembersGL.Uniforms.PointSize, size);
  }

  public final int getError()
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::getError()()");
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

  //  const const GLTextureId*uploadTexture(const IImage* image,
  //                                  int textureWidth, int textureHeight,
  //                                  bool generateMipmap);

  public final void setTextureCoordinates(int size, int stride, IFloatBuffer textureCoordinates)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::setTextureCoordinates(size=%d, stride=%d, textureCoordinates=%s)", size, stride, textureCoordinates.description());
	}
  
	if ((_textureCoordinates != textureCoordinates) || (_textureCoordinatesTimestamp != textureCoordinates.timestamp()))
	{
	  _nativeGL.vertexAttribPointer(GlobalMembersGL.Attributes.TextureCoord, size, false, stride, textureCoordinates);
	  _textureCoordinates = textureCoordinates;
	  _textureCoordinatesTimestamp = _textureCoordinates.timestamp();
	}
  }

  public final void bindTexture(IGLTextureId textureId)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::bindTexture()");
	}
  
	_nativeGL.bindTexture(GLTextureType.texture2D(), textureId);
  }

  //void enableBlend();
  //void disableBlend();


  //void enableDepthTest();
  //void disableDepthTest();

  public final void drawBillBoard(IGLTextureId textureId, IFloatBuffer vertices, float viewPortRatio)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::drawBillBoard()");
	}
  
	int TODO_refactor_billboard;
  
	_nativeGL.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 1);
  
	_nativeGL.uniform1f(GlobalMembersGL.Uniforms.ViewPortRatio, viewPortRatio);
  
	//disableDepthTest();
  
	//enableTexture2D();
	color(1, 1, 1, 1);
  
	bindTexture(textureId);
  
	vertexPointer(3, 0, vertices);
	setTextureCoordinates(2, 0, getBillboardTexCoord());
  
	_nativeGL.drawArrays(GLPrimitive.triangleStrip(), 0, vertices.size() / 3);
  
	//enableDepthTest();
  
	_nativeGL.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0);
  }

  public final void deleteTexture(IGLTextureId texture)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::deleteTexture()");
	}
  
	if (texture != null)
	{
	  if (_nativeGL.deleteTexture(texture))
	  {
		_texturesIdBag.addLast(texture);
	  }
  
	  //    _texturesIdTakeCounter++;
	}
  }

  /*void enableCullFace(int face);
  void disableCullFace();*/

  public final void transformTexCoords(float scaleX, float scaleY, float translationX, float translationY)
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::transformTexCoords()");
	}
  
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
	if (_verbose)
		ILogger.instance().logInfo("GL::transformTexCoords()");

	transformTexCoords((float) scaleX, (float) scaleY, (float) translationX, (float) translationY);
  }

  public final void transformTexCoords(Vector2D scale, Vector2D translation)
  {
	if (_verbose)
		ILogger.instance().logInfo("GL::transformTexCoords()");

	transformTexCoords((float) scale._x, (float) scale._y, (float) translation._x, (float) translation._y);
  }

  public final void transformTexCoords(MutableVector2D scale, MutableVector2D translation)
  {
	if (_verbose)
		ILogger.instance().logInfo("GL::transformTexCoords()");

	transformTexCoords((float) scale.x(), (float) scale.y(), (float) translation.x(), (float) translation.y());
  }


  public final void color(Color col)
  {
	if (_verbose)
		ILogger.instance().logInfo("GL::color()");

	color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public final void clearScreen(Color col)
  {
	if (_verbose)
		ILogger.instance().logInfo("GL::clearScreen()");

	clearScreen(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  /*void enableVertexFlatColor(const Color& c, float intensity) {
   if (_verbose) ILogger::instance()->logInfo("GL::enableVertexFlatColor()");
	enableVertexFlatColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), intensity);
  }*/


  /* // state handling
   void GL::enableTextures() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableTextures()");
   }
  
   if (!_enableTextures) {
   _nativeGL->enableVertexAttribArray(Attributes.TextureCoord);
   _enableTextures = true;
   }
   }
  
   void GL::disableTextures() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableTextures()");
   }
  
   if (_enableTextures) {
   _nativeGL->disableVertexAttribArray(Attributes.TextureCoord);
   _enableTextures = false;
   }
   }
  
   void GL::enableTexture2D() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableTexture2D()");
   }
  
   if (!_enableTexture2D) {
   _nativeGL->uniform1i(Uniforms.EnableTexture, 1);
   _enableTexture2D = true;
   }
   }
  
   void GL::disableTexture2D() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableTexture2D()");
   }
  
   if (_enableTexture2D) {
   _nativeGL->uniform1i(Uniforms.EnableTexture, 0);
   _enableTexture2D = false;
   }
   }*/
  
  /*void GL::enableVertexColor(IFloatBuffer* colors, float intensity) {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableVertexColor(color=%s, intensity=%f)",
   colors->description().c_str(),
   intensity);
   }
   if (!_enableVertexColor) {
   _nativeGL->uniform1i(Uniforms.EnableColorPerVertex, 1);
   _nativeGL->enableVertexAttribArray(Attributes.Color);
   _enableVertexColor = true;
   }
  
   if ((_colors != colors) ||
   (_colorsTimestamp != colors->timestamp()) ) {
   _nativeGL->vertexAttribPointer(Attributes.Color, 4, false, 0, colors);
   _colors = colors;
   _colorsTimestamp = _colors->timestamp();
   }
  
   _nativeGL->uniform1f(Uniforms.ColorPerVertexIntensity, intensity);
   }
  
   void GL::disableVertexColor() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableVertexColor()");
   }
  
   if (_enableVertexColor) {
   _nativeGL->disableVertexAttribArray(Attributes.Color);
   _nativeGL->uniform1i(Uniforms.EnableColorPerVertex, 0);
   _enableVertexColor = false;
   }
   }*/
  
  /*void GL::enableVerticesPosition() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableVerticesPosition()");
   }
   if (!_enableVerticesPosition) {
   _nativeGL->enableVertexAttribArray(Attributes.Position);
   _enableVerticesPosition = true;
   }
   }
  
   void GL::disableVerticesPosition() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableVerticesPosition()");
   }
  
   if (_enableVerticesPosition) {
   _nativeGL->disableVertexAttribArray(Attributes.Position);
   _enableVerticesPosition = false;
   }
   }*/
  
  /*void GL::enableVertexFlatColor(float r, float g, float b, float a,
   float intensity) {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableVertexFlatColor()");
   }
  
   if (!_enableFlatColor) {
   _nativeGL->uniform1i(Uniforms.EnableFlatColor, 1);
   _enableFlatColor = true;
   }
  
   color(r, g, b, a);
  
   //  _gl->uniform1f(Uniforms.FlatColorIntensity, intensity);
   if (_flatColorIntensity != intensity) {
   _nativeGL->uniform1f(Uniforms.FlatColorIntensity, intensity);
   _flatColorIntensity = intensity;
   }
   }
  
   void GL::disableVertexFlatColor() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableVertexFlatColor()");
   }
  
   if (_enableFlatColor) {
   _nativeGL->uniform1i(Uniforms.EnableFlatColor, 0);
   _enableFlatColor = false;
   }
   }*/
  
  /*void GL::enableDepthTest() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableDepthTest()");
   }
   if (!_enableDepthTest) {
   _nativeGL->enable(GLFeature::depthTest());
   _enableDepthTest = true;
   }
   }
  
   void GL::disableDepthTest() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableDepthTest()");
   }
  
   if (_enableDepthTest) {
   _nativeGL->disable(GLFeature::depthTest());
   _enableDepthTest = false;
   }
   }*/
  
  /*void GL::enableBlend() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::enableBlend()");
   }
   if (!_enableBlend) {
   _nativeGL->enable(GLFeature::blend());
   _enableBlend = true;
   }
   }
  
   void GL::disableBlend() {
   if (_verbose) {
   ILogger::instance()->logInfo("GL::disableBlend()");
   }
  
   if (_enableBlend) {
   _nativeGL->disable(GLFeature::blend());
   _enableBlend = false;
   }
   }*/
  
  public final void setBlendFuncSrcAlpha()
  {
	if (_verbose)
	{
	  ILogger.instance().logInfo("GL::setBlendFuncSrcAlpha()");
	}
  
	_nativeGL.blendFunc(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  }

  public final void getViewport(int[] v)
  {
	if (_verbose)
		ILogger.instance().logInfo("GL::getViewport()");

	_nativeGL.getIntegerv(GLVariable.viewport(), v);
  }

  public void dispose()
  {

	//    if (_lastImageData != NULL) {
	//      delete [] _lastImageData;
	//      _lastImageData = NULL;
	//    }

	if (_vertices != null)
		_vertices.dispose();
	if (_textureCoordinates != null)
		_textureCoordinates.dispose();
	if (_colors != null)
		_colors.dispose();

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int createProgram() const
  public final int createProgram()
  {
	return _nativeGL.createProgram();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void attachShader(int program, int shader) const
  public final void attachShader(int program, int shader)
  {
	_nativeGL.attachShader(program, shader);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int createShader(ShaderType type) const
  public final int createShader(ShaderType type)
  {
	return _nativeGL.createShader(type);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean compileShader(int shader, const String& source) const
  public final boolean compileShader(int shader, String source)
  {
	return _nativeGL.compileShader(shader, source);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void deleteShader(int shader) const
  public final void deleteShader(int shader)
  {
	_nativeGL.deleteShader(shader);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void printShaderInfoLog(int shader) const
  public final void printShaderInfoLog(int shader)
  {
	_nativeGL.printShaderInfoLog(shader);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean linkProgram(int program) const
  public final boolean linkProgram(int program)
  {
	return _nativeGL.linkProgram(program);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void printProgramInfoLog(int program) const
  public final void printProgramInfoLog(int program)
  {
	_nativeGL.linkProgram(program);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void deleteProgram(int program) const
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
		IFloatBuffer colors = state.getColors();
		if ((_colors != colors) || (_colorsTimestamp != colors.timestamp()))
		{
		  _nativeGL.vertexAttribPointer(GlobalMembersGL.Attributes.Color, 4, false, 0, colors);
		  _colors = colors;
		  _colorsTimestamp = _colors.timestamp();
		}
	  }
	  else
	  {
		_nativeGL.disableVertexAttribArray(GlobalMembersGL.Attributes.Color);
		_nativeGL.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 0);
	  }
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
		final int face = state.getCulledFace();
		if (_cullFace_face != face)
		{
		  _nativeGL.cullFace(face);
		  _cullFace_face = face;
		}
	  }
	  else
	  {
		_nativeGL.disable(GLFeature.cullFace());
	  }
	}
  
  }
}