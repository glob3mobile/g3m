//
//  PointCloudsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

#ifndef __G3MiOSSDK__PointCloudsRenderer__
#define __G3MiOSSDK__PointCloudsRenderer__

#include "DefaultRenderer.hpp"

#include "URL.hpp"

class PointCloudsRenderer : public DefaultRenderer {
private:

  class PointCloud {
  public:
    void initialize(const G3MContext* context);

    RenderState getRenderState(const G3MRenderContext* rc);
  };


  std::vector<PointCloud*> _clouds;
  std::vector<std::string> _errors;


protected:
  void onChangedContext();

public:

  ~PointCloudsRenderer();

  RenderState getRenderState(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  void addPointCloud(const URL& url);

  void removeAllPointClouds();
  
};

#endif
