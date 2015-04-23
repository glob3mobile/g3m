//
//  G3MAnimatedMarksDemoScene.cpp
//  G3MApp
//
//  Created by Jose Miguel SN on 20/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/IJSONParser.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/JSONArray.hpp>
#include <G3MiOSSDK/JSONNumber.hpp>
#include <G3MiOSSDK/Mark.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>
#include <G3MiOSSDK/IStringUtils.hpp>
#include <G3MiOSSDK/MarksRenderer.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>

#include "G3MDemoModel.hpp"

#include "G3MAnimatedMarksDemoScene.hpp"

class RescaleMarkTask: public PeriodicalTask{
  
  class RescaleMarkTaskGTask: public GTask{
    Mark* _mark;
    float _size;
    float _delta;
  public:
    
    ~RescaleMarkTaskGTask(){}
    
    RescaleMarkTaskGTask(Mark* mark):
    _mark(mark), _size(40), _delta(1)
    {
    }
    
    
    virtual void run(const G3MContext* context){
      if (_size < 30 || _size > 75){
        _delta = -_delta;
      }
      
      _size += _delta;
      
      _mark->setOnScreenSizeOnPixels(_size, _size);
    }
    
  };
  
  
public:
  RescaleMarkTask(Mark* mark, const TimeInterval& frameTime):
  PeriodicalTask(frameTime, new RescaleMarkTaskGTask(mark))
  {
  }
};

void G3MAnimatedMarksDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();
  
  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);
  
  
  MarksRenderer* marksRenderer = getModel()->getMarksRenderer();
  
  {
    Mark* animMark = new Mark(URL(URL::FILE_PROTOCOL + "radar-sprite.png"),
                              Geodetic3D::fromDegrees( 26.099999998178312, -15.41699999885168, 0),
                              ABSOLUTE,
                              4.5e+06,
                              NULL,
                              true,
                              NULL,
                              false);
    animMark->setOnScreenSizeOnProportionToImage(0.05, 0.1);
    g3mWidget->addPeriodicalTask(new TextureAtlasMarkAnimationTask(animMark, 4, 2, 7, TimeInterval::fromMilliseconds(100)));
    marksRenderer->addMark(animMark);
  }
  
  {
    Mark* animMark2 = new Mark(URL(URL::FILE_PROTOCOL + "radar-sprite.png"),
                               Geodetic3D::fromDegrees( 25.428140, -17.016841, 0),
                               ABSOLUTE,
                               4.5e+06,
                               NULL,
                               true,
                               NULL,
                               false);

    animMark2->setOnScreenSizeOnPixels(100,100);
    animMark2->setMarkAnchor(0.5, 1.0);
    marksRenderer->addMark(animMark2);
    g3mWidget->addPeriodicalTask(new TextureAtlasMarkAnimationTask(animMark2, 4, 2, 7, TimeInterval::fromMilliseconds(100)));
  }
  
  
  Geodetic3D canarias[] = { Geodetic3D::fromDegrees(28.131817, -15.440219, 0),
    Geodetic3D::fromDegrees(28.947345, -13.523105, 0),
    Geodetic3D::fromDegrees(28.473802, -13.859360, 0),
    Geodetic3D::fromDegrees(28.467706, -16.251426, 0),
    Geodetic3D::fromDegrees(28.701819, -17.762003, 0),
    Geodetic3D::fromDegrees(28.086595, -17.105796, 0),
    Geodetic3D::fromDegrees(27.810709, -17.917639, 0)
  };
  
  
  for (int i = 0; i < 7; i++)
  {
    
    Mark* pinMark = new Mark(URL(URL::FILE_PROTOCOL + "pin.png"),
                             canarias[i],
                             ABSOLUTE,
                             4.5e+06,
                             NULL,
                             true,
                             NULL,
                             false);

    pinMark->setMarkAnchor(0.5, 1.0);
    marksRenderer->addMark(pinMark);
    g3mWidget->addPeriodicalTask(new RescaleMarkTask(pinMark, TimeInterval::fromMilliseconds(100)));
  }
  
  {
    Mark* regMark = new Mark("HELLO ANIMATED MARKS!",
                             Geodetic3D::fromDegrees( 27.599999998178312, -15.41699999885168, 0),
                             ABSOLUTE);
    marksRenderer->addMark(regMark);
  }
  
  g3mWidget->setCameraPosition(Geodetic3D::fromDegrees(27.978501160595179442,
                                                       -15.537316508402767568,
                                                       1634789.5083396979608));
}

void G3MAnimatedMarksDemoScene::deactivate(const G3MContext* context) {
  G3MDemoScene::deactivate(context);
}

void G3MAnimatedMarksDemoScene::rawSelectOption(const std::string& option,
                                                int optionIndex) {
  
}