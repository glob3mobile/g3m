package org.glob3.mobile.generated;
//
//  AbstractMesh.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  AbstractMesh.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//




//class IFloatBuffer;
//class Color;
//class GeometryGLFeature;


public abstract class AbstractMesh extends TransformableMesh
{
  private float _pointSize;

  private GeometryGLFeature _geometryGLFeature;

  protected final int _primitive;
  protected final boolean _owner;
  protected final IFloatBuffer _vertices;
  protected final Color _flatColor;
  protected final IFloatBuffer _colors;
  protected final float _lineWidth;
  protected final boolean _depthTest;
  protected final IFloatBuffer _normals;
  protected final boolean _polygonOffsetFill;
  protected final float _polygonOffsetFactor;
  protected final float _polygonOffsetUnits;
  protected final boolean _cullFace;
  protected final int _culledFace;

  protected BoundingVolume _boundingVolume;
  protected final BoundingVolume computeBoundingVolume()
  {
    if (hasUserTransform())
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

  protected AbstractMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, boolean cullFace, int culledFace)
  {
     super(center);
     _primitive = primitive;
     _owner = owner;
     _vertices = vertices;
     _flatColor = flatColor;
     _colors = colors;
     _boundingVolume = null;
     _lineWidth = lineWidth;
     _pointSize = pointSize;
     _depthTest = depthTest;
     _normals = normals;
     _polygonOffsetFactor = polygonOffsetFactor;
     _polygonOffsetUnits = polygonOffsetUnits;
     _polygonOffsetFill = polygonOffsetFill;
     _cullFace = cullFace;
     _culledFace = culledFace;
     _geometryGLFeature = null;
  }

  protected abstract void renderMesh(G3MRenderContext rc, GLState glState);

  protected final void userTransformMatrixChanged()
  {
    if (_boundingVolume != null)
       _boundingVolume.dispose();
    _boundingVolume = null;
  }

  protected final void initializeGLState(GLState glState)
  {
    super.initializeGLState(glState);
  
    _geometryGLFeature = new GeometryGLFeature(_vertices, 3, 0, false, 0, _depthTest, _cullFace, _culledFace, _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits, _lineWidth, true, _pointSize); // needsPointSize -  Stride 0 -  Not normalized -  Index 0 -  Our buffer contains elements of 3 -  The attribute is a float vector of 4 elements
    glState.addGLFeature(_geometryGLFeature, true);
  
    if (_normals != null)
    {
      glState.addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0), false);
    }
  
    if ((_flatColor != null) && (_colors == null))
    {
      glState.addGLFeature(new FlatColorGLFeature(_flatColor, _flatColor.isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
  
      return;
    }
  
    if (_colors != null)
    {
      glState.addGLFeature(new ColorGLFeature(_colors, 4, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false); // Stride 0 -  Not normalized -  Index 0 -  Our buffer contains elements of 4 -  The attribute is a float vector of 4 elements RGBA
    }
  
  }

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
  
    if (_geometryGLFeature != null)
    {
      _geometryGLFeature._release();
    }
  
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

  public final Vector3D getVertex(int index)
  {
    final int p = index * 3;
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
    GLState glState = getGLState();
    glState.setParent(parentGLState);
    renderMesh(rc, glState);
  }

  public final void setPointSize(float pointSize)
  {
    if (_pointSize != pointSize)
    {
      _pointSize = pointSize;
  
      if (_geometryGLFeature != null)
      {
        _geometryGLFeature.setPointSize(_pointSize);
      }
    }
  }

}