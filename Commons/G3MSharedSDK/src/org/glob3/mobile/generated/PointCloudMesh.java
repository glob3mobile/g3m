package org.glob3.mobile.generated; 
//
//  PointCloudMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  PointCloudMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//




//class MutableMatrix44D;
//class IFloatBuffer;
//class Color;

public class PointCloudMesh extends Mesh
{
  protected final boolean _owner;
  protected final Vector3D _center ;
  protected final MutableMatrix44D _translationMatrix;
  protected final IFloatBuffer _vertices;
  protected final IFloatBuffer _colors;
  protected final float _pointSize;
  protected final boolean _depthTest;
  protected Color _borderColor ;

  protected BoundingVolume _boundingVolume;
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

  protected GLState _glState;

  protected final void createGLState()
  {
  
    _glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, _depthTest, false, 0, false, 0.0, 0.0, 1.0, true, _pointSize), false); //Polygon Offset - Cull and culled face - Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    _glState.addGLFeature(new PointShapeGLFeature(_borderColor), false);
  
    if (_translationMatrix != null)
    {
      _glState.addGLFeature(new ModelTransformGLFeature(_translationMatrix.asMatrix44D()), false);
    }
  
    if (_colors != null)
    {
      _glState.addGLFeature(new ColorGLFeature(_colors, 4, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false); // Stride 0 -  Not normalized -  Index 0 -  Our buffer contains elements of 4 -  The attribute is a float vector of 4 elements RGBA
  
    }
    else
    {
      throw new RuntimeException("PointCloudMesh without colors")
    }
  
  }

  protected boolean _showNormals;
  protected Mesh _normalsMesh;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Mesh createNormalsMesh();


  public PointCloudMesh(boolean owner, Vector3D center, IFloatBuffer vertices, float pointSize, IFloatBuffer colors, boolean depthTest, Color borderColor)
  {
     _owner = owner;
     _vertices = vertices;
     _colors = colors;
     _boundingVolume = null;
     _center = new Vector3D(center);
     _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(center));
     _pointSize = pointSize;
     _depthTest = depthTest;
     _glState = new GLState();
     _normalsMesh = null;
     _showNormals = false;
     _borderColor = new Color(borderColor);
    createGLState();
  }

  public void dispose()
  {
    if (_owner)
    {
      if (_vertices != null)
         _vertices.dispose();
      if (_colors != null)
         _colors.dispose();
    }
  
    if (_boundingVolume != null)
       _boundingVolume.dispose();
    if (_translationMatrix != null)
       _translationMatrix.dispose();
  
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

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return false;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
    _glState.setParent(parentGLState);
  
    GL gl = rc.getGL();
  
    gl.drawArrays(GLPrimitive.points(), 0, (int)_vertices.size() / 3, _glState, rc.getGPUProgramManager());
  }

  public final void showNormals(boolean v)
  {
    _showNormals = v;
  }

  public final IFloatBuffer getColorsFloatBuffer()
  {
    return (IFloatBuffer)_colors;
  }

}