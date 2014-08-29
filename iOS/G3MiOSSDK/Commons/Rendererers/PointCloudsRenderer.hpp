//
//  PointCloudsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

#ifndef __G3MiOSSDK__PointCloudsRenderer__
#define __G3MiOSSDK__PointCloudsRenderer__

#include "DefaultRenderer.hpp"

#include "URL.hpp"
#include "IBufferDownloadListener.hpp"
#include "TileRenderingListener.hpp"
#include "TimeInterval.hpp"
#include "RCObject.hpp"
#include "Sector.hpp"
#include <map>

class Sector;
class IDownloader;
class ITimer;
class Box;
class Mesh;

class PointCloudsRenderer : public DefaultRenderer {
private:

  class PointCloud;


  class PointCloudMetadataDownloadListener : public IBufferDownloadListener {
  private:
    PointCloud* _pointCloud;

  public:
    PointCloudMetadataDownloadListener(PointCloud* pointCloud) :
    _pointCloud(pointCloud)
    {
    }

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired);

  };


  class TileLayout;

  class TileLayoutBufferDownloadListener : public IBufferDownloadListener {
  private:
    TileLayout* _tileLayout;

  public:
    TileLayoutBufferDownloadListener(TileLayout* tileLayout);

    ~TileLayoutBufferDownloadListener();

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired) {
      // do nothing
    }
  };


  class TileLayoutStopper : public RCObject {
  private:
    PointCloud* _pointCloud;
    const int _totalSteps;
    std::vector<std::string> _tilesToStop;

    int _stepsDone;

    ~TileLayoutStopper() {

    }

  public:
    TileLayoutStopper(PointCloud* pointCloud,
                      int totalSteps,
                      const std::vector<std::string>& tilesToStop);


    void stepDone();
  };


  class TileLayout : public RCObject {
  private:
    PointCloud* _pointCloud;

    const std::string _cloudName;
    const std::string _tileID;
    const std::string _tileQuadKey;
    TileLayoutStopper* _stopper;

    bool _isInitialized;

    IDownloader* _downloader;
    long long _layoutRequestID;

    bool _canceled;

    std::vector<std::string> _nodesIDs;

  protected:
    ~TileLayout();

  public:
    TileLayout(PointCloud* pointCloud,
               const std::string& cloudName,
               const std::string& tileID,
               const std::string& tileQuadKey,
               TileLayoutStopper* stopper);


    bool isInitialized() const {
      return _isInitialized;
    }

    void initialize(const G3MContext* context,
                    const URL& serverURL,
                    long long downloadPriority,
                    const TimeInterval& timeToCache,
                    bool readExpired);

    void onDownload(IByteBuffer* buffer);

    void onError();

    void onCancel();

    void cancel();

  };


  class PointCloudNode;

  class NodeMetadataBufferDownloadListener : public IBufferDownloadListener {
  private:
    PointCloudNode* _node;

  public:
    NodeMetadataBufferDownloadListener(PointCloudNode* node);

    ~NodeMetadataBufferDownloadListener();

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired) {
      // do nothing
    }

  };


  class PointCloudNode : public RCObject {
  private:
    PointCloud* _pointCloud;

    const std::string _cloudName;
    const std::string _id;

    bool _isInitialized;
    int _referenceCountFromPointCloud;

    IDownloader* _downloader;
    long long _metadataRequestID;

    bool _canceled;

    Box* _bounds;
    std::vector<int>* _lodLevels;
    std::vector<IFloatBuffer*>* _lodPoints;

    Vector3D* _averagePoint;

    Mesh* _mesh;
    Mesh* getMesh();

    GLState* _glState;
    void updateGLState(const G3MRenderContext* rc);

  protected:
    ~PointCloudNode();

  public:
    PointCloudNode(PointCloud* pointCloud,
                   const std::string& cloudName,
                   const std::string& id);

    void retainFromPointCloud() {
      _referenceCountFromPointCloud++;
      _retain();
    }

    bool releaseFromPointCloud() {
      _referenceCountFromPointCloud--;
      return _release();
    }

    int getReferenceCountFromPointCloud() const {
      return _referenceCountFromPointCloud;
    }

    void releaseAllFromPointCloud() {
      for (int i = 0; i < _referenceCountFromPointCloud; i++) {
        releaseFromPointCloud();
      }
    }

    bool isInitialized() const {
      return _isInitialized;
    }

    void initialize(const G3MContext* context,
                    const URL& serverURL,
                    long long downloadPriority,
                    const TimeInterval& timeToCache,
                    bool readExpired);

    void cancel();

    void onMetadataDownload(IByteBuffer* buffer);

    void onMetadataError();

    void onMetadataCancel();

    void render(const G3MRenderContext* rc,
                GLState* glState);

  };


  class PointCloud {
  private:
#ifdef C_CODE
    const URL         _serverURL;
#endif
#ifdef JAVA_CODE
    private final URL _serverURL;
#endif
    const std::string _cloudName;

    const long long    _downloadPriority;
#ifdef C_CODE
    const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
    private final TimeInterval _timeToCache;
#endif
    const bool         _readExpired;
    IDownloader* _downloader;

    bool _downloadingMetadata;
    bool _errorDownloadingMetadata;
    bool _errorParsingMetadata;

    long long _pointsCount;
    Sector* _sector;
    double _minHeight;
    double _maxHeight;

    std::vector<const Tile*> _tilesStartedRendering;
    std::vector<std::string> _tilesStoppedRendering;
    std::map<std::string, TileLayout*> _visibleTiles;
    bool _visibleTilesNeedsInitialization;
    ITimer* _initializationTimer;

    std::map<std::string, PointCloudNode*> _nodes;
    bool _nodesNeedsInitialization;

  public:
    PointCloud(const URL& serverURL,
               const std::string& cloudName,
               long long downloadPriority,
               const TimeInterval& timeToCache,
               bool readExpired) :
    _serverURL(serverURL),
    _cloudName(cloudName),
    _downloadPriority(downloadPriority),
    _timeToCache(timeToCache),
    _readExpired(readExpired),
    _downloader(NULL),
    _downloadingMetadata(false),
    _errorDownloadingMetadata(false),
    _errorParsingMetadata(false),
    _pointsCount(-1),
    _sector(NULL),
    _minHeight(0),
    _maxHeight(0),
    _visibleTilesNeedsInitialization(false),
    _nodesNeedsInitialization(false),
    _initializationTimer(NULL)
    {
    }

    ~PointCloud();

    void initialize(const G3MContext* context);

    RenderState getRenderState(const G3MRenderContext* rc);

    void errorDownloadingMetadata();

    void downloadedMetadata(IByteBuffer* buffer);

    void render(const G3MRenderContext* rc,
                GLState* glState);

    void changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                               const std::vector<std::string>* tilesStoppedRendering);

    void createNode(const std::string& nodeID);
    void removeNode(const std::string& nodeID);

    void stopTiles(const std::vector<std::string>& tilesToStop);
  };


  class PointCloudsTileRenderingListener : public TileRenderingListener {
  private:
    PointCloudsRenderer* _pointCloudsRenderer;
  public:
    PointCloudsTileRenderingListener(PointCloudsRenderer* pointCloudsRenderer) :
    _pointCloudsRenderer(pointCloudsRenderer)
    {
    }

    void changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                               const std::vector<std::string>* tilesStoppedRendering);

  };


  std::vector<PointCloud*> _clouds;
  int _cloudsSize;
  std::vector<std::string> _errors;

  TileRenderingListener* _tileRenderingListener;

  std::map<const std::string, void*> _visibleTiles;

protected:
  void onChangedContext();

public:
  PointCloudsRenderer() :
  _cloudsSize(0)
  {
    _tileRenderingListener = new PointCloudsTileRenderingListener(this);
  }

  ~PointCloudsRenderer();

  RenderState getRenderState(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName,
                     long long downloadPriority,
                     const TimeInterval& timeToCache,
                     bool readExpired);

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName);

  void removeAllPointClouds();


  void changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                             const std::vector<std::string>* tilesStoppedRendering);
  
  TileRenderingListener* getTileRenderingListener() const {
    return _tileRenderingListener;
  }
  
};

#endif
