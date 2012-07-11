package org.glob3.mobile.generated; 
public class SGGGroupNode extends SGNode
{
  private java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();
  private boolean _boundsDirty;

  private void clearBounds()
  {
	_boundsDirty = true;
  }

  public SGGGroupNode()
  {
	  _boundsDirty = true;

  }

  public void transformationChanged()
  {
	super.transformationChanged();

	for (int i = 0; i < _children.size(); i++)
	{
	  SGNode child = _children.get(i);
	  child.transformationChanged();
	}
  }

  public final void childrenChanged()
  {
	clearBounds();

	if (_parent != null)
	{
	  _parent.childrenChanged();
	}
  }


  public final void addChild(SGNode node)
  {
	_children.add(node);
	node.setParent(this);
	childrenChanged();
  }

  public final void removeChild(int index)
  {
	_children.remove(index);
  }

  public final void removeChild(SGNode nodeToRemove)
  {
	for (int i = 0; i < _children.size(); i++)
	{
	  SGNode child = _children.get(i);
	  if (child == nodeToRemove)
	  {
		removeChild(i);
		break;
	  }
	}
  }

  public int render(RenderContext rc)
  {
	int max = DefineConstants.MAX_TIME_TO_RENDER;

	for (int i = 0; i < _children.size(); i++)
	{
	  SGNode child = _children.get(i);
	  final int childTimeToRender = child.render(rc);
	  if (childTimeToRender < max)
	  {
		max = childTimeToRender;
	  }
	}

	return max;
  }

}