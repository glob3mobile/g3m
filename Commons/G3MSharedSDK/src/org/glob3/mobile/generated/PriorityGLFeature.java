package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////////

public abstract class PriorityGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public final int _priority;

  public PriorityGLFeature(GLFeatureGroupName g, GLFeatureID id, int priority)
  {
     super(g, id);
     _priority = priority;
  }
}