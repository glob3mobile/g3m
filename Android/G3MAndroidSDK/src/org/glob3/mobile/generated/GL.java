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

  private java.util.LinkedList<Integer> _texturesIdBag = new java.util.LinkedList<Integer>();
  private int _texturesIdCounter;

  // state handling
  private boolean _enableTextures;
  private boolean _enableTexture2D;
  private boolean _enableVertexColor;
  private boolean _enableVertexNormal;
  private boolean _enableVerticesPosition;
  private boolean _enableFlatColor;
  private boolean _enableDepthTest;
  private boolean _enableBlend;

  private boolean _enableCullFace;

  private GLCullFace _cullFace_face = GLCullFace.Back;



//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private float[] loadModelView_M = new float[16];
  private void loadModelView()
  {
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//	static float M[16];
	_modelView.copyToFloatMatrix(loadModelView_M);
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Modelview, 1, 0, loadModelView_M);
  }

  private int getTextureID()
  {
	if (_texturesIdBag.size() == 0)
	{
	  final int bugdetSize = 256;
  
	  System.out.printf("= Creating %d texturesIds...\n", bugdetSize);
  
	  final java.util.ArrayList<Integer> ids = _gl.genTextures(bugdetSize);
  
	  for (int i = 0; i < bugdetSize; i++)
	  {
		_texturesIdBag.addLast(ids.get(i));
	  }
  
	  _texturesIdCounter += bugdetSize;
	  System.out.printf("= Created %d texturesIds (accumulated %ld).\n", bugdetSize, _texturesIdCounter);
	}
  
	int result = _texturesIdBag.getLast();
	_texturesIdBag.removeLast();
  
  //  printf("   - Assigning 1 texturesId from bag (bag size=%ld).\n", _texturesIdBag.size());
  
	return result;
  }


  public GL(INativeGL gl)
  {
	  _gl = gl;
	  _enableTextures = false;
	  _enableTexture2D = false;
	  _enableVertexColor = false;
	  _enableVertexNormal = false;
	  _enableVerticesPosition = false;
	  _enableFlatColor = false;
	  _enableBlend = false;
	  _enableDepthTest = false;
	  _enableCullFace = false;
	  _cullFace_face = Back;
	  _texturesIdCounter = 0;

  }

  public void dispose()
  {
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
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, true);
	  _enableTexture2D = true;
	}
  }

  public final void enableVertexFlatColor(float r, float g, float b, float a, float intensity)
  {
	if (!_enableFlatColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, true);
	  _enableFlatColor = true;
	}
	_gl.uniform4f(GlobalMembersGL.Uniforms.FlatColor, r, g, b, a);
	_gl.uniform1f(GlobalMembersGL.Uniforms.FlatColorIntensity, intensity);
  }

  public final void disableVertexFlatColor()
  {
	if (_enableFlatColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, false);
	  _enableFlatColor = false;
	}
  }

  public final void disableTexture2D()
  {
	if (_enableTexture2D)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, false);
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
	GLBufferType[] buffers = { ColorBuffer, DepthBuffer };
	_gl.clear(2, buffers);
  }

  public final void color(float r, float g, float b, float a)
  {
	_gl.uniform4f(GlobalMembersGL.Uniforms.FlatColor, r, g, b, a);
  }

  public final void enableVertexColor(float[] colors, float intensity)
  {
	if (!_enableVertexColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, true);
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.Color);
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Color, 4, Float, 0, 0, colors);
	  _gl.uniform1f(GlobalMembersGL.Uniforms.ColorPerVertexIntensity, intensity);
	  _enableVertexColor = true;
	}
  }

  public final void disableVertexColor()
  {
	if (_enableVertexColor)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.Color);
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, false);
	  _enableVertexColor = false;
	}
  }

  public final void enableVertexNormal(float[] normals)
  {
	//  if (normals != NULL) {
	if (!_enableVertexNormal)
	{
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.Normal);
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Normal, 3, Float, 0, 0, normals);
	  _enableVertexNormal = true;
	}
	//  }
  }

  public final void disableVertexNormal()
  {
	if (_enableVertexNormal)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.Normal);
	  _enableVertexNormal = false;
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

  public final void vertexPointer(int size, int stride, float[] vertex)
  {
	_gl.vertexAttribPointer(GlobalMembersGL.Attributes.Position, size, Float, 0, stride, (Object) vertex);
  }

  public final void drawTriangleStrip(int n, int[] i)
  {
	_gl.drawElements(TriangleStrip, n, UnsignedInt, i);
  }

  public final void drawLines(int n, int[] i)
  {
	_gl.drawElements(Lines, n, UnsignedInt, i);
  }

  public final void drawLineLoop(int n, int[] i)
  {
	_gl.drawElements(LineLoop, n, UnsignedInt, i);
  }

  public final void drawPoints(int n, int[] i)
  {
	_gl.drawElements(Points, n, UnsignedInt, i);
  }

//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private float[] setProjection_M = new float[16];
  public final void setProjection(MutableMatrix44D projection)
  {
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//	static float M[16];
	projection.copyToFloatMatrix(setProjection_M);
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Projection, 1, 0, setProjection_M);
  }

  public final void useProgram(int program)
  {
	// set shaders
	_gl.useProgram(program);
  
	// Extract the handles to attributes
	GlobalMembersGL.Attributes.Position = _gl.getAttribLocation(program, "Position");
	GlobalMembersGL.Attributes.TextureCoord = _gl.getAttribLocation(program, "TextureCoord");
	GlobalMembersGL.Attributes.Color = _gl.getAttribLocation(program, "Color");
	GlobalMembersGL.Attributes.Normal = _gl.getAttribLocation(program, "Normal");
  
	// Extract the handles to uniforms
	GlobalMembersGL.Uniforms.Projection = _gl.getUniformLocation(program, "Projection");
	GlobalMembersGL.Uniforms.Modelview = _gl.getUniformLocation(program, "Modelview");
	GlobalMembersGL.Uniforms.Sampler = _gl.getUniformLocation(program, "Sampler");
	GlobalMembersGL.Uniforms.EnableTexture = _gl.getUniformLocation(program, "EnableTexture");
	GlobalMembersGL.Uniforms.FlatColor = _gl.getUniformLocation(program, "FlatColor");
	GlobalMembersGL.Uniforms.TranslationTexCoord = _gl.getUniformLocation(program, "TranslationTexCoord");
	GlobalMembersGL.Uniforms.ScaleTexCoord = _gl.getUniformLocation(program, "ScaleTexCoord");
	GlobalMembersGL.Uniforms.PointSize = _gl.getUniformLocation(program, "PointSize");
  
	// default values
	_gl.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, 0, 0);
	_gl.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, 1, 1);
	_gl.uniform1f(GlobalMembersGL.Uniforms.PointSize, (float) 1.0);
  
	//BILLBOARDS
	GlobalMembersGL.Uniforms.BillBoard = _gl.getUniformLocation(program, "BillBoard");
	GlobalMembersGL.Uniforms.ViewPortRatio = _gl.getUniformLocation(program, "ViewPortRatio");
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, false); //NOT DRAWING BILLBOARD
  
	//FOR FLAT COLOR MIXING
	GlobalMembersGL.Uniforms.FlatColorIntensity = _gl.getUniformLocation(program, "FlatColorIntensity");
	GlobalMembersGL.Uniforms.ColorPerVertexIntensity = _gl.getUniformLocation(program, "ColorPerVertexIntensity");
	GlobalMembersGL.Uniforms.EnableColorPerVertex = _gl.getUniformLocation(program, "EnableColorPerVertex");
	GlobalMembersGL.Uniforms.EnableFlatColor = _gl.getUniformLocation(program, "EnableFlatColor");
  }

  public final void enablePolygonOffset(float factor, float units)
  {
	_gl.enable(PolygonOffsetFill);
	_gl.polygonOffset(factor, units);
  }

  public final void disablePolygonOffset()
  {
	_gl.disable(PolygonOffsetFill);
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

  public final int uploadTexture(IImage image, int textureWidth, int textureHeight)
  {
  
	byte[] imageData = new byte[textureWidth * textureHeight * 4];
	image.fillWithRGBA(imageData, textureWidth, textureHeight);
  
	_gl.blendFunc(SrcAlpha, OneMinusSrcAlpha);
	_gl.pixelStorei(Unpack, 1);
  
	int texID = getTextureID();
  
	_gl.bindTexture(Texture2D, texID);
	_gl.texParameteri(Texture2D, MinFilter, Linear);
	_gl.texParameteri(Texture2D, MagFilter, Linear);
	_gl.texParameteri(Texture2D, WrapS, ClampToEdge);
	_gl.texParameteri(Texture2D, WrapT, ClampToEdge);
	_gl.texImage2D(Texture2D, 0, RGBA, textureWidth, textureHeight, 0, RGBA, UnsignedByte, imageData);
  
	return texID;
  }

  public final void setTextureCoordinates(int size, int stride, float[] texcoord)
  {
	_gl.vertexAttribPointer(GlobalMembersGL.Attributes.TextureCoord, size, Float, 0, stride, (Object) texcoord);
  }

  public final void bindTexture(int n)
  {
	_gl.bindTexture(Texture2D, n);
  }

  public final void enableDepthTest()
  {
	if (!_enableDepthTest)
	{
	  _gl.enable(DepthTest);
	  _enableDepthTest = true;
	}
  }
  public final void disableDepthTest()
  {
	if (_enableDepthTest)
	{
	  _gl.disable(DepthTest);
	  _enableDepthTest = false;
	}
  }

  public final void enableBlend()
  {
	if (!_enableBlend)
	{
	  _gl.enable(Blend);
	  _enableBlend = true;
	}
  }
  public final void disableBlend()
  {
	if (_enableBlend)
	{
	  _gl.disable(Blend);
	  _enableBlend = false;
	}
  
  }

  public final void drawBillBoard(int textureId, Vector3D pos, float viewPortRatio)
  {
	float[] vertex = { (float) pos.x(), (float) pos.y(), (float) pos.z(), (float) pos.x(), (float) pos.y(), (float) pos.z(), (float) pos.x(), (float) pos.y(), (float) pos.z(), (float) pos.x(), (float) pos.y(), (float) pos.z() };
  
	float[] texcoord = { 1, 1, 1, 0, 0, 1, 0, 0 };
  
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, true);
  
	_gl.uniform1f(GlobalMembersGL.Uniforms.ViewPortRatio, viewPortRatio);
  
	disableDepthTest();
  
	enableTexture2D();
	color(1, 1, 1, 1);
  
	bindTexture(textureId);
  
	vertexPointer(3, 0, vertex);
	setTextureCoordinates(2, 0, texcoord);
  
	_gl.drawArrays(TriangleStrip, 0, 4);
  
	enableDepthTest();
  
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, false);
  }

  public final void deleteTexture(int glTextureId)
  {
	int[] textures = { glTextureId };
	_gl.deleteTextures(1, textures);
  
	_texturesIdBag.addLast(glTextureId);
  
  //  printf("   - Delete 1 texturesId (bag size=%ld).\n", _texturesIdBag.size());
  }

  public final void enableCullFace(GLCullFace face)
  {
	if (!_enableCullFace)
	{
	  _gl.enable(CullFacing);
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
	  _gl.disable(CullFacing);
	  _enableCullFace = false;
	}
  }

  public final void transformTexCoords(Vector2D scale, Vector2D translation)
  {
	_gl.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, (float) scale.x(), (float) scale.y());
	_gl.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, (float) translation.x(), (float) translation.y());
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

}