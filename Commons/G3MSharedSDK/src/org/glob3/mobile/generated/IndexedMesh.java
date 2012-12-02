package org.glob3.mobile.generated; 
//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;

public class IndexedMesh extends AbstractMesh
{
  private IIntBuffer _indices;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc) const
  protected final void rawRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.drawElements(_primitive, _indices);
  }

  private GLState _glState;


  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth, Color flatColor, IFloatBuffer colors)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, flatColor, colors, (float)0.0);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth, Color flatColor)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, flatColor, null, (float)0.0);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(const int primitive, boolean owner, const Vector3D& center, IFloatBuffer* vertices, IIntBuffer* indices, float lineWidth, Color* flatColor = null, IFloatBuffer* colors = null, const float colorsIntensity = (float)0.0) : AbstractMesh(primitive, owner, center, vertices, lineWidth, flatColor, colors, colorsIntensity), _indices(indices)
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
	  super(primitive, owner, center, vertices, lineWidth, flatColor, colors, colorsIntensity);
	  _indices = indices;
<<<<<<< HEAD
	  _flatColor = flatColor;
	  _colors = colors;
	  _colorsIntensity = colorsIntensity;
	  _extent = null;
	  _center = new Vector3D(center);
	  _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(center));
	  _lineWidth = lineWidth;
	_glState = new GLState();
	_glState.enableVerticesPosition();
	if (_colors != null)
	  _glState.enableVertexColor(_colors, _colorsIntensity);
	if (_flatColor != null)
	{
	  _glState.enableFlatColor(_flatColor, _colorsIntensity);
	  if (_flatColor.isTransparent())
	  {
		_glState.enableBlend();
	  }
	}
=======
>>>>>>> origin/webgl-port
  }

  public void dispose()
  {
	if (_owner)
	{
	  if (_indices != null)
		  _indices.dispose();
<<<<<<< HEAD
	  if (_colors != null)
		  _colors.dispose();
	  _flatColor = null;
	}
  
	if (_glState != null)
		_glState.dispose();
	_extent = null;
	if (_translationMatrix != null)
		_translationMatrix.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void render(const G3MRenderContext* rc) const
  public void render(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
	gl.setState(_glState);
  
	//gl->enableVerticesPosition();
  
  /*  if (_colors == NULL) {
	  gl->disableVertexColor();
	}
	else {
	  gl->enableVertexColor(_colors, _colorsIntensity);
  <<<<<<< HEAD
	}*/
  
  /*  if (_flatColor == NULL) {
  =======
	}
  
	bool blend = false;
	if (_flatColor == NULL) {
  >>>>>>> origin/webgl-port
	  gl->disableVertexFlatColor();
	}
	else {
	  if (_flatColor->isTransparent()) {
		gl->enableBlend();
		gl->setBlendFuncSrcAlpha();
		blend = true;
	  }
	  gl->enableVertexFlatColor(*_flatColor, _colorsIntensity);
	}*/
  
  
	if (_flatColor != null)
	  if (_flatColor.isTransparent())
		gl.setBlendFuncSrcAlpha();
  
  
	gl.vertexPointer(3, 0, _vertices);
  
	gl.lineWidth(_lineWidth);
  
	if (_translationMatrix != null)
	{
	  gl.pushMatrix();
	  gl.multMatrixf(_translationMatrix);
	}
  
	if (_primitive == GLPrimitive.triangles())
	{
	  gl.drawTriangles(_indices);
	}
	else if (_primitive == GLPrimitive.triangleStrip())
	{
	  gl.drawTriangleStrip(_indices);
	}
	else if (_primitive == GLPrimitive.triangleFan())
	{
	  gl.drawTriangleFan(_indices);
	}
	else if (_primitive == GLPrimitive.lines())
	{
	  gl.drawLines(_indices);
	}
	else if (_primitive == GLPrimitive.lineStrip())
	{
	  gl.drawLineStrip(_indices);
	}
	else if (_primitive == GLPrimitive.lineLoop())
	{
	  gl.drawLineLoop(_indices);
	}
	else if (_primitive == GLPrimitive.points())
	{
	  gl.drawPoints(_indices);
	}
  
	if (_translationMatrix != null)
	{
	  gl.popMatrix();
	}
	//gl->disableVerticesPosition();
  
	/*if (blend) {
	  gl->disableBlend();
	}*/
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* getExtent() const
  public final Extent getExtent()
  {
	if (_extent == null)
	{
	  _extent = computeExtent();
	}
	return _extent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
  public final int getVertexCount()
  {
	return _vertices.size() / 3;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int i) const
  public final Vector3D getVertex(int i)
  {
	final int p = i * 3;
	return new Vector3D(_vertices.get(p) + _center._x, _vertices.get(p+1) + _center._y, _vertices.get(p+2) + _center._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GLState* getGLState() const
  public final GLState getGLState()
  {
	  return _glState;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
  public final boolean isTransparent(G3MRenderContext rc)
  {
	if (_flatColor == null)
	{
	  return false;
=======
>>>>>>> origin/webgl-port
	}
  }

}