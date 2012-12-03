package org.glob3.mobile.generated; 
//
//  AbstractMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  AbstractMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableMatrix44D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;

public abstract class AbstractMesh extends Mesh
{
  protected final int _primitive;
  protected final boolean _owner;
  protected Vector3D _center ;
  protected final MutableMatrix44D _translationMatrix;
  protected IFloatBuffer _vertices;
  protected Color _flatColor;
  protected IFloatBuffer _colors;
  protected final float _colorsIntensity;
  protected final float _lineWidth;

  protected Extent _extent;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* computeExtent() const
  protected final Extent computeExtent()
  {
  
	final int vertexCount = getVertexCount();
  
	if (vertexCount <= 0)
	{
	  return null;
	}
  
	double minx = 1e10;
	double miny = 1e10;
	double minz = 1e10;
	double maxx = -1e10;
	double maxy = -1e10;
	double maxz = -1e10;
  
	for (int i = 0; i < vertexCount; i++)
	{
	  final int p = i * 3;
  
	  final double x = _vertices.get(p) + _center._x;
	  final double y = _vertices.get(p+1) + _center._y;
	  final double z = _vertices.get(p+2) + _center._z;
  
	  if (x < minx)
		  minx = x;
	  if (x > maxx)
		  maxx = x;
  
	  if (y < miny)
		  miny = y;
	  if (y > maxy)
		  maxy = y;
  
	  if (z < minz)
		  minz = z;
	  if (z > maxz)
		  maxz = z;
	}
  
	return new Box(new Vector3D(minx, miny, minz), new Vector3D(maxx, maxy, maxz));
  }

//  GLState*          _glState;

  protected AbstractMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
	  _primitive = primitive;
	  _owner = owner;
	  _vertices = vertices;
	  _flatColor = flatColor;
	  _colors = colors;
	  _colorsIntensity = colorsIntensity;
	  _extent = null;
	  _center = new Vector3D(center);
	  _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(center));
	  _lineWidth = lineWidth;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void rawRender(const G3MRenderContext* rc, const GLState& parentState) const = 0;
  protected abstract void rawRender(G3MRenderContext rc, GLState parentState);

  public void dispose()
  {
	if (_owner)
	{
	  if (_vertices != null)
		  _vertices.dispose();
	  if (_colors != null)
		  _colors.dispose();
	  if (_flatColor != null)
		  _flatColor.dispose();
	}
  
	_extent = null;
	if (_translationMatrix != null)
		_translationMatrix.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const G3MRenderContext *rc, const GLState& parentState) const
  public final void render(G3MRenderContext rc, GLState parentState)
  {
	GL gl = rc.getGL();
  
	GLState state = new GLState(parentState);
	state.enableVerticesPosition();
	if (_colors != null)
	{
	  state.enableVertexColor(_colors, _colorsIntensity);
	}
	if (_flatColor != null)
	{
	  state.enableFlatColor(_flatColor, _colorsIntensity);
	  if (_flatColor.isTransparent())
	  {
		state.enableBlend();
		gl.setBlendFuncSrcAlpha();
	  }
	}
	else
	{
	  state.disableVertexColor();
	}
  
	/*
	 gl->enableVerticesPosition();
  
	 if (_colors == NULL) {
	 gl->disableVertexColor();
	 }
	 else {
	 gl->enableVertexColor(_colors, _colorsIntensity);
	 }
  
	 bool blend = false;
	 if (_flatColor == NULL) {
	 gl->disableVertexFlatColor();
	 }
	 else {
	 if (_flatColor->isTransparent()) {
	 gl->enableBlend();
	 gl->setBlendFuncSrcAlpha();
	 blend = true;
	 }
	 gl->enableVertexFlatColor(*_flatColor, _colorsIntensity);
	 }
	 */
  
	gl.vertexPointer(3, 0, _vertices);
  
	gl.lineWidth(_lineWidth);
  
	if (_translationMatrix != null)
	{
	  gl.pushMatrix();
	  gl.multMatrixf(_translationMatrix);
	}
  
	gl.setState(state);
	rawRender(rc, state);
  
  
	if (_translationMatrix != null)
	{
	  gl.popMatrix();
	}
  
	/*
	 if (blend) {
	 gl->disableBlend();
	 }
	
	 gl->disableVerticesPosition();*/
  
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
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
  public final boolean isTransparent(G3MRenderContext rc)
  {
	if (_flatColor == null)
	{
	  return false;
	}
	return _flatColor.isTransparent();
  }

}