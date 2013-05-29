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

public class SGNode extends GLClient
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


  //void SGNode::setParent(SGNode* parent) {
  //  _parent = parent;
  //}
  
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

  public void rawRender(G3MRenderContext rc)
  {
  
  }


  //GLGlobalState* SGNode::createState(const G3MRenderContext* rc,
  //                             const GLGlobalState& parentState) {
  //  return  NULL;
  //}
  //
  //GPUProgramState* SGNode::createGPUProgramState(const G3MRenderContext* rc,
  //                                               const GPUProgramState* parentState){
  //  return new GPUProgramState(parentState);
  //}
  
  
  public void render(G3MRenderContext rc)
  {
  //  GLGlobalState* myState = createState(rc, parentState);
  //  GLGlobalState* state;
  //  if (myState == NULL) {
  //    state = (GLGlobalState*) &parentState;
  //  }
  //  else {
  //    state = myState;
  //  }
  
  //  GPUProgramState* myGPUProgramState = createGPUProgramState(rc, parentProgramState);
    rawRender(rc);
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      child.render(rc);
    }
  
  //  delete myGPUProgramState;
  //  delete myState;
  }

  //  SGShape* getShape() const {
  //    if (_shape != NULL) {
  //      return _shape;
  //    }
  //    if (_parent != NULL) {
  //      return _parent->getShape();
  //    }
  //    return NULL;
  //  }

//  virtual GLGlobalState* createState(const G3MRenderContext* rc,
//                               const GLGlobalState& parentState);
//  
//  virtual GPUProgramState* createGPUProgramState(const G3MRenderContext* rc,
//                                                 const GPUProgramState* parentState);

  public final int getChildrenCount()
  {
    return _children.size();
  }

  public final SGNode getChild(int i)
  {
    return _children.get(i);
  }

  public final void notifyGLClientChildrenParentHasChanged()
  {
  
    if (_shape != null)
    {
      _shape.actualizeGLGlobalState(this);
    }
  
    final int nChildren = getChildrenCount();
    for (int i = 0; i < nChildren; i++)
    {
      _children.get(i).actualizeGLGlobalState(this);
    }
  }
}