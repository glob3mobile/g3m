package org.glob3.mobile.generated; 
//
//  GLStateTreeNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//

//
//  GLStateTreeNode.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//




//class SceneGraphNode;

public class GLStateTreeNode
{

  protected SceneGraphNode _sgNode;
  protected java.util.ArrayList<GLStateTreeNode> _children = new java.util.ArrayList<GLStateTreeNode>(); //OTHER IMPLEMENTATIONS MAY HAVE ONLY ONE CHILD (FOR PERFORMANCE)
  protected final GLStateTreeNode _const _parent;
  protected GLState _state;

//C++ TO JAVA CONVERTER TODO TASK: Java has no concept of a 'friend' class:
//  friend class GLStateTree; //Tree can create nodes

  protected GLStateTreeNode(SceneGraphNode sgNode, GLStateTreeNode parent)
  {
     _sgNode = sgNode;
     _state = null;
     _parent = parent;
  }

  protected GLStateTreeNode()
  {
     _sgNode = null;
     _state = null;
     _parent = null;
  }

  protected final java.util.LinkedList<SceneGraphNode> getHierachy()
  {
    java.util.LinkedList<SceneGraphNode> h = new java.util.LinkedList<SceneGraphNode>();
    final GLStateTreeNode ancestor = this;
    while (ancestor != null)
    {
      h.addFirst(ancestor.getSGNode());
      ancestor = ancestor._parent;
    }
    return h;
  }

  protected final void prune(SceneGraphNode sgNode)
  {
    for (java.util.Iterator<GLStateTreeNode> it = _children.iterator(); it.hasNext();)
    {
      (it.next()).prune(sgNode);
  
      if ((it.next())._sgNode == sgNode)
      {
        delete (it.next());
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
        _children.erase(it);
      }
    }
  }

  protected final SceneGraphNode getSGNode()
  {
    return _sgNode;
  }


  public void dispose()
  {
    for (java.util.Iterator<GLStateTreeNode> it = _children.iterator(); it.hasNext();)
    {
      delete (it.next());
    }
    if (_state != null)
       _state.dispose();
  }

  public final GLStateTreeNode createChildNodeForSGNode(SceneGraphNode sgNode)
  {
    GLStateTreeNode child = new GLStateTreeNode(sgNode, this);
    _children.add(child);
    return child;
  }

  public final GLStateTreeNode getChildNodeForSGNode(SceneGraphNode node)
  {
    for (java.util.Iterator<GLStateTreeNode> it = _children.iterator(); it.hasNext();)
    {
      if ((it.next())._sgNode == node)
      {
        return it.next();
      }
    }
    return null;
  }

  public final GLState getGLState()
  {
    if (_state == null)
    {
      //Creating state
      java.util.LinkedList<SceneGraphNode> hierachi = getHierachy();
      _state = new GLState();
      for (java.util.Iterator<SceneGraphNode> it = hierachi.iterator(); it.hasNext();)
      {
        SceneGraphNode sgNode = it.next();
        if (sgNode != null)
        {
          sgNode.modifiyGLState(_state);
        }
      }
    }
    return _state;
  }
}