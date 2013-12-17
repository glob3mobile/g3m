//
//  HUDQuadWidget.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#ifndef __G3MiOSSDK__HUDQuadWidget__
#define __G3MiOSSDK__HUDQuadWidget__

#include "HUDWidget.hpp"

#include "URL.hpp"
class IImage;

class HUDQuadWidget : public HUDWidget {
private:
  const URL   _imageURL;
  const float _x;
  const float _y;
  const float _width;
  const float _height;

public:
  HUDQuadWidget(const URL& imageURL,
                float x,
                float y,
                float width,
                float height) :
  _imageURL(imageURL),
  _x(x),
  _y(y),
  _width(width),
  _height(height)
  {
  }


  void initialize(const G3MContext* context);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  void render(const G3MRenderContext* rc,
              GLState* glState);


  /** private, do not call */
  void onImageDownload(IImage* image);

  /** private, do not call */
  void onImageDownloadError(const URL& url);

};

#endif
