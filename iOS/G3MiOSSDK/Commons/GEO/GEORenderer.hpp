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

class GEORenderer : public LeafRenderer {
private:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif
  std::vector<GEOObject*> _children;

  const GEOSymbolizer* _symbolizer;
  
  GPUProgramState _programState;

public:

  GEORenderer(const GEOSymbolizer* symbolizer) :
  _symbolizer(symbolizer),
  _programState(NULL)
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

  void addGEOObject(GEOObject* geoObject);
  
  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {

  }

  void onDestroy(const G3MContext* context) {

  }

  void initialize(const G3MContext* context);
  
  bool isReadyToRender(const G3MRenderContext* rc);

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
