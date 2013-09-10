package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////////

public abstract class PriorityGLFeature extends GLFeature
{
  private final int _priority;
  public PriorityGLFeature(GLFeatureGroupName g, GLFeatureID id, int p)
  {
     super(g, id);
     _priority = p;
  }

  public final int getPriority()
  {
     return _priority;
  }
}