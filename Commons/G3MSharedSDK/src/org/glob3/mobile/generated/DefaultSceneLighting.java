package org.glob3.mobile.generated; 
public class DefaultSceneLighting extends SceneLighting
{

  public final void modifyGLState(GLState glState)
  {
    glState.addGLFeature(new DirectionLightGLFeature(new Vector3D(1, 0,0), Color.yellow(), (float)0.0), false);
  }

}