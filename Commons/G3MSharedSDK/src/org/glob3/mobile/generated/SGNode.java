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

public class SGNode
{
  private final String _id;
  private final String _sId;

  private SGNode _parent;
  private java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();


  private void setParent(SGNode parent)
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

  public void rawRender(G3MRenderContext rc)
  {
  
  }

  public final void render(G3MRenderContext rc)
  {
	prepareRender(rc);
  
	rawRender(rc);
  
	final int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  SGNode child = _children.get(i);
	  child.render(rc);
	}
  
	cleanUpRender(rc);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGShape* getShape() const
  public final SGShape getShape()
  {
	// return (_parent == NULL) ? _shape : _parent->getShape();
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
}