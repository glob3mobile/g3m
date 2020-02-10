//
//  GEORenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3M__GEORenderer__
#define __G3M__GEORenderer__

#include "DefaultRenderer.hpp"

#include <vector>

#include "URL.hpp"
#include "TimeInterval.hpp"

class GEOSymbolizer;
class GEORenderer_ObjectSymbolizerPair;
class MeshRenderer;
class ShapesRenderer;
class MarksRenderer;
class GEOVectorLayer;
class GEOObject;


class GEORenderer : public DefaultRenderer {
private:

  class LoadQueueItem {
  public:
    const URL          _url;
    const TimeInterval _timeToCache;
    GEOSymbolizer*     _symbolizer;
    const long long    _priority;
    const bool         _readExpired;
    const bool         _isBSON;

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
  
  void cleanLoadQueue();

  std::vector<GEORenderer_ObjectSymbolizerPair*> _children;

  const GEOSymbolizer* _defaultSymbolizer;
  MeshRenderer*        _meshRenderer;
  ShapesRenderer*      _shapesRenderer;
  MarksRenderer*       _marksRenderer;
  GEOVectorLayer*      _geoVectorLayer;

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
   geoVectorLayer: Can be NULL as long as no GEORasterSymbol is used in any symbolizer.

   */
  GEORenderer(const GEOSymbolizer* defaultSymbolizer,
              MeshRenderer*        meshRenderer,
              ShapesRenderer*      shapesRenderer,
              MarksRenderer*       marksRenderer,
              GEOVectorLayer*      geoVectorLayer);

  virtual ~GEORenderer();

  /**
   Add a new GEOObject.

   symbolizer: The symbolizer to be used for the given geoObject.  Can be NULL as long as a defaultSymbolizer was given in the GEORenderer constructor.
   */
  void addGEOObject(GEOObject* geoObject,
                    GEOSymbolizer* symbolizer = NULL);
  
  void onChangedContext();
  
  void onLostContext();
  
  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  MeshRenderer* getMeshRenderer() const {
    return _meshRenderer;
  }

  MarksRenderer* getMarksRenderer() const {
    return _marksRenderer;
  }

  ShapesRenderer* getShapesRenderer() const {
    return _shapesRenderer;
  }

  GEOVectorLayer* getGEOVectorLayer() const {
    return _geoVectorLayer;
  }

  void setGEOVectorLayer(GEOVectorLayer* geoVectorLayer,
                         bool deletePrevious);

  void loadJSON(const URL& url);

  void loadJSON(const URL& url,
                GEOSymbolizer* symbolizer);

  void loadJSON(const URL& url,
                GEOSymbolizer* symbolizer,
                long long priority,
                const TimeInterval& timeToCache,
                bool readExpired);

  void loadBSON(const URL& url);

  void loadBSON(const URL& url,
                GEOSymbolizer* symbolizer);

  void loadBSON(const URL& url,
                GEOSymbolizer* symbolizer,
                long long priority,
                const TimeInterval& timeToCache,
                bool readExpired);

  void setEnable(bool enable);
  
};

#endif
