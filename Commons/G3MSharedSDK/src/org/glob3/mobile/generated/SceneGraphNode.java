package org.glob3.mobile.generated; 
//
//  SceneGraphNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

//
//  SceneGraphNode.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//








public abstract class SceneGraphNode extends GLClient
{
  private GLStateTreeNode _lastParentStateNode;
  private GLStateTreeNode _lastStateNode;

  private boolean _isVisible; //TODO: enable
  private java.util.ArrayList<SceneGraphNode> _children = new java.util.ArrayList<SceneGraphNode>();


  protected abstract void rawRender(G3MRenderContext rc, GLStateTreeNode myStateTreeNode);

  protected abstract boolean isInsideCameraFrustum(G3MRenderContext rc); //TODO: isVisible

//  int getChildrenCount() const {
//    return _children.size();
//  }
//  
//  const SceneGraphNode* getChild(int i) const {
//    return _children[i];
//  }

  protected void onInitialize(G3MContext context)
  {
  }

  protected void onTouchEventRecived(G3MEventContext ec, TouchEvent touchEvent)
  {
  }

  public SceneGraphNode()
  {
     _isVisible = true;
     _lastParentStateNode = null;
     _lastStateNode = null;
  }

  public void dispose()
  {
    GLStateTree.prune(this); //Deleting states
  }

  public abstract void modifiyGLState(GLState state);

  public final void initialize(G3MContext context)
  {
    onInitialize(context);
    for (java.util.Iterator<SceneGraphNode> it = _children.iterator(); it.hasNext();)
    {
      SceneGraphNode child = it.next();
      child.initialize(context);
    }
  }

  public final void render(G3MRenderContext rc, GLStateTreeNode parentStateTreeNode)
  {
  
    if (_isVisible && isInsideCameraFrustum(rc))
    {
  
      if (_lastParentStateNode != parentStateTreeNode)
      {
        _lastParentStateNode = parentStateTreeNode;
        _lastStateNode = parentStateTreeNode.getChildNodeForSGNode(this);
        if (_lastStateNode == null)
        {
          _lastStateNode = parentStateTreeNode.createChildNodeForSGNode(this);
        }
      }
  
      rawRender(rc, _lastStateNode);
  
      for (java.util.Iterator<SceneGraphNode> it = _children.iterator(); it.hasNext();)
      {
        SceneGraphNode child = it.next();
        child.render(rc, _lastStateNode);
      }
    }
  }

  public final boolean isVisible()
  {
     return _isVisible;
  }

  public final void setVisible(boolean v)
  {
     _isVisible = v;
  }

  public final void addChild(SceneGraphNode child)
  {
    _children.add(child);
  }

  public final void eraseChild(SceneGraphNode child)
  {
    _children.remove(child);
  }

  public final void touchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    onTouchEventRecived(ec, touchEvent);
    for (java.util.Iterator<SceneGraphNode> it = _children.iterator(); it.hasNext();)
    {
      SceneGraphNode child = it.next();
      child.touchEvent(ec, touchEvent);
    }
  }


}