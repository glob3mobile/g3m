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

class Sector;

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
  

  class PointCloud {
  private:
    const URL         _serverURL;
    const std::string _cloudName;

    const long long    _downloadPriority;
#ifdef C_CODE
    const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
    private final TimeInterval _timeToCache;
#endif
    const bool         _readExpired;

    bool _downloadingMetadata;
    bool _errorDownloadingMetadata;
    bool _errorParsingMetadata;

    long long _pointsCount;
    Sector* _sector;
    double _minHeight;
    double _maxHeight;

    std::vector<const Sector*> _startedRendering;
    std::vector<const Sector*> _stoppedRendering;

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
    _downloadingMetadata(false),
    _errorDownloadingMetadata(false),
    _errorParsingMetadata(false),
    _pointsCount(-1),
    _sector(NULL),
    _minHeight(0),
    _maxHeight(0)
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
                               const std::vector<const Tile*>* tilesStoppedRendering);

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
                               const std::vector<const Tile*>* tilesStoppedRendering);

  };


  std::vector<PointCloud*> _clouds;
  std::vector<std::string> _errors;

  TileRenderingListener* _tileRenderingListener;

protected:
  void onChangedContext();

public:
  PointCloudsRenderer()
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
                             const std::vector<const Tile*>* tilesStoppedRendering);

  TileRenderingListener* getTileRenderingListener() const {
    return _tileRenderingListener;
  }

};

#endif
