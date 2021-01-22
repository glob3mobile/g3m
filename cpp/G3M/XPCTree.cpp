//
//  XPCTree.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/18/21.
//

#include "XPCTree.hpp"

#include "XPCNode.hpp"


XPCTree::XPCTree(const std::string& id,
                 XPCNode* rootNode) :
_id(id),
_rootNode(rootNode)
{
  
}


XPCTree::~XPCTree() {
  _rootNode->_release();
}


const Sector* XPCTree::getSector() const {
  return _rootNode->getSector();
}


long long XPCTree::render(const XPCPointCloud* pointCloud,
                          const G3MRenderContext* rc,
                          GLState* glState,
                          const Frustum* frustum,
                          long long nowInMS,
                          bool renderDebug) const {
  
  return (_rootNode == NULL) ? 0 : _rootNode->render(pointCloud,
                                                     _id,
                                                     rc,
                                                     glState,
                                                     frustum,
                                                     nowInMS,
                                                     renderDebug);
}


const bool XPCTree::touchesRay(const Ray& ray) const {
  return (_rootNode != NULL) && _rootNode->touchesRay(ray);
}
