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


  class TileLayout : public RCObject {
  private:
    PointCloud* _pointCloud;

    const std::string _cloudName;
    const std::string _tileID;
    const std::string _tileQuadKey;

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
               const std::string& tileQuadKey);


    bool isInitialized() const {
      return _isInitialized;
    }

    void initialize(const G3MContext* context,
                    const URL& serverURL,
                    long long downloadPriority,
                    const TimeInterval& timeToCache,
                    bool readExpired);

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void cancel();

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

    std::map<std::string, TileLayout*> _visibleTiles;
    bool _visibleTilesNeedsInitialization;
    ITimer* _initializationTimer;

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
