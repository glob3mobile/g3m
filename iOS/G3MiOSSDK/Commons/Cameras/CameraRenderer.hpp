//
//  CameraRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRenderer
#define G3MiOSSDK_CameraRenderer

#include <vector>

#include "ProtoRenderer.hpp"
#include "RenderState.hpp"
#include "Context.hpp"
#include "Effects.hpp"


#include "Effects.hpp"

class RenderState;
class CameraEventHandler;


class ILogger;
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
  CameraContext(Gesture gesture, Camera* nextCamera):
  _currentGesture(gesture),
  _nextCamera(nextCamera)
  {}

  ~CameraContext() {

  }

  const Gesture getCurrentGesture() const { return _currentGesture; }
  void setCurrentGesture(const Gesture& gesture) { _currentGesture = gesture; }
  Camera* getNextCamera() { return _nextCamera; }
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

  void addHandler(CameraEventHandler* handler) {
    _handlers.push_back(handler);
  }

  void setProcessTouchEvents(bool processTouchEvents) {
    _processTouchEvents = processTouchEvents;
  }

  void render(const G3MRenderContext* rc, GLState* glState);
  
  void initialize(const G3MContext* context) {
    
  }

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
    
  }

  RenderState getRenderState(const G3MRenderContext* rc) {
    return RenderState::ready();
  }

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
};


#endif
