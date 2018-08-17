package org.glob3.mobile.generated;import java.util.*;

public abstract class GLColorGroupFeature extends PriorityGLFeature
{
  private final boolean _blend;
  private final int _sFactor;
  private final int _dFactor;

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public GLColorGroupFeature(GLFeatureID id, int priority, boolean blend, int sFactor, int dFactor)
  {
	  super(GLFeatureGroupName.COLOR_GROUP, id, priority);
	  _blend = blend;
	  _sFactor = sFactor;
	  _dFactor = dFactor;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void blendingOnGlobalGLState(GLGlobalState* state) const
  public final void blendingOnGlobalGLState(GLGlobalState state)
  {
	if (_blend)
	{
	  state.enableBlend();
	  state.setBlendFactors(_sFactor, _dFactor);
	}
	else
	{
	  state.disableBlend();
	}
  }
}
