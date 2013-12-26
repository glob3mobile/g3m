//
//  StenciledMultiTexturedHUDQuadWidget.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#ifndef __G3MiOSSDK__StenciledMultiTexturedHUDQuadWidget__
#define __G3MiOSSDK__StenciledMultiTexturedHUDQuadWidget__

#include "HUDWidget.hpp"

#include "URL.hpp"
#include "Vector2D.hpp"
#include "Angle.hpp"
#include "MultiTextureMapping.hpp"
class HUDPosition;
class IImage;
class Mesh;
class SimpleTextureMapping;

class StenciledMultiTexturedHUDQuadWidget : public HUDWidget {
private:
#ifdef C_CODE
  const URL _imageURL;
#endif
#ifdef JAVA_CODE
  private final URL _imageURL;
#endif

#ifdef C_CODE
  const URL _imageURL2;
#endif
#ifdef JAVA_CODE
  private final URL _imageURL2;
#endif

#ifdef C_CODE
  const URL _stencilURL;
#endif
#ifdef JAVA_CODE
  private final URL _stencilURL;
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
  bool _downloadingImages;
  IImage* _image2;
  IImage* _stencilImage;
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
  StenciledMultiTexturedHUDQuadWidget(const URL& imageURL,
                             const URL& imageURL2,
                             const URL& stencilURL,
                HUDPosition* x,
                HUDPosition* y,
                float width,
                float height) :
  _imageURL(imageURL),
  _imageURL2(imageURL2),
  _stencilURL(stencilURL),
  _x(x),
  _y(y),
  _width(width),
  _height(height),
  _mesh(NULL),
  _mtMapping(NULL),
  _image(NULL),
  _image2(NULL),
  _stencilImage(NULL),
  _downloadingImages(false),
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

  ~StenciledMultiTexturedHUDQuadWidget();

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
