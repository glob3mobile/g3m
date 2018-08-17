package org.glob3.mobile.generated;import java.util.*;

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
//class GLGlobalState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;

public class SGNode
{
  protected final String _id;
  protected final String _sId;

  //  SGNode*              _parent;
  protected java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();


  //  void setParent(SGNode* parent);

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected final G3MContext _context;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected G3MContext _context = new protected();
//#endif

  protected SGShape _shape;


  public SGNode(String id, String sId)
  //  _parent(NULL)
  {
	  _id = id;
	  _sId = sId;
	  _context = null;
	  _shape = null;
  }


  ///#include "GPUProgramState.hpp"
  
  
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

  public void prepareRender(G3MRenderContext rc)
  {
  
  }

  public void cleanUpRender(G3MRenderContext rc)
  {
  
  }

  public void render(G3MRenderContext rc, GLState parentGLState, boolean renderNotReadyShapes)
  {
  
  //  ILogger::instance()->logInfo("Rendering SG: " + description());
  
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getChildrenCount() const
  public final int getChildrenCount()
  {
	return _children.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGNode* getChild(int i) const
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
