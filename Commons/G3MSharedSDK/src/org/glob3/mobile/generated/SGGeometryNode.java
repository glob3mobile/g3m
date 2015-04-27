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

  public final void centerGeometryAtZero()
  {
  
    double maxX = 9e99;
    double maxY = 9e99;
    double maxZ = 9e99;
    double minX = -9e99;
    double minY = -9e99;
    double minZ = -9e99;
  
    for (int i = 0; i < _vertices.size(); i+=3)
    {
      double x = _vertices.get(i);
      double y = _vertices.get(i+1);
      double z = _vertices.get(i+2);
  
      if (x > maxX)
      {
        maxX = x;
      }
      else if (x < minX)
      {
        minX = x;
      }
  
      if (y > maxY)
      {
        maxY = y;
      }
      else if (y < minY)
      {
        minY = y;
      }
  
      if (z > maxZ)
      {
        maxZ = z;
      }
      else if (z < minZ)
      {
        minZ = z;
      }
    }
  
    Vector3D center = new Vector3D(maxX-minX, maxY-minY, maxZ-minZ);
  
  
    _glState.addGLFeature(new ModelTransformGLFeature(MutableMatrix44D.createTranslationMatrix(center.times(-1)).asMatrix44D()), false);
  
  }

}