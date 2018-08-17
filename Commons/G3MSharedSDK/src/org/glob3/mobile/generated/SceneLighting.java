package org.glob3.mobile.generated;import java.util.*;

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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MeshRenderer;

public abstract class SceneLighting
{
  public void dispose()
  {
  }
  public abstract void modifyGLState(GLState glState, G3MRenderContext rc);
}
