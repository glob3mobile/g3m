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
class URL;
class TimeInterval;
class XPCPointColorizer;
class XPCMetadataListener;
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
  
protected:

  void onChangedContext();


public:

  XPCRenderer();

  ~XPCRenderer();

  void removeAllPointClouds();

  RenderState getRenderState(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName,
                     long long downloadPriority,
                     const TimeInterval& timeToCache,
                     bool readExpired,
                     XPCPointColorizer* pointColorizer,
                     bool deletePointColorizer,
                     const double minProjectedArea,
                     float pointSize = 1.0f,
                     bool dynamicPointSize = true,
                     const bool depthTest = true,
                     float verticalExaggeration = 1.0f,
                     float deltaHeight = 0,
                     XPCMetadataListener* metadataListener = NULL,
                     bool deleteMetadataListener = true,
                     bool verbose = false);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

};

#endif
