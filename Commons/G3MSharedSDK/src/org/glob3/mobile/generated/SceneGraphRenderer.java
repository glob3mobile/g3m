package org.glob3.mobile.generated; 
//
//  SceneGraphRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//

//
//  SceneGraphRenderer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//




public class SceneGraphRenderer extends Renderer
{

  private GLStateTreeNode _rootState;
  private Camera _camera;

  private java.util.ArrayList<SceneGraphNode> _nodes = new java.util.ArrayList<SceneGraphNode>();
  private boolean _usesCurrentCamera;

  public SceneGraphRenderer(java.util.ArrayList<SceneGraphNode> nodes, boolean usesCurrentCamera)
  {
     _camera = null;
     _nodes = nodes;
     _usesCurrentCamera = usesCurrentCamera;
    System.out.print("SCENE GRAPH CREATED");

  }

  public final void render(G3MRenderContext rc)
  {
  
    if (_usesCurrentCamera)
    {
  
    if (_camera == null)
    {
      _camera = (Camera)rc.getCurrentCamera();
      int nNodes = _nodes.size();
      for (int i = 0; i < nNodes; i++)
      {
        _camera.addChild(_nodes.get(i));
      }
    }
  
    _camera.SceneGraphNode.render(rc, _rootState);
  
    }
    else
    {
      int nNodes = _nodes.size();
      for (int i = 0; i < nNodes; i++)
      {
        _nodes.get(i).render(rc, _rootState);
      }
    }
  }


  /////////////////////////////////

  public final boolean isEnable()
  {
    return true;
  }

  public final void setEnable(boolean enable)
  {
  }


  public final void initialize(G3MContext context)
  {
    _rootState = GLStateTree.createNodeForSGNode(null); // GLStateTreeNode::createRootNodeForSGNode(NULL);

    int size = _nodes.size();
    for (int i = 0; i < size; i++)
    {
      _nodes.get(i).initialize(context);
    }
//    
//    MarksRenderer* mr = new MarksRenderer(true);
//    rc->getCurrentCamera->addChildren(mr);
//    
//    for (int i = 0; i < 2000; i++){
//      const Angle latitude  = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
//      const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360));
//      
//      Mark* m = new Mark("Random Mark",
//                         Geodetic3D(latitude,
//                                    longitude,
//                                    0),
//                         0);
//      
//      m->initialize(context, 100);
//      mr->addMark(m);
//    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }


  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {

    for (int i = 0; i < _nodes.size(); i++)
    {
      _nodes.get(i).touchEvent(ec, touchEvent);
    }

    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void start(G3MRenderContext rc)
  {
  }

  public final void stop(G3MRenderContext rc)
  {
  }

  public final void onResume(G3MContext context)
  {
  }

  public final void onPause(G3MContext context)
  {
  }

  public final void onDestroy(G3MContext context)
  {
  }

  public final void rawRender(G3MRenderContext rc, GLStateTreeNode myStateTreeNode)
  {
  }
  public final boolean isInsideCameraFrustum(G3MRenderContext rc)
  {
     return true;
  }
  public final void modifiyGLState(GLState state)
  {
  }

}