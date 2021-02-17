//
//  XPCPointCloud.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCPointCloud.hpp"

#include "GAsyncTask.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"
#include "ILogger.hpp"
#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "IFactory.hpp"
#include "IDeviceInfo.hpp"
#include "IIntBuffer.hpp"
#include "IStringBuilder.hpp"
#include "BoundingVolume.hpp"

#include "XPCMetadata.hpp"
#include "XPCMetadataListener.hpp"
#include "XPCPointSelectionListener.hpp"
#include "XPCPointColorizer.hpp"
#include "XPCSelectionResult.hpp"


class XPCMetadataParserAsyncTask : public GAsyncTask {
private:
  XPCPointCloud* _pointCloud;
  IByteBuffer*   _buffer;
  
  XPCMetadata* _metadata;
  
public:
  XPCMetadataParserAsyncTask(XPCPointCloud* pointCloud,
                             IByteBuffer* buffer) :
  _pointCloud(pointCloud),
  _buffer(buffer),
  _metadata(NULL)
  {
    _pointCloud->_retain();
  }
  
  ~XPCMetadataParserAsyncTask() {
    _pointCloud->_release();
    
    delete _buffer;
    
    delete _metadata;
  }
  
  void runInBackground(const G3MContext* context) {
    _metadata = XPCMetadata::fromBuffer(_buffer);
    
    delete _buffer;
    _buffer = NULL;
  }
  
  void onPostExecute(const G3MContext* context) {
    if (_metadata) {
      _pointCloud->parsedMetadata(_metadata);
      _metadata = NULL; // moves ownership to pointCloud
    }
    else {
      _pointCloud->errorParsingMetadata();
    }
  }
  
};


class XPCMetadataDownloadListener : public IBufferDownloadListener {
private:
  XPCPointCloud*      _pointCloud;
  const IThreadUtils* _threadUtils;
  
public:
  
  XPCMetadataDownloadListener(XPCPointCloud* pointCloud,
                              const IThreadUtils* threadUtils) :
  _pointCloud(pointCloud),
  _threadUtils(threadUtils)
  {
    _pointCloud->_retain();
  }
  
  void onDownload(const URL& url,
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
    
    _threadUtils->invokeAsyncTask(new XPCMetadataParserAsyncTask(_pointCloud, buffer),
                                  true);
  }
  
  void onError(const URL& url) {
    _pointCloud->errorDownloadingMetadata();
  }
  
  void onCancel(const URL& url) {
    // do nothing
  }
  
  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }
  
  ~XPCMetadataDownloadListener() {
    _pointCloud->_release();
  }
  
};


XPCPointCloud::XPCPointCloud(const URL& serverURL,
                             const std::string& cloudName,
                             long long downloadPriority,
                             const TimeInterval& timeToCache,
                             bool readExpired,
                             XPCPointColorizer* pointColorizer,
                             bool deletePointColorizer,
                             const double minProjectedArea,
                             float pointSize,
                             bool dynamicPointSize,
                             const bool depthTest,
                             float verticalExaggeration,
                             double deltaHeight,
                             XPCMetadataListener* metadataListener,
                             bool deleteMetadataListener,
                             XPCPointSelectionListener* pointSelectionListener,
                             bool deletePointSelectionListener,
                             bool verbose) :
_serverURL(serverURL),
_cloudName(cloudName),
_downloadPriority(downloadPriority),
_timeToCache(timeToCache),
_readExpired(readExpired),
_pointColorizer(pointColorizer),
_deletePointColorizer(deletePointColorizer),
_minProjectedArea(minProjectedArea),
_pointSize(pointSize),
_dynamicPointSize(dynamicPointSize),
_depthTest(depthTest),
_verticalExaggeration(verticalExaggeration),
_deltaHeight(deltaHeight),
_metadataListener(metadataListener),
_deleteMetadataListener(deleteMetadataListener),
_pointSelectionListener(pointSelectionListener),
_deletePointSelectionListener(deletePointSelectionListener),
_verbose(verbose),
_downloadingMetadata(false),
_errorDownloadingMetadata(false),
_errorParsingMetadata(false),
_metadata(NULL),
_lastRenderedCount(0),
_requiredDimensionIndices(NULL),
_canceled(false),
_selection(NULL),
_fence(NULL)
{
  
}

void XPCPointCloud::initialize(const G3MContext* context) {
  _downloadingMetadata      = true;
  _errorDownloadingMetadata = false;
  _errorParsingMetadata     = false;
  
  const URL metadataURL(_serverURL, _cloudName);
  
  ILogger::instance()->logInfo("Downloading metadata for \"%s\"", _cloudName.c_str());
  
  context->getDownloader()->requestBuffer(metadataURL,
                                          _downloadPriority + 200,
                                          _timeToCache,
                                          _readExpired,
                                          new XPCMetadataDownloadListener(this, context->getThreadUtils()),
                                          true);
}

void XPCPointCloud::errorDownloadingMetadata() {
  _downloadingMetadata      = false;
  _errorDownloadingMetadata = true;
}

void XPCPointCloud::errorParsingMetadata() {
  _downloadingMetadata  = false;
  _errorParsingMetadata = true;
}

void XPCPointCloud::initializePointColorizer() {
  IIntBuffer* requiredDimensionIndices = NULL;
  if (_pointColorizer != NULL) {
    requiredDimensionIndices = _pointColorizer->initialize(_metadata);
  }
  if (_requiredDimensionIndices != requiredDimensionIndices) {
    delete _requiredDimensionIndices;
    _requiredDimensionIndices = requiredDimensionIndices;
  }
}

void XPCPointCloud::parsedMetadata(XPCMetadata* metadata) {
  _lastRenderedCount   = 0;
  _downloadingMetadata = false;
  
  ILogger::instance()->logInfo("Parsed metadata for \"%s\"", _cloudName.c_str());

  if (_canceled) {
    delete metadata;
    return;
  }
  
  if (_metadata != metadata) {
    delete _metadata;
  }
  _metadata = metadata;

  initializePointColorizer();
  
  if (_metadataListener != NULL) {
    _metadataListener->onMetadata(_metadata);
    if (_deleteMetadataListener) {
      delete _metadataListener;
    }
    _metadataListener = NULL;
  }
}


XPCPointCloud::~XPCPointCloud() {
  if (_deletePointColorizer) {
    delete _pointColorizer;
  }
  if (_deleteMetadataListener) {
    delete _metadataListener;
  }
  
  if (_deletePointSelectionListener) {
    delete _pointSelectionListener;
  }

  if (_metadata != NULL) {
    _metadata->cancel();
  }
  delete _metadata;
  
  delete _requiredDimensionIndices;

  delete _selection;

  delete _fence;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void XPCPointCloud::cancel() {
  _canceled = true;
  if (_metadata != NULL) {
    _metadata->cancel();
  }
}

const float XPCPointCloud::getDevicePointSize() const {
  return _pointSize * IFactory::instance()->getDeviceInfo()->getDevicePixelRatio();
}

RenderState XPCPointCloud::getRenderState(const G3MRenderContext* rc) {
  if (_downloadingMetadata) {
    return RenderState::busy();
  }
  
  if (_errorDownloadingMetadata) {
    return RenderState::error("Error downloading metadata of \"" + _cloudName + "\" from \"" + _serverURL._path + "\"");
  }
  
  if (_errorParsingMetadata) {
    return RenderState::error("Error parsing metadata of \"" + _cloudName + "\" from \"" + _serverURL._path + "\"");
  }
  
  return RenderState::ready();
}

const IIntBuffer* XPCPointCloud::getRequiredDimensionIndices() const {
  return _requiredDimensionIndices;
}

long long XPCPointCloud::requestNodeContentBuffer(IDownloader* downloader,
                                                  const std::string& treeID,
                                                  const std::string& nodeID,
                                                  const long long deltaPriority,
                                                  IBufferDownloadListener* listener,
                                                  bool deleteListener) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_cloudName);
  isb->addString("/");
  isb->addString(treeID);
  isb->addString("/");
  isb->addString(nodeID.empty() ? "-root-" : nodeID);
  
  if (_requiredDimensionIndices != NULL) {
    for (size_t i = 0; i < _requiredDimensionIndices->size(); i++) {
      isb->addString( (i == 0) ? "?requiredDimensionIndices=" : ",");
      isb->addInt( _requiredDimensionIndices->get(i) );
    }
  }
  
  const std::string path = isb->getString();
  delete isb;
  
  const URL nodeContentURL(_serverURL, path);
  
  return downloader->requestBuffer(nodeContentURL,
                                   _downloadPriority + deltaPriority,
                                   _timeToCache,
                                   _readExpired,
                                   listener,
                                   deleteListener);
}

void XPCPointCloud::render(const G3MRenderContext* rc,
                           ITimer* lastSplitTimer,
                           GLState* glState,
                           const Frustum* frustum,
                           long long nowInMS,
                           bool renderDebug) {
  if (_metadata != NULL) {
    const long long renderedCount = _metadata->render(this,
                                                      rc,
                                                      lastSplitTimer,
                                                      glState,
                                                      frustum,
                                                      nowInMS,
                                                      renderDebug,
                                                      _selection,
                                                      _fence);
    
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

const bool XPCPointCloud::selectPoints(XPCSelectionResult* selectionResult) {
  if ((_pointSelectionListener == NULL) || (_metadata == NULL)) {
    return false;
  }

  if (_fence != NULL) {
    if (!_fence->touchesRay(selectionResult->_ray)) {
      return false;
    }
  }

  return _metadata->selectPoints(selectionResult, this);
}

const bool XPCPointCloud::selectedPoint(const Vector3D& cartesian,
                                        const Geodetic3D& geodetic,
                                        const std::string& treeID,
                                        const std::string& nodeID,
                                        const int pointIndex,
                                        const double distanceToRay) {
  if (_pointSelectionListener == NULL) {
    return false;
  }

  return _pointSelectionListener->onSelectedPoint(this,
                                                  cartesian,
                                                  geodetic,
                                                  treeID,
                                                  nodeID,
                                                  pointIndex,
                                                  distanceToRay);
}

void XPCPointCloud::setPointColorizer(XPCPointColorizer* pointColorizer,
                                      bool deletePointColorizer) {
  if (_pointColorizer != pointColorizer) {
    if (_deletePointColorizer) {
      delete _pointColorizer;
    }

    _pointColorizer = pointColorizer;

    if (_metadata != NULL) {
      initializePointColorizer();
      _metadata->reloadNodes();
    }
  }
  _deletePointColorizer = deletePointColorizer;
}

void XPCPointCloud::setPointSelectionListener(XPCPointSelectionListener* pointSelectionListener,
                                              bool deletePointSelectionListener) {
  if (_pointSelectionListener != pointSelectionListener) {
    if (_deletePointSelectionListener) {
      delete _pointSelectionListener;
    }

    _pointSelectionListener = pointSelectionListener;
  }
  _deletePointSelectionListener = deletePointSelectionListener;
}

void XPCPointCloud::setDepthTest(const bool depthTest) {
  if (_depthTest != depthTest) {
    _depthTest = depthTest;

    if (_metadata != NULL) {
      _metadata->reloadNodes();
    }
  }
}

void XPCPointCloud::setVerticalExaggeration(const float verticalExaggeration) {
  if (_verticalExaggeration != verticalExaggeration) {
    _verticalExaggeration = verticalExaggeration;

    if (_metadata != NULL) {
      _metadata->reloadNodes();
    }
  }
}

void XPCPointCloud::setDeltaHeight(const double deltaHeight) {
  if (_deltaHeight != deltaHeight) {
    _deltaHeight = deltaHeight;

    if (_metadata != NULL) {
      _metadata->reloadNodes();
    }
  }
}

void XPCPointCloud::setFence(BoundingVolume* fence) {
  if (_fence != fence) {
    delete _fence;

    _fence = fence;

    if (_metadata != NULL) {
      _metadata->reloadNodes();
    }
  }
}

const BoundingVolume* XPCPointCloud::getFence() const {
  return _fence;
};

void XPCPointCloud::setSelection(BoundingVolume* selection) {
  if (_selection != selection) {
    delete _selection;

    _selection = selection;

    if (_metadata != NULL) {
      _metadata->reloadNodes();
    }
  }
}

const BoundingVolume* XPCPointCloud::getSelection() const {
  return _selection;
};
