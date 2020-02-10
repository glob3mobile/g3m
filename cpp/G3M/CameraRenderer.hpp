//
//  CameraRenderer.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//

#ifndef G3M_CameraRenderer
#define G3M_CameraRenderer

#include "ProtoRenderer.hpp"

#include <vector>

class CameraEventHandler;
class CameraContext;
class TouchEvent;
class RenderState;


class CameraRenderer: public ProtoRenderer {
private:
  bool _processTouchEvents;

  std::vector<CameraEventHandler*> _handlers;
  size_t                           _handlersSize;

  std::vector<std::string> _errors;

  CameraContext *_cameraContext;

public:
  CameraRenderer() :
  _cameraContext(NULL),
  _processTouchEvents(true),
  _handlersSize(0)
  {
  }

  ~CameraRenderer();

  void setProcessTouchEvents(bool processTouchEvents) {
    _processTouchEvents = processTouchEvents;
  }

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void initialize(const G3MContext* context) {

  }

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  RenderState getRenderState(const G3MRenderContext* rc);

  void start(const G3MRenderContext* rc) {

  }

  void stop(const G3MRenderContext* rc) {

  }

  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {

  }

  void onDestroy(const G3MContext* context) {

  }

  void removeAllHandlers(bool deleteHandlers);

  void addHandler(CameraEventHandler* handler);
  
  void removeHandler(CameraEventHandler* handler);
  
};

#endif
