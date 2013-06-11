package org.glob3.mobile.generated; 
//
//  SGNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




//class G3MContext;
//class G3MRenderContext;
//class SGShape;
//class GLGlobalState;
//class GPUProgramState;

public class SGNode
{
  protected final String _id;
  protected final String _sId;

  //  SGNode*              _parent;
  protected java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();


  //  void setParent(SGNode* parent);

  protected G3MContext _context;

  protected SGShape _shape;


  public SGNode(String id, String sId)
  //  _parent(NULL)
  {
     _id = id;
     _sId = sId;
     _context = null;
     _shape = null;
  }

  public void dispose()
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  }

  public void initialize(G3MContext context, SGShape shape)
  {
    _context = context;
    _shape = shape;
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      child.initialize(context, shape);
    }
  }

  public final void addNode(SGNode child)
  {
    //  child->setParent(this);
    _children.add(child);
    if (_context != null)
    {
      child.initialize(_context, _shape);
    }
  }

  public boolean isReadyToRender(G3MRenderContext rc)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      if (!child.isReadyToRender(rc))
      {
        return false;
      }
    }
  
    return true;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  virtual void rawRender(G3MRenderContext rc);

  public void render(G3MRenderContext rc)
  {
    rawRender(rc);
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      child.render(rc);
    }
  }

  public final int getChildrenCount()
  {
    return _children.size();
  }

  public final SGNode getChild(int i)
  {
    return _children.get(i);
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  virtual GLState getGLState(GLState parentGLState);

  public void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
  }

  public void render(G3MRenderContext rc, GLState parentGLState)
  {
  
    GLState glState = getGLState(parentGLState);
  
    rawRender(rc, glState);
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      child.render(rc, glState);
    }
  }
}