package org.glob3.mobile.generated;import java.util.*;

//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

//
//  MeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;


public abstract class MeshLoadListener
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void dispose();
//#endif

  public abstract void onError(URL url);
  public abstract void onBeforeAddMesh(Mesh mesh);
  public abstract void onAfterAddMesh(Mesh mesh);
}
