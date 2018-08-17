package org.glob3.mobile.generated;//
//  AbstractGeometryMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

//
//  AbstractGeometryMesh.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableMatrix44D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;

public abstract class AbstractGeometryMesh extends Mesh
{

  protected final int _primitive;
  protected final Vector3D _center = new Vector3D();
  protected final MutableMatrix44D _translationMatrix;
  protected final IFloatBuffer _vertices;
  protected final boolean _ownsVertices;
  protected final IFloatBuffer _normals;
  protected final boolean _ownsNormals;
  protected final float _lineWidth;
  protected final float _pointSize;
  protected final boolean _depthTest;

  protected final boolean _polygonOffsetFill;
  protected final float _polygonOffsetFactor;
  protected final float _polygonOffsetUnits;

  protected BoundingVolume _extent;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* computeBoundingVolume() const
  protected final BoundingVolume computeBoundingVolume()
  {
	final int vertexCount = getVertexCount();
  
	if (vertexCount == 0)
	{
	  return null;
	}
  
	double minX = 1e12;
	double minY = 1e12;
	double minZ = 1e12;
  
	double maxX = -1e12;
	double maxY = -1e12;
	double maxZ = -1e12;
  
	for (int i = 0; i < vertexCount; i++)
	{
	  final int i3 = i * 3;
  
	  final double x = _vertices.get(i3) + _center._x;
	  final double y = _vertices.get(i3 + 1) + _center._y;
	  final double z = _vertices.get(i3 + 2) + _center._z;
  
	  if (x < minX)
		  minX = x;
	  if (x > maxX)
		  maxX = x;
  
	  if (y < minY)
		  minY = y;
	  if (y > maxY)
		  maxY = y;
  
	  if (z < minZ)
		  minZ = z;
	  if (z > maxZ)
		  maxZ = z;
	}
  
	return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
  }

  protected AbstractGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
	  _primitive = primitive;
	  _vertices = vertices;
	  _ownsVertices = ownsVertices;
	  _normals = normals;
	  _ownsNormals = ownsNormals;
	  _extent = null;
	  _center = new Vector3D(center);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center));
	  _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(new Vector3D(center)));
	  _lineWidth = lineWidth;
	  _pointSize = pointSize;
	  _depthTest = depthTest;
	  _glState = new GLState();
	  _normalsMesh = null;
	  _showNormals = false;
	  _polygonOffsetFactor = polygonOffsetFactor;
	  _polygonOffsetUnits = polygonOffsetUnits;
	  _polygonOffsetFill = polygonOffsetFill;
	createGLState();
  }

  protected GLState _glState;

  protected final void createGLState()
  {
	_glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, true, false, 0, _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits, _lineWidth, true, _pointSize), false); //Polygon Offset - Cull and culled face - Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
	if (_normals != null)
	{
	  _glState.addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0), false);
	}
  
	if (_translationMatrix != null)
	{
	  _glState.addGLFeature(new ModelTransformGLFeature(_translationMatrix.asMatrix44D()), false);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void rawRender(const G3MRenderContext* rc) const = 0;
  protected abstract void rawRender(G3MRenderContext rc);

  protected boolean _showNormals;
  protected Mesh _normalsMesh;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createNormalsMesh() const
  protected final Mesh createNormalsMesh()
  {
  
	DirectMesh verticesMesh = new DirectMesh(GLPrimitive.points(), false, _center, _vertices, (float)1.0, (float)2.0, new Color(Color.red()), null, (float)1.0, false, null);
  
	FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
	BoundingVolume volume = getBoundingVolume();
	Sphere sphere = volume.createSphere();
	double normalsSize = sphere.getRadius() / 10.0;
	if (sphere != null)
		sphere.dispose();
  
	final int size = _vertices.size();
  
  ///#warning FOR TILES NOT TAKING ALL VERTICES [Apparently there's not enough graphical memory]
  
	for (int i = 0; i < size; i+=6)
	{
	  Vector3D v = new Vector3D(_vertices.get(i), _vertices.get(i+1), _vertices.get(i+2));
	  Vector3D n = new Vector3D(_normals.get(i), _normals.get(i+1), _normals.get(i+2));
  
	  Vector3D v_n = v.add(n.normalized().times(normalsSize));
  
	  fbb.add(v);
	  fbb.add(v_n);
	}
  
	IFloatBuffer normalsVer = fbb.create();
	if (fbb != null)
		fbb.dispose();
  
  
  
	DirectMesh normalsMesh = new DirectMesh(GLPrimitive.lines(), true, _center, normalsVer, (float)2.0, (float)1.0, new Color(Color.blue()));
  
	CompositeMesh compositeMesh = new CompositeMesh();
	compositeMesh.addMesh(verticesMesh);
	compositeMesh.addMesh(normalsMesh);
  
	return normalsMesh;
  
  }

  public void dispose()
  {
	if (_ownsVertices)
	{
	  if (_vertices != null)
		  _vertices.dispose();
	}
	if (_ownsNormals)
	{
	  if (_normals != null)
		  _normals.dispose();
	}
  
	if (_extent != null)
		_extent.dispose();
	if (_translationMatrix != null)
		_translationMatrix.dispose();
  
	_glState._release();
  
	if (_normalsMesh != null)
		_normalsMesh.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* getBoundingVolume() const
  public final BoundingVolume getBoundingVolume()
  {
	if (_extent == null)
	{
	  _extent = computeBoundingVolume();
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
	return false; //TODO: CHECK
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const
  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
	_glState.setParent(parentGLState);
	rawRender(rc);
  
	//RENDERING NORMALS
	if (_normals != null)
	{
	  if (_showNormals)
	  {
		if (_normalsMesh == null)
		{
		  _normalsMesh = createNormalsMesh();
		}
		if (_normalsMesh != null)
		{
		  _normalsMesh.render(rc, parentGLState);
		}
	  }
	  else
	  {
		if (_normalsMesh != null)
		{
		  if (_normalsMesh != null)
			  _normalsMesh.dispose();
		  _normalsMesh = null;
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
  public final void showNormals(boolean v)
  {
	_showNormals = v;
  }

}
