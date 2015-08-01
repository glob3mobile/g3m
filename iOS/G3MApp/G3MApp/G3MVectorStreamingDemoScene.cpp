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

#include "G3MDemoModel.hpp"


class G3MVectorStreamingDemoScene_Symbolizer : public VectorStreamingRenderer::VectorSetSymbolizer {
public:
  Mark* createMark(const GEO2DPointGeometry* geometry) const {
    const GEOFeature* feature = geometry->getFeature();

    const JSONObject* properties = feature->getProperties();

//    /**
//     * Creates a mark just with label, without icon
//     */
//    Mark(const std::string& label,
//         const Geodetic3D&  position,
//         AltitudeMode       altitudeMode,
//         double             minDistanceToCamera=4.5e+06,
//         const float        labelFontSize=20,
//         const Color*       labelFontColor=Color::newFromRGBA(1, 1, 1, 1),
//         const Color*       labelShadowColor=Color::newFromRGBA(0, 0, 0, 1),
//         MarkUserData*      userData=NULL,
//         bool               autoDeleteUserData=true,
//         MarkTouchListener* listener=NULL,
//         bool               autoDeleteListener=false);


    const std::string label = properties->getAsString("name")->value();
    const Geodetic3D  position( geometry->getPosition(), 0);

    double maxPopulation = 22315474;
    double population = properties->getAsNumber("population")->value();
    float labelFontSize = (float) (10.0 * (population / maxPopulation) + 14.0) ;

    return new Mark(label,
                    position,
                    ABSOLUTE,
                    0, // minDistanceToCamera
                    labelFontSize
                    );
  }

};


void G3MVectorStreamingDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();


  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  VectorStreamingRenderer* renderer = model->getVectorStreamingRenderer();
  renderer->addVectorSet(URL("http://192.168.1.12:8080/server-mapboo/public/VectorialStreaming/"),
                         "GEONames-PopulatedPlaces_LOD",
                         new G3MVectorStreamingDemoScene_Symbolizer(),
                         true, // deleteSymbolizer
                         DownloadPriority::LOWER,
                         TimeInterval::zero(),
                         true, // readExpired
                         true // verbose
                         );


  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(46.612016780685230799, 7.8587244849714883443, 5410460) );
  
}
