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


class XPCRenderer : public DefaultRenderer {

private:
  ITimer* _timer;

  std::vector<XPCPointCloud*> _clouds;
  size_t                      _cloudsSize;
  std::vector<std::string>    _errors;

  GLState* _glState;


protected:

  void onChangedContext();


public:

  XPCRenderer();

  ~XPCRenderer();

  void removeAllPointClouds();

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName,
                     long long downloadPriority,
                     const TimeInterval& timeToCache,
                     bool readExpired,
                     const XPCPointColorizer* pointColorizer,
                     bool deletePointColorizer,
                     float pointSize = 1.0f,
                     bool dynamicPointSize = true,
                     float verticalExaggeration = 1.0f,
                     double deltaHeight = 0,
                     XPCMetadataListener* metadataListener = NULL,
                     bool deleteMetadataListener = true,
                     bool verbose = false);

};

#endif
