package org.glob3.mobile.generated;import java.util.*;

public class GLFeatureColorGroup extends GLFeatureGroup
{
  public final void apply(GLFeatureSet features, tangible.RefObject<GPUVariableValueSet> vs, tangible.RefObject<GLGlobalState> state)
  {
  
	  final int featuresSize = features.size();
	  int priority = -1;
	  for (int i = 0; i < featuresSize; i++)
	  {
		  final GLFeature f = features.get(i);
		  if (f._group == GLFeatureGroupName.COLOR_GROUP)
		  {
			  PriorityGLFeature pf = ((PriorityGLFeature) f);
			  if (pf._priority > priority)
			  {
				  if (pf._id != GLFeatureID.GLF_BLENDING_MODE) //We do not take into account Blending if TexID not set
				  {
					  priority = pf._priority;
				  }
			  }
		  }
	  }
  
	  for (int i = 0; i < featuresSize; i++)
	  {
		  final GLFeature f = features.get(i);
		  if (f._group == GLFeatureGroupName.COLOR_GROUP)
		  {
			  PriorityGLFeature pf = ((PriorityGLFeature) f);
			  if (pf._priority == priority)
			  {
				  pf.applyOnGlobalGLState(state.argvalue);
				  vs.argvalue.combineWith(f.getGPUVariableValueSet());
			  }
		  }
	  }
  }
}
