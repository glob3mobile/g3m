//
//  G3MVectorStreaming2DemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 7/30/15.
//

#include "G3MVectorStreaming2DemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/VectorStreamingRenderer.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/GEO2DPointGeometry.hpp>
#include <G3MiOSSDK/GEOFeature.hpp>
#include <G3MiOSSDK/Mark.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/JSONString.hpp>
#include <G3MiOSSDK/JSONNumber.hpp>
#include <G3MiOSSDK/StackLayoutImageBuilder.hpp>
#include <G3MiOSSDK/CircleImageBuilder.hpp>
#include <G3MiOSSDK/LabelImageBuilder.hpp>
#include <G3MiOSSDK/MarksRenderer.hpp>

#include "G3MDemoModel.hpp"
#include <sstream>


class G3MVectorStreaming2DemoScene_Symbolizer : public VectorStreamingRenderer::VectorSetSymbolizer {
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
    const Geodetic3D position(cluster->getPosition()->_latitude,
                              cluster->getPosition()->_longitude,
                              0);

    std::stringstream labelStream;
    labelStream << cluster->getSize();

    std::string label;
    labelStream >> label;

    const double clusterPercent = (double) cluster->getSize() / metadata->_featuresCount;

    const IMathUtils* mu = IMathUtils::instance();

    const float labelFontSize = (float) ((14.0 * clusterPercent) + 16.0) ;

    const double area = (15000.0 * clusterPercent);
    const int radius = 12 + mu->round((float) mu->sqrt(area / PI));

    Mark* mark = new Mark(new StackLayoutImageBuilder(new CircleImageBuilder(Color::WHITE,
                                                                             radius),
                                                      new LabelImageBuilder(label,
                                                                            GFont::sansSerif(labelFontSize, true),
                                                                            Vector2F(2, 2),     // margin
                                                                            Color::BLACK,       // color
                                                                            Color::WHITE,       // shadowColor
                                                                            5.0f,               // shadowBlur
                                                                            Vector2F(0, 0),     // shadowOffset
                                                                            Color::TRANSPARENT, // backgroundColor
                                                                            4.0f                // cornerRadius
                                                                            )
                                                      ),
                          position,
                          ABSOLUTE,
                          0 // minDistanceToCamera
                          );

    return mark;
  }

};


void G3MVectorStreaming2DemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  model->getMarksRenderer()->setRenderInReverse(true);

  VectorStreamingRenderer* renderer = model->getVectorStreamingRenderer();
  renderer->addVectorSet(//URL("http://192.168.1.12:8080/server-mapboo/public/VectorialStreaming/"),
                         URL("http://mapboo.com/server-mapboo/public/v1/VectorialStreaming/"),
                         "55f922dc0c1bc2b9673c6203", // tornados clustering
                         "om|yr|mo|dy|date|time|tz|st|stf|stn|mag|inj|fat|loss|closs|slat|slon|elat|elon|len|wid|",
                         new G3MVectorStreaming2DemoScene_Symbolizer(),
                         true, // deleteSymbolizer
                         DownloadPriority::HIGHER,
                         TimeInterval::zero(),
                         true, // readExpired
                         true, // verbose
                         true, // haltOnError
                         VectorStreamingRenderer::Format::SERVER,
                         Angle::fromDegrees(90), // minSectorSize,
                         12500000                // minProjectedArea
                         );

  //  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(46.612016780685230799, 7.8587244849714883443, 5410460) );

}
