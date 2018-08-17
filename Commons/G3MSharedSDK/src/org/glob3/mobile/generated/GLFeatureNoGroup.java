package org.glob3.mobile.generated;import java.util.*;

public class GLFeatureNoGroup extends GLFeatureGroup
{
  public final void apply(GLFeatureSet features, tangible.RefObject<GPUVariableValueSet> vs, tangible.RefObject<GLGlobalState> state)
  {
  
	  final int size = features.size();
	  for(int i = 0; i < size; i++)
	  {
		  final GLFeature f = features.get(i);
		  if (f._group == GLFeatureGroupName.NO_GROUP)
		  {
			  f.applyOnGlobalGLState(state.argvalue);
			  vs.argvalue.combineWith(f.getGPUVariableValueSet());
		  }
	  }
  }
}
