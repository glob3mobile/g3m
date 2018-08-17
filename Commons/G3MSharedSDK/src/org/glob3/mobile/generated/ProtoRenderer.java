package org.glob3.mobile.generated;//
//  ProtoRenderer.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 23/04/14.
//
//

//
//  ProtoRenderer.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 18/03/14.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;

public abstract class ProtoRenderer
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

  public abstract void initialize(G3MContext context);


  public abstract void render(G3MRenderContext rc, GLState glState);

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public abstract void start(G3MRenderContext rc);

  public abstract void stop(G3MRenderContext rc);

  // Android activity lifecyle
  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);

}
