//
//  G3MMarksDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#include "G3MMarksDemoScene.hpp"

#include <G3M/G3MWidget.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/IDownloader.hpp>
#include <G3M/DownloadPriority.hpp>
#include <G3M/IBufferDownloadListener.hpp>
#include <G3M/IJSONParser.hpp>
#include <G3M/JSONObject.hpp>
#include <G3M/JSONArray.hpp>
#include <G3M/JSONNumber.hpp>
#include <G3M/Mark.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/IStringUtils.hpp>
#include <G3M/MarksRenderer.hpp>
#include <G3M/BingMapsLayer.hpp>

#include <G3M/Cylinder.hpp>
#include <G3M/MeshRenderer.hpp>
#include <G3M/CompositeRenderer.hpp>
#include <G3M/Arrow.hpp>
#include <G3M/TranslateScaleGizmo.hpp>
#include <G3M/ShapesRenderer.hpp>
#include <G3M/EllipsoidShape.hpp>

#include "G3MDemoModel.hpp"


class G3MMarksDemoScene_BufferDownloadListener : public IBufferDownloadListener {
private:
  G3MMarksDemoScene* _scene;
public:
  G3MMarksDemoScene_BufferDownloadListener(G3MMarksDemoScene* scene) :
  _scene(scene)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {

    const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer);
    if (jsonBaseObject == NULL) {
      ILogger::instance()->logError("Can't parse (1) \"%s\"", url._path.c_str());
    }
    else {
      const JSONObject* jsonObject = jsonBaseObject->asObject();
      if (jsonObject == NULL) {
        ILogger::instance()->logError("Can't parse (2) \"%s\"", url._path.c_str());
      }
      else {
        const IStringUtils* su = IStringUtils::instance();

        const JSONArray* list = jsonObject->getAsArray("list");
        for (int i = 0; i < list->size(); i++) {

          const JSONObject* city = list->getAsObject(i);
          const JSONObject* coords = city->getAsObject("coord");
          const Geodetic3D position = Geodetic3D::fromDegrees(coords->getAsNumber("lat")->value(),
                                                              coords->getAsNumber("lon")->value(),
                                                              0);
          const JSONArray* weather = city->getAsArray("weather");
          const JSONObject* weatherObject = weather->getAsObject(0);

          std::string icon;
          if (weatherObject->getAsString("icon", "DOUBLE") == "DOUBLE") {
            icon = su->toString( (int) weatherObject->getAsNumber("icon")->value() ) + "d.png";
            if (icon.length() < 7) {
              icon = "0" + icon;
            }
          }
          else {
            icon = weatherObject->getAsString("icon", "DOUBLE") + ".png";
          }

          Mark* mark = new Mark(city->getAsString("name", ""),
                                URL("http://openweathermap.org/img/w/" + icon),
                                position,
                                RELATIVE_TO_GROUND,
                                0,                              // minDistanceToCamera
                                true,                           // labelBottom
                                13,                             // labelFontSize
                                Color::newFromRGBA(1, 1, 1, 1), // labelFontColor
                                Color::newFromRGBA(0, 0, 0, 1), // labelShadowColor
                                -10                             // labelGapSize
                                );

          _scene->addMark(mark);
        }
      }

      delete jsonBaseObject;
    }

    delete buffer;
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }

};

void G3MMarksDemoScene::addMark(Mark* mark) {
  getModel()->getMarksRenderer()->addMark(mark);
}

class GizmoListener : public TranslateScaleGizmoListener{
  EllipsoidShape* _shape;
  const Planet* _planet;
public:
  
  GizmoListener(EllipsoidShape* shape, const Planet* planet):
  _shape(shape), _planet(planet){}
  
  void onChanged(const TranslateScaleGizmo& gizmo) override{
    printf("Gizmo P: %s S: %0.2f\n", gizmo.getCoordinateSystem()._origin.description().c_str(), gizmo.getScale());
    
    Geodetic3D geoPos = _planet->toGeodetic3D(gizmo.getCoordinateSystem()._origin);
    _shape->setPosition(geoPos);
    _shape->setScale(gizmo.getScale());
  }
  
  void onChangeEnded(const TranslateScaleGizmo& gizmo) override{
    printf("Change ended on gizmo P: %s S: %0.2f\n", gizmo.getCoordinateSystem()._origin.description().c_str(), gizmo.getScale());
  }
};


void G3MMarksDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  g3mWidget->setBackgroundColor(Color::fromRGBA(0.9f, 0.21f, 0.21f, 1.0f));

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

//  IDownloader* downloader = context->getDownloader();
//
//  _requestID = downloader->requestBuffer(URL("http://openweathermap.org/data/2.5/box/city?bbox=-80,-180,80,180,4&cluster=yes&appid=e1079e4aa327b6cf16aa5b68d47ed1e2"),
//                                         DownloadPriority::HIGHEST,
//                                         TimeInterval::fromHours(1),
//                                         true,
//                                         new G3MMarksDemoScene_BufferDownloadListener(this),
//                                         true);

//  g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(23.2, 5.5, 3643920),
//                                       Angle::zero(), // heading
//                                       Angle::fromDegrees(30 - 90) // pitch
//                                       );

//  Mark* mark = new Mark("Las Palmas",
//                        URL("https://icons-for-free.com/iconfiles/png/512/sun+sunny+weather+icon-1320196635525068067.png"),
//                        Geodetic3D::fromDegrees(28.09973, -15.41343, 0),
//                        RELATIVE_TO_GROUND,
//                        0,                              // minDistanceToCamera
//                        true,                           // labelBottom
//                        13,                             // labelFontSize
//                        Color::newFromRGBA(1, 1, 1, 1), // labelFontColor
//                        Color::newFromRGBA(0, 0, 0, 1), // labelShadowColor
//                        -10                             // labelGapSize
//                        );
//
//  addMark(mark);
  
  Geodetic3D geoPos = Geodetic3D::fromDegrees(28.09973, -15.41343, 0);


  g3mWidget->setAnimatedCameraPosition(Geodetic3D(geoPos._latitude, geoPos._longitude, geoPos._height + 10000));



  double size = 1000.0;
  EllipsoidShape* ellipsoid = new EllipsoidShape(new Geodetic3D(geoPos),
                                                 AltitudeMode::ABSOLUTE,
                                                 Vector3D(size, size, size),
                                                 18,
                                                 1.0,
                                                 false,
                                                 false,
                                                 Color::fromRGBA255(128, 128, 128, 128),
                                                 new Color(Color::WHITE),
                                                 true);
  model->getShapesRenderer()->addShape(ellipsoid);

  const double scale                     = 1.0;
  const double maxScale                  = 2;
  const double lineWidthRatio            = 0.01;
  const double headLengthRatio           = 0.05;
  const double headWidthRatio            = 2.0;
  const double scaleArrowLengthSizeRatio = 0.15;

  TranslateScaleGizmo* gizmo = new TranslateScaleGizmo(context->getPlanet()->getCoordinateSystemAt(geoPos),
                                                       size,
                                                       scale,
                                                       maxScale,
                                                       lineWidthRatio,
                                                       headLengthRatio,
                                                       headWidthRatio,
                                                       scaleArrowLengthSizeRatio);
  gizmo->setListener(new GizmoListener(ellipsoid, context->getPlanet()));
  
  model->getCompositeRenderer()->addRenderer(gizmo);
}

void G3MMarksDemoScene::deactivate(const G3MContext* context) {
  context->getDownloader()->cancelRequest(_requestID);

  G3MDemoScene::deactivate(context);
}

void G3MMarksDemoScene::rawSelectOption(const std::string& option,
                                          int optionIndex) {
  
}
