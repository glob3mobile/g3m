package org.glob3.mobile.generated; 
//#define N_GLFEATURES_GROUPS 4
public abstract class GLFeatureGroup extends GLFeatureSet
{

  public void dispose()
  {
  }

  public static GLFeatureGroup createGroup(GLFeatureGroupName name)
  {
    switch (name)
    {
      case UNRECOGNIZED_GROUP:
        return null;
      case NO_GROUP:
        return new GLFeatureNoGroup();
      case CAMERA_GROUP:
        return new GLFeatureCameraGroup();
      case COLOR_GROUP:
        return new GLFeatureColorGroup();
      case LIGHTING_GROUP:
        return new GLFeatureLightingGroup();
      default:
        return null;
    }
  }
  public static GLFeatureGroupName getGroupName(int i)
  {
    switch (i)
    {
      case 0:
        return GLFeatureGroupName.NO_GROUP;
      case 1:
        return GLFeatureGroupName.CAMERA_GROUP;
      case 2:
        return GLFeatureGroupName.COLOR_GROUP;
      case 3:
        return GLFeatureGroupName.LIGHTING_GROUP;
      default:
        return GLFeatureGroupName.UNRECOGNIZED_GROUP;
    }
  }

  public abstract void addToGPUVariableSet(GPUVariableValueSet vs);
  public abstract void applyOnGlobalGLState(GLGlobalState state);
}