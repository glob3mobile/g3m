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

  private final INativeGL _gl;

  private MutableMatrix44D _modelView = new MutableMatrix44D();

  // stack of ModelView matrices
  private java.util.LinkedList<MutableMatrix44D> _matrixStack = new java.util.LinkedList<MutableMatrix44D>();

  private final java.util.LinkedList<IGLTextureId> _texturesIdBag = new java.util.LinkedList<IGLTextureId>();
  private int _texturesIdAllocationCounter;
  private int _texturesIdGetCounter;
  private int _texturesIdTakeCounter;

  // state handling
  private boolean _enableTextures;
  private boolean _enableTexture2D;
  private boolean _enableVertexColor;
  private boolean _enableVerticesPosition;
  private boolean _enableFlatColor;
  private boolean _enableDepthTest;
  private boolean _enableBlend;

  private boolean _enableCullFace;

  private int _cullFace_face;




  private float _scaleX;
  private float _scaleY;
  private float _translationX;
  private float _translationY;

  private IFloatBuffer _vertices;
  private IFloatBuffer _textureCoordinates;
  private IFloatBuffer _colors;

  private float _flatColorR;
  private float _flatColorG;
  private float _flatColorB;
  private float _flatColorA;
  private float _flatColorIntensity;

  private void loadModelView()
  {
	float[] M = _modelView.getColumnMajorFloatArray();
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Modelview, 1, false, M);
  }

  private IGLTextureId getGLTextureId()
  {
	if (_texturesIdBag.size() == 0)
	{
	  final int bugdetSize = 256;
  
	  ILogger.instance().logInfo("= Creating %d texturesIds...", bugdetSize);
  
	  final java.util.ArrayList<IGLTextureId> ids = _gl.genTextures(bugdetSize);
  
	  for (int i = 0; i < bugdetSize; i++)
	  {
		//      _texturesIdBag.push_back(ids[i]);
		_texturesIdBag.addFirst(ids.get(i));
	  }
  
	  _texturesIdAllocationCounter += bugdetSize;
  
	  ILogger.instance().logInfo("= Created %d texturesIds (accumulated %d).", bugdetSize, _texturesIdAllocationCounter);
	}
  
	_texturesIdGetCounter++;
  
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
  private int checkedGetAttribLocation(IGLProgramId program, String name)
  {
	int l = _gl.getAttribLocation(program, name);
	if (l == -1)
	{
	  ILogger.instance().logError("Error fetching Attribute, Program = %d, Variable = %s", program, name);
	  _errorGettingLocationOcurred = true;
	}
	return l;
  }
  private IGLUniformID checkedGetUniformLocation(IGLProgramId program, String name)
  {
	IGLUniformID uID = _gl.getUniformLocation(program, name);
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


  public GL(INativeGL gl)
//  _enableFlatColor(false),
  {
	  _gl = gl;
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
	  _texturesIdGetCounter = 0;
	  _texturesIdTakeCounter = 0;
	  _vertices = null;
	  _textureCoordinates = null;
	  _colors = null;
	  _flatColorR = 0F;
	  _flatColorG = 0F;
	  _flatColorB = 0F;
	  _flatColorA = 0F;
	  _flatColorIntensity = 0F;
	  _billboardTexCoord = null;
	//Init Constants
	GLCullFace.init(gl);
	GLBufferType.init(gl);
	GLFeature.init(gl);
	GLType.init(gl);
	GLPrimitive.init(gl);
	GLBlendFactor.init(gl);
	GLTextureType.init(gl);
	GLTextureParameter.init(gl);
	GLTextureParameterValue.init(gl);
	GLAlignment.init(gl);
	GLFormat.init(gl);
	GLVariable.init(gl);
	GLError.init(gl);
  }

  public final void enableVerticesPosition()
  {
	if (!_enableVerticesPosition)
	{
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.Position);
	  _enableVerticesPosition = true;
	}
  }


  // state handling
  public final void enableTextures()
  {
	if (!_enableTextures)
	{
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.TextureCoord);
	  _enableTextures = true;
	}
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void verticesColors(boolean v);

  public final void enableTexture2D()
  {
	if (!_enableTexture2D)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, 1);
	  _enableTexture2D = true;
	}
  }

  public final void enableVertexFlatColor(float r, float g, float b, float a, float intensity)
  {
	if (!_enableFlatColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, 1);
	  _enableFlatColor = true;
	}
  
	color(r, g, b, a);
  
	//  _gl->uniform1f(Uniforms.FlatColorIntensity, intensity);
	if (_flatColorIntensity != intensity)
	{
	  _gl.uniform1f(GlobalMembersGL.Uniforms.FlatColorIntensity, intensity);
	  _flatColorIntensity = intensity;
	}
  }

  public final void disableVertexFlatColor()
  {
	if (_enableFlatColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, 0);
	  _enableFlatColor = false;
	}
  }

  public final void disableTexture2D()
  {
	if (_enableTexture2D)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, 0);
	  _enableTexture2D = false;
	}
  }

  public final void disableVerticesPosition()
  {
	if (_enableVerticesPosition)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.Position);
	  _enableVerticesPosition = false;
	}
  }

  public final void disableTextures()
  {
	if (_enableTextures)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.TextureCoord);
	  _enableTextures = false;
	}
  }

  public final void clearScreen(float r, float g, float b, float a)
  {
	_gl.clearColor(r, g, b, a);
	_gl.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
  }

  public final void color(float r, float g, float b, float a)
  {
	if ((_flatColorR != r) || (_flatColorG != g) || (_flatColorB != b) || (_flatColorA != a))
	{
	  _gl.uniform4f(GlobalMembersGL.Uniforms.FlatColor, r, g, b, a);
  
	  _flatColorR = r;
	  _flatColorG = g;
	  _flatColorB = b;
	  _flatColorA = a;
	}
  }

  public final void enableVertexColor(IFloatBuffer colors, float intensity)
  {
  
	if (!_enableVertexColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 1);
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.Color);
	  _enableVertexColor = true;
	}
  
	if ((_colors != colors) || (_colors.timestamp() != colors.timestamp()))
	{
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Color, 4, false, 0, colors);
	  _colors = colors;
	}
  
	_gl.uniform1f(GlobalMembersGL.Uniforms.ColorPerVertexIntensity, intensity);
  }

  public final void disableVertexColor()
  {
	if (_enableVertexColor)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.Color);
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 0);
	  _enableVertexColor = false;
	}
  }

  public final void pushMatrix()
  {
	_matrixStack.addLast(_modelView);
  }

  public final void popMatrix()
  {
	_modelView = _matrixStack.getLast();
	_matrixStack.removeLast();
  
	loadModelView();
  }

  public final void loadMatrixf(MutableMatrix44D modelView)
  {
	_modelView = modelView;
  
	loadModelView();
  }

  public final void multMatrixf(MutableMatrix44D m)
  {
	_modelView = _modelView.multiply(m);
  
	loadModelView();
  }

  public final void vertexPointer(int size, int stride, IFloatBuffer vertices)
  {
	if ((_vertices != vertices) || (_vertices.timestamp() != vertices.timestamp()))
	{
  
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Position, size, false, stride, vertices);
	  _vertices = vertices;
	}
  }

  public final void drawTriangleStrip(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.triangleStrip(), indices.size(), indices);
  }

  public final void drawLines(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.lineLoop(), indices.size(), indices);
  }

  public final void drawLineLoop(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.lineLoop(), indices.size(), indices);
  }

  public final void drawPoints(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.points(), indices.size(), indices);
  }

  public final void setProjection(MutableMatrix44D projection)
  {
	float[] M = projection.getColumnMajorFloatArray();
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Projection, 1, false, M);
  }

  public final boolean useProgram(IGLProgramId program)
  {
	// set shaders
	_gl.useProgram(program);
  
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
	_gl.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, _scaleX, _scaleY);
	_gl.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, _translationX, _translationY);
	_gl.uniform1f(GlobalMembersGL.Uniforms.PointSize, 1);
  
	//BILLBOARDS
	GlobalMembersGL.Uniforms.BillBoard = checkedGetUniformLocation(program, "BillBoard");
	GlobalMembersGL.Uniforms.ViewPortRatio = checkedGetUniformLocation(program, "ViewPortRatio");
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0); //NOT DRAWING BILLBOARD
  
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
	_gl.enable(GLFeature.polygonOffsetFill());
	_gl.polygonOffset(factor, units);
  }

  public final void disablePolygonOffset()
  {
	_gl.disable(GLFeature.polygonOffsetFill());
  }

  public final void lineWidth(float width)
  {
	_gl.lineWidth(width);
  }

  public final void pointSize(float size)
  {
	_gl.uniform1f(GlobalMembersGL.Uniforms.PointSize, size);
  }

  public final int getError()
  {
	return _gl.getError();
  }

  public final IGLTextureId uploadTexture(IImage image, int format, boolean generateMipmap)
  {
	final IGLTextureId texId = getGLTextureId();
	if (texId != null)
	{
  
	  _gl.blendFunc(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
	  _gl.pixelStorei(GLAlignment.unpack(), 1);
  
	  _gl.bindTexture(GLTextureType.texture2D(), texId);
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.minFilter(), GLTextureParameterValue.linear());
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.magFilter(), GLTextureParameterValue.linear());
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.wrapS(), GLTextureParameterValue.clampToEdge());
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.wrapT(), GLTextureParameterValue.clampToEdge());
	  _gl.texImage2D(image, format);
  
	  if (generateMipmap)
	  {
		_gl.generateMipmap(GLTextureType.texture2D());
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

  public final void setTextureCoordinates(int size, int stride, IFloatBuffer texcoord)
  {
	if ((_textureCoordinates != texcoord) || (_textureCoordinates.timestamp() != texcoord.timestamp()))
	{
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.TextureCoord, size, false, stride, texcoord);
	  _textureCoordinates = texcoord;
	}
  }

  public final void bindTexture(IGLTextureId textureId)
  {
	_gl.bindTexture(GLTextureType.texture2D(), textureId);
  }

  public final void enableDepthTest()
  {
	if (!_enableDepthTest)
	{
	  _gl.enable(GLFeature.depthTest());
	  _enableDepthTest = true;
	}
  }
  public final void disableDepthTest()
  {
	if (_enableDepthTest)
	{
	  _gl.disable(GLFeature.depthTest());
	  _enableDepthTest = false;
	}
  }

  public final void enableBlend()
  {
	if (!_enableBlend)
	{
	  _gl.enable(GLFeature.blend());
	  _enableBlend = true;
	}
  }
  public final void disableBlend()
  {
	if (_enableBlend)
	{
	  _gl.disable(GLFeature.blend());
	  _enableBlend = false;
	}
  
  }

  public final void drawBillBoard(IGLTextureId textureId, IFloatBuffer vertices, float viewPortRatio)
  {
	int TODO_refactor_billboard;
  
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 1);
  
	_gl.uniform1f(GlobalMembersGL.Uniforms.ViewPortRatio, viewPortRatio);
  
	disableDepthTest();
  
	enableTexture2D();
	color(1, 1, 1, 1);
  
	bindTexture(textureId);
  
	vertexPointer(3, 0, vertices);
	setTextureCoordinates(2, 0, getBillboardTexCoord());
  
	_gl.drawArrays(GLPrimitive.triangleStrip(), 0, vertices.size() / 3);
  
	enableDepthTest();
  
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0);
  }

  public final void deleteTexture(IGLTextureId textureId)
  {
	if (textureId == null)
	{
	  return;
	}
	IGLTextureId textures[] = { textureId };
	_gl.deleteTextures(1, textures);
  
	_texturesIdBag.addLast(textureId);
  
	_texturesIdTakeCounter++;
  }

  public final void enableCullFace(int face)
  {
	if (!_enableCullFace)
	{
	  _gl.enable(GLFeature.cullFace());
	  _enableCullFace = true;
	}
  
	if (_cullFace_face != face)
	{
	  _gl.cullFace(face);
	  _cullFace_face = face;
	}
  }
  public final void disableCullFace()
  {
	if (_enableCullFace)
	{
	  _gl.disable(GLFeature.cullFace());
	  _enableCullFace = false;
	}
  }

  public final void transformTexCoords(float scaleX, float scaleY, float translationX, float translationY)
  {
	if ((_scaleX != scaleX) || (_scaleY != scaleY))
	{
	  _gl.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, scaleX, scaleY);
	  _scaleX = scaleX;
	  _scaleY = scaleY;
	}
  
	if ((_translationX != translationX) || (_translationY != translationY))
	{
	  _gl.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, translationX, translationY);
	  _translationX = translationX;
	  _translationY = translationY;
	}
  }

  public final void transformTexCoords(double scaleX, double scaleY, double translationX, double translationY)
  {
	transformTexCoords((float) scaleX, (float) scaleY, (float) translationX, (float) translationY);
  }

  public final void transformTexCoords(Vector2D scale, Vector2D translation)
  {
	transformTexCoords((float) scale.x(), (float) scale.y(), (float) translation.x(), (float) translation.y());
  }

  public final void transformTexCoords(MutableVector2D scale, MutableVector2D translation)
  {
	transformTexCoords((float) scale.x(), (float) scale.y(), (float) translation.x(), (float) translation.y());
  }


  public final void color(Color col)
  {
	color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public final void clearScreen(Color col)
  {
	clearScreen(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public final void enableVertexFlatColor(Color c, float intensity)
  {
	enableVertexFlatColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), intensity);
  }

  public final void setBlendFuncSrcAlpha()
  {
	_gl.blendFunc(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  }

  public final void getViewport(int[] v)
  {
	_gl.getIntegerv(GLVariable.viewport(), v);
  }

  public void dispose()
  {

//    if (_lastImageData != NULL) {
//      delete [] _lastImageData;
//      _lastImageData = NULL;
//    }

	if (_vertices != null)
	{
	  if (_vertices != null)
		  _vertices.dispose();
	}
	if (_textureCoordinates != null)
	{
	  if (_textureCoordinates != null)
		  _textureCoordinates.dispose();
	}
	if (_colors != null)
	{
	  if (_colors != null)
		  _colors.dispose();
	}

  }

}