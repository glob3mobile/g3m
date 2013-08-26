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
  private IShortBuffer _indices;
  protected final void rawRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, _glState, rc.getGPUProgramManager());
  }

  public IndexedGeometryMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize)
  {
     this(primitive, owner, center, vertices, indices, lineWidth, pointSize, true);
  }
  public IndexedGeometryMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth)
  {
     this(primitive, owner, center, vertices, indices, lineWidth, 1, true);
  }
  public IndexedGeometryMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, boolean depthTest)
  {
     super(primitive, owner, center, vertices, lineWidth, pointSize, depthTest);
     _indices = indices;
  
  }

  public void dispose()
  {
    if (_owner)
    {
      if (_indices != null)
         _indices.dispose();
    }
  
    super.dispose();
  
  }


}