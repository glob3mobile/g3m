//
//  SceneGraphNode.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#ifndef __G3MiOSSDK__SceneGraphNode__
#define __G3MiOSSDK__SceneGraphNode__

#include <iostream>

#include "Context.hpp"
#include "GLState.hpp"

//CLASE PARA PRUEBAS LLEVAR A SU FICHERO
class SceneGraphNode;
class GLStateTreeNode{
  SceneGraphNode* _sgNode;
  std::vector<GLStateTreeNode*> _children;
public:
  GLStateTreeNode(SceneGraphNode* sgNode): _sgNode(sgNode){}
  GLStateTreeNode* getStateNodeForSGNode(SceneGraphNode* node){
    return NULL;
  }
  
  void addChildren(GLStateTreeNode* child){
    _children.push_back(child);
  }
};

class SceneGraphNode{
  
  bool _isVisible;
  
  std::vector<SceneGraphNode*> _children;
  
public:
  SceneGraphNode():_isVisible(true){}
  
  virtual ~SceneGraphNode(){}
  
  virtual void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode);
  
  void render(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){
    if (_isVisible && isInsideCameraFrustum(rc->getCurrentCamera())){
      rawRender(rc, myStateTreeNode);
      
      int count = getChildrenCount();
      for (int i = 0; i < count; i++) {
        SceneGraphNode* child = _children[i];
        GLStateTreeNode* childSTN = myStateTreeNode->getStateNodeForSGNode(child);
        if (childSTN == NULL){
          childSTN = new GLStateTreeNode(child);
          myStateTreeNode->addChildren(childSTN);
        }
        
        child->render(rc, childSTN);
      }
    }
  }
  
  virtual bool isInsideCameraFrustum(const Camera* rc);
  
  bool isVisible() const { return _isVisible;}
  
  void setVisible(bool v) { _isVisible = v;}
  
  void addChildren(SceneGraphNode* child){
    _children.push_back(child);
  }
  
  int getChildrenCount() const {
    return _children.size();
  }
  
  SceneGraphNode* getChild(int i) const {
    return _children[i];
  }

  
};

#endif /* defined(__G3MiOSSDK__SceneGraphNode__) */
