package org.glob3.mobile.generated;
//
//  IndexedGeometryMesh.cpp
//  G3M
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

//
//  IndexedGeometryMesh.hpp
//  G3M
//
//  Created by Jose Miguel SN on 23/06/13.
//
//





//class IShortBuffer;

public class IndexedGeometryMesh extends AbstractGeometryMesh
{
  private boolean _ownsIndices;
  private IShortBuffer _indices;
  protected final void rawRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, _glState, rc.getGPUProgramManager());
  }

  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, boolean cullFace)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, cullFace, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, true, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, 1, true, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, 1, 1, true, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, boolean cullFace, int culledFace)
  {
     super(primitive, center, vertices, ownsVertices, null, false, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, cullFace, culledFace);
     _indices = indices;
     _ownsIndices = ownsIndices;
  //  ILogger::instance()->logInfo("Created an IndexedGeometryMesh with %d vertices, %d indices",
  //                               vertices->size(),
  //                               indices->size());
  }

  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, boolean cullFace)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, cullFace, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, true, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, 1, true, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices)
  {
     this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, 1, 1, true, false, 0, 0, false, GLCullFace.back());
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, boolean cullFace, int culledFace)
  {
     super(primitive, center, vertices, ownsVertices, normals, ownsNormals, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, cullFace, culledFace);
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