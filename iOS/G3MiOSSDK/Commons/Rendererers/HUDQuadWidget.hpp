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
class HUDPosition;
class IImage;
class Mesh;
class SimpleTextureMapping;

class HUDQuadWidget : public HUDWidget {
private:
#ifdef C_CODE
  const URL _imageURL;
#endif
#ifdef JAVA_CODE
  private final URL _imageURL;
#endif
//  const float _x;
//  const float _y;
  const HUDPosition* _x;
  const HUDPosition* _y;
  const float _width;
  const float _height;

  float _texCoordsTranslationU;
  float _texCoordsTranslationV;
  float _texCoordsScaleU;
  float _texCoordsScaleV;
  float _texCoordsRotationInRadians;
  float _texCoordsRotationCenterU;
  float _texCoordsRotationCenterV;

  IImage* _image;
  bool _downloadingImage;
  std::vector<std::string> _errors;

  Mesh* _mesh;
  SimpleTextureMapping* _simpleTextureMapping;
  Mesh* createMesh(const G3MRenderContext* rc);
  Mesh* getMesh(const G3MRenderContext* rc);

  void cleanMesh();

protected:
  void rawRender(const G3MRenderContext* rc,
                 GLState* glState);

public:
  HUDQuadWidget(const URL& imageURL,
                HUDPosition* x,
                HUDPosition* y,
                float width,
                float height) :
  _imageURL(imageURL),
  _x(x),
  _y(y),
  _width(width),
  _height(height),
  _mesh(NULL),
  _simpleTextureMapping(NULL),
  _image(NULL),
  _downloadingImage(false),
  _texCoordsTranslationU(0),
  _texCoordsTranslationV(0),
  _texCoordsScaleU(1),
  _texCoordsScaleV(1),
  _texCoordsRotationInRadians(0),
  _texCoordsRotationCenterU(0),
  _texCoordsRotationCenterV(0)
  {
  }

  void setTexCoordsTranslation(float u, float v);

  void setTexCoordsScale(float u, float v);

  void setTexCoordsRotation(float angleInRadians,
                            float centerU,
                            float centerV);

  void setTexCoordsRotation(const Angle& angle,
                            float centerU,
                            float centerV) {
    setTexCoordsRotation((float) angle._radians,
                         centerU, centerV);
  }

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
