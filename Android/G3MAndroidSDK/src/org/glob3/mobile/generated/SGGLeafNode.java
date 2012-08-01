package org.glob3.mobile.generated; 
public abstract class SGGLeafNode extends SGNode
{
  protected boolean isVisible(RenderContext rc)
  {
	return true;
  }

  protected abstract int rawRender(RenderContext rc);


  public int render(RenderContext rc)
  {

	if (!isVisible(rc))
	{
	  return Renderer.maxTimeToRender;
	}

	IGL gl = rc.getGL();

	gl.pushMatrix();
	MutableMatrix44D fullMatrix = getFullMatrix();
	gl.loadMatrixf(fullMatrix);

	int timeToRender = rawRender(rc);

	gl.pushMatrix();

	return timeToRender;
  }

}