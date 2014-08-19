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

    bool _downloadingMetadata;
    bool _errorDownloadingMetadata;
    bool _errorParsingMetadata;

    long long _pointsCount;
    Sector* _sector;
    double _minHeight;
    double _maxHeight;

  public:
    PointCloud(const URL& serverURL,
               const std::string& cloudName) :
    _serverURL(serverURL),
    _cloudName(cloudName),
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
  };


  std::vector<PointCloud*> _clouds;
  std::vector<std::string> _errors;


protected:
  void onChangedContext();

public:

  ~PointCloudsRenderer();

  RenderState getRenderState(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName);

  void removeAllPointClouds();
  
};

#endif
