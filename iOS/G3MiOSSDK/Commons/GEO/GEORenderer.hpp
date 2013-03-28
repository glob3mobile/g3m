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

#include <vector>
class GEOObject;
class GEOSymbolizer;
class MeshRenderer;
class MarksRenderer;
class ShapesRenderer;


class GEORenderer : public LeafRenderer {
private:
  std::vector<GEOObject*> _children;

  const GEOSymbolizer* _symbolizer;
  MeshRenderer*   _meshRenderer;
  ShapesRenderer* _shapesRenderer;
  MarksRenderer*  _marksRenderer;

public:

  GEORenderer(const GEOSymbolizer* symbolizer,
              MeshRenderer*   meshRenderer,
              ShapesRenderer* shapesRenderer,
              MarksRenderer*  marksRenderer) :
  _symbolizer(symbolizer),
  _meshRenderer(meshRenderer),
  _shapesRenderer(shapesRenderer),
  _marksRenderer(marksRenderer)
  {

  }

  virtual ~GEORenderer();

  void addGEOObject(GEOObject* geoObject);
  
  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {

  }

  void onDestroy(const G3MContext* context) {

  }

  void initialize(const G3MContext* context) {

  }
  
  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState);

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
  
};

#endif
