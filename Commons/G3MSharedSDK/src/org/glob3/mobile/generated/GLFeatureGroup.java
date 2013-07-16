package org.glob3.mobile.generated; 
//#define N_GLFEATURES_GROUPS 3
public abstract class GLFeatureGroup extends GLFeatureSet
{

//  static GLFeatureGroup* _noGroup;
//  static GLFeatureGroup* _cameraGroup;
//  static GLFeatureGroup* _colorGroup;

  public void dispose()
  {
  }


  //GLFeatureGroup* GLFeatureGroup::getGroup(GLFeatureGroupName name){
  //  switch (name) {
  //    case UNRECOGNIZED_GROUP:
  //      return NULL;
  //    case NO_GROUP:
  //      if (_noGroup == NULL){
  //        _noGroup = new GLFeatureNoGroup();
  //      }
  //      return _noGroup;
  //    case CAMERA_GROUP:
  //      if (_cameraGroup == NULL){
  //        _cameraGroup = new GLFeatureCameraGroup();
  //      }
  //      return _cameraGroup;
  //
  //    case COLOR_GROUP:
  //      if (_colorGroup == NULL){
  //        _colorGroup = new GLFeatureColorGroup();
  //      }
  //      return _colorGroup;
  //    default:
  //      return NULL;
  //  }
  //}
  
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
//  static GLFeatureGroup* getGroup(GLFeatureGroupName name);
//  static GLFeatureGroup* getGroup(int i);

  //GLFeatureGroup* GLFeatureGroup::_noGroup = NULL;
  //GLFeatureGroup* GLFeatureGroup::_cameraGroup = NULL;
  //GLFeatureGroup* GLFeatureGroup::_colorGroup = NULL;
  //
  //GLFeatureGroup* GLFeatureGroup::getGroup(int i){
  //  switch (i) {
  //    case -1:
  //      return getGroup(UNRECOGNIZED_GROUP);
  //    case 0:
  //      return getGroup(NO_GROUP);
  //    case 1:
  //      return getGroup(CAMERA_GROUP);
  //    case 2:
  //      return getGroup(COLOR_GROUP);
  //    default:
  //      return NULL;
  //  }
  //}
  
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

//  virtual GPUVariableValueSet* createGPUVariableSet()= 0;
  public abstract void addToGPUVariableSet(GPUVariableValueSet vs);
  public abstract void applyOnGlobalGLState(GLGlobalState state);
}