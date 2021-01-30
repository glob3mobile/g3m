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

const double XPCTree::getMinHeight() const {
  return _rootNode->getMinHeight();
}

const double XPCTree::getMaxHeight() const {
  return _rootNode->getMaxHeight();
}

long long XPCTree::render(const XPCPointCloud* pointCloud,
                          const G3MRenderContext* rc,
                          ITimer* lastSplitTimer,
                          GLState* glState,
                          const Frustum* frustum,
                          long long nowInMS,
                          bool renderDebug,
                          const XPCSelectionResult* selectionResult) const {
  return (_rootNode == NULL) ? 0 : _rootNode->render(pointCloud,
                                                     _id,
                                                     rc,
                                                     lastSplitTimer,
                                                     glState,
                                                     frustum,
                                                     nowInMS,
                                                     renderDebug,
                                                     selectionResult);
}

const bool XPCTree::selectPoints(XPCSelectionResult* selectionResult,
                                 const XPCPointCloud* pointCloud) const {
  return (_rootNode != NULL) && _rootNode->selectPoints(selectionResult, pointCloud, _id);
}
