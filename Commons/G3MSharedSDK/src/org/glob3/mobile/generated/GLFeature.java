package org.glob3.mobile.generated; 
public abstract class GLFeature extends RCObject
{

  public GLFeature(GLFeatureGroupName group, GLFeatureID id)
  {
     _group = group;
     _id = id;
  }

  public final GPUVariableValueSet getGPUVariableValueSet()
  {
    return _values;
  }

  public final GLFeatureGroupName getGroup()
  {
    return _group;
  }

  public final GLFeatureID getID()
  {
    return _id;
  }

  public abstract void applyOnGlobalGLState(GLGlobalState state);

  protected final GLFeatureGroupName _group;
  protected GPUVariableValueSet _values = new GPUVariableValueSet();
  protected final GLFeatureID _id;
}