//
//  CameraContext.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/14/17.
//
//

#ifndef CameraContext_hpp
#define CameraContext_hpp

#include "CameraEventGesture.hpp"

class Camera;


class CameraContext {
private:
  CameraEventGesture _currentGesture;
  Camera*            _nextCamera;

public:
  CameraContext(const CameraEventGesture& gesture,
                Camera* nextCamera):
  _currentGesture(gesture),
  _nextCamera(nextCamera)
  {
  }

  ~CameraContext() {
  }

  const CameraEventGesture getCurrentGesture() const {
    return _currentGesture;
  }

  void setCurrentGesture(const CameraEventGesture& gesture) {
    _currentGesture = gesture;
  }

  Camera* getNextCamera() {
    return _nextCamera;
  }
};

#endif
