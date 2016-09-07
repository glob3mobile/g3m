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

  protected final void rawRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
  
    gl.drawArrays(_primitive, 0, (int)_renderVerticesCount, _glState, rc.getGPUProgramManager());
  }

//  Mesh* createNormalsMesh() const;


  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, 0, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, false, 0, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, null, false, 0, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, true, null, false, 0, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, 0.0f, true, null, false, 0, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, null, 0.0f, true, null, false, 0, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, null, null, 0.0f, true, null, false, 0, 0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
     super(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits);
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