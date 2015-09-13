//
//  G3MVectorStreamingDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 7/30/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MVectorStreamingDemoScene.hpp"

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

#include "G3MDemoModel.hpp"
#include <sstream>


class G3MVectorStreamingDemoScene_Symbolizer : public VectorStreamingRenderer::VectorSetSymbolizer {
public:
  Mark* createFeatureMark(const GEO2DPointGeometry* geometry) const {
    const GEOFeature* feature = geometry->getFeature();

    const JSONObject* properties = feature->getProperties();

    const std::string label = properties->getAsString("name")->value();
    const Geodetic3D  position( geometry->getPosition(), 0);

    double maxPopulation = 22315474;
    double population = properties->getAsNumber("population")->value();
    float labelFontSize = (float) (14.0 * (population / maxPopulation) + 16.0) ;

    Mark* mark = new Mark(label,
                          position,
                          ABSOLUTE,
                          0, // minDistanceToCamera
                          labelFontSize
                          // Color::newFromRGBA(1, 1, 0, 1)
                          );
    mark->setZoomInAppears(true);
    return mark;
  }

  Mark* createClusterMark(const VectorStreamingRenderer::Cluster* cluster,
                          long long featuresCount) const {
    const Geodetic3D  position(cluster->getPosition()->_latitude,
                               cluster->getPosition()->_longitude,
                               0);

    std::stringstream labelStream;
//    labelStream << "(";
    labelStream << cluster->getSize();
//    labelStream << ")";

    std::string label;
    labelStream >> label;

    // float labelFontSize = (float) (14.0 * ((float) cluster->getSize() / featuresCount) + 16.0) ;
    float labelFontSize = 18.0f;

    Mark* mark = new Mark(new StackLayoutImageBuilder(new CircleImageBuilder(Color::white(), 32),
                                                      new LabelImageBuilder(label,
                                                                            GFont::sansSerif(labelFontSize, true),
                                                                            2.0f,                 // margin
                                                                            Color::black(),       // color
                                                                            Color::transparent(), // shadowColor
                                                                            5.0f,                 // shadowBlur
                                                                            0.0f,                 // shadowOffsetX
                                                                            0.0f,                 // shadowOffsetY
                                                                            Color::white(),       // backgroundColor
                                                                            4.0f                  // cornerRadius
                                                                            )
                                                      ),
                          position,
                          ABSOLUTE,
                          0 // minDistanceToCamera
                          );

//    Mark* mark = new Mark(label,
//                          position,
//                          ABSOLUTE,
//                          0, // minDistanceToCamera
//                          labelFontSize,
//                          Color::newFromRGBA(1, 1, 0, 1)
//                          );

    return mark;
  }

};


void G3MVectorStreamingDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();


  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  model->getMarksRenderer()->setRenderInReverse(true);

  VectorStreamingRenderer* renderer = model->getVectorStreamingRenderer();
  renderer->addVectorSet(URL("http://192.168.1.12:8080/server-mapboo/public/VectorialStreaming/"),
                         "GEONames-PopulatedPlaces_LOD",
                         "name|population|featureClass|featureCode",
                         new G3MVectorStreamingDemoScene_Symbolizer(),
                         true, // deleteSymbolizer
                         //DownloadPriority::LOWER,
                         DownloadPriority::HIGHER,
                         TimeInterval::zero(),
                         true, // readExpired
                         true, // verbose
                         true // haltOnError
                         );


  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(46.612016780685230799, 7.8587244849714883443, 5410460) );
  
}
