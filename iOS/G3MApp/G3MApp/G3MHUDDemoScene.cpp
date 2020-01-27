//
//  G3MHUDDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 12/2/14.
//

#include "G3MHUDDemoScene.hpp"

#include <G3MSharedSDK/BingMapsLayer.hpp>
#include <G3MSharedSDK/LayerSet.hpp>
#include <G3MSharedSDK/CanvasImageBuilder.hpp>
#include <G3MSharedSDK/ICanvas.hpp>
#include <G3MSharedSDK/Color.hpp>
#include <G3MSharedSDK/IStringUtils.hpp>
#include <G3MSharedSDK/GFont.hpp>
#include <G3MSharedSDK/G3MContext.hpp>
#include <G3MSharedSDK/HUDQuadWidget.hpp>
#include <G3MSharedSDK/HUDRenderer.hpp>
#include <G3MSharedSDK/HUDRelativePosition.hpp>
#include <G3MSharedSDK/HUDRelativeSize.hpp>
#include <G3MSharedSDK/LabelImageBuilder.hpp>
#include <G3MSharedSDK/DownloaderImageBuilder.hpp>
#include <G3MSharedSDK/HUDAbsolutePosition.hpp>
#include <G3MSharedSDK/GTask.hpp>
#include <G3MSharedSDK/G3MWidget.hpp>
#include <G3MSharedSDK/PeriodicalTask.hpp>
#include <G3MSharedSDK/IMathUtils.hpp>
#include <G3MSharedSDK/BoxImageBackground.hpp>

#include "G3MDemoModel.hpp"


class AltimeterCanvasImageBuilder : public CanvasImageBuilder {
private:
  float _altitude = 38500;
  float _step     = 100;

protected:
  void buildOnCanvas(const G3MContext* context,
                     ICanvas* canvas) {
    const float width  = canvas->getWidth();
    const float height = canvas->getHeight();

    canvas->setFillColor(Color::fromRGBA(0, 0, 0, 0.5));
    canvas->fillRectangle(0, 0, width, height);

    canvas->setFillColor(Color::WHITE);


    const IStringUtils* su = context->getStringUtils();

    int altitude = _altitude;

    canvas->setFont(GFont::monospaced(32));
    for (int y = 0; y <= height; y += 16) {
      if ((y % 80) == 0) {
        canvas->fillRectangle(0, y-1.5f, width/6.0f, 3);

        const std::string label = su->toString(altitude);
        const Vector2F labelExtent = canvas->textExtent(label);
        canvas->fillText(label,
                         width/6.0f * 1.25f,
                         y - labelExtent._y/2);

        altitude -= 100;
      }
      else {
        canvas->fillRectangle(0, y-0.5f, width/8.0f, 1);
      }
    }

    canvas->setLineColor(Color::WHITE);
    canvas->setLineWidth(8);
    canvas->strokeRectangle(0, 0, width, height);
  }


  const std::string getImageName(const G3MContext* context) const {
    const IStringUtils* su = context->getStringUtils();

    return "_AltimeterCanvasImage_" + getResolutionID(context) + "_" + su->toString(_altitude);
  }


public:
  AltimeterCanvasImageBuilder() :
  CanvasImageBuilder(256, 256*3, true)
  {
  }

  bool isMutable() const {
    return true;
  }

  void step() {
    _altitude += _step;
    if (_altitude > 40000) {
      _altitude = 40000;
      _step *= -1;
    }
    if (_altitude < 0) {
      _altitude = 0;
      _step *= -1;
    }

    changed();
  }

};


class AnimateHUDWidgetsTask : public GTask {
private:
  HUDQuadWidget*     _compass1;
  HUDQuadWidget*     _compass2;
  HUDQuadWidget*     _ruler;
  LabelImageBuilder* _labelBuilder;
  AltimeterCanvasImageBuilder* _altimeterCanvasImageBuilder;

  double _angleInRadians;

  float _translationV;
  float _translationStep;

public:
  AnimateHUDWidgetsTask(HUDQuadWidget* compass1,
                        HUDQuadWidget* compass2,
                        HUDQuadWidget* ruler,
                        LabelImageBuilder* labelBuilder,
                        AltimeterCanvasImageBuilder* altimeterCanvasImageBuilder) :
  _compass1(compass1),
  _compass2(compass2),
  _ruler(ruler),
  _labelBuilder(labelBuilder),
  _altimeterCanvasImageBuilder(altimeterCanvasImageBuilder),
  _angleInRadians(0),
  _translationV(0),
  _translationStep(0.002)
  {
  }

  void run(const G3MContext* context) {
    _angleInRadians += Angle::fromDegrees(1)._radians;
    //        _labelBuilder->setText( Angle::fromRadians(_angleInRadians).description() );
    double degrees = Angle::fromRadians(_angleInRadians)._degrees;
    while (degrees > 360) {
      degrees -= 360;
    }
    const std::string degreesText = IStringUtils::instance()->toString( IMathUtils::instance()->round( degrees )  );
    _labelBuilder->setText( "     " + degreesText );

    //        _compass1->setTexCoordsRotation(_angleInRadians,
    //                                        0.5f, 0.5f);
    _compass2->setTexCoordsRotation(-_angleInRadians,
                                    0.5f, 0.5f);

    //        _compass3->setTexCoordsRotation(Angle::fromRadians(_angle),
    //                                        0.5f, 0.5f);

    if (_translationV > 0.5 || _translationV < 0) {
      _translationStep *= -1;
    }
    _translationV += _translationStep;
    _ruler->setTexCoordsTranslation(0, _translationV);

    _altimeterCanvasImageBuilder->step();
  }
};


void G3MHUDDemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  HUDRenderer* hudRenderer = model->getHUDRenderer();






  AltimeterCanvasImageBuilder* altimeterCanvasImageBuilder = new AltimeterCanvasImageBuilder();
  HUDQuadWidget* test = new HUDQuadWidget(altimeterCanvasImageBuilder,
                                          new HUDRelativePosition(0,
                                                                  HUDRelativePosition::VIEWPORT_WIDTH,
                                                                  HUDRelativePosition::LEFT,
                                                                  10),
                                          new HUDRelativePosition(0.5,
                                                                  HUDRelativePosition::VIEWPORT_HEIGHT,
                                                                  HUDRelativePosition::MIDDLE),
                                          new HUDRelativeSize(0.22,
                                                              HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                          new HUDRelativeSize(0.66,
                                                              HUDRelativeSize::VIEWPORT_MIN_AXIS)
                                          );
  hudRenderer->addWidget(test);

  LabelImageBuilder* labelBuilder = new LabelImageBuilder("glob3",               /* text         */
                                                          GFont::monospaced(38), /* font         */
                                                          Color::YELLOW,         /* color        */
                                                          Color::BLACK,          /* shadowColor  */
                                                          3,                     /* shadowBlur   */
                                                          Vector2F(1, -1),       /* shadowOffset */
                                                          new BoxImageBackground(Vector2F::zero(),   /* margin          */
                                                                                 0,                  /* borderWidth     */
                                                                                 Color::TRANSPARENT, /* borderColor     */
                                                                                 Vector2F(6, 6),     /* padding         */
                                                                                 Color::RED,         /* backgroundColor */
                                                                                 4                   /* cornerRadius    */),
                                                          true /* mutable */);

  HUDQuadWidget* label = new HUDQuadWidget(labelBuilder,
                                           new HUDAbsolutePosition(10),
                                           new HUDAbsolutePosition(10),
                                           new HUDRelativeSize(1, HUDRelativeSize::BITMAP_WIDTH),
                                           new HUDRelativeSize(1, HUDRelativeSize::BITMAP_HEIGHT) );
  hudRenderer->addWidget(label);

  HUDQuadWidget* compass2 = new HUDQuadWidget(new DownloaderImageBuilder(URL("file:///CompassHeadings.png")),
                                              new HUDRelativePosition(0.5,
                                                                      HUDRelativePosition::VIEWPORT_WIDTH,
                                                                      HUDRelativePosition::CENTER),
                                              new HUDRelativePosition(0.5,
                                                                      HUDRelativePosition::VIEWPORT_HEIGHT,
                                                                      HUDRelativePosition::MIDDLE),
                                              new HUDRelativeSize(0.25,
                                                                  HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                              new HUDRelativeSize(0.125,
                                                                  HUDRelativeSize::VIEWPORT_MIN_AXIS));
  compass2->setTexCoordsRotation(Angle::fromDegrees(30),
                                 0.5f, 0.5f);
  compass2->setTexCoordsScale(1, 0.5f);
  hudRenderer->addWidget(compass2);

  float visibleFactor = 3;
  HUDQuadWidget* ruler = new HUDQuadWidget(new DownloaderImageBuilder(URL("file:///altimeter-ruler-1536x113.png")),
                                           new HUDRelativePosition(1,
                                                                   HUDRelativePosition::VIEWPORT_WIDTH,
                                                                   HUDRelativePosition::RIGHT,
                                                                   10),
                                           new HUDRelativePosition(0.5,
                                                                   HUDRelativePosition::VIEWPORT_HEIGHT,
                                                                   HUDRelativePosition::MIDDLE),
                                           new HUDRelativeSize(2 * (113.0 / 1536.0),
                                                               HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                           new HUDRelativeSize(2 / visibleFactor,
                                                               HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                           new DownloaderImageBuilder(URL("file:///widget-background.png")));
  ruler->setTexCoordsScale(1 , 1.0f / visibleFactor);
  hudRenderer->addWidget(ruler);

  g3mWidget->addPeriodicalTask(new PeriodicalTask(TimeInterval::fromMilliseconds(50),
                                                  new AnimateHUDWidgetsTask(label,
                                                                            compass2,
                                                                            ruler,
                                                                            labelBuilder,
                                                                            altimeterCanvasImageBuilder)));
  
}
