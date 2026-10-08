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
#include <G3M/MarkBuilder.hpp>
#include <G3M/FixedMarkAnchor.hpp>
#include <G3M/DownloaderImageFactory.hpp>
#include <G3M/LabelImageFactory.hpp>
#include <G3M/LabelStyle.hpp>
#include <G3M/GFont.hpp>
#include <G3M/FittedLabelImageFactory.hpp>
#include <G3M/RowLayoutImageFactory.hpp>
#include <G3M/BoxImageBackground.hpp>
#include <G3M/ResizerImageFactory.hpp>
#include <G3M/AbsoluteImageSizer.hpp>
#include <G3M/CircleImageFactory.hpp>
#include <G3M/MarkAnchor.hpp>
#include <G3M/IImage.hpp>
#include <G3M/IFactory.hpp>
#include <G3M/IDeviceInfo.hpp>
#include <G3M/IDownloader.hpp>
#include <G3M/IBufferDownloadListener.hpp>
#include <G3M/IByteBuffer.hpp>
#include <G3M/IJSONParser.hpp>
#include <G3M/JSONBaseObject.hpp>
#include <G3M/JSONArray.hpp>
#include <G3M/JSONObject.hpp>
#include <G3M/IMathUtils.hpp>
#include <G3M/MarkFilter.hpp>
#include <G3M/StackLayoutImageFactory.hpp>
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


/** the anchor on the centre of an icon drawn at the left of the image; the image width is known only once it exists */
class G3MMarksDemoScene_IconCenterMarkAnchor : public MarkAnchor {
private:
  const float _iconCenterX; // pixels from the image's left edge

public:
  G3MMarksDemoScene_IconCenterMarkAnchor(const float iconCenterX) :
  _iconCenterX(iconCenterX)
  {
  }

  Vector2F getAnchor(const IImage* image) const {
    return Vector2F(_iconCenterX / image->getWidth(), 0.5f);
  }
};


class G3MMarksDemoScene_MagnitudeUserData : public MarkUserData {
public:
  const double _magnitude;

  G3MMarksDemoScene_MagnitudeUserData(const double magnitude) :
  _magnitude(magnitude)
  {
  }
};


class G3MMarksDemoScene_AllMarksFilter : public MarkFilter {
public:
  bool test(const Mark* mark) const {
    return true;
  }
};


class G3MMarksDemoScene_LondonDownloadListener : public IBufferDownloadListener {
private:
  G3MMarksDemoScene* _scene;
  const int          _featureGeneration;

public:
  G3MMarksDemoScene_LondonDownloadListener(G3MMarksDemoScene* scene) :
  _scene(scene),
  _featureGeneration(scene->getFeatureGeneration())
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    if (_scene->getFeatureGeneration() == _featureGeneration) {
      const JSONBaseObject* json = IJSONParser::instance()->parse(buffer);
      if (json != NULL) {
        _scene->addLondonMarks(json->asArray());
        delete json;
      }
    }
    delete buffer;
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Can't load %s", url._path.c_str());
  }

  void onCancel(const URL& url) {
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
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

  if (option == "Basic - icon, anchor, scale") {
    showBasicMark();
  }
  else if (option == "Animated - sprites, resizing") {
    showAnimatedMarks();
  }
  else if (option == "Moving - across the horizon") {
    showMovingMark();
  }
  else if (option == "Labels - alignment, icon anchor") {
    showLabels();
  }
  else if (option == "London - declutter") {
    showLondon();
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

void G3MMarksDemoScene::rawSelectGroupOption(size_t groupIndex,
                                             const std::string& option,
                                             int optionIndex) {
  if (groupIndex == 0) {
    rawSelectOption(option, optionIndex);
  }
  else if (groupIndex == 3) {
    getModel()->getMarksRenderer()->setHorizonBand(option == "Band");
  }
  else if (getOptionGroup(0)->isSelectedOption("London - declutter")) {
    if (groupIndex == 1) {
      moveLondonCamera(option);
    }
    else {
      applyLondonDeclutter(option);
    }
  }
}

/** By magnitude: the marks carry it as their priority. By drawing order: the same marks without priority */
void G3MMarksDemoScene::applyLondonDeclutter(const std::string& declutterOption) {
  MarksRenderer* marksRenderer = getModel()->getMarksRenderer();
  marksRenderer->setDeclutter((declutterOption == "By magnitude") || (declutterOption == "By drawing order"));
  _londonPrioritized = (declutterOption != "By drawing order");

  const std::vector<Mark*> marks = marksRenderer->getAllMarks(G3MMarksDemoScene_AllMarksFilter());
  for (size_t i = 0; i < marks.size(); i++) {
    Mark* mark = marks[i];
    const G3MMarksDemoScene_MagnitudeUserData* magnitude = (const G3MMarksDemoScene_MagnitudeUserData*) mark->getUserData();
    if (magnitude != NULL) {
      mark->setPriority(_londonPrioritized ? magnitude->_magnitude : NAND);
    }
  }
}

bool G3MMarksDemoScene::isOptionGroupVisible(size_t groupIndex) const {
  // the horizon applies to every feature; the camera and declutter groups only to London
  return (groupIndex == 0) || (groupIndex == 3) || getOptionGroup(0)->isSelectedOption("London - declutter");
}

// the periodical tasks animate the marks: they go with them
void G3MMarksDemoScene::removeFeature() {
  _featureGeneration++;

  G3MDemoModel* model = getModel();
  model->getMarksRenderer()->setDeclutter(false);
  model->getMarksRenderer()->setHint(NULL);
  model->getG3MWidget()->removeAllPeriodicalTasks();
  model->getMarksRenderer()->removeAllMarks();
}

void G3MMarksDemoScene::showBasicMark() {
  G3MDemoModel* model = getModel();

  MarkBuilder builder;
  builder.setMinDistanceToCamera(4.5e+06);
  builder.setPosition(Geodetic3D::fromDegrees(21.580896830714426216, -71.930032768589384773, 0));
  builder.addOutfit(new DownloaderImageFactory(URL("file:///mark-icon-1.png")),
                    new FixedMarkAnchor(0.5, 1)); // the bottom centre sits on the position
  builder.setTouchListener(new G3MMarksDemoScene_MarkTouchListener(), true);
  Mark* mark = builder.build();

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

  MarkBuilder builder;
  builder.setMinDistanceToCamera(4.5e+06);

  {
    builder.setPosition(Geodetic3D::fromDegrees( 26.099999998178312, -15.41699999885168, 0));
    builder.addOutfit(new DownloaderImageFactory(URL(URL::FILE_PROTOCOL + "radar-sprite.png")));
    Mark* animMark = builder.build();
    animMark->setScreenSizeScale(0.05, 0.1);
    g3mWidget->addPeriodicalTask(new TextureAtlasMarkAnimationTask(animMark, 4, 2, 7, TimeInterval::fromMilliseconds(100)));
    marksRenderer->addMark(animMark);
  }

  {
    builder.setPosition(Geodetic3D::fromDegrees( 25.428140, -17.016841, 0));
    builder.addOutfit(new DownloaderImageFactory(URL(URL::FILE_PROTOCOL + "radar-sprite.png")),
                      new FixedMarkAnchor(0.5, 1));
    Mark* animMark2 = builder.build();

    animMark2->setScreenSize(100,100);
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
    builder.setPosition(canarias[i]);
    builder.addOutfit(new DownloaderImageFactory(URL(URL::FILE_PROTOCOL + "pin.png")),
                      new FixedMarkAnchor(0.5, 1));
    Mark* pinMark = builder.build();

    marksRenderer->addMark(pinMark);
    g3mWidget->addPeriodicalTask(new G3MMarksDemoScene_RescaleMarkTask(pinMark, TimeInterval::fromMilliseconds(100)));
  }

  {
    builder.setPosition(Geodetic3D::fromDegrees( 27.599999998178312, -15.41699999885168, 0));
    builder.addOutfit(new LabelImageFactory("HELLO ANIMATED MARKS!",
                                            LabelStyle::shadowed(GFont::sansSerif(20),
                                                                 Color::WHITE,
                                                                 Color::BLACK,
                                                                 1,
                                                                 Vector2F(2, 2))));
    marksRenderer->addMark(builder.build());
  }

  animateCameraTo(Geodetic3D::fromDegrees(16.978838148049202772, -16.774575794632177406, 770825.79245571023785),
                  Angle::fromDegrees(-3.011899),
                  Angle::fromDegrees(-36.396848));
}

void G3MMarksDemoScene::showMovingMark() {
  G3MDemoModel* model = getModel();

  const Geodetic2D southernmost = Geodetic2D::fromDegrees(40, -3.7);

  // no distance limit: only the horizon hides it
  MarkBuilder builder;
  builder.setPosition(Geodetic3D(southernmost, 0));
  builder.addOutfit(new DownloaderImageFactory(URL(URL::FILE_PROTOCOL + "pin.png")),
                    new FixedMarkAnchor(0.5, 1));
  builder.setTouchListener(new G3MMarksDemoScene_MarkTouchListener(), true);
  Mark* mark = builder.build();
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

void G3MMarksDemoScene::showLabels() {
  MarksRenderer* marksRenderer = getModel()->getMarksRenderer();

  const LabelStyle boxedStyle = LabelStyle::boxed(GFont::sansSerif(16),
                                                  Color::WHITE,
                                                  Vector2F(6, 4),               /* padding         */
                                                  Color::fromRGBA(0, 0, 0, 0.6f), /* backgroundColor */
                                                  6                             /* cornerRadius    */);

  MarkBuilder builder;

  // a long name, split in two lines by FittedLabelImageFactory, with each alignment
  const std::string longName = "Bibliothèque nationale de France";
  const HorizontalAlignment alignments[] = { Left, Center, Right };
  for (int i = 0; i < 3; i++) {
    builder.setPosition(Geodetic3D::fromDegrees(48.86, 2.20 + (i * 0.15), 0));
    builder.addOutfit(new FittedLabelImageFactory(longName,
                                                  boxedStyle,
                                                  "Washington, D.C.", /* maxWidthText      */
                                                  1,                  /* minFontSizeFactor: keep the size */
                                                  2,                  /* lineSeparation    */
                                                  alignments[i]));
    marksRenderer->addMark(builder.build());
  }

  // an icon and a label in one box, anchored on the icon's centre by a MarkAnchor computed from the image
  {
    const float pixelRatio = IFactory::instance()->getDeviceInfo()->getDevicePixelRatio();
    const int   iconPoints = 24;
    const float padding    = 8; // pixels: the row layout draws on a plain canvas
    const Geodetic3D position = Geodetic3D::fromDegrees(48.80, 2.35, 0);

    IImageFactory* icon = new ResizerImageFactory(new DownloaderImageFactory(URL("file:///mark-icon-1.png")),
                                                  new AbsoluteImageSizer(iconPoints),
                                                  new AbsoluteImageSizer(iconPoints));
    IImageFactory* label = new FittedLabelImageFactory(longName,
                                                       LabelStyle::plain(GFont::sansSerif(16), Color::WHITE),
                                                       "Washington, D.C.",
                                                       1,
                                                       2,
                                                       Left);

    builder.setPosition(position);
    builder.addOutfit(new RowLayoutImageFactory(icon,
                                                label,
                                                new BoxImageBackground(Vector2F::ZERO,     /* margin      */
                                                                       0,                  /* borderWidth */
                                                                       Color::TRANSPARENT, /* borderColor */
                                                                       Vector2F(padding, padding),
                                                                       Color::fromRGBA(0, 0, 0, 0.6f),
                                                                       12),
                                                (int) (6 * pixelRatio)),
                      // the resizer draws on a retina canvas: the icon is iconPoints times the pixel ratio
                      new G3MMarksDemoScene_IconCenterMarkAnchor(padding + ((iconPoints * pixelRatio) / 2)));
    marksRenderer->addMark(builder.build());

    // a red dot on the same position shows where the anchor lands
    builder.setPosition(position);
    builder.addOutfit(new CircleImageFactory(Color::RED, 3));
    marksRenderer->addMark(builder.build());
  }

  animateCameraTo(Geodetic3D::fromDegrees(48.55, 2.35, 45000),
                  Angle::zero(),
                  Angle::fromDegrees(-60));
}

void G3MMarksDemoScene::showLondon() {
  G3MWidget* g3mWidget = getModel()->getG3MWidget();

  // the third outfit of every mark: a dot that says there is more when zooming in
  getModel()->getMarksRenderer()->setHint(new StackLayoutImageFactory(new CircleImageFactory(Color::fromRGBA(0, 0, 0, 0.6f), 4),
                                                                       new CircleImageFactory(Color::WHITE, 2)));

  g3mWidget->getG3MContext()->getDownloader()->requestBuffer(URL("file:///London-Wikipedia.json"),
                                                             100000,
                                                             TimeInterval::zero(),
                                                             false,
                                                             new G3MMarksDemoScene_LondonDownloadListener(this),
                                                             true);

  moveLondonCamera(getOptionGroup(1)->getTitle());
  applyLondonDeclutter(getOptionGroup(2)->getTitle());
}

void G3MMarksDemoScene::moveLondonCamera(const std::string& cameraOption) {
  if (cameraOption == "Westminster") {
    animateCameraTo(Geodetic3D::fromDegrees(51.475, -0.125, 2500),
                    Angle::zero(),
                    Angle::fromDegrees(-40));
  }
  else if (cameraOption == "Bloomsbury and the City") {
    animateCameraTo(Geodetic3D::fromDegrees(51.495, -0.110, 3000),
                    Angle::zero(),
                    Angle::fromDegrees(-40));
  }
  else {
    // from where only London itself fits down to the street level, to watch the marks unfold while zooming in
    getModel()->getG3MWidget()->setAnimatedCameraPosition(TimeInterval::fromSeconds(20),
                                                          Geodetic3D::fromDegrees(51.50, -0.12, 300000),
                                                          Geodetic3D::fromDegrees(51.50, -0.125, 2500),
                                                          Angle::zero(),            Angle::zero(),            /* heading */
                                                          Angle::fromDegrees(-90),  Angle::fromDegrees(-90)  /* pitch: looking straight down */);
  }
}

/**
 * The Wikipedia articles of central London as pythagoras draws them: one box
 * with the icon on the left of the label, the font size from the magnitude.
 * Each mark also carries its icon-only outfit, for when space is short.
 */
void G3MMarksDemoScene::addLondonMarks(const JSONArray* articles) {
  if (articles == NULL) {
    return;
  }

  const IMathUtils* mu = IMathUtils::instance();

  double minMagnitude = mu->maxDouble();
  double maxMagnitude = mu->minDouble();
  for (size_t i = 0; i < articles->size(); i++) {
    const double magnitude = articles->getAsObject(i)->getAsNumber("magnitude", 0);
    minMagnitude = mu->min(minMagnitude, magnitude);
    maxMagnitude = mu->max(maxMagnitude, magnitude);
  }

  const float minFontSize = 10;
  const float maxFontSize = 16;

  const float    pixelRatio      = IFactory::instance()->getDeviceInfo()->getDevicePixelRatio();
  const Vector2F padding         = Vector2F(6, 4).times(pixelRatio); // pixels: the row layout draws on a plain canvas
  const int      separation      = mu->round(5 * pixelRatio);
  const Color    backgroundColor = Color::fromRGBA(0, 0, 0, 0.6f);
  const float    cornerRadius    = 6 * pixelRatio;

  MarksRenderer* marksRenderer = getModel()->getMarksRenderer();
  MarkBuilder builder;

  for (size_t i = 0; i < articles->size(); i++) {
    const JSONObject* article = articles->getAsObject(i);

    const double magnitude = article->getAsNumber("magnitude", minMagnitude);
    const double alpha     = (maxMagnitude > minMagnitude) ? ((magnitude - minMagnitude) / (maxMagnitude - minMagnitude)) : 1;
    const float  fontSize  = mu->round((float) (minFontSize + ((maxFontSize - minFontSize) * alpha)));

    const std::string title = article->getAsString("title", "");
    const std::string icon  = article->getAsString("icon", "");

    builder.setPosition(Geodetic3D::fromDegrees(article->getAsNumber("lat", 0),
                                                article->getAsNumber("lon", 0),
                                                0));

    if (icon.empty()) {
      builder.addOutfit(new FittedLabelImageFactory(title,
                                                    LabelStyle::boxed(GFont::sansSerif(fontSize),
                                                                      Color::WHITE,
                                                                      padding.div(pixelRatio),
                                                                      backgroundColor,
                                                                      cornerRadius / pixelRatio),
                                                    "Washington, D.C.",
                                                    0.7f,
                                                    2));
    }
    else {
      const URL iconURL("file:///wp-" + icon + ".png");
      // about one and a half W of its label, as in pythagoras (a sans-serif W is ~0.94 of the font size)
      const int iconPoints = mu->round(fontSize * 1.4f);

      builder.addOutfit(new RowLayoutImageFactory(new ResizerImageFactory(new DownloaderImageFactory(iconURL),
                                                                          new AbsoluteImageSizer(iconPoints),
                                                                          new AbsoluteImageSizer(iconPoints)),
                                                  new FittedLabelImageFactory(title,
                                                                              LabelStyle::plain(GFont::sansSerif(fontSize), Color::WHITE),
                                                                              "Washington, D.C.",
                                                                              0.7f,
                                                                              2,
                                                                              Left),
                                                  new BoxImageBackground(Vector2F::ZERO, 0, Color::TRANSPARENT,
                                                                         padding, backgroundColor, cornerRadius),
                                                  separation),
                        // the resizer draws on a retina canvas: the icon is iconPoints times the pixel ratio
                        new G3MMarksDemoScene_IconCenterMarkAnchor(padding._x + ((iconPoints * pixelRatio) / 2)));

      builder.addOutfit(new RowLayoutImageFactory(new ResizerImageFactory(new DownloaderImageFactory(iconURL),
                                                                          new AbsoluteImageSizer(iconPoints),
                                                                          new AbsoluteImageSizer(iconPoints)),
                                                  new BoxImageBackground(Vector2F::ZERO, 0, Color::TRANSPARENT,
                                                                         padding, backgroundColor, cornerRadius),
                                                  separation));
    }

    builder.setUserData(new G3MMarksDemoScene_MagnitudeUserData(magnitude), true);
    if (_londonPrioritized) {
      builder.setPriority(magnitude);
    }
    marksRenderer->addMark(builder.build());
  }
}

void G3MMarksDemoScene::deactivate(const G3MContext* context) {
  removeFeature();
  getModel()->getMarksRenderer()->setHorizonBand(true); // g3m's default, for the other scenes

  G3MDemoScene::deactivate(context);
}
