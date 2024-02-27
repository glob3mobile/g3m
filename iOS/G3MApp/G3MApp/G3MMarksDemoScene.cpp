//
//  G3MMarksDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#include "G3MMarksDemoScene.hpp"

#include <G3M/G3MWidget.hpp>
#include <G3M/Color.hpp>
#include <G3M/BingMapsLayer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Mark.hpp>
#include <G3M/MarksRenderer.hpp>
#include <G3M/MarkTouchListener.hpp>

#include "G3MDemoModel.hpp"

class G3MMarksDemoScene_MarkTouchListener : public MarkTouchListener {
  bool touchedMark(Mark* mark) {
    ILogger::instance()->logInfo("Mark touched!");

    return true;
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

  Mark* mark = new Mark(URL("file:///mark-icon-1.png"),                                            // iconURL
                        Geodetic3D::fromDegrees(21.580896830714426216, -71.930032768589384773, 0), // position
                        ABSOLUTE,                                                                  // altitudeMode
                        4500000,                                                                   // minDistanceToCamera=4.5e+06
                        NULL,                                                                      // userData=NULL,
                        true,                                                                      // autoDeleteUserData=true,
                        new G3MMarksDemoScene_MarkTouchListener(),                                 // MarkTouchListener* listener=NULL,
                        true                                                                       // autoDeleteListener=false
                        );

//  mark->setMarkAnchor(1, 0.5);
  mark->setMarkAnchor(0.5, 1);
//  mark->setMarkAnchor(1, 1);

//  mark->setOnScreenSizeOnProportionToImage(2, 0.5);
//  mark->setOnScreenSizeOnProportionToImage(0.5, 2);

  model->getMarksRenderer()->addMark(mark);

  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(21.580896830714426216, -71.930032768589384773, 160000));


}

void G3MMarksDemoScene::deactivate(const G3MContext* context) {
  getModel()->getMarksRenderer()->removeAllMarks();

  G3MDemoScene::deactivate(context);
}
