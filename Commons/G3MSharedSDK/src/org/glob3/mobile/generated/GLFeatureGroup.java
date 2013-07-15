package org.glob3.mobile.generated; 
//#define N_GLFEATURES_GROUPS 3
public abstract class GLFeatureGroup extends GLFeatureSet
{

  private static GLFeatureGroup _noGroup = null;
  private static GLFeatureGroup _cameraGroup = null;
  private static GLFeatureGroup _colorGroup = null;

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
      default:
        return null;
    }
  }
  public static GLFeatureGroup getGroup(GLFeatureGroupName name)
  {
    switch (name)
    {
      case UNRECOGNIZED_GROUP:
        return null;
      case NO_GROUP:
        if (_noGroup == null)
        {
          _noGroup = new GLFeatureNoGroup();
        }
        return _noGroup;
      case CAMERA_GROUP:
        if (_cameraGroup == null)
        {
          _cameraGroup = new GLFeatureCameraGroup();
        }
        return _cameraGroup;
  
      case COLOR_GROUP:
        if (_colorGroup == null)
        {
          _colorGroup = new GLFeatureColorGroup();
        }
        return _colorGroup;
      default:
        return null;
    }
  }
  public static GLFeatureGroup getGroup(int i)
  {
    switch (i)
    {
      case -1:
        return getGroup(GLFeatureGroupName.UNRECOGNIZED_GROUP);
      case 0:
        return getGroup(GLFeatureGroupName.NO_GROUP);
      case 1:
        return getGroup(GLFeatureGroupName.CAMERA_GROUP);
      case 2:
        return getGroup(GLFeatureGroupName.COLOR_GROUP);
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
      default:
        return GLFeatureGroupName.UNRECOGNIZED_GROUP;
    }
  }

//  virtual GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl)= 0;

  public abstract GPUVariableValueSet createGPUVariableSet();
  public abstract void applyOnGlobalGLState(GLGlobalState state);
}