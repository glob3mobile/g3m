package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////////

public abstract class PriorityGLFeature extends GLFeature
{
  private final int _priority;
  public PriorityGLFeature(GLFeatureGroupName g, int p)
  {
     super(g);
     _priority = p;
  }

  public final int getPriority()
  {
     return _priority;
  }
}