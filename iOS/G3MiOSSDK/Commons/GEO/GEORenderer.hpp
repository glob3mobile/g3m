//
//  GEORenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEORenderer__
#define __G3MiOSSDK__GEORenderer__

#include "LeafRenderer.hpp"
#include "DownloadPriority.hpp"
#include "URL.hpp"
#include "TimeInterval.hpp"
#include <vector>

class GEOObject;
class GEOSymbolizer;
class MeshRenderer;
class MarksRenderer;
class ShapesRenderer;
class GEOTileRasterizer;
class GEORenderer_ObjectSymbolizerPair;

class GEORenderer : public LeafRenderer {
private:

  class LoadQueueItem {
  public:
#ifdef C_CODE
    const URL          _url;
    const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
    public final URL _url;
    public final TimeInterval _timeToCache;
#endif
    GEOSymbolizer*  _symbolizer;
    const long long _priority;
    const bool      _readExpired;
    const bool      _isBSON;

    LoadQueueItem(const URL& url,
                  GEOSymbolizer* symbolizer,
                  long long priority,
                  const TimeInterval& timeToCache,
                  bool readExpired,
                  bool isBSON) :
    _url(url),
    _symbolizer(symbolizer),
    _priority(priority),
    _timeToCache(timeToCache),
    _readExpired(readExpired),
    _isBSON(isBSON)
    {

    }

    ~LoadQueueItem() {
    }
  };

  void drainLoadQueue();

  std::vector<GEORenderer_ObjectSymbolizerPair*> _children;

  const GEOSymbolizer* _defaultSymbolizer;

  MeshRenderer*      _meshRenderer;
  ShapesRenderer*    _shapesRenderer;
  MarksRenderer*     _marksRenderer;
  GEOTileRasterizer* _geoTileRasterizer;

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  std::vector<LoadQueueItem*> _loadQueue;

  void requestBuffer(const URL& url,
                     GEOSymbolizer* symbolizer,
                     long long priority,
                     const TimeInterval& timeToCache,
                     bool readExpired,
                     bool isBSON);

public:

  /**
   Creates a GEORenderer.

   defaultSymbolizer: Default Symbolizer, can be NULL.  In case of NULL, one instance of GEOSymbolizer must be passed in every call to addGEOObject();

   meshRenderer:   Can be NULL as long as no GEOMarkSymbol is used in any symbolizer.
   shapesRenderer: Can be NULL as long as no GEOShapeSymbol is used in any symbolizer.
   marksRenderer:  Can be NULL as long as no GEOMeshSymbol is used in any symbolizer.

   */
  GEORenderer(const GEOSymbolizer* defaultSymbolizer,
              MeshRenderer*        meshRenderer,
              ShapesRenderer*      shapesRenderer,
              MarksRenderer*       marksRenderer,
              GEOTileRasterizer*   geoTileRasterizer) :
  _defaultSymbolizer(defaultSymbolizer),
  _meshRenderer(meshRenderer),
  _shapesRenderer(shapesRenderer),
  _marksRenderer(marksRenderer),
  _geoTileRasterizer(geoTileRasterizer),
  _context(NULL)
  {
  }

  virtual ~GEORenderer();

  /**
   Add a new GEOObject.

   symbolizer: The symbolizer to be used for the given geoObject.  Can be NULL as long as a defaultSymbolizer was given in the GEORenderer constructor.
   */
  void addGEOObject(GEOObject* geoObject,
                    GEOSymbolizer* symbolizer = NULL);

  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {

  }

  void onDestroy(const G3MContext* context) {

  }

  void initialize(const G3MContext* context);

  RenderState getRenderState(const G3MRenderContext* rc) {
    return RenderState::ready();
  }

  void render(const G3MRenderContext* rc, GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void start(const G3MRenderContext* rc) {

  }

  void stop(const G3MRenderContext* rc) {

  }

  MeshRenderer* getMeshRenderer() {
    return _meshRenderer;
  }

  MarksRenderer* getMarksRenderer() {
    return _marksRenderer;
  }

  ShapesRenderer* getShapesRenderer() {
    return _shapesRenderer;
  }

  GEOTileRasterizer* getGeoTileRasterizer() {
    return _geoTileRasterizer;
  }

  void loadJSON(const URL& url) {
    loadJSON(url,
             NULL,
             DownloadPriority::MEDIUM,
             TimeInterval::fromDays(30),
             true);
  }

  void loadJSON(const URL& url,
                GEOSymbolizer* symbolizer) {
    loadJSON(url,
             symbolizer,
             DownloadPriority::MEDIUM,
             TimeInterval::fromDays(30),
             true);
  }

  void loadJSON(const URL& url,
                GEOSymbolizer* symbolizer,
                long long priority,
                const TimeInterval& timeToCache,
                bool readExpired);

  void loadBSON(const URL& url) {
    loadBSON(url,
             NULL,
             DownloadPriority::MEDIUM,
             TimeInterval::fromDays(30),
             true);
  }

  void loadBSON(const URL& url,
                GEOSymbolizer* symbolizer) {
    loadBSON(url,
             symbolizer,
             DownloadPriority::MEDIUM,
             TimeInterval::fromDays(30),
             true);
  }

  void loadBSON(const URL& url,
                GEOSymbolizer* symbolizer,
                long long priority,
                const TimeInterval& timeToCache,
                bool readExpired);

  void setEnable(bool enable);
  
};

#endif
