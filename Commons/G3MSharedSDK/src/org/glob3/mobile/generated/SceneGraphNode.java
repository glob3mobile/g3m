

package org.glob3.mobile.generated;

//
//  SceneGraphNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

//
//  SceneGraphNode.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//


public abstract class SceneGraphNode
         extends
            GLClient {
   private GLStateTreeNode                           _lastParentStateNode;
   private GLStateTreeNode                           _lastStateNode;

   private boolean                                   _isVisible;                                           //TODO: enable
   private final java.util.ArrayList<SceneGraphNode> _children = new java.util.ArrayList<SceneGraphNode>();


   protected abstract void rawRender(G3MRenderContext rc,
                                     GLStateTreeNode myStateTreeNode);


   protected abstract boolean isInsideCameraFrustum(G3MRenderContext rc); //TODO: isVisible


   //  int getChildrenCount() const {
   //    return _children.size();
   //  }
   //  
   //  const SceneGraphNode* getChild(int i) const {
   //    return _children[i];
   //  }

   protected void onInitialize(final G3MContext context) {
   }


   protected void onTouchEventRecived(final G3MEventContext ec,
                                      final TouchEvent touchEvent) {
   }


   public SceneGraphNode() {
      _isVisible = true;
      _lastParentStateNode = null;
      _lastStateNode = null;
   }


   @Override
   public void dispose() {
      GLStateTree.prune(this); //Deleting states
   }


   public abstract void modifiyGLState(GLState state);


   public void initialize(final G3MContext context) {
      onInitialize(context);
      for (final SceneGraphNode child : _children) {
         child.initialize(context);
      }
   }


   public final void render(final G3MRenderContext rc,
                            final GLStateTreeNode parentStateTreeNode) {

      if (_isVisible && isInsideCameraFrustum(rc)) {

         if (_lastParentStateNode != parentStateTreeNode) {
            _lastParentStateNode = parentStateTreeNode;
            _lastStateNode = parentStateTreeNode.getChildNodeForSGNode(this);
            if (_lastStateNode == null) {
               _lastStateNode = parentStateTreeNode.createChildNodeForSGNode(this);
            }
         }

         rawRender(rc, _lastStateNode);

         for (final SceneGraphNode child : _children) {
            child.render(rc, _lastStateNode);
         }
      }
   }


   public final boolean isVisible() {
      return _isVisible;
   }


   public final void setVisible(final boolean v) {
      _isVisible = v;
   }


   public final void addChild(final SceneGraphNode child) {
      _children.add(child);
   }


   public final void eraseChild(final SceneGraphNode child) {
      for (final java.util.Iterator<SceneGraphNode> it = _children.iterator(); it.hasNext();) {
         if (it.next() == child) {
            //C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
            _children.erase(it);
         }
      }
   }


   public final void touchEvent(final G3MEventContext ec,
                                final TouchEvent touchEvent) {
      onTouchEventRecived(ec, touchEvent);
      for (final SceneGraphNode child : _children) {
         child.touchEvent(ec, touchEvent);
      }
   }


}
