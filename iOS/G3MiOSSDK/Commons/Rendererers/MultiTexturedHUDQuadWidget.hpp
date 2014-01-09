//
//  MultiTexturedHUDQuadWidget.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#ifndef __G3MiOSSDK__MultiTexturedHUDQuadWidget__
#define __G3MiOSSDK__MultiTexturedHUDQuadWidget__

#include "HUDWidget.hpp"

#include "URL.hpp"
#include "Vector2D.hpp"
#include "Angle.hpp"
#include "MultiTextureMapping.hpp"
class HUDPosition;
class IImage;
class Mesh;
class SimpleTextureMapping;

class MultiTexturedHUDQuadWidget : public HUDWidget {
private:
#ifdef C_CODE
  const URL _imageURL1;
  const URL _imageURL2;
#endif
#ifdef JAVA_CODE
  private final URL _imageURL1;
  private final URL _imageURL2;
#endif

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

  IImage* _image1;
  IImage* _image2;
  bool _downloadingImage;
  std::vector<std::string> _errors;

  Mesh* _mesh;
  MultiTextureMapping* _mtMapping;

  Mesh* createMesh(const G3MRenderContext* rc);
  Mesh* getMesh(const G3MRenderContext* rc);

  void cleanMesh();

protected:
  void rawRender(const G3MRenderContext* rc,
                 GLState* glState);

public:
  MultiTexturedHUDQuadWidget(const URL& imageURL1,
                             const URL& imageURL2,
                HUDPosition* x,
                HUDPosition* y,
                float width,
                float height) :
  _imageURL1(imageURL1),
  _imageURL2(imageURL2),
  _x(x),
  _y(y),
  _width(width),
  _height(height),
  _mesh(NULL),
  _mtMapping(NULL),
  _image1(NULL),
  _image2(NULL),
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

  ~MultiTexturedHUDQuadWidget();

  void initialize(const G3MContext* context);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  RenderState getRenderState(const G3MRenderContext* rc);

  /** private, do not call */
  void onImageDownload(IImage* image, const URL& url);

  /** private, do not call */
  void onImageDownloadError(const URL& url);
  
};

#endif
