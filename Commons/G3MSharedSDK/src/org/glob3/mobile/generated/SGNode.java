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
//class InitializationContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGShape;

public class SGNode
{
  private String _id;
  private String _sId;

  private SGNode _parent;
  private java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();


  private void setParent(SGNode parent)
  {
	_parent = parent;
  }

  protected InitializationContext _initializationContext;

  protected SGShape _shape;

  protected void prepareRender(RenderContext rc)
  {
  
  }

  protected void cleanUpRender(RenderContext rc)
  {
  
  }

  protected void rawRender(RenderContext rc)
  {
  
  }


  public SGNode()
  {
	  _initializationContext = null;
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

  public final void initialize(InitializationContext ic, SGShape shape)
  {
	_initializationContext = ic;
	_shape = shape;
  
	final int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  SGNode child = _children.get(i);
	  child.initialize(ic, shape);
	}
  }

  public final void addNode(SGNode child)
  {
	child.setParent(this);
	_children.add(child);
	if (_initializationContext != null)
	{
	  child.initialize(_initializationContext, _shape);
	}
  }

  public final void setId(String id)
  {
	_id = id;
  }

  public final void setSId(String sId)
  {
	_sId = sId;
  }

  public boolean isReadyToRender(RenderContext rc)
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

  public final void render(RenderContext rc)
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