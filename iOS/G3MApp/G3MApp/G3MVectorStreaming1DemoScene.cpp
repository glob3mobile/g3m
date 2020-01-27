//
//  G3MVectorStreaming1DemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 7/30/15.
//

#include "G3MVectorStreaming1DemoScene.hpp"

#include <G3MSharedSDK/G3MWidget.hpp>
#include <G3MSharedSDK/BingMapsLayer.hpp>
#include <G3MSharedSDK/LayerSet.hpp>
#include <G3MSharedSDK/VectorStreamingRenderer.hpp>
#include <G3MSharedSDK/DownloadPriority.hpp>
#include <G3MSharedSDK/GEO2DPointGeometry.hpp>
#include <G3MSharedSDK/GEOFeature.hpp>
#include <G3MSharedSDK/Mark.hpp>
#include <G3MSharedSDK/JSONObject.hpp>
#include <G3MSharedSDK/JSONString.hpp>
#include <G3MSharedSDK/JSONNumber.hpp>
#include <G3MSharedSDK/StackLayoutImageBuilder.hpp>
#include <G3MSharedSDK/CircleImageBuilder.hpp>
#include <G3MSharedSDK/LabelImageBuilder.hpp>
#include <G3MSharedSDK/MarksRenderer.hpp>

#include "G3MDemoModel.hpp"
#include <sstream>


class G3MVectorStreaming1DemoScene_Symbolizer : public VectorStreamingRenderer::VectorSetSymbolizer {
public:
  Mark* createGeometryMark(const VectorStreamingRenderer::Metadata* metadata,
                           const VectorStreamingRenderer::Node* node,
                           const GEO2DPointGeometry* geometry) const {
    const GEOFeature* feature = geometry->getFeature();

    const JSONObject* properties = feature->getProperties();

    const int mag = properties->getAsNumber("mag")->value();

    const Geodetic3D  position( geometry->getPosition(), 0);

    int red   = 255;
    int green = 255;
    int blue  = 255;
    int alpha = 171;
    // int alpha = 255;
    switch (mag) {
      case 0:
        red   = 0;
        green = 250;
        blue  = 244;
        break;
      case 1:
        red   = 255;
        green = 255;
        blue  = 204;
        break;
      case 2:
        red   = 255;
        green = 231;
        blue  = 117;
        break;
      case 3:
        red   = 255;
        green = 193;
        blue  = 64;
        break;
      case 4:
        red   = 255;
        green = 143;
        blue  = 32;
        break;
      case 5:
        red   = 255;
        green = 96;
        blue  = 96;
        break;

      default:
        break;
    }
    Color featureColor = Color::fromRGBA255(red, green, blue, alpha);

    int pointSize = 12;

    Mark* mark = new Mark(new CircleImageBuilder(featureColor, pointSize),
                          position,
                          ABSOLUTE,
                          0 // minDistanceToCamera
                          );

    return mark;
  }

  Mark* createGeometryMark(const VectorStreamingRenderer::Metadata* metadata,
                           const VectorStreamingRenderer::Node* node,
                           const GEO3DPointGeometry* geometry) const {
    return NULL;
  }

  Mark* createClusterMark(const VectorStreamingRenderer::Metadata* metadata,
                          const VectorStreamingRenderer::Node* node,
                          const VectorStreamingRenderer::Cluster* cluster) const {
    return NULL;
  }

};


void G3MVectorStreaming1DemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  model->getMarksRenderer()->setRenderInReverse(true);

  VectorStreamingRenderer* renderer = model->getVectorStreamingRenderer();
  renderer->addVectorSet(URL("http://mapboo.com/server-mapboo/public/v1/VectorialStreaming/"),
                         "55f91bab0c1bc2b9673c5f8a", // tornados sorting
                         "om|yr|mo|dy|date|time|tz|st|stf|stn|mag|inj|fat|loss|closs|slat|slon|elat|elon|len|wid|",
                         new G3MVectorStreaming1DemoScene_Symbolizer(),
                         true, // deleteSymbolizer
                         DownloadPriority::HIGHER,
                         TimeInterval::zero(),
                         true, // readExpired
                         true, // verbose
                         true, // haltOnError
                         VectorStreamingRenderer::Format::SERVER,
                         Angle::fromDegrees(90), // minSectorSize,
                         12500000,               // minProjectedArea
                         0, //minHeight,
                         0  //maxHeight
                         );


  //  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(46.612016780685230799, 7.8587244849714883443, 5410460) );

}
