package org.glob3.mobile.generated; 
//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGGeometryNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




//class IFloatBuffer;
//class IShortBuffer;
//class GPUProgramState;

public class SGGeometryNode extends SGNode
{
  private final int _primitive;
  private IFloatBuffer _vertices;
  private IFloatBuffer _colors;
  private IFloatBuffer _uv;
  private IFloatBuffer _normals;
  private IShortBuffer _indices;

  private GLState _glState;
  private void createGLState()
  {
  
    _glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, true, false, 0, false, (float)0.0, (float)0.0, (float)1.0, true, (float)1.0), false); //Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    if (_normals != null)
    {
  
      //    _glState->addGLFeature(new DirectionLightGLFeature(Vector3D(1, 0,0),  Color::yellow(),
      //                                                      (float)0.0), false);
  
      _glState.addGLFeature(new VertexNormalGLFeature(_normals,3,0,false,0), false);
  
  
    }
  
    if (_uv != null)
    {
      _glState.addGLFeature(new TextureCoordsGLFeature(_uv, 2, 0, false, 0, false, Vector2F.zero(), Vector2F.zero()), false);
    }
  }


  public SGGeometryNode(String id, String sId, int primitive, IFloatBuffer vertices, IFloatBuffer colors, IFloatBuffer uv, IFloatBuffer normals, IShortBuffer indices)
  {
     super(id, sId);
     _primitive = primitive;
     _vertices = vertices;
     _colors = colors;
     _uv = uv;
     _normals = normals;
     _indices = indices;
     _glState = new GLState();
    createGLState();
  }

  public void dispose()
  {
    if (_vertices != null)
       _vertices.dispose();
    if (_colors != null)
       _colors.dispose();
    if (_uv != null)
       _uv.dispose();
    if (_normals != null)
       _normals.dispose();
    if (_indices != null)
       _indices.dispose();
  
    _glState._release();
  
    super.dispose();
  
  }

  public final void rawRender(G3MRenderContext rc, GLState glState)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, glState, rc.getGPUProgramManager());
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    _glState.setParent(parentState);
    return _glState;
  }

  public final String description()
  {
    return "SGGeometryNode";
  }

  public final Vector3D mostDistantVertexFromCenter(MutableMatrix44D transformation)
  {
    double max = 0;
    MutableVector3D res = new MutableVector3D(0, 0, 0);
    for (int i = 0; i < _vertices.size(); i+=3)
    {
      Vector3D v = new Vector3D(_vertices.get(i), _vertices.get(i+1), _vertices.get(i+2));
  
      Vector3D v2 = v.transformedBy(transformation, 1.0);
      double d = v2.squaredLength();
      if (max < d)
      {
        max = d;
        res.copyFrom(v2);
      }
    }
  
    Vector3D res2 = super.mostDistantVertexFromCenter(transformation);
  
    return (max > res2.squaredLength())? res.asVector3D() : res2;
  }

  public final Vector3D getMax(MutableMatrix44D transformation)
  {
  
    double maxX = -9e99;
    double maxY = -9e99;
    double maxZ = -9e99;
  
    for (int i = 0; i < _vertices.size(); i+=3)
    {
      Vector3D v = new Vector3D(_vertices.get(i), _vertices.get(i+1), _vertices.get(i+2));
  
      Vector3D v2 = v.transformedBy(transformation, 1.0);
      if (v2._x > maxX)
      {
        maxX = v2._x;
      }
  
      if (v2._y > maxY)
      {
        maxY = v2._y;
      }
  
      if (v2._z > maxZ)
      {
        maxZ = v2._z;
      }
    }
  
    Vector3D max = new Vector3D(maxX, maxY, maxZ);
  
    return Vector3D.maxOnAllAxis(max, super.getMax(transformation));
  }

  public final Vector3D getMin(MutableMatrix44D transformation)
  {
  
    double minX = 9e99;
    double minY = 9e99;
    double minZ = 9e99;
  
    for (int i = 0; i < _vertices.size(); i+=3)
    {
      Vector3D v = new Vector3D(_vertices.get(i), _vertices.get(i+1), _vertices.get(i+2));
  
      Vector3D v2 = v.transformedBy(transformation, 1.0);
      if (v2._x < minX)
      {
        minX = v2._x;
      }
  
      if (v2._y < minY)
      {
        minY = v2._y;
      }
      if (v2._z < minZ)
      {
        minZ = v2._z;
      }
    }
  
    Vector3D min = new Vector3D(minX, minY, minZ);
  
    return Vector3D.minOnAllAxis(min, super.getMin(transformation));
  }

}