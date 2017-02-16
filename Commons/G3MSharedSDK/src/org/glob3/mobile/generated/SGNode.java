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
//class SGShape;
//class G3MRenderContext;
//class GLState;


public class SGNode
{
  protected final String _id;
  protected final String _sID;

  protected java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();

  protected G3MContext _context;

  protected SGShape _shape;


  public SGNode(String id, String sID)
  {
     _id = id;
     _sID = sID;
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

  public void prepareRender(G3MRenderContext rc)
  {
  
  }

  public void cleanUpRender(G3MRenderContext rc)
  {
  
  }

  public void render(G3MRenderContext rc, GLState parentGLState, boolean renderNotReadyShapes)
  {
    final GLState glState = createState(rc, parentGLState);
    if (glState != null)
    {
      prepareRender(rc);
  
      rawRender(rc, glState);
  
      final int childrenCount = _children.size();
      for (int i = 0; i < childrenCount; i++)
      {
        SGNode child = _children.get(i);
        child.render(rc, glState, renderNotReadyShapes);
      }
  
      cleanUpRender(rc);
    }
    else
    {
      ILogger.instance().logError("NO GLSTATE");
    }
  }

  public GLState createState(G3MRenderContext rc, GLState parentState)
  {
    return parentState;
  }

  public final int getChildrenCount()
  {
    return _children.size();
  }

  public final SGNode getChild(int i)
  {
    return _children.get(i);
  }

  public void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
  }

  public String description()
  {
    return "SGNode";
  }

}
