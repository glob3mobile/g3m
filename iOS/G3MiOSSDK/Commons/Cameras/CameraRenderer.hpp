//
//  CameraRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//

#ifndef G3MiOSSDK_CameraRenderer
#define G3MiOSSDK_CameraRenderer

#include <vector>

#include "ProtoRenderer.hpp"

class RenderState;
class CameraEventHandler;
class Camera;
class Vector3D;
class TouchEvent;


enum Gesture {
  None,
  Drag,
  Zoom,
  Rotate,
  DoubleDrag
};


class CameraContext {
private:
  Gesture _currentGesture;
  Camera* _nextCamera;

public:
  CameraContext(Gesture gesture,
                Camera* nextCamera):
  _currentGesture(gesture),
  _nextCamera(nextCamera)
  {
  }

  ~CameraContext() {
  }

  const Gesture getCurrentGesture() const {
    return _currentGesture;
  }

  void setCurrentGesture(const Gesture& gesture) {
    _currentGesture = gesture;
  }

  Camera* getNextCamera() {
    return _nextCamera;
  }
};


class CameraRenderer: public ProtoRenderer {
private:
  bool _processTouchEvents;
  std::vector<CameraEventHandler*> _handlers;
  CameraContext *_cameraContext;

public:
  CameraRenderer() :
  _cameraContext(NULL),
  _processTouchEvents(true)
  {
  }

  ~CameraRenderer();

  void removeAllHandlers(bool deleteHandlers);

  void addHandler(CameraEventHandler* handler);

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
  
  void removeHandler(CameraEventHandler* handler);

};

#endif
