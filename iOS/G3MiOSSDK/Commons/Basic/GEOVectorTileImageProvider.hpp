//
//  GEOVectorTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/17/14.
//
//

#ifndef __G3MiOSSDK__GEOVectorTileImageProvider__
#define __G3MiOSSDK__GEOVectorTileImageProvider__

#include "TileImageProvider.hpp"

#include "FrameTask.hpp"
#include "Vector2I.hpp"
#include "QuadTree.hpp"
#include <map>
#include "IImageListener.hpp"

class GEOVectorLayer;

class GEOVectorTileImageProvider : public TileImageProvider {
public:

  class GEORasterizerCanvasImageListener : public IImageListener {
  private:
    const TileImageContribution* _contribution;
    const std::string            _tileId;

    TileImageListener* _listener;
    bool               _deleteListener;

    const std::string getImageId(const std::string& tileId) const;

  public:
    GEORasterizerCanvasImageListener(const TileImageContribution* contribution,
                                     const std::string& tileId,
                                     TileImageListener* listener,
                                     bool deleteListener) :
    _contribution(contribution),
    _tileId(tileId),
    _listener(listener),
    _deleteListener(deleteListener)
    {
    }

    void imageCreated(const IImage* image);
  };


  class GEORasterizerQuadTreeVisitor : public QuadTreeVisitor {
  private:
    ICanvas*             _canvas;
    GEORasterProjection* _projection;
    const int            _tileLevel;

  public:
    GEORasterizerQuadTreeVisitor(ICanvas*             canvas,
                                 GEORasterProjection* projection,
                                 int                  tileLevel) :
    _canvas(canvas),
    _projection(projection),
    _tileLevel(tileLevel)
    {
    }

    bool visitElement(const Sector&           sector,
                      const QuadTree_Content* content) const;

    void endVisit(bool aborted) const;

  };


  class GEORasterizerFrameTask : public FrameTask {
  private:
    GEOVectorTileImageProvider*  _geoVectorTileImageProvider;
#ifdef C_CODE
    const TileImageContribution* _contribution;
#endif
#ifdef JAVA_CODE
    private TileImageContribution _contribution;
#endif
    const std::string            _tileId;
    const Sector                 _tileSector;
    const bool                   _tileMercator;
    const int                    _tileLevel;
    const int                    _resolutionWidth;
    const int                    _resolutionHeight;
    TileImageListener*           _listener;
    const bool                   _deleteListener;
    bool                         _isCanceled;

  public:
    GEORasterizerFrameTask(GEOVectorTileImageProvider* geoVectorTileImageProvider,
                           const TileImageContribution* contribution,
                           const std::string& tileId,
                           const Sector& tileSector,
                           bool tileMercator,
                           int tileLevel,
                           const Vector2I& resolution,
                           TileImageListener* listener,
                           bool deleteListener) :
    _geoVectorTileImageProvider(geoVectorTileImageProvider),
    _contribution(contribution),
    _tileId(tileId),
    _tileSector(tileSector),
    _tileMercator(tileMercator),
    _tileLevel(tileLevel),
    _resolutionWidth(resolution._x),
    _resolutionHeight(resolution._y),
    _listener(listener),
    _deleteListener(deleteListener),
    _isCanceled(false)
    {
      _geoVectorTileImageProvider->_retain();
    }

    ~GEORasterizerFrameTask();

    bool isCanceled(const G3MRenderContext* rc);

    void execute(const G3MRenderContext* rc);

    void cancel();
  };


private:
#ifdef C_CODE
  const GEOVectorLayer* _layer;
#endif
#ifdef JAVA_CODE
  private GEOVectorLayer _layer;
#endif

  std::map<const std::string, GEORasterizerFrameTask*> _rasterizers;

public:

  GEOVectorTileImageProvider(const GEOVectorLayer* layer);

  const TileImageContribution* contribution(const Tile* tile);

  void create(const Tile* tile,
              const TileImageContribution* contribution,
              const Vector2I& resolution,
              long long tileDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener,
              FrameTasksExecutor* frameTasksExecutor);

  void cancel(const std::string& tileId);

  void rasterizerDeleted(const std::string& tileId);

  void rasterize(const TileImageContribution* contribution,
                 const std::string& tileId,
                 const Sector& tileSector,
                 bool tileMercator,
                 int tileLevel,
                 int resolutionWidth,
                 int resolutionHeight,
                 TileImageListener* listener,
                 bool deleteListener);

  void layerDeleted(const GEOVectorLayer* layer);

};

#endif
