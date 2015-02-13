//
//  G3MNonOverlappingMarksDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 1/30/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MNonOverlappingMarksDemoScene.hpp"

#include <G3MiOSSDK/OSMLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>
#include <G3MiOSSDK/DownloaderImageBuilder.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/LabelImageBuilder.hpp>
#include <G3MiOSSDK/ColumnLayoutImageBuilder.hpp>

#include "G3MDemoModel.hpp"

void G3MNonOverlappingMarksDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();


  OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);


  NonOverlappingMarksRenderer* renderer = model->getNonOverlappingMarksRenderer();

  const URL markBitmapURL("file:///g3m-marker.png");
  const URL anchorBitmapURL("file:///anchorWidget.png");



//  NonOverlappingMark* mark1 = new NonOverlappingMark(new DownloaderImageBuilder(markBitmapURL),
//                                                     new DownloaderImageBuilder(anchorBitmapURL),
//                                                     Geodetic3D::fromDegrees(28.131817, -15.440219, 0));
  NonOverlappingMark* mark1 = new NonOverlappingMark(new ColumnLayoutImageBuilder(new DownloaderImageBuilder(markBitmapURL),
                                                                                  new LabelImageBuilder("$118",
                                                                                                        GFont::sansSerif(),
                                                                                                        4,
                                                                                                        Color::black(),
                                                                                                        Color::transparent(),
                                                                                                        0,0,0,
                                                                                                        Color::white(),
                                                                                                        4
                                                                                                        ),
                                                                                  0,              // margin
                                                                                  0.5,            // borderWidth,
                                                                                  Color::black(), // borderColor
                                                                                  4,              // padding
                                                                                  Color::gray(),  // backgroundColor
                                                                                  8,              // cornerRadius
                                                                                  2               // childrenSeparation
                                                                                  ),
                                                     new DownloaderImageBuilder(anchorBitmapURL),
                                                     Geodetic3D::fromDegrees(28.131817, -15.440219, 0));
  renderer->addMark(mark1);

  NonOverlappingMark* mark2 = new NonOverlappingMark(new DownloaderImageBuilder(markBitmapURL),
                                                     new DownloaderImageBuilder(anchorBitmapURL),
                                                     Geodetic3D::fromDegrees(28.947345, -13.523105, 0));
  renderer->addMark(mark2);

  NonOverlappingMark* mark3 = new NonOverlappingMark(new DownloaderImageBuilder(markBitmapURL),
                                                     new DownloaderImageBuilder(anchorBitmapURL),
                                                     Geodetic3D::fromDegrees(28.473802, -13.859360, 0));
  renderer->addMark(mark3);

  NonOverlappingMark* mark4 = new NonOverlappingMark(new DownloaderImageBuilder(markBitmapURL),
                                                     new DownloaderImageBuilder(anchorBitmapURL),
                                                     Geodetic3D::fromDegrees(28.467706, -16.251426, 0));
  renderer->addMark(mark4);

  NonOverlappingMark* mark5 = new NonOverlappingMark(new DownloaderImageBuilder(markBitmapURL),
                                                     new DownloaderImageBuilder(anchorBitmapURL),
                                                     Geodetic3D::fromDegrees(28.701819, -17.762003, 0));
  renderer->addMark(mark5);

  NonOverlappingMark* mark6 = new NonOverlappingMark(new DownloaderImageBuilder(markBitmapURL),
                                                     new DownloaderImageBuilder(anchorBitmapURL),
                                                     Geodetic3D::fromDegrees(28.086595, -17.105796, 0));
  renderer->addMark(mark6);

  NonOverlappingMark* mark7 = new NonOverlappingMark(new DownloaderImageBuilder(markBitmapURL),
                                                     new DownloaderImageBuilder(anchorBitmapURL),
                                                     Geodetic3D::fromDegrees(27.810709, -17.917639, 0));
  renderer->addMark(mark7);


  // Canarias, here we go!
  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(28.034468668529083146,
                                                                -15.904092315837871752,
                                                                1634079) );
}
