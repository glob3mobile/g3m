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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGShape;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;

public class SGNode
{
  protected final String _id;
  protected final String _sId;

  protected SGNode _parent;
  protected java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();


  protected final void setParent(SGNode parent)
  {
	_parent = parent;
  }

  protected G3MContext _context;

  protected SGShape _shape;


  public SGNode(String id, String sId)
  {
	  _id = id;
	  _sId = sId;
	  _context = null;
	  _shape = null;
	  _parent = null;

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
	child.setParent(this);
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

  public void rawRender(G3MRenderContext rc, GLState parentState)
  {
  
  }

  public void render(G3MRenderContext rc, GLState parentState)
  {
	final GLState myState = createState(rc, parentState);
	final GLState state;
	if (myState == null)
	{
	  state = parentState;
	}
	else
	{
	  state = myState;
	}
  
	prepareRender(rc);
  
	rawRender(rc, state);
  
	final int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  SGNode child = _children.get(i);
	  child.render(rc, state);
	}
  
	cleanUpRender(rc);
  
	if (myState != null)
		myState.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGShape* getShape() const
  public final SGShape getShape()
  {
	if (_shape != null)
	{
	  return _shape;
	}
	if (_parent != null)
	{
	  return _parent.getShape();
	}
	return null;
  }

  public GLState createState(G3MRenderContext rc, GLState parentState)
  {
	return null;
  }

}