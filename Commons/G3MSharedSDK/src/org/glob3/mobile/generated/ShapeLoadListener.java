package org.glob3.mobile.generated;import java.util.*;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGShape;

public abstract class ShapeLoadListener
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

  public abstract void onBeforeAddShape(SGShape shape);
  public abstract void onAfterAddShape(SGShape shape);
}
