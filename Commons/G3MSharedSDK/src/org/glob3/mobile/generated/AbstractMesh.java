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




//class MutableMatrix44D;
//class IFloatBuffer;
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
  protected final float _pointSize;
  protected final boolean _depthTest;

  protected BoundingVolume _boundingVolume;
  protected final BoundingVolume computeBoundingVolume()
  {
    final int vertexCount = getVertexCount();
  
    if (vertexCount <= 0)
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

  protected AbstractMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
  {
     _primitive = primitive;
     _owner = owner;
     _vertices = vertices;
     _flatColor = flatColor;
     _colors = colors;
     _colorsIntensity = colorsIntensity;
     _boundingVolume = null;
     _center = new Vector3D(center);
     _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(center));
     _lineWidth = lineWidth;
     _pointSize = pointSize;
     _depthTest = depthTest;
    createGLState();
  }

  protected abstract void rawRender(G3MRenderContext rc);
//  virtual void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const = 0;

  protected GLState _glState = new GLState();

  protected final void createGLState()
  {
  
    _glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, true, false, 0, false, (float)0.0, (float)0.0, _lineWidth, true, _pointSize), false); //POINT SIZE - Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    if (_translationMatrix != null)
    {
      _glState.addGLFeature(new ModelTransformGLFeature(_translationMatrix.asMatrix44D()), false);
    }
  
    if (_flatColor != null && _colors == null) //FlatColorMesh Shader
    {
  
      _glState.addGLFeature(new FlatColorGLFeature(_flatColor, _flatColor.isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
  
  
  
  
      return;
    }
  
    if (_colors != null)
    {
      _glState.addGLFeature(new ColorGLFeature(_colors, 4, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 4 - The attribute is a float vector of 4 elements RGBA
  
    }
  }


  ///#include "GPUProgramState.hpp"
  
  
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
  
    if (_boundingVolume != null)
       _boundingVolume.dispose();
    if (_translationMatrix != null)
       _translationMatrix.dispose();
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

  public final boolean isTransparent(G3MRenderContext rc)
  {
    if (_flatColor == null)
    {
      return false;
    }
    return _flatColor.isTransparent();
  }

  public final void render(G3MRenderContext rc, GLState parentGLState)
  {
  
    _glState.setParent(parentGLState);
    rawRender(rc);
  }

}