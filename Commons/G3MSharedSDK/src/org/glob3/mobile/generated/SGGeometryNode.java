package org.glob3.mobile.generated;
//
//  SGGeometryNode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGGeometryNode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



//class IFloatBuffer;
//class IShortBuffer;


public class SGGeometryNode extends SGNode
{
  private final int _primitive;
  private IFloatBuffer _vertices;
  private IFloatBuffer _colors;
  private IFloatBuffer _uv;
  private IFloatBuffer _normals;
  private IShortBuffer _indices;
  private final boolean _depthTest;

  private GLState _glState;
  private void createGLState()
  {
    _glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, _depthTest, false, 0, false, 0, 0, 1, true, 1), false); // pointSize -  needsPointSize -  lineWidth -  polygonOffsetUnits -  polygonOffsetFactor -  polygonOffsetFill -  culledFace -  cullFace -  depthTestEnabled -  stride -  normalized -  index -  arrayElementSize -  buffer
  
    if (_normals != null)
    {
      _glState.addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0), false);
    }
  
    if (_uv != null)
    {
      _glState.addGLFeature(new TextureCoordsGLFeature(_uv, 2, 0, false, 0, false, Vector2F.ZERO, Vector2F.ZERO), false);
    }
  }



  public SGGeometryNode(String id, String sID, int primitive, IFloatBuffer vertices, IFloatBuffer colors, IFloatBuffer uv, IFloatBuffer normals, IShortBuffer indices, boolean depthTest)
  {
     super(id, sID);
     _primitive = primitive;
     _vertices = vertices;
     _colors = colors;
     _uv = uv;
     _normals = normals;
     _indices = indices;
     _depthTest = depthTest;
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

}