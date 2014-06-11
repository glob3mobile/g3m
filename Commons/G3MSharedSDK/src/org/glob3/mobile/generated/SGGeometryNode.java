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

  private Box _boundingBox;


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
  
    // compute boundingBox
    float xmin = 1e10F;
    float ymin = 1e10F;
    float zmin = 1e10F;
    float xmax = -1e10F;
    float ymax = -1e10F;
    float zmax = -1e10F;
    int verticesCount = vertices.size() / 3;
    for (int i = 0; i < verticesCount *3; i+=3)
    {
      float x = vertices.get(i);
      float y = vertices.get(i+1);
      float z = vertices.get(i+2);
      if (x<xmin)
         xmin = x;
      if (y<ymin)
         ymin = y;
      if (z<zmin)
         zmin = z;
      if (x>xmax)
         xmax = x;
      if (y>ymax)
         ymax = y;
      if (z>zmax)
         zmax = z;
    }
    final Vector3D lower = new Vector3D(xmin, ymin, zmin);
    final Vector3D upper = new Vector3D(xmax, ymax, zmax);
    _boundingBox = new Box(lower, upper);
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
  
    if (_boundingBox != null)
       _boundingBox.dispose();
  
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

  public final Box getCopyBoundingBox()
  {
    return new Box(_boundingBox);
  }

}