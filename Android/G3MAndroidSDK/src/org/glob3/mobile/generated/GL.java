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







public class GL
{

  private final INativeGL _gl;

  private MutableMatrix44D _modelView = new MutableMatrix44D();

  // stack of ModelView matrices
  private java.util.LinkedList<MutableMatrix44D> _matrixStack = new java.util.LinkedList<MutableMatrix44D>();

  private java.util.LinkedList<GLTextureId> _texturesIdBag = new java.util.LinkedList<GLTextureId>();
  private int _texturesIdAllocationCounter;
  private int _texturesIdGetCounter;
  private int _texturesIdTakeCounter;

  // state handling
  private boolean _enableTextures;
  private boolean _enableTexture2D;
//  bool _enableVertexColor;
  private boolean _enableVerticesPosition;
  private boolean _enableFlatColor;
  private boolean _enableDepthTest;
  private boolean _enableBlend;

  private boolean _enableCullFace;

  GLCullFace _cullFace_face = GLCullFace.Back;



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

//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private float[] loadModelView_M = new float[16];
  private void loadModelView()
  {
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//	static float M[16];
	_modelView.copyToColumnMajorFloatArray(loadModelView_M);
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Modelview, 1, false, loadModelView_M);
  }

  private GLTextureId getGLTextureId()
  {
	if (_texturesIdBag.size() == 0)
	{
	  final int bugdetSize = 256;
  
	  System.out.printf("= Creating %d texturesIds...\n", bugdetSize);
  
	  final java.util.ArrayList<GLTextureId> ids = _gl.genTextures(bugdetSize);
  
	  for (int i = 0; i < bugdetSize; i++)
	  {
		//      _texturesIdBag.push_back(ids[i]);
		_texturesIdBag.addFirst(ids.get(i));
	  }
  
	  _texturesIdAllocationCounter += bugdetSize;
  
	}
  
	_texturesIdGetCounter++;
  
	final GLTextureId result = _texturesIdBag.getLast();
	_texturesIdBag.removeLast();
  
	//  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
	//         result.getGLTextureId(),
	//         _texturesIdBag.size(),
	//         _texturesIdGetCounter,
	//         _texturesIdTakeCounter,
	//         _texturesIdGetCounter - _texturesIdTakeCounter);
  
	return result;
  }


  private int _lastTextureWidth;
  private int _lastTextureHeight;
  byte[] _lastImageData;

  //Get Locations warning of errors
  private boolean _errorGettingLocationOcurred;
  private int checkedGetAttribLocation(int program, String name)
  {
	int l = _gl.getAttribLocation(program, name);
	if (l == -1)
	{
	  ILogger.instance().logError("Error fetching Attribute, Program = %d, Variable = %s", program, name);
	  _errorGettingLocationOcurred = true;
	}
	return l;
  }
  private int checkedGetUniformLocation(int program, String name)
  {
	int l = _gl.getUniformLocation(program, name);
	if (l == -1)
	{
	  ILogger.instance().logError("Error fetching Uniform, Program = %d, Variable = %s", program, name);
	  _errorGettingLocationOcurred = true;
	}
	return l;
  }

  private IFloatBuffer _billboardTexCoord;
  private IFloatBuffer getBillboardTexCoord()
  {
  
	if (_billboardTexCoord == null)
	{
	  //  const static float texcoord[] = {
	  //    1, 1,
	  //    1, 0,
	  //    0, 1,
	  //    0, 0
	  //  };
  
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
//  _enableVertexColor(false),
//  _enableFlatColor(false),
  {
	  _gl = gl;
	  _enableTextures = false;
	  _enableTexture2D = false;
	  _enableVerticesPosition = false;
	  _enableBlend = false;
	  _enableDepthTest = false;
	  _enableCullFace = false;
	  _cullFace_face = GLCullFace.Back;
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
	  _lastTextureWidth = -1;
	  _lastTextureHeight = -1;
	  _lastImageData = null;
	  _billboardTexCoord = null;
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
	GLBufferType[] buffers = { GLBufferType.ColorBuffer, GLBufferType.DepthBuffer };
	_gl.clear(2, buffers);
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
  
	//  _gl->uniform4f(Uniforms.FlatColor, r, g, b, a);
  }

  public final void enableVertexColor(IFloatBuffer colors, float intensity)
  {
  
	//if (!_enableVertexColor) {
	_gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 1);
	_gl.enableVertexAttribArray(GlobalMembersGL.Attributes.Color);
  
	int __TODO_cache_buffer;
	if ((_colors != colors) || (_colors.timestamp() != colors.timestamp()))
	{
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Color, 4, false, 0, colors);
	  _colors = colors;
	}
  
	_gl.uniform1f(GlobalMembersGL.Uniforms.ColorPerVertexIntensity, intensity);
	//_enableVertexColor = true;
	//}
  }

  public final void disableVertexColor()
  {
	//  if (_enableVertexColor) {
	_gl.disableVertexAttribArray(GlobalMembersGL.Attributes.Color);
	_gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 0);
	//    _enableVertexColor = false;
	//  }
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
	int __TODO_text_cache_buffer;
  
	if ((_vertices != vertices) || (_vertices.timestamp() != vertices.timestamp()))
	{
  
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Position, size, false, stride, vertices);
	  _vertices = vertices;
	}
  }

  public final void drawTriangleStrip(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.TriangleStrip, indices.size(), indices);
  }

  public final void drawLines(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.Lines, indices.size(), indices);
  }

  public final void drawLineLoop(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.LineLoop, indices.size(), indices);
  }

  public final void drawPoints(IIntBuffer indices)
  {
	_gl.drawElements(GLPrimitive.Points, indices.size(), indices);
  }

//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private float[] setProjection_M = new float[16];
  public final void setProjection(MutableMatrix44D projection)
  {
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//	static float M[16];
	projection.copyToColumnMajorFloatArray(setProjection_M);
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Projection, 1, false, setProjection_M);
  }

  public final boolean useProgram(int program)
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
	_gl.enable(GLFeature.PolygonOffsetFill);
	_gl.polygonOffset(factor, units);
  }

  public final void disablePolygonOffset()
  {
	_gl.disable(GLFeature.PolygonOffsetFill);
  }

  public final void lineWidth(float width)
  {
	_gl.lineWidth(width);
  }

  public final void pointSize(float size)
  {
	_gl.uniform1f(GlobalMembersGL.Uniforms.PointSize, size);
  }

  public final GLError getError()
  {
	return _gl.getError();
  }

  public final GLTextureId uploadTexture(IImage image, int textureWidth, int textureHeight, boolean generateMipmap)
  {
	final GLTextureId texId = getGLTextureId();
	if (texId.isValid())
	{
	  final boolean lastImageDataIsValid = ((_lastTextureWidth == textureWidth) && (_lastTextureHeight == textureHeight) && (_lastImageData != null));
  
  
  	byte[] imageData;
  
  	if (lastImageDataIsValid) {
  	  imageData = _lastImageData;
  	}
  	else {
  	  imageData = new byte[textureWidth * textureHeight * 4];
  	  _lastImageData = imageData;
  	  _lastTextureWidth = textureWidth;
  	  _lastTextureHeight = textureHeight;
  	}
  
  	image.fillWithRGBA8888(imageData, textureWidth, textureHeight);
  
  	_gl.blendFunc(GLBlendFactor.SrcAlpha, GLBlendFactor.OneMinusSrcAlpha);
  	_gl.pixelStorei(GLAlignment.Unpack, 1);
  
  	_gl.bindTexture(GLTextureType.Texture2D, texId.getGLTextureId());
  	_gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MinFilter, GLTextureParameterValue.Linear);
  	_gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MagFilter, GLTextureParameterValue.Linear);
  	_gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapS, GLTextureParameterValue.ClampToEdge);
  	_gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapT, GLTextureParameterValue.ClampToEdge);
  	_gl.texImage2D(GLTextureType.Texture2D, 0, GLFormat.RGBA, textureWidth, textureHeight, 0, GLFormat.RGBA, GLType.UnsignedByte, imageData);
  
  	if (generateMipmap) {
  	  _gl.generateMipmap(GLTextureType.Texture2D);
  	}
  
	}
	else
	{
	  System.out.print("can't get a valid texture id\n");
	}
  
	return texId;
  }

  public final void setTextureCoordinates(int size, int stride, IFloatBuffer texcoord)
  {
	int __TODO_cache_buffer;
  
	if ((_textureCoordinates != texcoord) || (_textureCoordinates.timestamp() != texcoord.timestamp()))
	{
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.TextureCoord, size, false, stride, texcoord);
	  _textureCoordinates = texcoord;
	}
  }

  public final void bindTexture(GLTextureId textureId)
  {
	_gl.bindTexture(GLTextureType.Texture2D, textureId.getGLTextureId());
  }

  public final void enableDepthTest()
  {
	if (!_enableDepthTest)
	{
	  _gl.enable(GLFeature.DepthTest);
	  _enableDepthTest = true;
	}
  }
  public final void disableDepthTest()
  {
	if (_enableDepthTest)
	{
	  _gl.disable(GLFeature.DepthTest);
	  _enableDepthTest = false;
	}
  }

  public final void enableBlend()
  {
	if (!_enableBlend)
	{
	  _gl.enable(GLFeature.Blend);
	  _enableBlend = true;
	}
  }
  public final void disableBlend()
  {
	if (_enableBlend)
	{
	  _gl.disable(GLFeature.Blend);
	  _enableBlend = false;
	}
  
  }

  public final void drawBillBoard(GLTextureId textureId, IFloatBuffer vertices, float viewPortRatio)
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
  
	_gl.drawArrays(GLPrimitive.TriangleStrip, 0, vertices.size() / 3);
  
	enableDepthTest();
  
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0);
  }

  public final void deleteTexture(GLTextureId textureId)
  {
	if (!textureId.isValid())
	{
	  return;
	}
	int[] textures = { textureId.getGLTextureId() };
	_gl.deleteTextures(1, textures);
  
	_texturesIdBag.addLast(textureId);
  
	_texturesIdTakeCounter++;
  }

  public final void enableCullFace(GLCullFace face)
  {
	if (!_enableCullFace)
	{
	  _gl.enable(GLFeature.CullFacing);
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
	  _gl.disable(GLFeature.CullFacing);
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
	_gl.blendFunc(GLBlendFactor.SrcAlpha, GLBlendFactor.OneMinusSrcAlpha);
  }

  public final void getViewport(int[] v)
  {
	_gl.getIntegerv(GLVariable.Viewport, v);
  }

  public void dispose()
  {

	if (_lastImageData != null)
	{
	  _lastImageData = null;
	  _lastImageData = null;
	}

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