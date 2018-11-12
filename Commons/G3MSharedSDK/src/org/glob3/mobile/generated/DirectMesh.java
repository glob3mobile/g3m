package org.glob3.mobile.generated;
//
//  DirectMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  DirectMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//





public class DirectMesh extends AbstractMesh
{
  private int _renderVerticesCount;

  protected final void renderMesh(G3MRenderContext rc, GLState glState)
  {
    GL gl = rc.getGL();
    gl.drawArrays(_primitive, 0, (int)_renderVerticesCount, glState, rc.getGPUProgramManager());
  }

  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, boolean cullFace)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, cullFace, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, 0, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, depthTest, normals, polygonOffsetFill, 0, 0, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, depthTest, normals, false, 0, 0, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, depthTest, null, false, 0, 0, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, true, null, false, 0, 0, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, null, true, null, false, 0, 0, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, null, null, true, null, false, 0, 0, false, GLCullFace.back());
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, boolean cullFace, int culledFace)
  {
     super(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, cullFace, culledFace);
    _renderVerticesCount = vertices.size() / 3;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final void setRenderVerticesCount(int renderVerticesCount)
  {
    if (renderVerticesCount > getRenderVerticesCount())
    {
      throw new RuntimeException("Invalid renderVerticesCount");
    }
    _renderVerticesCount = renderVerticesCount;
  }

  public final int getRenderVerticesCount()
  {
    return _renderVerticesCount;
  }

}
