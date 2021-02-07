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
  _rootNode->cancel();
  _rootNode->_release();
}

void XPCTree::cancel() const {
  _rootNode->cancel();
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
  if (_rootNode == NULL) {
    return 0;
  }

  _renderingState.reset();

  return _rootNode->render(pointCloud,
                           _id,
                           rc,
                           lastSplitTimer,
                           glState,
                           frustum,
                           nowInMS,
                           renderDebug,
                           selectionResult,
                           _renderingState);
}

const bool XPCTree::selectPoints(XPCSelectionResult* selectionResult,
                                 const XPCPointCloud* pointCloud) const {
  return (_rootNode != NULL) && _rootNode->selectPoints(selectionResult, pointCloud, _id);
}
