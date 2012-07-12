package org.glob3.mobile.generated; 
public class SceneGraphRenderer extends Renderer
{
  private SGGGroupNode _rootNode;

  public SceneGraphRenderer()
  {
	  _rootNode = new SGGGroupNode();

  }

  public SceneGraphRenderer(SGGGroupNode rootNode)
  {
	  _rootNode = rootNode;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGGGroupNode* getRootNode() const
  public final SGGGroupNode getRootNode()
  {
	return _rootNode;
  }

  public void initialize(InitializationContext ic)
  {
  
  }

  public int render(RenderContext rc)
  {
	return _rootNode.render(rc);
  }

  public boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public void onResizeViewportEvent(int width, int height)
  {
  
  }

  public void dispose()
  {
  }

}