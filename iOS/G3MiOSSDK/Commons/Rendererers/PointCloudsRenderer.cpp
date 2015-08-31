//
//  PointCloudsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

#include "PointCloudsRenderer.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "Planet.hpp"
#include "DownloadPriority.hpp"
#include "Sector.hpp"
#include "ByteBufferIterator.hpp"
#include "ErrorHandling.hpp"
#include "IStringBuilder.hpp"
#include "Camera.hpp"
#include "DirectMesh.hpp"
#include "IFactory.hpp"
#include "IDeviceInfo.hpp"

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onDownload(const URL& url,
                                                                         IByteBuffer* buffer,
                                                                         bool expired) {
#ifdef C_CODE
  ILogger::instance()->logInfo("Downloaded metadata for \"%s\" (bytes=%ld)",
                               _pointCloud->getCloudName().c_str(),
                               buffer->size());
#endif
#ifdef JAVA_CODE
  ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%d)", _pointCloud.getCloudName(), buffer.size());
#endif

  _threadUtils->invokeAsyncTask(new PointCloudMetadataParserAsyncTask(_pointCloud, buffer),
                                true);
}

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onError(const URL& url) {
  _pointCloud->errorDownloadingMetadata();
}

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onCancel(const URL& url) {
  // do nothing
}

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onCanceledDownload(const URL& url,
                                                                                 IByteBuffer* buffer,
                                                                                 bool expired) {
  // do nothing
}


void PointCloudsRenderer::PointCloud::initialize(const G3MContext* context) {
  _downloadingMetadata = true;
  _errorDownloadingMetadata = false;
  _errorParsingMetadata = false;

  const std::string planetType = context->getPlanet()->getType();

  const URL metadataURL(_serverURL,
                        _cloudName +
                        "?planet=" + planetType +
                        "&verticalExaggeration=" + IStringUtils::instance()->toString(_verticalExaggeration) +
                        "&deltaHeight=" + IStringUtils::instance()->toString(_deltaHeight) +
                        "&format=binary");

  ILogger::instance()->logInfo("Downloading metadata for \"%s\"", _cloudName.c_str());

  context->getDownloader()->requestBuffer(metadataURL,
                                          _downloadPriority,
                                          _timeToCache,
                                          _readExpired,
                                          new PointCloudsRenderer::PointCloudMetadataDownloadListener(this, context->getThreadUtils()),
                                          true);
}

PointCloudsRenderer::PointCloud::~PointCloud() {
//  delete _rootNode;
  if (_rootNode != NULL) {
    _rootNode->_release();
  }
  delete _sector;
}

void PointCloudsRenderer::PointCloud::errorDownloadingMetadata() {
  _downloadingMetadata = false;
  _errorDownloadingMetadata = true;
}

PointCloudsRenderer::PointCloudMetadataParserAsyncTask::~PointCloudMetadataParserAsyncTask() {
  delete _sector;
  delete _buffer;
//  delete _rootNode;
  if (_rootNode != NULL) {
    _rootNode->_release();
  }
}

void PointCloudsRenderer::PointCloudMetadataParserAsyncTask::onPostExecute(const G3MContext* context) {
  _pointCloud->parsedMetadata(_pointsCount, _sector, _minHeight, _maxHeight, _averageHeight, _rootNode);
  _sector   = NULL; // moves ownership to pointCloud
  _rootNode = NULL; // moves ownership to pointCloud
}

PointCloudsRenderer::PointCloudLeafNode* PointCloudsRenderer::PointCloudMetadataParserAsyncTask::parseLeafNode(ByteBufferIterator& it) {
  const int idLength = it.nextUInt8();
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  for (int i = 0; i < idLength; i++) {
    isb->addInt( it.nextUInt8() );
  }
  const std::string id = isb->getString();
  delete isb;

  const int byteLevelsCount = it.nextUInt8();
  const int shortLevelsCount = it.nextUInt8();
  const int intLevelsCount = it.nextUInt8();
  const int levelsCountLength = (int) byteLevelsCount + shortLevelsCount + intLevelsCount;

  int* levelsCount = new int[levelsCountLength];

  for (int i = 0; i < byteLevelsCount; i++) {
    levelsCount[i] = it.nextUInt8();
  }
  for (int i = 0; i < shortLevelsCount; i++) {
    levelsCount[byteLevelsCount + i] = it.nextInt16();
  }
  for (int i = 0; i < intLevelsCount; i++) {
    levelsCount[byteLevelsCount + shortLevelsCount + i] =  it.nextInt32();
  }

  const float averageX = it.nextFloat();
  const float averageY = it.nextFloat();
  const float averageZ = it.nextFloat();

  const Vector3D* average = new Vector3D(averageX, averageY, averageZ);

  const double lowerX = (double) it.nextFloat() + averageX;
  const double lowerY = (double) it.nextFloat() + averageY;
  const double lowerZ = (double) it.nextFloat() + averageZ;
  const double upperX = (double) it.nextFloat() + averageX;
  const double upperY = (double) it.nextFloat() + averageY;
  const double upperZ = (double) it.nextFloat() + averageZ;
  const Box* bounds = new Box(Vector3D(lowerX, lowerY, lowerZ),
                              Vector3D(upperX, upperY, upperZ));

  const int firstPointsCount = it.nextInt32();
  IFloatBuffer* firstPointsVerticesBuffer = IFactory::instance()->createFloatBuffer( firstPointsCount * 3 );
  for (int i = 0; i < firstPointsCount; i++) {
    const float x = it.nextFloat();
    const float y = it.nextFloat();
    const float z = it.nextFloat();
    const int i3 = i * 3;
    firstPointsVerticesBuffer->rawPut(i3 + 0, x);
    firstPointsVerticesBuffer->rawPut(i3 + 1, y);
    firstPointsVerticesBuffer->rawPut(i3 + 2, z);
  }

  IFloatBuffer* firstPointsHeightsBuffer = IFactory::instance()->createFloatBuffer( firstPointsCount );
  for (int i = 0; i < firstPointsCount; i++) {
    firstPointsHeightsBuffer->rawPut(i, it.nextFloat());
  }

  return new PointCloudLeafNode(id,
                                levelsCountLength,
                                levelsCount,
                                average,
                                bounds,
                                firstPointsVerticesBuffer,
                                firstPointsHeightsBuffer);
}


void PointCloudsRenderer::PointCloudMetadataParserAsyncTask::runInBackground(const G3MContext* context) {
  ByteBufferIterator it(_buffer);

  _pointsCount = it.nextInt64();

  const double lowerLatitude  = it.nextDouble();
  const double lowerLongitude = it.nextDouble();
  const double upperLatitude  = it.nextDouble();
  const double upperLongitude = it.nextDouble();

  _sector = new Sector(Geodetic2D::fromRadians(lowerLatitude, lowerLongitude),
                       Geodetic2D::fromRadians(upperLatitude, upperLongitude));

  _minHeight = it.nextDouble();
  _maxHeight = it.nextDouble();
  _averageHeight = it.nextDouble();

  const int leafNodesCount = it.nextInt32();
  std::vector<PointCloudLeafNode*> leafNodes;

  for (int i = 0; i < leafNodesCount; i++) {
    leafNodes.push_back( parseLeafNode(it) );
  }

  if (it.hasNext()) {
    THROW_EXCEPTION("Logic error");
  }

  if (leafNodesCount != leafNodes.size()) {
    THROW_EXCEPTION("Logic error");
  }

  delete _buffer;
  _buffer = NULL;

  _rootNode = new PointCloudInnerNode("");
  for (int i = 0; i < leafNodesCount; i++) {
    _rootNode->addLeafNode( leafNodes[i] );
  }
  leafNodes.clear();

  _rootNode = _rootNode->pruneUnneededParents();

  const Box* fullBounds = _rootNode->getBounds(); // force inner-node's bounds here, in background
  ILogger::instance()->logInfo("rootNode fullBounds=%s", fullBounds->description().c_str());

  const Vector3D average = _rootNode->getAverage();
  ILogger::instance()->logInfo("rootNode average=%s", average.description().c_str());
}

PointCloudsRenderer::PointCloudInnerNode* PointCloudsRenderer::PointCloudInnerNode::pruneUnneededParents() {
  PointCloudNode* onlyChild = NULL;
  for (int i = 0; i < 4; i++) {
    PointCloudNode* child = _children[i];
    if (child != NULL) {
      if (onlyChild == NULL) {
        onlyChild = child;
      }
      else {
        return this;
      }
    }
  }

  if (onlyChild != NULL) {
    if (onlyChild->isInner()) {
      PointCloudInnerNode* result = ((PointCloudInnerNode*) onlyChild)->pruneUnneededParents();

      // forget childrens to avoid deleting them from the destructor
      for (int i = 0; i < 4; i++) { _children[i] = NULL; }
      _release();

      return result;
    }
  }

  return this;
}

void PointCloudsRenderer::PointCloudInnerNode::calculatePointsCountAndAverage() {

  _pointsCount = 0;
  double sumX = 0;
  double sumY = 0;
  double sumZ = 0;

  for (int i = 0; i < 4; i++) {
    PointCloudNode* child = _children[i];
    if (child != NULL) {
      const long long childPointsCount = child->getPointsCount();
      _pointsCount += childPointsCount;

      const Vector3D childAverage = child->getAverage();
      sumX += (childAverage._x * childPointsCount);
      sumY += (childAverage._y * childPointsCount);
      sumZ += (childAverage._z * childPointsCount);
    }
  }

  _average = new Vector3D(sumX / _pointsCount,
                          sumY / _pointsCount,
                          sumZ / _pointsCount);
}

Box* PointCloudsRenderer::PointCloudInnerNode::calculateBounds() {
  Box* bounds = NULL;
  for (int i = 0; i < 4; i++) {
    PointCloudNode* child = _children[i];
    if (child != NULL) {
      const Box* childBounds = child->getBounds();
      if (childBounds == NULL) {
        THROW_EXCEPTION("Logic error");
      }
      if (bounds == NULL) {
#ifdef C_CODE
        bounds = new Box(childBounds->_lower, childBounds->_upper);
#endif
#ifdef JAVA_CODE
        bounds = childBounds;
#endif
      }
      else {
        if (!childBounds->fullContainedInBox(bounds)) {
#ifdef C_CODE
          Box* previousBounds = bounds;
          bounds = previousBounds->mergedWithBox(childBounds);
          delete previousBounds;
#endif
#ifdef JAVA_CODE
          bounds = bounds.mergedWithBox(childBounds);
#endif
        }
      }
    }
  }
  return bounds;
}

PointCloudsRenderer::PointCloudInnerNode::~PointCloudInnerNode() {
  for (int i = 0; i < 4; i++) {
    PointCloudNode* child = _children[i];
    if (child != NULL) {
      child->_release();
    }
  }

  delete _bounds;
  delete _average;

  delete _mesh;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PointCloudsRenderer::PointCloudInnerNode::addLeafNode(PointCloudLeafNode* leafNode) {
  const int idLength = _id.length();
  const int childIndex = leafNode->_id[idLength] - '0';
  if ((idLength + 1) == leafNode->_id.length()) {
    if (_children[childIndex] != NULL) {
      THROW_EXCEPTION("Logic error!");
    }
    _children[childIndex] = leafNode;
  }
  else {
    PointCloudInnerNode* innerChild = (PointCloudInnerNode*) (_children[childIndex]);
    if (innerChild == NULL) {
      IStringBuilder* isb = IStringBuilder::newStringBuilder();
      isb->addString(_id);
      isb->addInt(childIndex);
      const std::string childID = isb->getString();
      delete isb;

      innerChild = new PointCloudInnerNode(childID);
      _children[childIndex] = innerChild;
    }
    innerChild->addLeafNode(leafNode);
  }
}

void PointCloudsRenderer::PointCloud::parsedMetadata(long long pointsCount,
                                                     Sector* sector,
                                                     double minHeight,
                                                     double maxHeight,
                                                     double averageHeight,
                                                     PointCloudInnerNode* rootNode) {
  _pointsCount = pointsCount;
  _sector = sector;
  _minHeight = minHeight;
  _maxHeight = maxHeight;
  _averageHeight = averageHeight;

  _downloadingMetadata = false;
  _rootNode = rootNode;

  ILogger::instance()->logInfo("Parsed metadata for \"%s\"", _cloudName.c_str());

  if (_metadataListener != NULL) {
    _metadataListener->onMetadata(_pointsCount,
                                  *sector,
                                  _minHeight,
                                  _maxHeight,
                                  _averageHeight);
    if (_deleteListener) {
      delete _metadataListener;
    }
    _metadataListener = NULL;
  }

}

RenderState PointCloudsRenderer::PointCloud::getRenderState(const G3MRenderContext* rc) {
  if (_downloadingMetadata) {
    return RenderState::busy();
  }

  if (_errorDownloadingMetadata) {
    return RenderState::error("Error downloading metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
  }

  if (_errorParsingMetadata) {
    return RenderState::error("Error parsing metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
  }

  return RenderState::ready();
}

void PointCloudsRenderer::PointCloud::render(const G3MRenderContext* rc,
                                             GLState* glState,
                                             const Frustum* frustum,
                                             long long nowInMS) {
  if (_rootNode != NULL) {
// #warning TODO: make plugable the colorization of the cloud
#ifdef C_CODE
    const double maxHeight = (_colorPolicy == MIN_MAX_HEIGHT) ? _maxHeight : _averageHeight * 3;
#endif
#ifdef JAVA_CODE
    final double maxHeight = (_colorPolicy == ColorPolicy.MIN_MAX_HEIGHT) ? _maxHeight : _averageHeight * 3;
#endif

    const long long renderedCount = _rootNode->render(this, rc, glState, frustum, _minHeight, maxHeight, _pointSize, nowInMS);
    // const long long renderedCount = _rootNode->render(this, rc, glState, frustum, _minHeight, _averageHeight * 3, _pointSize, nowInMS);
    // const long long renderedCount = _rootNode->render(this, rc, glState, frustum, _minHeight, _maxHeight, _pointSize, nowInMS);

    if (_lastRenderedCount != renderedCount) {
      if (_verbose) {
#ifdef C_CODE
        ILogger::instance()->logInfo("\"%s\": Rendered %ld points", _cloudName.c_str(), renderedCount);
#endif
#ifdef JAVA_CODE
        ILogger.instance().logInfo("\"%s\": Rendered %d points", _cloudName, renderedCount);
#endif
      }
      _lastRenderedCount = renderedCount;
    }
  }
}

long long PointCloudsRenderer::PointCloudNode::render(const PointCloud* pointCloud,
                                                      const G3MRenderContext* rc,
                                                      GLState* glState,
                                                      const Frustum* frustum,
                                                      double minHeight,
                                                      double maxHeight,
                                                      float pointSize,
                                                      long long nowInMS) {
  const Box* bounds = getBounds();
  if (bounds != NULL) {
    if (bounds->touchesFrustum(frustum)) {
      bool justRecalculatedProjectedArea = false;
      if ((_projectedArea == -1) ||
          ((_lastProjectedAreaTimeInMS + 350) < nowInMS)) {
        const double currentProjectedArea = bounds->projectedArea(rc);
        if (currentProjectedArea != _projectedArea) {
          _projectedArea = currentProjectedArea;
          _lastProjectedAreaTimeInMS = nowInMS;
          justRecalculatedProjectedArea = true;
        }
      }

// #warning TODO: quality factor 1
      const double minProjectedArea = 250;
      if (_projectedArea >= minProjectedArea) {
        const long long renderedCount = rawRender(pointCloud,
                                                  rc,
                                                  glState,
                                                  frustum,
                                                  _projectedArea,
                                                  minHeight,
                                                  maxHeight,
                                                  pointSize,
                                                  nowInMS,
                                                  justRecalculatedProjectedArea);
        _rendered = true;
        return renderedCount;
      }
    }
  }

  if (_rendered) {
    stoppedRendering(rc);
    _rendered = false;
  }

  return 0;
}

long long PointCloudsRenderer::PointCloudInnerNode::rawRender(const PointCloud* pointCloud,
                                                              const G3MRenderContext* rc,
                                                              GLState* glState,
                                                              const Frustum* frustum,
                                                              const double projectedArea,
                                                              double minHeight,
                                                              double maxHeight,
                                                              float pointSize,
                                                              long long nowInMS,
                                                              bool justRecalculatedProjectedArea) {
  long long renderedCount = 0;
  for (int i = 0; i < 4; i++) {
    PointCloudNode* child = _children[i];
    if (child != NULL) {
      renderedCount += child->render(pointCloud, rc, glState, frustum, minHeight, maxHeight, pointSize, nowInMS);
    }
  }

  if (renderedCount == 0) {
    if (_mesh == NULL) {
      const Vector3D average = getAverage();

      const float averageX = (float) average._x;
      const float averageY = (float) average._y;
      const float averageZ = (float) average._z;

      IFloatBuffer* pointsBuffer = rc->getFactory()->createFloatBuffer(3);
      pointsBuffer->rawPut(0, (float) (average._x - averageX) );
      pointsBuffer->rawPut(1, (float) (average._y - averageY) );
      pointsBuffer->rawPut(2, (float) (average._z - averageZ) );

      _mesh = new DirectMesh(GLPrimitive::points(),
                             true,
                             Vector3D(averageX, averageY, averageZ),
                             pointsBuffer,
                             1,
                             pointSize * 2,
                             Color::newFromRGBA(1, 1, 0, 1), // flatColor
                             NULL, // colors
                             1,    // colorsIntensity
                             true);
    }
    _mesh->render(rc, glState);
    renderedCount = 1;
  }
  else {
    if (_mesh != NULL) {
      delete _mesh;
      _mesh = NULL;
    }
  }

  return renderedCount;
}

void PointCloudsRenderer::PointCloudInnerNode::stoppedRendering(const G3MRenderContext* rc) {
  delete _mesh;
  _mesh = NULL;

  for (int i = 0; i < 4; i++) {
    PointCloudNode* child = _children[i];
    if (child != NULL) {
      child->stoppedRendering(rc);
    }
  }
}

PointCloudsRenderer::PointCloudLeafNode::~PointCloudLeafNode() {
  for (int i = 0; i < _levelsCount; i++) {
    delete _levelsVerticesBuffers[i];
    delete _levelsHeightsBuffers[i];
  }
#ifdef C_CODE
  delete [] _levelsVerticesBuffers;
  delete [] _levelsHeightsBuffers;
  delete [] _levelsPointsCount;
#endif
  delete _average;
  delete _bounds;
  delete _firstPointsVerticesBuffer;
  delete _firstPointsHeightsBuffer;
  delete _firstPointsColorsBuffer;
  delete _mesh;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

int PointCloudsRenderer::PointCloudLeafNode::calculateCurrentLoadedLevel() const {
  const int loadedPointsCount = _firstPointsVerticesBuffer->size() / 3;
  int accummulated = 0;
  for (int i = 0; i < _levelsCount; i++) {
    const int levelPointsCount = _levelsPointsCount[i];
    accummulated += levelPointsCount;
    if (accummulated == loadedPointsCount) {
      return i;
    }
  }
  return -1;
}


long long PointCloudsRenderer::PointCloud::requestBufferForLevel(const G3MRenderContext* rc,
                                                                 const std::string& nodeID,
                                                                 int level,
                                                                 IBufferDownloadListener *listener,
                                                                 bool deleteListener) const {

  const std::string planetType = rc->getPlanet()->getType();

  const URL url(_serverURL,
                _cloudName +
                "/" + nodeID +
                "/" + IStringUtils::instance()->toString(level) +
                "?planet=" + planetType +
                "&verticalExaggeration=" + IStringUtils::instance()->toString(_verticalExaggeration) +
                "&deltaHeight=" + IStringUtils::instance()->toString(_deltaHeight) +
                "&format=binary");

  //  ILogger::instance()->logInfo("Downloading metadata for \"%s\"", _cloudName.c_str());

  return rc->getDownloader()->requestBuffer(url,
                                            _downloadPriority - level,
                                            _timeToCache,
                                            _readExpired,
                                            listener,
                                            deleteListener);
}


void PointCloudsRenderer::PointCloudLeafNodeLevelParserTask::runInBackground(const G3MContext* context) {
  ByteBufferIterator it(_buffer);

  const int pointsCount = it.nextInt32();

  _verticesBuffer = IFactory::instance()->createFloatBuffer( pointsCount * 3 );
  for (int i = 0; i < pointsCount * 3; i++) {
    _verticesBuffer->rawPut(i, it.nextFloat());
  }

  _heightsBuffer = IFactory::instance()->createFloatBuffer( pointsCount );
  for (int i = 0; i < pointsCount; i++) {
    _heightsBuffer->rawPut(i, it.nextFloat());
  }

  if (it.hasNext()) {
    ILogger::instance()->logError("Logic error");
  }

  delete _buffer;
  _buffer = NULL;
}

void PointCloudsRenderer::PointCloudLeafNodeLevelParserTask::onPostExecute(const G3MContext* context) {
  _leafNode->onLevelBuffersDownload(_level, _verticesBuffer, _heightsBuffer);
  _verticesBuffer = NULL; // moves ownership to _leafNode
  _heightsBuffer  = NULL; // moves ownership to _leafNode
}


void PointCloudsRenderer::PointCloudLeafNodeLevelListener::onDownload(const URL& url,
                                                                      IByteBuffer* buffer,
                                                                      bool expired) {
  _threadUtils->invokeAsyncTask(new PointCloudLeafNodeLevelParserTask(_leafNode,
                                                                      _level,
                                                                      buffer),
                                true);
}

void PointCloudsRenderer::PointCloudLeafNodeLevelListener::onError(const URL& url) {
  _leafNode->onLevelBufferError(_level);
}

void PointCloudsRenderer::PointCloudLeafNodeLevelListener::onCancel(const URL& url) {
  _leafNode->onLevelBufferCancel(_level);
}

void PointCloudsRenderer::PointCloudLeafNode::onLevelBuffersDownload(int level,
                                                                     IFloatBuffer* verticesBuffer,
                                                                     IFloatBuffer* heightsBuffer) {
  _loadingLevel = -1;
  _loadingLevelRequestID = -1;

  const int levelCount = _levelsPointsCount[level];

  if ((verticesBuffer->size() / 3 != levelCount) ||
      (heightsBuffer->size() != levelCount)) {
    ILogger::instance()->logError("Invalid buffer size");
    delete verticesBuffer;
    delete heightsBuffer;
  }
  else {
    //ILogger::instance()->logInfo("-> loaded level %s/%d (needed=%d)",  _id.c_str(), level, _neededLevel);

    if ((_levelsVerticesBuffers[level] != NULL) ||
        (_levelsHeightsBuffers[level]  != NULL)) {
//      THROW_EXCEPTION("Logic error");

      delete _levelsVerticesBuffers[level];
      _levelsVerticesBuffers[level] = NULL;
      delete _levelsHeightsBuffers[level];
      _levelsHeightsBuffers[level]  = NULL;
    }

    if (_currentLoadedLevel+1 != level) {
      delete verticesBuffer;
      delete heightsBuffer;
      return;
    }

    _levelsVerticesBuffers[level] = verticesBuffer;
    _levelsHeightsBuffers[level]  = heightsBuffer;

    _currentLoadedLevel = level;
    //ILogger::instance()->logInfo("node %s changed _currentLoadedLevel=%d (1)", _id.c_str(), _currentLoadedLevel);

    delete _mesh;
    _mesh = NULL;
  }

}

void PointCloudsRenderer::PointCloudLeafNode::onLevelBufferError(int level) {
  _loadingLevel = -1;
  _loadingLevelRequestID = -1;
}

void PointCloudsRenderer::PointCloudLeafNode::onLevelBufferCancel(int level) {
  _loadingLevel = -1;
  _loadingLevelRequestID = -1;
}

DirectMesh* PointCloudsRenderer::PointCloudLeafNode::createMesh(double minHeight,
                                                                double maxHeight,
                                                                float pointSize) {
  const int firstPointsVerticesBufferSize = _firstPointsVerticesBuffer->size();

  const Color baseColor = Color::magenta();
  const int wheelSize = 2147483647;
  const IMathUtils* mu = IMathUtils::instance();

  if (_currentLoadedLevel <= _preloadedLevel) {
    const int firstPointsCount = firstPointsVerticesBufferSize / 3;

    if (_firstPointsColorsBuffer == NULL) {
      const double deltaHeight = maxHeight - minHeight;

      _firstPointsColorsBuffer = IFactory::instance()->createFloatBuffer( firstPointsCount * 4 );
      for (int i = 0; i < firstPointsCount; i++) {
        const float height = _firstPointsHeightsBuffer->get(i);
        const float alpha = (float) ((height - minHeight) / deltaHeight);

        const Color color = baseColor.wheelStep(wheelSize,
                                                mu->round(wheelSize * alpha) );

        const int i4 = i*4;
        _firstPointsColorsBuffer->rawPut(i4 + 0, color._red);
        _firstPointsColorsBuffer->rawPut(i4 + 1, color._green);
        _firstPointsColorsBuffer->rawPut(i4 + 2, color._blue);
        _firstPointsColorsBuffer->rawPut(i4 + 3, color._alpha);
      }
    }

    DirectMesh* mesh = new DirectMesh(GLPrimitive::points(),
                                      false,
                                      *_average,
                                      _firstPointsVerticesBuffer,
                                      1,
                                      pointSize,
                                      NULL,                     // flatColor
                                      _firstPointsColorsBuffer, // colors
                                      1,                        // colorsIntensity
                                      true);
    mesh->setRenderVerticesCount( mu->min(_neededPoints, firstPointsCount) );

    return mesh;
  }

  int pointsCount = 0;
  for (int i = 0; i <= _currentLoadedLevel; i++) {
    pointsCount += _levelsPointsCount[i];
  }

  IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer( pointsCount * 3 );

  vertices->rawPut(0, _firstPointsVerticesBuffer);
  int cursor = firstPointsVerticesBufferSize;
  for (int level = _preloadedLevel+1; level <= _currentLoadedLevel; level++) {
    IFloatBuffer* levelVerticesBuffers = _levelsVerticesBuffers[level];
    if (levelVerticesBuffers != NULL) {
      vertices->rawPut(cursor, levelVerticesBuffers);
      cursor += levelVerticesBuffers->size();
    }
  }

  IFloatBuffer* colors   = IFactory::instance()->createFloatBuffer( pointsCount * 4 );
  const double deltaHeight = maxHeight - minHeight;
  const int firstPointsCount = firstPointsVerticesBufferSize / 3;

  for (int i = 0; i < firstPointsCount; i++) {
    const float height = _firstPointsHeightsBuffer->get(i);
    const float alpha = (float) ((height - minHeight) / deltaHeight);

    const Color color = baseColor.wheelStep(wheelSize,
                                            mu->round(wheelSize * alpha) );

    const int i4 = i*4;
    colors->rawPut(i4 + 0, color._red);
    colors->rawPut(i4 + 1, color._green);
    colors->rawPut(i4 + 2, color._blue);
    colors->rawPut(i4 + 3, color._alpha);
  }

  cursor = firstPointsCount * 4;
  for (int level = _preloadedLevel+1; level <= _currentLoadedLevel; level++) {
    IFloatBuffer* levelHeightsBuffers = _levelsHeightsBuffers[level];
    if (levelHeightsBuffers != NULL) {
      for (int i = 0; i < _levelsPointsCount[level]; i++) {
        const float height = levelHeightsBuffers->get(i);
        const float alpha = (float) ((height - minHeight) / deltaHeight);

        const Color color = baseColor.wheelStep(wheelSize,
                                                mu->round(wheelSize * alpha) );

        const int offset = cursor + i*4;
        colors->rawPut(offset + 0, color._red);
        colors->rawPut(offset + 1, color._green);
        colors->rawPut(offset + 2, color._blue);
        colors->rawPut(offset + 3, color._alpha);
      }
      cursor += _levelsPointsCount[level] * 4;
    }
  }

  DirectMesh* mesh = new DirectMesh(GLPrimitive::points(),
                                    true,
                                    *_average,
                                    vertices,
                                    1,
                                    pointSize,
                                    NULL,   // flatColor
                                    colors, // colors
                                    1,      // colorsIntensity
                                    true);
  // mesh->setRenderVerticesCount( mu->min(_neededPoints, firstPointsCount) );
  mesh->setRenderVerticesCount( pointsCount );

  return mesh;
}


long long PointCloudsRenderer::PointCloudLeafNode::rawRender(const PointCloud* pointCloud,
                                                             const G3MRenderContext* rc,
                                                             GLState* glState,
                                                             const Frustum* frustum,
                                                             const double projectedArea,
                                                             double minHeight,
                                                             double maxHeight,
                                                             float pointSize,
                                                             long long nowInMS,
                                                             bool justRecalculatedProjectedArea) {

  if (justRecalculatedProjectedArea) {
// #warning TODO: quality factor 2
    const int intendedPointsCount = IMathUtils::instance()->round((float) projectedArea * 0.09f);
    // const int intendedPointsCount = IMathUtils::instance()->round((float) projectedArea * 0.25f);
    int accummulated = 0;
    int neededLevel = -1;
    int neededPoints = -1;
    for (int i = 0; i < _levelsCount; i++) {
      const int levelPointsCount = _levelsPointsCount[i];
      neededPoints = accummulated;
      accummulated += levelPointsCount;
      if (accummulated > intendedPointsCount) {
        break;
      }
      neededLevel = i;
    }

    if (neededLevel != _neededLevel) {
//      ILogger::instance()->logInfo("Needed Level changed for %s from=%d to=%d, needed points=%d, projectedArea=%f",
//                                   _id.c_str(),
//                                   _neededLevel,
//                                   neededLevel,
//                                   neededPoints,
//                                   projectedArea);
      _neededLevel = neededLevel;
      _neededPoints = neededPoints;
      if (_mesh != NULL) {
        _mesh->setRenderVerticesCount( IMathUtils::instance()->min(_neededPoints, _mesh->getRenderVerticesCount()) );
      }
    }
  }

  if ((_loadingLevel >= 0) &&
      (_neededLevel < _loadingLevel) &&
      (_loadingLevelRequestID >= 0)) {
    rc->getDownloader()->cancelRequest(_loadingLevelRequestID);
    _loadingLevelRequestID = -1;
  }
  else {
    if (_neededLevel > _currentLoadedLevel) {
      if (_loadingLevel < 0) {
        _loadingLevel = _currentLoadedLevel + 1;

        _loadingLevelRequestID = pointCloud->requestBufferForLevel(rc,
                                                                   _id,
                                                                   _loadingLevel,
                                                                   new PointCloudLeafNodeLevelListener(this,
                                                                                                       _loadingLevel,
                                                                                                       rc->getThreadUtils()),
                                                                   true);
      }
    }
    if ((_neededLevel < _currentLoadedLevel) &&
        (_neededLevel > _preloadedLevel)) {
      for (int i = _neededLevel+1; i <= _currentLoadedLevel; i++) {
        delete _levelsVerticesBuffers[i];
        _levelsVerticesBuffers[i] = NULL;
        delete _levelsHeightsBuffers[i];
        _levelsHeightsBuffers[i] = NULL;
      }
      _currentLoadedLevel = _neededLevel;
      //ILogger::instance()->logInfo("node %s changed _currentLoadedLevel=%d (2)", _id.c_str(), _currentLoadedLevel);
      delete _mesh;
      _mesh = NULL;
    }
  }


  if (_mesh == NULL) {
    _mesh = createMesh(minHeight, maxHeight, pointSize);
  }
  _mesh->render(rc, glState);
//#warning remove debug code
//  getBounds()->render(rc, glState, Color::blue());
  return _mesh->getRenderVerticesCount();
}


void PointCloudsRenderer::PointCloudLeafNode::stoppedRendering(const G3MRenderContext* rc) {
  if (_loadingLevelRequestID >= 0) {
//    ILogger::instance()->logInfo("Canceling level request");
    rc->getDownloader()->cancelRequest(_loadingLevelRequestID);
    _loadingLevelRequestID = -1;
  }

  delete _mesh;
  _mesh = NULL;

  delete _firstPointsColorsBuffer;
  _firstPointsColorsBuffer = NULL;

  if (_currentLoadedLevel > _preloadedLevel) {
    _loadingLevel = -1;
    if (_loadingLevelRequestID >= 0) {
      rc->getDownloader()->cancelRequest(_loadingLevelRequestID);
      _loadingLevelRequestID = -1;
    }

    for (int i = 0; i < _levelsCount; i++) {
      delete _levelsVerticesBuffers[i];
      _levelsVerticesBuffers[i] = NULL;

      delete _levelsHeightsBuffers[i];
      _levelsHeightsBuffers[i] = NULL;
    }
    _currentLoadedLevel = _preloadedLevel;
    //ILogger::instance()->logInfo("node %s changed _currentLoadedLevel=%d (3)", _id.c_str(), _currentLoadedLevel);
  }
}


PointCloudsRenderer::PointCloudsRenderer() :
_cloudsSize(0),
_glState(new GLState()),
_timer(NULL)
{
}

PointCloudsRenderer::~PointCloudsRenderer() {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    delete cloud;
  }

  _glState->_release();
  delete _timer;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PointCloudsRenderer::onChangedContext() {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->initialize(_context);
  }
}

RenderState PointCloudsRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    const RenderState childRenderState = cloud->getRenderState(rc);

    const RenderState_Type childRenderStateType = childRenderState._type;

    if (childRenderStateType == RENDER_ERROR) {
      errorFlag = true;

      const std::vector<std::string> childErrors = childRenderState.getErrors();
#ifdef C_CODE
      _errors.insert(_errors.end(),
                     childErrors.begin(),
                     childErrors.end());
#endif
#ifdef JAVA_CODE
      _errors.addAll(childErrors);
#endif
    }
    else if (childRenderStateType == RENDER_BUSY) {
      busyFlag = true;
    }
  }

  if (errorFlag) {
    return RenderState::error(_errors);
  }
  else if (busyFlag) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}

void PointCloudsRenderer::addPointCloud(const URL& serverURL,
                                        const std::string& cloudName,
                                        ColorPolicy colorPolicy,
                                        float pointSize,
                                        float verticalExaggeration,
                                        double deltaHeight,
                                        PointCloudMetadataListener* metadataListener,
                                        bool deleteListener,
                                        bool verbose) {
  addPointCloud(serverURL,
                cloudName,
                DownloadPriority::MEDIUM,
                TimeInterval::fromDays(30),
                true,
                colorPolicy,
                pointSize,
                verticalExaggeration,
                deltaHeight,
                metadataListener,
                deleteListener,
                verbose);
}

void PointCloudsRenderer::addPointCloud(const URL& serverURL,
                                        const std::string& cloudName,
                                        long long downloadPriority,
                                        const TimeInterval& timeToCache,
                                        bool readExpired,
                                        ColorPolicy colorPolicy,
                                        float pointSize,
                                        float verticalExaggeration,
                                        double deltaHeight,
                                        PointCloudMetadataListener* metadataListener,
                                        bool deleteListener,
                                        bool verbose) {
  PointCloud* pointCloud = new PointCloud(serverURL,
                                          cloudName,
                                          verticalExaggeration,
                                          deltaHeight,
                                          colorPolicy,
                                          pointSize,
                                          downloadPriority,
                                          timeToCache,
                                          readExpired,
                                          metadataListener,
                                          deleteListener,
                                          verbose);
  if (_context != NULL) {
    pointCloud->initialize(_context);
  }
  _clouds.push_back(pointCloud);
  _cloudsSize = _clouds.size();
}

void PointCloudsRenderer::removeAllPointClouds() {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    delete cloud;
  }
  _clouds.clear();
  _cloudsSize = _clouds.size();
}

void PointCloudsRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState) {
  if (_cloudsSize > 0) {
//    const IDeviceInfo* deviceInfo = IFactory::instance()->getDeviceInfo();
//    const float deviceQualityFactor = deviceInfo->getQualityFactor();
////    const double factor = _tilesRenderParameters->_texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
////    const double correctionFactor = (deviceInfo->getDPI() * deviceQualityFactor) / factor;
//    const double correctionFactor = (deviceInfo->getDPI() * deviceQualityFactor) / 256;

    if (_timer == NULL) {
      _timer = rc->getFactory()->createTimer();
    }
    const long long nowInMS = _timer->elapsedTimeInMilliseconds();

    const Camera* camera = rc->getCurrentCamera();
    ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
    if (f == NULL) {
      _glState->addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else {
      f->setMatrix(camera->getModelViewMatrix44D());
    }

    _glState->setParent(glState);

    const Frustum* frustum = camera->getFrustumInModelCoordinates();
    for (int i = 0; i < _cloudsSize; i++) {
      PointCloud* cloud = _clouds[i];
      cloud->render(rc, _glState, frustum, nowInMS);
    }
  }
}
