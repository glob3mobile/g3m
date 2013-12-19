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
#include "Vector2D.hpp"
#include "Angle.hpp"
class IImage;
class Mesh;

class HUDQuadWidget : public HUDWidget {
private:
#ifdef C_CODE
  const URL _imageURL;
#endif
#ifdef JAVA_CODE
  private final URL _imageURL;
#endif
  const float _x;
  const float _y;
  const float _width;
  const float _height;

  float _texCoordsTranslationX;
  float _texCoordsTranslationY;
  float _texCoordsScaleX;
  float _texCoordsScaleY;
  float _texCoordsRotationInRadians;
  float _texCoordsRotationCenterX;
  float _texCoordsRotationCenterY;

  IImage* _image;
  bool _downloadingImage;
  std::vector<std::string> _errors;

  Mesh* _mesh;
  Mesh* createMesh(const G3MRenderContext* rc) const;
  Mesh* getMesh(const G3MRenderContext* rc);

protected:
  void rawRender(const G3MRenderContext* rc,
                 GLState* glState);

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
  _height(height),
  _mesh(NULL),
  _image(NULL),
  _downloadingImage(false),
  _texCoordsTranslationX(0),
  _texCoordsTranslationY(0),
  _texCoordsScaleX(1),
  _texCoordsScaleY(1),
  _texCoordsRotationInRadians(0),
  _texCoordsRotationCenterX(0),
  _texCoordsRotationCenterY(0)
  {
  }

  void setTexCoordsTranslation(const Vector2D& translation);
  void setTexCoordsScale(const Vector2D& scale);
  void setTexCoordsRotation(const Angle& rotation,
                            const Vector2D& center);

  ~HUDQuadWidget();

  void initialize(const G3MContext* context);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  RenderState getRenderState(const G3MRenderContext* rc);

  /** private, do not call */
  void onImageDownload(IImage* image);

  /** private, do not call */
  void onImageDownloadError(const URL& url);
  
};

#endif
