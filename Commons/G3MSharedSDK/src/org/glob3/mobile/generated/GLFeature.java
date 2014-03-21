package org.glob3.mobile.generated; 
public abstract class GLFeature extends RCObject
{
  public void dispose()
  {
    super.dispose();
  }

  protected GPUVariableValueSet _values = new GPUVariableValueSet();

  public final GLFeatureGroupName _group;
  public final GLFeatureID _id;

  public GLFeature(GLFeatureGroupName group, GLFeatureID id)
  {
     _group = group;
     _id = id;
  }

  public final GPUVariableValueSet getGPUVariableValueSet()
  {
    return _values;
  }

  public abstract void applyOnGlobalGLState(GLGlobalState state);

}