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
#include "GPUProgramState.hpp"

#include <vector>
class GEOObject;
class GEOSymbolizer;
class MeshRenderer;
class MarksRenderer;
class ShapesRenderer;
class GEORenderer_ObjectSymbolizerPair;

class GEORenderer : public LeafRenderer {
private:
  std::vector<GEORenderer_ObjectSymbolizerPair*> _children;

//<<<<<<< HEAD
//  const GEOSymbolizer* _symbolizer;
//  
//  
//
//public:
//
//  GEORenderer(const GEOSymbolizer* symbolizer) :
//  _symbolizer(symbolizer),
//  _programState(NULL)
//=======
  const GEOSymbolizer* _defaultSymbolizer;

  MeshRenderer*   _meshRenderer;
  ShapesRenderer* _shapesRenderer;
  MarksRenderer*  _marksRenderer;
  
  GPUProgramState _programState;

public:

  /**
   Creates a GEORenderer.

   defaultSymbolizer: Default Symbolizer, can be NULL.  In case of NULL, one instance of GEOSymbolizer must be passed in every call to addGEOObject();

   meshRenderer:   Can be NULL as long as no GEOMarkSymbol is used in any symbolizer.
   shapesRenderer: Can be NULL as long as no GEOShapeSymbol is used in any symbolizer.
   marksRenderer:  Can be NULL as long as no GEOMeshSymbol is used in any symbolizer.

   */
  GEORenderer(const GEOSymbolizer* defaultSymbolizer,
              MeshRenderer*   meshRenderer,
              ShapesRenderer* shapesRenderer,
              MarksRenderer*  marksRenderer) :
  _defaultSymbolizer(defaultSymbolizer),
  _meshRenderer(meshRenderer),
  _shapesRenderer(shapesRenderer),
  _marksRenderer(marksRenderer),
  _programState(NULL)
//>>>>>>> webgl-port
  {
    _programState.setUniformValue("BillBoard", false);
    _programState.setUniformValue("EnableTexture", false);
    _programState.setUniformValue("PointSize", (float)1.0);
    _programState.setUniformValue("ScaleTexCoord", Vector2D(1.0,1.0));
    _programState.setUniformValue("TextureExtent", Vector2D(0.0,0.0));
    _programState.setUniformValue("TranslationTexCoord", Vector2D(0.0,0.0));
    _programState.setUniformValue("ViewPortExtent", Vector2D(0.0,0.0));
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
