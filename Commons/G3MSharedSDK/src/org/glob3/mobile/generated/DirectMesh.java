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
  protected final void rawRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
  
    final int verticesCount = getVertexCount();
    gl.drawArrays(_primitive, 0, verticesCount, _glState, rc.getGPUProgramManager());
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Mesh createNormalsMesh();


  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, null);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, true, null);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, 0.0f, true, null);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, null, 0.0f, true, null);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize)
  {
     this(primitive, owner, center, vertices, lineWidth, pointSize, null, null, 0.0f, true, null);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals)
  {
     super(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals);
  }

  public void dispose()
  {
    super.dispose();
  }

}