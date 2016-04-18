package org.glob3.mobile.generated; 
//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//


//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//



//class IShortBuffer;

public class IndexedMesh extends AbstractMesh
{
  private IShortBuffer _indices;
  private boolean _ownsIndices;
  protected final void rawRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, _glState, rc.getGPUProgramManager());
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Mesh createNormalsMesh();

  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, false, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, null, false, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, colorsIntensity, true, null, false, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, colors, 0.0f, true, null, false, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, flatColor, null, 0.0f, true, null, false, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, null, null, 0.0f, true, null, false, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, 1, null, null, 0.0f, true, null, false, 0, 0);
  }
  public IndexedMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
     super(primitive, ownsVertices, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits);
     _indices = indices;
     _ownsIndices = ownsIndices;
  
  }

  public void dispose()
  {
    if (_ownsIndices)
    {
      if (_indices != null)
         _indices.dispose();
    }
  
    super.dispose();
  
  }

  public final IShortBuffer getIndices()
  {
    return _indices;
  }
}