//
//  HUDWidget.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#ifndef __G3MiOSSDK__HUDWidget__
#define __G3MiOSSDK__HUDWidget__

class G3MContext;
class G3MEventContext;
class G3MRenderContext;
class GLState;
class RenderState;


class HUDWidget {
private:
  bool _enable;

protected:
  virtual void rawRender(const G3MRenderContext* rc,
                         GLState* glState) = 0;

public:
  HUDWidget() :
  _enable(true)
  {
  }

  void setEnable(bool enable) {
    _enable = enable;
  }

  bool isEnable() const {
    return _enable;
  }

  virtual ~HUDWidget() {

  }

  virtual void initialize(const G3MContext* context) = 0;

  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width,
                                     int height) = 0;

  virtual RenderState getRenderState(const G3MRenderContext* rc) = 0;

  void render(const G3MRenderContext* rc,
              GLState* glState);

};

#endif
