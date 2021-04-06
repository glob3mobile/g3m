//
//  XPCRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCRenderer_hpp
#define XPCRenderer_hpp

#include "DefaultRenderer.hpp"

class ITimer;
class XPCPointCloud;
class Camera;
class XPCSelectionResult;


class XPCRenderer : public DefaultRenderer {

private:
  ITimer* _timer;

  std::vector<XPCPointCloud*> _clouds;
  size_t                      _cloudsSize;
  std::vector<std::string>    _errors;

  GLState* _glState;

#ifdef C_CODE
  const Camera*  _lastCamera;
#endif
#ifdef JAVA_CODE
  private Camera _lastCamera;
#endif
  bool _renderDebug;
  XPCSelectionResult* _selectionResult;

  ITimer* _lastSplitTimer;
  
protected:

  void onChangedContext();


public:

  XPCRenderer();

  ~XPCRenderer();


  RenderState getRenderState(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void addPointCloud(XPCPointCloud* pointCloud);

  bool removePointCloud(XPCPointCloud* pointCloud);

  void removeAllPointClouds();

  void render(const G3MRenderContext* rc,
              GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

};

#endif
