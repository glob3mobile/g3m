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


class XPCPointCloud : public RCObject {
private:
  const URL                  _serverURL;
  const std::string          _cloudName;
  const long long            _downloadPriority;

  const TimeInterval         _timeToCache;
  const bool                 _readExpired;

  XPCPointColorizer*         _pointColorizer;
  const bool                 _deletePointColorizer;

  const double               _minProjectedArea;

  const float                _pointSize;
  const bool                 _dynamicPointSize;
  const bool                 _depthTest;

  const float                _verticalExaggeration;
  const float                _deltaHeight;

  XPCMetadataListener*       _metadataListener;
  const bool                 _deleteMetadataListener;

  XPCPointSelectionListener* _pointSelectionListener;
  const bool                 _deletePointSelectionListener;

  const bool                 _verbose;

  bool _downloadingMetadata;
  bool _errorDownloadingMetadata;
  bool _errorParsingMetadata;

  bool _canceled;

  IIntBuffer* _requiredDimensionIndices;

  XPCMetadata* _metadata;
  long long _lastRenderedCount;


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
                float pointSize,
                bool dynamicPointSize,
                const bool depthTest,
                float verticalExaggeration,
                float deltaHeight,
                XPCMetadataListener* metadataListener,
                bool deleteMetadataListener,
                XPCPointSelectionListener* pointSelectionListener,
                bool deletePointSelectionListener,
                bool verbose);

  const std::string getCloudName() const {
    return _cloudName;
  }

  const bool isVerbose() const {
    return _verbose;
  }

  const bool isDynamicPointSize() const {
    return _dynamicPointSize;
  }

  const float getVerticalExaggeration() const {
    return _verticalExaggeration;
  }

  const float getDeltaHeight() const {
    return _deltaHeight;
  }

  const double getMinProjectedArea() const {
    return _minProjectedArea;
  }

  const XPCMetadata* getMetadada() const {
    return _metadata;
  }

  const float getDevicePointSize() const;

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
              bool renderDebug,
              const XPCSelectionResult* selectionResult);

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

  const bool selectPoints(XPCSelectionResult* selectionResult) const;

  const bool selectedPoint(const Vector3D& cartesian,
                           const Geodetic3D& geodetic,
                           const std::string& treeID,
                           const std::string& nodeID,
                           const int pointIndex,
                           const double distanceToRay) const;

  void cancel();

};

#endif
