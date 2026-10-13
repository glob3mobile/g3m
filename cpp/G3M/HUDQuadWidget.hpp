//
//  HUDQuadWidget.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#ifndef __G3M__HUDQuadWidget__
#define __G3M__HUDQuadWidget__

#include "HUDWidget.hpp"

#include <vector>
//#include "URL.hpp"
#include "Vector2D.hpp"
#include "Angle.hpp"
class HUDPosition;
class HUDSize;
class Mesh;
class TransformableTextureMapping;
class IImageFactory;

#include "ChangedListener.hpp"
#include "HUDWidgetImage.hpp"

class HUDQuadWidget : public HUDWidget, public ChangedListener {
private:
  HUDWidgetImage* _image;
  HUDWidgetImage* _backgroundImage; // NULL when the widget has no background

  const HUDPosition* _xPosition;
  const HUDPosition* _yPosition;

  const HUDSize* _widthSize;
  const HUDSize* _heightSize;

  float _texCoordsTranslationU;
  float _texCoordsTranslationV;
  float _texCoordsScaleU;
  float _texCoordsScaleV;
  float _texCoordsRotationInRadians;
  float _texCoordsRotationCenterU;
  float _texCoordsRotationCenterV;

  std::vector<std::string> _errors;

  Mesh* _mesh;
  TransformableTextureMapping* _textureMapping;
  Mesh* createMesh(const G3MRenderContext* rc);
  Mesh* getMesh(const G3MRenderContext* rc);

  void cleanMesh();

  void addErrors(const HUDWidgetImage* widgetImage);

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

protected:
  void rawRender(const G3MRenderContext* rc,
                 GLState* glState);

public:
  HUDQuadWidget(IImageFactory* imageFactory,
                HUDPosition* xPosition,
                HUDPosition* yPosition,
                HUDSize* widthSize,
                HUDSize* heightSize,
                IImageFactory* backgroundImageFactory = NULL) :
  _image(new HUDWidgetImage(imageFactory)),
  _backgroundImage((backgroundImageFactory == NULL) ? NULL : new HUDWidgetImage(backgroundImageFactory)),
  _xPosition(xPosition),
  _yPosition(yPosition),
  _widthSize(widthSize),
  _heightSize(heightSize),
  _mesh(NULL),
  _textureMapping(NULL),
  _texCoordsTranslationU(0),
  _texCoordsTranslationV(0),
  _texCoordsScaleU(1),
  _texCoordsScaleV(1),
  _texCoordsRotationInRadians(0),
  _texCoordsRotationCenterU(0),
  _texCoordsRotationCenterV(0),
  _context(NULL)
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

  void changed();
  
};

#endif
