package org.glob3.mobile.generated;import java.util.*;

///////////////////////////////////////////////////////////////////////////////////////////

public abstract class PriorityGLFeature extends GLFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final int _priority;

  public PriorityGLFeature(GLFeatureGroupName g, GLFeatureID id, int priority)
  {
	  super(g, id);
	  _priority = priority;
  }
}
