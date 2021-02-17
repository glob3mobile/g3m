//
//  XPCPointCloud.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCPointCloud_hpp
#define XPCPointCloud_hpp

#include "RCObject.hpp"
#include "URL.hpp"
#include "TimeInterval.hpp"
#include "RenderState.hpp"

class XPCPointColorizer;
class XPCMetadataListener;
class G3MContext;
class G3MRenderContext;
class GLState;
class Frustum;
class XPCMetadata;
class IDownloader;
class IBufferDownloadListener;
class IIntBuffer;
class XPCSelectionResult;
class XPCPointSelectionListener;
class Vector3D;
class Geodetic3D;
class ITimer;
class BoundingVolume;


class XPCPointCloud : public RCObject {
private:
  const URL                  _serverURL;
  const std::string          _cloudName;
  const long long            _downloadPriority;

  const TimeInterval         _timeToCache;
  const bool                 _readExpired;

  XPCPointColorizer*         _pointColorizer;
  bool                       _deletePointColorizer;

  double                     _minProjectedArea;

  float                      _pointSize;
  bool                       _dynamicPointSize;
  bool                       _depthTest;

  float                      _verticalExaggeration;
  double                     _deltaHeight;

  XPCMetadataListener*       _metadataListener;
  const bool                 _deleteMetadataListener;

  XPCPointSelectionListener* _pointSelectionListener;
  bool                       _deletePointSelectionListener;

  const bool                 _verbose;

  bool _downloadingMetadata;
  bool _errorDownloadingMetadata;
  bool _errorParsingMetadata;

  bool _canceled;

  IIntBuffer* _requiredDimensionIndices;

  XPCMetadata* _metadata;
  long long _lastRenderedCount;


  BoundingVolume* _selection;
  BoundingVolume* _fence;

  void initializePointColorizer();

protected:
  virtual ~XPCPointCloud();

public:
  XPCPointCloud(const URL& serverURL,
                const std::string& cloudName,
                long long downloadPriority,
                const TimeInterval& timeToCache,
                bool readExpired,
                XPCPointColorizer* pointColorizer,
                bool deletePointColorizer,
                const double minProjectedArea,
                float pointSize = 1.0f,
                bool dynamicPointSize = true,
                const bool depthTest = true,
                float verticalExaggeration = 1.0f,
                double deltaHeight = 0,
                XPCMetadataListener* metadataListener = NULL,
                bool deleteMetadataListener = true,
                XPCPointSelectionListener* pointSelectionListener = NULL,
                bool deletePointSelectionListener = true,
                bool verbose = false);

  const std::string getCloudName() const {
    return _cloudName;
  }

  const bool isVerbose() const {
    return _verbose;
  }

  void setPointColorizer(XPCPointColorizer* pointColorizer,
                         bool deletePointColorizer);

  void setPointSelectionListener(XPCPointSelectionListener* pointSelectionListener,
                                 bool deletePointSelectionListener);

  const bool isDynamicPointSize() const {
    return _dynamicPointSize;
  }

  void setDynamicPointSize(const bool dynamicPointSize) {
    _dynamicPointSize = dynamicPointSize;
  }

  void setDepthTest(const bool depthTest);

  void setVerticalExaggeration(const float verticalExaggeration);

  void setDeltaHeight(const double deltaHeight);

  const float getVerticalExaggeration() const {
    return _verticalExaggeration;
  }

  const double getDeltaHeight() const {
    return _deltaHeight;
  }

  const double getMinProjectedArea() const {
    return _minProjectedArea;
  }

  void setMinProjectedArea(const double minProjectedArea) {
    _minProjectedArea = minProjectedArea;
  }

  const XPCMetadata* getMetadada() const {
    return _metadata;
  }

  const float getDevicePointSize() const;

  const float getPointSize() const {
    return _pointSize;
  }

  void setPointSize(const float pointSize) {
    _pointSize = pointSize;
  }

  const bool depthTest() const {
    return _depthTest;
  }

  void initialize(const G3MContext* context);

  RenderState getRenderState(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc,
              ITimer* lastSplitTimer,
              GLState* glState,
              const Frustum* frustum,
              long long nowInMS,
              bool renderDebug);

  void errorDownloadingMetadata();
  void errorParsingMetadata();
  void parsedMetadata(XPCMetadata* metadata);

  long long requestNodeContentBuffer(IDownloader* downloader,
                                     const std::string& treeID,
                                     const std::string& nodeID,
                                     const long long deltaPriority,
                                     IBufferDownloadListener* listener,
                                     bool deleteListener) const;

  const IIntBuffer* getRequiredDimensionIndices() const;

  XPCPointColorizer* getPointsColorizer() const {
    return _pointColorizer;
  }

  const bool selectPoints(XPCSelectionResult* selectionResult);

  const bool selectedPoint(const Vector3D& cartesian,
                           const Geodetic3D& geodetic,
                           const std::string& treeID,
                           const std::string& nodeID,
                           const int pointIndex,
                           const double distanceToRay);

  void cancel();

  void setSelection(BoundingVolume* selection);
  const BoundingVolume* getSelection() const;

  void setFence(BoundingVolume* fence);
  const BoundingVolume* getFence() const;

};

#endif
