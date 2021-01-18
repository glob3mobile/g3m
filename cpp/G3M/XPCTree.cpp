//
//  XPCTree.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/18/21.
//

#include "XPCTree.hpp"

#include "XPCNode.hpp"


XPCTree::XPCTree(const int id,
                 XPCNode* rootNode) :
_id(id),
_rootNode(rootNode)
{
  
}


XPCTree::~XPCTree() {
  delete _rootNode;
}


long long XPCTree::render(const XPCPointCloud* pointCloud,
                          const G3MRenderContext* rc,
                          GLState* glState,
                          const Frustum* frustum,
                          long long nowInMS) const {
  
  return (_rootNode == NULL) ? 0 : _rootNode->render(pointCloud,
                                                     rc,
                                                     glState,
                                                     frustum,
                                                     nowInMS);
}
