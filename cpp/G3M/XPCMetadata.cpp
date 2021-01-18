//
//  XPCMetadata.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCMetadata.hpp"

#include "JSONObject.hpp"

#include "XPCDimension.hpp"
#include "XPCNode.hpp"


XPCMetadata* XPCMetadata::fromJSON(const JSONObject* jsonObject) {
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::vector<XPCDimension*>* dimensions = XPCDimension::fromJSON( jsonObject->getAsArray("dimensions") );

  //  const std::vector<XPCNode*>* rootNodes = XPCNode::fromJSON( jsonObject->getAsArray("rootNodes") );

  const std::vector<XPCTree*>* rootNodes = XPCNode::fromJSON( jsonObject->getAsArray("rootNodes") );

  return new XPCMetadata(dimensions, rootNodes);
}


XPCMetadata::XPCMetadata(const std::vector<XPCDimension*>* dimensions,
                         const std::vector<XPCNode*>* rootNodes) :
_dimensions(dimensions),
_rootNodes(rootNodes),
_rootNodesSize( _dimensions->size() )
{

}

XPCMetadata::~XPCMetadata() {
  for (size_t i = 0; i < _dimensions->size(); i++) {
    const XPCDimension* dimension = _dimensions->at(i);
    delete dimension;
  }
#ifdef C_CODE
  delete _dimensions;
#endif

  for (size_t i = 0; i < _rootNodes->size(); i++) {
    const XPCNode* rootNode = _rootNodes->at(i);
    delete rootNode;
  }
#ifdef C_CODE
  delete _rootNodes;
#endif
}

long long XPCMetadata::render(const XPCPointCloud* pointCloud,
                              const G3MRenderContext* rc,
                              GLState* glState,
                              const Frustum* frustum,
                              long long nowInMS) {

  long long renderedCount = 0;

  for (size_t i = 0; i < _rootNodesSize; i++) {
    XPCNode* rootNode = _rootNodes->at(i);

    renderedCount += rootNode->render(pointCloud,
                                      rc,
                                      glState,
                                      frustum,
                                      nowInMS);
  }

  return renderedCount;
}
