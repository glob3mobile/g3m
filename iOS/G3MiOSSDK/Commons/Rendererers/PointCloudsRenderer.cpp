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
//#include "SurfaceElevationProvider.hpp"

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onDownload(const URL& url,
                                                                         IByteBuffer* buffer,
                                                                         bool expired) {
  ILogger::instance()->logInfo("Downloaded metadata for \"%s\" (bytes=%ld)",
                               _pointCloud->getCloudName().c_str(),
                               buffer->size());

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
//  const float verticalExaggeration = context->getSurfaceElevationProvider()->getVerticalExaggeration();

  const URL metadataURL(_serverURL, _cloudName + "?planet=" + planetType + "&format=binary");

  ILogger::instance()->logInfo("Downloading metadata for \"%s\"", _cloudName.c_str());

  context->getDownloader()->requestBuffer(metadataURL,
                                          _downloadPriority,
                                          _timeToCache,
                                          _readExpired,
                                          new PointCloudsRenderer::PointCloudMetadataDownloadListener(this, context->getThreadUtils()),
                                          true);
}

PointCloudsRenderer::PointCloud::~PointCloud() {
  delete _rootNode;
  delete _sector;
}

void PointCloudsRenderer::PointCloud::errorDownloadingMetadata() {
  _downloadingMetadata = false;
  _errorDownloadingMetadata = true;
}

PointCloudsRenderer::PointCloudMetadataParserAsyncTask::~PointCloudMetadataParserAsyncTask() {
  delete _sector;
  delete _buffer;
  delete _rootNode;
}

//class DebugVisitor : public PointCloudsRenderer::PointCloudNodeVisitor {
//private:
//  int _innerNodesCount;
//  int _leafNodesCount;
//public:
//  DebugVisitor() :
//  _innerNodesCount(0),
//  _leafNodesCount(0)
//  {
//  }
//
//  void visitInnerNode(const PointCloudsRenderer::PointCloudInnerNode* innerNode) {
//    printf("Inner: \"%s\"\n", innerNode->_id.c_str());
//    _innerNodesCount++;
//  }
//
//  void visitLeafNode(const PointCloudsRenderer::PointCloudLeafNode* leafNode) {
//    printf(" Leaf: \"%s\"\n", leafNode->_id.c_str());
//    _leafNodesCount++;
//  }
//
//};

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

  const int leafNodesCount = it.nextInt32();
  std::vector<PointCloudLeafNode*> leafNodes;

  for (int i = 0; i < leafNodesCount; i++) {
    const int idLength = it.nextUInt8();
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    for (int j = 0; j < idLength; j++) {
      isb->addInt( it.nextUInt8() );
    }
    const std::string id = isb->getString();
    delete isb;

    const int byteLevelsCount = it.nextUInt8();
    const int shortLevelsCount = it.nextUInt8();
    const int intLevelsCount = it.nextUInt8();
    const int levelsCountLength = (int) byteLevelsCount + shortLevelsCount + intLevelsCount;

    int* levelsCount = new int[levelsCountLength];

    for (int j = 0; j < byteLevelsCount; j++) {
      levelsCount[j] = it.nextUInt8();
    }
    for (int j = 0; j < shortLevelsCount; j++) {
      levelsCount[byteLevelsCount + j] = it.nextInt16();
    }
    for (int j = 0; j < intLevelsCount; j++) {
      levelsCount[byteLevelsCount + shortLevelsCount + j] =  it.nextInt32();
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
    IFloatBuffer* firstPointsBuffer = IFactory::instance()->createFloatBuffer( firstPointsCount * 3 * 4 );
    for (int j = 0; j < firstPointsCount; j++) {
      const float x = it.nextFloat();
      const float y = it.nextFloat();
      const float z = it.nextFloat();
      const int j3 = j * 3;
      firstPointsBuffer->rawPut(j3 + 0, x);
      firstPointsBuffer->rawPut(j3 + 1, y);
      firstPointsBuffer->rawPut(j3 + 2, z);
    }

    leafNodes.push_back( new PointCloudLeafNode(id,
                                                levelsCountLength,
                                                levelsCount,
                                                average,
                                                bounds,
                                                firstPointsBuffer) );
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

  _rootNode = _rootNode->pruneUnneededParents();

  const Box* fullBounds = _rootNode->getBounds(); // force inner-node's bounds here, in background
  ILogger::instance()->logInfo("rootNode fullBounds=%s", fullBounds->description().c_str());

  const Vector3D average = _rootNode->getAverage();
  ILogger::instance()->logInfo("rootNode average=%s", average.description().c_str());

  //  PointCloudNodeVisitor* visitor = new DebugVisitor();
  //  _rootNode->acceptVisitor(visitor);
  //  delete visitor;
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
      delete this;

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

//void PointCloudsRenderer::PointCloudInnerNode::acceptVisitor(PointCloudNodeVisitor* visitor) {
//  visitor->visitInnerNode(this);
//
//  if (_children[0] != NULL) { _children[0]->acceptVisitor(visitor); }
//  if (_children[1] != NULL) { _children[1]->acceptVisitor(visitor); }
//  if (_children[2] != NULL) { _children[2]->acceptVisitor(visitor); }
//  if (_children[3] != NULL) { _children[3]->acceptVisitor(visitor); }
//}
//
//void PointCloudsRenderer::PointCloudLeafNode::acceptVisitor(PointCloudNodeVisitor* visitor) {
//  visitor->visitLeafNode(this);
//}


PointCloudsRenderer::PointCloudInnerNode::~PointCloudInnerNode() {
  delete _children[0];
  delete _children[1];
  delete _children[2];
  delete _children[3];

  delete _bounds;
  delete _average;

  delete _mesh;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PointCloudsRenderer::PointCloudInnerNode::addLeafNode(PointCloudLeafNode* leafNode) {
  const int idLenght = _id.length();
  const int childIndex = leafNode->_id[idLenght] - '0';
  if ((idLenght + 1) == leafNode->_id.length()) {
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

void PointCloudsRenderer::PointCloudMetadataParserAsyncTask::onPostExecute(const G3MContext* context) {
  _pointCloud->parsedMetadata(_pointsCount, _sector, _minHeight, _maxHeight, _rootNode);
  _sector = NULL; // moves ownership to pointCloud
  _rootNode = NULL; // moves ownership to pointCloud
}

void PointCloudsRenderer::PointCloud::parsedMetadata(long long pointsCount,
                                                     Sector* sector,
                                                     double minHeight,
                                                     double maxHeight,
                                                     PointCloudInnerNode* rootNode) {
  _pointsCount = pointsCount;
  _sector = sector;
  _minHeight = minHeight;
  _maxHeight = maxHeight;

  _downloadingMetadata = false;
  _rootNode = rootNode;

  ILogger::instance()->logInfo("Parsed metadata for \"%s\"", _cloudName.c_str());

  if (_metadataListener != NULL) {
    _metadataListener->onMetadata(pointsCount,
                                  *sector,
                                  minHeight,
                                  maxHeight);
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
    const long long renderedCount = _rootNode->render(rc, glState, frustum, _minHeight, _maxHeight, nowInMS);

    if (_lastRenderedCount != renderedCount) {
      ILogger::instance()->logInfo("\"%s\": Rendered %ld points", _cloudName.c_str(), renderedCount);
      _lastRenderedCount = renderedCount;
    }
  }
}

long long PointCloudsRenderer::PointCloudNode::render(const G3MRenderContext* rc,
                                                      GLState* glState,
                                                      const Frustum* frustum,
                                                      double minHeight,
                                                      double maxHeight,
                                                      long long nowInMS) {
  const Box* bounds = getBounds();
  if (bounds != NULL) {
    if (bounds->touchesFrustum(frustum)) {
      if ((_projectedArea == -1) ||
          ((_lastProjectedAreaTimeInMS + 500) < nowInMS)) {
        _projectedArea = bounds->projectedArea(rc);
        _lastProjectedAreaTimeInMS = nowInMS;
      }

      const double minProjectedArea = 500;
      if (_projectedArea >= minProjectedArea) {
        const long long renderedCount = rawRender(rc,
                                                  glState,
                                                  frustum,
                                                  _projectedArea,
                                                  minHeight,
                                                  maxHeight,
                                                  nowInMS);
        _rendered = true;
        return renderedCount;
      }
    }
  }

  if (_rendered) {
    _rendered = false;
//    delete _projectedAreaTimer;
//    _projectedAreaTimer = NULL;
  }

  return 0;
}

long long PointCloudsRenderer::PointCloudInnerNode::rawRender(const G3MRenderContext* rc,
                                                              GLState* glState,
                                                              const Frustum* frustum,
                                                              const double projectedArea,
                                                              double minHeight,
                                                              double maxHeight,
                                                              long long nowInMS) {
  long long renderedCount = 0;
  for (int i = 0; i < 4; i++) {
    PointCloudNode* child = _children[i];
    if (child != NULL) {
      renderedCount += child->render(rc, glState, frustum, minHeight, maxHeight, nowInMS);
    }
  }

  if (renderedCount == 0) {
    //_bounds->render(rc, glState, _renderColor);
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
                             2,
                             Color::newFromRGBA(1, 1, 0, 1),
                             NULL, // colors
                             1,    // colorsIntensity
                             false);
    }
    _mesh->render(rc, glState);
    renderedCount = 1;
  }

  return renderedCount;
}

PointCloudsRenderer::PointCloudLeafNode::~PointCloudLeafNode() {
  delete _mesh;
#ifdef C_CODE
  delete [] _levelsCount;
#endif
  delete _average;
  delete _bounds;
  delete _firstPointsBuffer;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

long long PointCloudsRenderer::PointCloudLeafNode::rawRender(const G3MRenderContext* rc,
                                                             GLState* glState,
                                                             const Frustum* frustum,
                                                             const double projectedArea,
                                                             double minHeight,
                                                             double maxHeight,
                                                             long long nowInMS) {
  if (_mesh == NULL) {
    _mesh = new DirectMesh(GLPrimitive::points(),
                           false,
                           *_average,
                           _firstPointsBuffer,
                           1,
                           2,
                           Color::newFromRGBA(1, 1, 1, 1),
                           NULL, // colors
                           1,    // colorsIntensity
                           false);
  }
  _mesh->render(rc, glState);
  return _firstPointsBuffer->size();
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
                                        PointCloudMetadataListener* metadataListener,
                                        bool deleteListener) {
  addPointCloud(serverURL,
                cloudName,
                DownloadPriority::MEDIUM,
                TimeInterval::fromDays(30),
                true,
                metadataListener,
                deleteListener);
}

void PointCloudsRenderer::addPointCloud(const URL& serverURL,
                                        const std::string& cloudName,
                                        long long downloadPriority,
                                        const TimeInterval& timeToCache,
                                        bool readExpired,
                                        PointCloudMetadataListener* metadataListener,
                                        bool deleteListener) {
  PointCloud* pointCloud = new PointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, metadataListener, deleteListener);
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
