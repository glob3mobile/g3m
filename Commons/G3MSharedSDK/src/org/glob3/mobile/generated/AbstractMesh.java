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





//class IFloatBuffer;
//class Color;
//class ModelTransformGLFeature;


public abstract class AbstractMesh extends Mesh
{
  private MutableMatrix44D _transformMatrix;
  private ModelTransformGLFeature _transformGLFeature;
  private MutableMatrix44D _userTransformMatrix;
  private MutableMatrix44D getTransformMatrix()
  {
    if (_transformMatrix == null)
    {
      _transformMatrix = createTransformMatrix();
    }
    return _transformMatrix;
  }
  private MutableMatrix44D createTransformMatrix()
  {
    if (_center.isNan() || _center.isZero())
    {
      return new MutableMatrix44D(_userTransformMatrix);
    }
  
    final MutableMatrix44D centerM = MutableMatrix44D.createTranslationMatrix(_center);
    if (_userTransformMatrix == null)
    {
      return new MutableMatrix44D(centerM);
    }
  
    MutableMatrix44D result = new MutableMatrix44D();
    result.copyValueOfMultiplication(centerM, _userTransformMatrix);
  
    return result;
  }

  private void createGLState()
  {
    _glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, _depthTest, false, 0, _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits, _lineWidth, true, _pointSize), false); // needsPointSize -  culledFace -  cullFace -  Stride 0 -  Not normalized -  Index 0 -  Our buffer contains elements of 3 -  The attribute is a float vector of 4 elements
  
    if (_normals != null)
    {
      _glState.addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0), false);
    }
  
    _glState.addGLFeature(_transformGLFeature, true);
  
    if ((_flatColor != null) && (_colors == null))
    {
      _glState.addGLFeature(new FlatColorGLFeature(_flatColor, _flatColor.isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
  
      return;
    }
  
    if (_colors != null)
    {
      _glState.addGLFeature(new ColorGLFeature(_colors, 4, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false); // Stride 0 -  Not normalized -  Index 0 -  Our buffer contains elements of 4 -  The attribute is a float vector of 4 elements RGBA
    }
  
  }

  private boolean _showNormals;
  private Mesh _normalsMesh;
  private Mesh createNormalsMesh()
  {
    DirectMesh verticesMesh = new DirectMesh(GLPrimitive.points(), false, _center, _vertices, 1.0f, 2.0f, new Color(Color.RED), null, false, null);
  
    FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    BoundingVolume volume = getBoundingVolume();
    Sphere sphere = volume.createSphere();
    double normalsSize = sphere.getRadius() / 100.0;
    if (sphere != null)
       sphere.dispose();
  
    final int size = _vertices.size();
    for (int i = 0; i < size; i+=3)
    {
      final Vector3D v = new Vector3D(_vertices.get(i), _vertices.get(i+1), _vertices.get(i+2));
      final Vector3D n = new Vector3D(_normals.get(i), _normals.get(i+1), _normals.get(i+2));
  
      final Vector3D v_n = v.add(n.normalized().times(normalsSize));
  
      fbb.add(v);
      fbb.add(v_n);
    }
  
    DirectMesh normalsMesh = new DirectMesh(GLPrimitive.lines(), true, _center, fbb.create(), 2.0f, 1.0f, new Color(Color.BLUE));
  
    if (fbb != null)
       fbb.dispose();
  
    CompositeMesh compositeMesh = new CompositeMesh();
    compositeMesh.addMesh(verticesMesh);
    compositeMesh.addMesh(normalsMesh);
  
    return compositeMesh;
  
  }

  protected final int _primitive;
  protected final boolean _owner;
  protected final Vector3D _center ;
  protected final IFloatBuffer _vertices;
  protected final Color _flatColor;
  protected final IFloatBuffer _colors;
  protected final float _lineWidth;
  protected final float _pointSize;
  protected final boolean _depthTest;
  protected final IFloatBuffer _normals;
  protected final boolean _polygonOffsetFill;
  protected final float _polygonOffsetFactor;
  protected final float _polygonOffsetUnits;

  protected GLState _glState;

  protected BoundingVolume _boundingVolume;
  protected final BoundingVolume computeBoundingVolume()
  {
    if (!_userTransformMatrix.isIdentity())
    {
      return null;
    }
  
    final int vertexCount = getVertexCount();
  
    if (vertexCount == 0)
    {
      return null;
    }
  
    final IMathUtils mu = IMathUtils.instance();
  
    double minX = mu.maxDouble();
    double minY = mu.maxDouble();
    double minZ = mu.maxDouble();
  
    double maxX = mu.minDouble();
    double maxY = mu.minDouble();
    double maxZ = mu.minDouble();
  
    for (int i = 0; i < vertexCount; i++)
    {
      final int i3 = i * 3;
  
      final double x = _vertices.get(i3) + _center._x;
      final double y = _vertices.get(i3 + 1) + _center._y;
      final double z = _vertices.get(i3 + 2) + _center._z;
  
      if (x < minX)
      {
         minX = x;
      }
      if (y < minY)
      {
         minY = y;
      }
      if (z < minZ)
      {
         minZ = z;
      }
  
      if (x > maxX)
      {
         maxX = x;
      }
      if (y > maxY)
      {
         maxY = y;
      }
      if (z > maxZ)
      {
         maxZ = z;
      }
    }
  
    return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
  }

  protected AbstractMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
     _primitive = primitive;
     _owner = owner;
     _vertices = vertices;
     _flatColor = flatColor;
     _colors = colors;
     _boundingVolume = null;
     _center = new Vector3D(center);
     _lineWidth = lineWidth;
     _pointSize = pointSize;
     _depthTest = depthTest;
     _glState = new GLState();
     _normals = normals;
     _normalsMesh = null;
     _showNormals = false;
     _polygonOffsetFactor = polygonOffsetFactor;
     _polygonOffsetUnits = polygonOffsetUnits;
     _polygonOffsetFill = polygonOffsetFill;
     _transformMatrix = null;
     _userTransformMatrix = MutableMatrix44D.newIdentity();
    _transformGLFeature = new ModelTransformGLFeature(getTransformMatrix().asMatrix44D());
  
    createGLState();
  }

  protected abstract void rawRender(G3MRenderContext rc);

  public void dispose()
  {
    if (_owner)
    {
      if (_vertices != null)
         _vertices.dispose();
      if (_colors != null)
         _colors.dispose();
      if (_normals != null)
         _normals.dispose();
    }
  
    if (_flatColor != null)
       _flatColor.dispose();
  
    if (_boundingVolume != null)
       _boundingVolume.dispose();
  
    if (_transformMatrix != null)
       _transformMatrix.dispose();
    if (_userTransformMatrix != null)
       _userTransformMatrix.dispose();
  
    _transformGLFeature._release();
  
    _glState._release();
  
    if (_normalsMesh != null)
       _normalsMesh.dispose();
  
    super.dispose();
  }

  public final BoundingVolume getBoundingVolume()
  {
    if (_boundingVolume == null)
    {
      _boundingVolume = computeBoundingVolume();
    }
    return _boundingVolume;
  }

  public final int getVertexCount()
  {
    return _vertices.size() / 3;
  }

  public final Vector3D getVertex(int i)
  {
    final int p = i * 3;
    return new Vector3D(_vertices.get(p) + _center._x, _vertices.get(p+1) + _center._y, _vertices.get(p+2) + _center._z);
  }

  public boolean isTransparent(G3MRenderContext rc)
  {
    if (_flatColor == null)
    {
      return false;
    }
    return _flatColor.isTransparent();
  }

  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
    _glState.setParent(parentGLState);
    rawRender(rc);
  
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

  public final void showNormals(boolean v)
  {
    _showNormals = v;
  }

  public final void setUserTransformMatrix(MutableMatrix44D userTransformMatrix)
  {
    if (userTransformMatrix == null)
    {
      throw new RuntimeException("userTransformMatrix is NULL");
    }
  
    if (userTransformMatrix != _userTransformMatrix)
    {
      if (_userTransformMatrix != null)
         _userTransformMatrix.dispose();
      _userTransformMatrix = userTransformMatrix;
    }
  
    if (_transformMatrix != null)
       _transformMatrix.dispose();
    _transformMatrix = null;
  
    if (_boundingVolume != null)
       _boundingVolume.dispose();
    _boundingVolume = null;
  
    _transformGLFeature.setMatrix(getTransformMatrix().asMatrix44D());
  }

}
