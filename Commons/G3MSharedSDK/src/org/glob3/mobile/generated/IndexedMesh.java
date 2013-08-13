package org.glob3.mobile.generated; 
//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//class IShortBuffer;

public class IndexedMesh extends AbstractMesh
{
  private IShortBuffer _indices;
  protected final void rawRender(G3MRenderContext rc)
  {
    GL gl = rc.getGL();
    gl.drawElements(_primitive, _indices, _glState, rc.getGPUProgramManager());
  }

  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
     this(primitive, owner, center, vertices, indices, lineWidth, pointSize, flatColor, colors, colorsIntensity, true);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
  {
     this(primitive, owner, center, vertices, indices, lineWidth, pointSize, flatColor, colors, 0.0f, true);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, Color flatColor)
  {
     this(primitive, owner, center, vertices, indices, lineWidth, pointSize, flatColor, null, 0.0f, true);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize)
  {
     this(primitive, owner, center, vertices, indices, lineWidth, pointSize, null, null, 0.0f, true);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth)
  {
     this(primitive, owner, center, vertices, indices, lineWidth, 1, null, null, 0.0f, true);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
  {
     super(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest);
     _indices = indices;
  
  }

  public void dispose()
  {
    if (_owner)
    {
      if (_indices != null)
         _indices.dispose();
    }
  
    JAVA_POST_DISPOSE
  }
}