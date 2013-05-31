

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


public class SceneGraphRenderer
         extends
            Renderer {

   private GLStateTreeNode                     _rootState;
   private Camera                              _camera;

   private java.util.ArrayList<SceneGraphNode> _nodes = new java.util.ArrayList<SceneGraphNode>();
   private final boolean                       _usesCurrentCamera;


   public SceneGraphRenderer(final java.util.ArrayList<SceneGraphNode> nodes,
                             final boolean usesCurrentCamera) {
      _camera = null;
      _nodes = nodes;
      _usesCurrentCamera = usesCurrentCamera;
      System.out.print("SCENE GRAPH CREATED");

   }


   @Override
   public final void render(final G3MRenderContext rc) {

      if (_usesCurrentCamera) {

         if (_camera == null) {
            _camera = rc.getCurrentCamera();
            final int nNodes = _nodes.size();
            for (int i = 0; i < nNodes; i++) {
               _camera.addChild(_nodes.get(i));
            }
         }

         _camera.render(rc, _rootState);

      }
      else {
         final int nNodes = _nodes.size();
         for (int i = 0; i < nNodes; i++) {
            _nodes.get(i).render(rc, _rootState);
         }
      }
   }


   /////////////////////////////////

   @Override
   public final boolean isEnable() {
      return true;
   }


   @Override
   public final void setEnable(final boolean enable) {
   }


   @Override
   public final void initialize(final G3MContext context) {
      _rootState = GLStateTree.createNodeForSGNode(null); // GLStateTreeNode::createRootNodeForSGNode(NULL);

      final int size = _nodes.size();
      for (int i = 0; i < size; i++) {
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


   @Override
   public final boolean isReadyToRender(final G3MRenderContext rc) {
      return true;
   }


   @Override
   public final boolean onTouchEvent(final G3MEventContext ec,
                                     final TouchEvent touchEvent) {

      for (int i = 0; i < _nodes.size(); i++) {
         _nodes.get(i).touchEvent(ec, touchEvent);
      }

      return false;
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {
   }


   @Override
   public final void start(final G3MRenderContext rc) {
   }


   @Override
   public final void stop(final G3MRenderContext rc) {
   }


   @Override
   public final void onResume(final G3MContext context) {
   }


   @Override
   public final void onPause(final G3MContext context) {
   }


   @Override
   public final void onDestroy(final G3MContext context) {
   }


   @Override
   public final void rawRender(final G3MRenderContext rc,
                               final GLStateTreeNode myStateTreeNode) {
   }


   @Override
   public final boolean isInsideCameraFrustum(final G3MRenderContext rc) {
      return true;
   }


   @Override
   public final void modifiyGLState(final GLState state) {
   }

}
