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
    const int treesCount = it.nextInt32();
    for (int i = 0; i < treesCount; i++) {
      const std::string id = it.nextZeroTerminatedString();

      const double lowerLatitudeDegrees  = it.nextDouble();
      const double lowerLongitudeDegrees = it.nextDouble();
      const double upperLatitudeDegrees  = it.nextDouble();
      const double upperLongitudeDegrees = it.nextDouble();

      const Sector* sector = Sector::newFromDegrees(lowerLatitudeDegrees, lowerLongitudeDegrees,
                                                    upperLatitudeDegrees, upperLongitudeDegrees);

      const double minZ = it.nextDouble();
      const double maxZ = it.nextDouble();

      XPCNode* rootNode = new XPCNode(id, sector, minZ, maxZ);

      XPCTree* tree = new XPCTree(i, rootNode);
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

XPCMetadata::~XPCMetadata() {
  for (size_t i = 0; i < _dimensions->size(); i++) {
    const XPCDimension* dimension = _dimensions->at(i);
    delete dimension;
  }
#ifdef C_CODE
  delete _dimensions;
#endif

  for (size_t i = 0; i < _trees->size(); i++) {
    const XPCTree* tree = _trees->at(i);
    delete tree;
  }
#ifdef C_CODE
  delete _trees;
#endif
}

long long XPCMetadata::render(const XPCPointCloud* pointCloud,
                              const G3MRenderContext* rc,
                              GLState* glState,
                              const Frustum* frustum,
                              long long nowInMS) {

  long long renderedCount = 0;

  for (size_t i = 0; i < _treesSize; i++) {
    const XPCTree* tree = _trees->at(i);

    renderedCount += tree->render(pointCloud,
                                  rc,
                                  glState,
                                  frustum,
                                  nowInMS);
  }

  return renderedCount;
}
