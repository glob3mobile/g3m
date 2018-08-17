package org.glob3.mobile.generated;import java.util.*;

//#define N_GLFEATURES_GROUPS 4

public abstract class GLFeatureGroup
{

  private static GLFeatureGroup[] _groups = null;


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

  public static void applyToAllGroups(GLFeatureSet features, tangible.RefObject<GPUVariableValueSet> vs, tangible.RefObject<GLGlobalState> state)
  {
  
	  if (_groups == null)
	  {
		  _groups = new GLFeatureGroup[DefineConstants.N_GLFEATURES_GROUPS];
  
		  for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
		  {
			  GLFeatureGroupName groupName = GLFeatureGroup.getGroupName(i);
			  _groups[i] = GLFeatureGroup.createGroup(groupName);
		  }
	  }
  
	  for (int i = 0; i < DefineConstants.N_GLFEATURES_GROUPS; i++)
	  {
		  tangible.RefObject<GPUVariableValueSet> tempRef_vs = new tangible.RefObject<GPUVariableValueSet>(vs);
		  tangible.RefObject<GLGlobalState> tempRef_state = new tangible.RefObject<GLGlobalState>(state);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _groups[i]->apply(features, vs, state);
		  _groups[i].apply(new GLFeatureSet(features), tempRef_vs, tempRef_state);
		  vs.argvalue = tempRef_vs.argvalue;
		  state.argvalue = tempRef_state.argvalue;
	  }
  
  }

  public abstract void apply(GLFeatureSet features, tangible.RefObject<GPUVariableValueSet> vs, tangible.RefObject<GLGlobalState> state);
}
