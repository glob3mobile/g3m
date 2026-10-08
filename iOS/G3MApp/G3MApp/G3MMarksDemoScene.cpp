//
//  G3MMarksDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#include "G3MMarksDemoScene.hpp"

#include <G3M/G3MWidget.hpp>
#include <G3M/Color.hpp>
#include <G3M/Layer.hpp>
#include <G3M/TimeInterval.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Geodetic2D.hpp>
#include <G3M/Mark.hpp>
#include <G3M/MarksRenderer.hpp>
#include <G3M/MarkTouchListener.hpp>
#include <G3M/TouchEvent.hpp>
#include <G3M/PeriodicalTask.hpp>
#include <G3M/TextureAtlasMarkAnimationTask.hpp>
#include <G3M/Angle.hpp>

#include "G3MDemoModel.hpp"

class G3MMarksDemoScene_MarkTouchListener : public MarkTouchListener {
  bool touchedMark(Mark* mark, const TouchEvent* touchEvent) {
    ILogger::instance()->logInfo("Mark touched! " + touchEvent->description());

    return true;
  }
};


/** grows and shrinks a mark between 30 and 75 pixels, one pixel per period */
class G3MMarksDemoScene_RescaleMarkTask: public PeriodicalTask {

  class RescaleMarkTaskGTask: public GTask {
    Mark* _mark;
    float _size;
    float _delta;

  public:

    ~RescaleMarkTaskGTask() {}

    RescaleMarkTaskGTask(Mark* mark):
    _mark(mark), _size(40), _delta(1)
    {
    }

    virtual void run(const G3MContext* context) {
      if (_size < 30 || _size > 75) {
        _delta = -_delta;
      }

      _size += _delta;

      _mark->setScreenSize(_size, _size);
    }

  };

public:
  G3MMarksDemoScene_RescaleMarkTask(Mark* mark, const TimeInterval& frameTime):
  PeriodicalTask(frameTime, new RescaleMarkTaskGTask(mark))
  {
  }
};


/** moves a mark north and south along its meridian, one step per period, so it crosses the horizon */
class G3MMarksDemoScene_MoveMarkTask: public PeriodicalTask {

  class MoveMarkTaskGTask: public GTask {
    Mark*        _mark;
    const double _longitudeInDegrees;
    const double _southernmostLatitudeInDegrees;
    const double _northernmostLatitudeInDegrees;
    double       _latitudeInDegrees;
    double       _stepInDegrees;

  public:

    ~MoveMarkTaskGTask() {}

    MoveMarkTaskGTask(Mark* mark,
                      const Geodetic2D& southernmost,
                      const double northernmostLatitudeInDegrees,
                      const double stepInDegrees):
    _mark(mark),
    _longitudeInDegrees(southernmost._longitude._degrees),
    _southernmostLatitudeInDegrees(southernmost._latitude._degrees),
    _northernmostLatitudeInDegrees(northernmostLatitudeInDegrees),
    _latitudeInDegrees(southernmost._latitude._degrees),
    _stepInDegrees(stepInDegrees)
    {
    }

    virtual void run(const G3MContext* context) {
      if ((_latitudeInDegrees < _southernmostLatitudeInDegrees) ||
          (_latitudeInDegrees > _northernmostLatitudeInDegrees)) {
        _stepInDegrees = -_stepInDegrees;
      }

      _latitudeInDegrees += _stepInDegrees;

      _mark->setPosition(Geodetic3D::fromDegrees(_latitudeInDegrees, _longitudeInDegrees, 0));
    }

  };

public:
  G3MMarksDemoScene_MoveMarkTask(Mark* mark,
                                 const Geodetic2D& southernmost,
                                 const double northernmostLatitudeInDegrees,
                                 const double stepInDegrees,
                                 const TimeInterval& frameTime):
  PeriodicalTask(frameTime, new MoveMarkTaskGTask(mark, southernmost, northernmostLatitudeInDegrees, stepInDegrees))
  {
  }
};


void G3MMarksDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model = getModel();

  Layer* layer = model->createRasterLayer();
  model->getLayerSet()->addLayer(layer);
}

void G3MMarksDemoScene::rawSelectOption(const std::string& option,
                                        int optionIndex) {
  removeFeature();

  if (option == "Basic") {
    showBasicMark();
  }
  else if (option == "Animated") {
    showAnimatedMarks();
  }
  else if (option == "Moving") {
    showMovingMark();
  }
}

/** from wherever the camera is, as it is, to a pose that looks at the marks with the horizon in view */
void G3MMarksDemoScene::animateCameraTo(const Geodetic3D& position,
                                        const Angle& heading,
                                        const Angle& pitch) {
  getModel()->getG3MWidget()->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                                        position,
                                                        heading,
                                                        pitch);
}

// the periodical tasks animate the marks: they go with them
void G3MMarksDemoScene::removeFeature() {
  G3MDemoModel* model = getModel();
  model->getG3MWidget()->removeAllPeriodicalTasks();
  model->getMarksRenderer()->removeAllMarks();
}

void G3MMarksDemoScene::showBasicMark() {
  G3MDemoModel* model = getModel();

  Mark* mark = new Mark(URL("file:///mark-icon-1.png"),                                            // iconURL
                        Geodetic3D::fromDegrees(21.580896830714426216, -71.930032768589384773, 0), // position
                        ABSOLUTE,                                                                  // altitudeMode
                        4500000,                                                                   // minDistanceToCamera=4.5e+06
                        NULL,                                                                      // userData=NULL
                        true,                                                                      // autoDeleteUserData=true
                        new G3MMarksDemoScene_MarkTouchListener(),                                 // MarkTouchListener* listener=NULL
                        true                                                                       // autoDeleteListener=false
                        );

//  mark->setMarkAnchor(1, 0.5);
  mark->setMarkAnchor(0.5, 1);
//  mark->setMarkAnchor(1, 1);

//  mark->setScreenSizeScale(2, 0.5);
  mark->setScreenSizeScale(0.5, 1.5);

  model->getMarksRenderer()->addMark(mark);

  animateCameraTo(Geodetic3D::fromDegrees(10.58, -73.17, 770000),
                  Angle::fromDegrees(-3),
                  Angle::fromDegrees(-36));
}

void G3MMarksDemoScene::showAnimatedMarks() {
  G3MDemoModel*  model         = getModel();
  G3MWidget*     g3mWidget     = model->getG3MWidget();
  MarksRenderer* marksRenderer = model->getMarksRenderer();

  {
    Mark* animMark = new Mark(URL(URL::FILE_PROTOCOL + "radar-sprite.png"),
                              Geodetic3D::fromDegrees( 26.099999998178312, -15.41699999885168, 0),
                              ABSOLUTE,
                              4.5e+06,
                              NULL,
                              true,
                              NULL,
                              false);
    animMark->setScreenSizeScale(0.05, 0.1);
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

    animMark2->setScreenSize(100,100);
    animMark2->setMarkAnchor(0.5, 1.0);
    marksRenderer->addMark(animMark2);
    g3mWidget->addPeriodicalTask(new TextureAtlasMarkAnimationTask(animMark2, 4, 2, 7, TimeInterval::fromMilliseconds(100)));
  }

  Geodetic3D canarias[] = {
    Geodetic3D::fromDegrees(28.131817, -15.440219, 0),
    Geodetic3D::fromDegrees(28.947345, -13.523105, 0),
    Geodetic3D::fromDegrees(28.473802, -13.859360, 0),
    Geodetic3D::fromDegrees(28.467706, -16.251426, 0),
    Geodetic3D::fromDegrees(28.701819, -17.762003, 0),
    Geodetic3D::fromDegrees(28.086595, -17.105796, 0),
    Geodetic3D::fromDegrees(27.810709, -17.917639, 0)
  };

  for (int i = 0; i < 7; i++) {
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
    g3mWidget->addPeriodicalTask(new G3MMarksDemoScene_RescaleMarkTask(pinMark, TimeInterval::fromMilliseconds(100)));
  }

  {
    Mark* regMark = new Mark("HELLO ANIMATED MARKS!",
                             Geodetic3D::fromDegrees( 27.599999998178312, -15.41699999885168, 0),
                             ABSOLUTE);
    marksRenderer->addMark(regMark);
  }

  animateCameraTo(Geodetic3D::fromDegrees(16.978838148049202772, -16.774575794632177406, 770825.79245571023785),
                  Angle::fromDegrees(-3.011899),
                  Angle::fromDegrees(-36.396848));
}

void G3MMarksDemoScene::showMovingMark() {
  G3MDemoModel* model = getModel();

  const Geodetic2D southernmost = Geodetic2D::fromDegrees(40, -3.7);

  Mark* mark = new Mark(URL(URL::FILE_PROTOCOL + "pin.png"),
                        Geodetic3D(southernmost, 0),
                        ABSOLUTE,
                        0,     // minDistanceToCamera: visible at any distance, only the horizon hides it
                        NULL,
                        true,
                        new G3MMarksDemoScene_MarkTouchListener(),
                        true);
  mark->setMarkAnchor(0.5, 1.0);
  model->getMarksRenderer()->addMark(mark);

  // from Madrid to beyond the camera's horizon and back
  model->getG3MWidget()->addPeriodicalTask(new G3MMarksDemoScene_MoveMarkTask(mark,
                                                                              southernmost,
                                                                              85,
                                                                              0.2,
                                                                              TimeInterval::fromMilliseconds(50)));

  animateCameraTo(Geodetic3D::fromDegrees(23.492082217034354841, -7.2163600884566534432, 3060044.582639911212),
                  Angle::fromDegrees(-3.743394),
                  Angle::fromDegrees(-58.954799));
}

void G3MMarksDemoScene::deactivate(const G3MContext* context) {
  removeFeature();

  G3MDemoScene::deactivate(context);
}
