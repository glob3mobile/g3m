package org.glob3.mobile.generated; 
//
//  SceneLighting.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//

//
//  SceneLighting.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//


//class GLState;
//class G3MRenderContext;
//class Vector3D;
//class MeshRenderer;

public abstract class SceneLighting
{
  public void dispose()
  {
  }
  public abstract void modifyGLState(GLState glState, G3MRenderContext rc);
}