package org.glob3.mobile.generated; 
//
//  IndexedGeometryMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

//
//  IndexedGeometryMesh.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//



//class IShortBuffer;

public class IndexedGeometryMesh extends AbstractGeometryMesh
{
  private boolean _ownsIndices;
  private IShortBuffer _indices;
  protected final void rawRender(G3MRenderContext rc, GLState glState, RenderType renderType)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, glState, rc.getGPUProgramManager(), renderType);
  }

  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, true);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth)
  {
     this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, 1, true);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest)
  {
     super(primitive, ownsVertices, center, vertices, lineWidth, pointSize, depthTest);
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



}