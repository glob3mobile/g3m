//
//  XPCMetadata.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCMetadata.hpp"

#include "ByteBufferIterator.hpp"
#include "ILogger.hpp"
#include "Sector.hpp"
#include "ErrorHandling.hpp"
#include "IStringUtils.hpp"

#include "XPCDimension.hpp"
#include "XPCNode.hpp"
#include "XPCTree.hpp"


XPCMetadata* XPCMetadata::fromBuffer(const IByteBuffer* buffer) {
  if (buffer == NULL) {
    return NULL;
  }

  ByteBufferIterator it(buffer);


  unsigned char version = it.nextUInt8();
  if (version != 1) {
    ILogger::instance()->logError("Unssuported format version");
    return NULL;
  }

  std::vector<XPCDimension*>* dimensions = new std::vector<XPCDimension*>();
  {
    const int dimensionsCount = it.nextInt32();
    for (int i = 0; i < dimensionsCount; i++) {
      const std::string name = it.nextZeroTerminatedString();
      unsigned char     size = it.nextUInt8();
      const std::string type = it.nextZeroTerminatedString();

      dimensions->push_back( new XPCDimension(name, size, type) );
    }
  }

  std::vector<XPCTree*>* trees = new std::vector<XPCTree*>();
  {
    const IStringUtils* su = IStringUtils::instance();

    const int treesCount = it.nextInt32();
    for (int i = 0; i < treesCount; i++) {
      const std::string treeID = su->toString(i);

      XPCNode* rootNode = XPCNode::fromByteBufferIterator(it);

      XPCTree* tree = new XPCTree(treeID, rootNode);
      trees->push_back(tree);
    }
  }

  if (it.hasNext()) {
    THROW_EXCEPTION("Logic error");
  }

  return new XPCMetadata(dimensions, trees);
}


XPCMetadata::XPCMetadata(const std::vector<XPCDimension*>* dimensions,
                         const std::vector<XPCTree*>*      trees) :
_dimensions(dimensions),
_trees(trees),
_treesSize( _trees->size() )
{

}


const size_t XPCMetadata::getTreesCount() const {
  return _treesSize;
}

const XPCTree* XPCMetadata::getTree(const size_t i) const {
  return _trees->at(i);
}

const size_t XPCMetadata::getDimensionsCount() const {
  return _dimensions->size();
}

const XPCDimension* XPCMetadata::getDimension(const size_t i) const {
  return _dimensions->at(i);
}


XPCMetadata::~XPCMetadata() {
  for (size_t i = 0; i < _trees->size(); i++) {
    const XPCTree* tree = _trees->at(i);
    delete tree;
  }
#ifdef C_CODE
  delete _trees;
#endif

  for (size_t i = 0; i < _dimensions->size(); i++) {
    const XPCDimension* dimension = _dimensions->at(i);
    delete dimension;
  }
#ifdef C_CODE
  delete _dimensions;
#endif
}

long long XPCMetadata::render(const XPCPointCloud* pointCloud,
                              const G3MRenderContext* rc,
                              ITimer* lastSplitTimer,
                              GLState* glState,
                              const Frustum* frustum,
                              long long nowInMS,
                              bool renderDebug,
                              const XPCSelectionResult* selectionResult) {

  long long renderedCount = 0;

  for (size_t i = 0; i < _treesSize; i++) {
    const XPCTree* tree = _trees->at(i);

    renderedCount += tree->render(pointCloud,
                                  rc,
                                  lastSplitTimer,
                                  glState,
                                  frustum,
                                  nowInMS,
                                  renderDebug,
                                  selectionResult);
  }

  return renderedCount;
}

const bool XPCMetadata::selectPoints(XPCSelectionResult* selectionResult,
                                     const XPCPointCloud* pointCloud) const {
  bool selectedPoints = false;
  for (size_t i = 0; i < _treesSize; i++) {
    const XPCTree* tree = _trees->at(i);
    if (tree->selectPoints(selectionResult, pointCloud)) {
      selectedPoints = true;
    }
  }
  return selectedPoints;
}
