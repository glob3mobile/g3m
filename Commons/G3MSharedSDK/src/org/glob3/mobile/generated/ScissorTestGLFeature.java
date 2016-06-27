package org.glob3.mobile.generated; 
public class ScissorTestGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private int x;
  private int y;
  private int width;
  private int height;

  public ScissorTestGLFeature(int x1, int y1, int w, int h)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_BILLBOARD);
    x = x1;
    y = y1;
    width = w;
    height = h;
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    state.enableScissorTest(x, y, width, height);
  }

  public final void changeParams(int x1, int y1, int w, int h)
  {
    x = x1;
    y = y1;
    width = w;
    height = h;
  }
}