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
//#include "GPUProgramState.hpp"

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
  std::vector<GEORenderer_ObjectSymbolizerPair*> _children;

  const GEOSymbolizer* _defaultSymbolizer;

  MeshRenderer*      _meshRenderer;
  ShapesRenderer*    _shapesRenderer;
  MarksRenderer*     _marksRenderer;
  GEOTileRasterizer* _geoTileRasterizer;
  
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
  _geoTileRasterizer(geoTileRasterizer)
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

  void initialize(const G3MContext* context) {

  }

  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
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
};

#endif
