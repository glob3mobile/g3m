//
//  G3MCityGMLDemoScene.cpp
//  G3MApp
//
//  Created by Jose Miguel SN on 29/3/16.
//  Copyright © 2016 Igo Software SL. All rights reserved.
//

#include "G3MCityGMLDemoScene.hpp"

#include <G3MiOSSDK/IFactory.hpp>
#include <G3MiOSSDK/IXMLNode.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/G3MContext.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/TimeInterval.hpp>
#include <G3MiOSSDK/IStringUtils.hpp>
#include <G3MiOSSDK/CityGMLParser.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>

#include <G3MiOSSDK/EllipsoidalPlanet.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>

#include <G3MiOSSDK/TerrainTouchListener.hpp>
#include <G3MiOSSDK/ColorLegend.hpp>
#include <G3MiOSSDK/BuildingDataParser.hpp>

#include <G3MiOSSDK/CameraSingleDragHandler.hpp>
#include <G3MiOSSDK/CameraDoubleDragHandler.hpp>
#include <G3MiOSSDK/CameraRotationHandler.hpp>
#include <G3MiOSSDK/CameraDoubleTapHandler.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>

#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/OSMLayer.hpp>
#include <G3MiOSSDK/AbstractMesh.hpp>
#include <G3MiOSSDK/CityGMLBuildingTessellator.hpp>
#include <G3MiOSSDK/URL.hpp>

#include <G3MiOSSDK/HUDQuadWidget.hpp>
#include <G3MiOSSDK/HUDRenderer.hpp>
#include <G3MiOSSDK/HUDRelativePosition.hpp>
#include <G3MiOSSDK/HUDRelativeSize.hpp>
#include <G3MiOSSDK/LabelImageBuilder.hpp>
#include <G3MiOSSDK/DownloaderImageBuilder.hpp>
#include <G3MiOSSDK/HUDAbsolutePosition.hpp>
#include <G3MiOSSDK/GTask.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>
#include <G3MiOSSDK/FloatBuffer_iOS.hpp>


class MyTerrainTL: public TerrainTouchListener {
  
  G3MWidget* _widget;
  
  bool _usingVR;
  
  class AltitudeFixerLM: public ILocationModifier{
    Geodetic3D modify(const Geodetic3D& location){
      return Geodetic3D::fromDegrees(location._latitude._degrees, location._longitude._degrees, 3);
    }
  };
  
  bool _fixAltitude;
  
public:
  
  
  MyTerrainTL(G3MWidget* widget, bool fixAltitude):
  _widget(widget),
  _usingVR(false),
  _fixAltitude(fixAltitude){
    
  }
  
  bool onTerrainTouch(const G3MEventContext* ec,
                      const Vector2F&        pixel,
                      const Camera*          camera,
                      const Geodetic3D&      position,
                      const Tile*            tile){
    
    CameraRenderer* cameraRenderer = _widget->getCameraRenderer();
    cameraRenderer->clearHandlers();
    
    if (_usingVR){
      const bool useInertia = true;
      cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
      cameraRenderer->addHandler(new CameraDoubleDragHandler());
      cameraRenderer->addHandler(new CameraRotationHandler());
      cameraRenderer->addHandler(new CameraDoubleTapHandler());
      
      
      _widget->getNextCamera()->forceZNear(NAND);
    } else{
      
      ILocationModifier * lm = NULL;
      if (_fixAltitude){
        lm = new AltitudeFixerLM();
      }
      
      DeviceAttitudeCameraHandler* dac = new DeviceAttitudeCameraHandler(true, lm);
      cameraRenderer->addHandler(dac);
      
      _widget->getNextCamera()->forceZNear(1.0);
    }
    
    _usingVR = !_usingVR;
    
    return true;
  }
  
};

class ColouringCityGMLDemoSceneBDL : public IBufferDownloadListener {
private:
  G3MCityGMLDemoScene* _demo;
  std::vector<CityGMLBuilding*> _buildings;
public:
  ColouringCityGMLDemoSceneBDL(G3MCityGMLDemoScene* demo, std::vector<CityGMLBuilding*> buildings) :
  _demo(demo),
  _buildings(buildings)
  {
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    delete buffer;
    BuildingDataParser::includeDataInBuildingSet(s, _buildings);
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
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

class TimeEvolutionTask: public GTask{
  
  AbstractMesh* _abstractMesh;
  
  float _delta;
  int _step;
  
  float* _initialColors;
  LabelImageBuilder* _labelBuilder;
public:
  
  TimeEvolutionTask(AbstractMesh* abstractMesh, LabelImageBuilder* labelBuilder):
  _abstractMesh(abstractMesh),
  _delta(0.0),
  _step(0),
  _labelBuilder(labelBuilder)
  {
    
    IFloatBuffer* colors = _abstractMesh->getColorsFloatBuffer();
    _initialColors = new float[colors->size()];
    
    for (int i = 0; i < colors->size(); i++) {
      _initialColors[i] = colors->get(i);
    }
    
    
  }
  
  void run(const G3MContext* context){
    
    IFloatBuffer* colors = _abstractMesh->getColorsFloatBuffer();
    const IMathUtils* mu = IMathUtils::instance();
    
    double factor = (1.0f + mu->sin(_delta)) / 2.0;
    _delta += 0.1;
    
    FloatBuffer_iOS fb(colors->size());
    float *newColors = fb.getPointer();
    
    for (int i = 0; i < colors->size(); i+=4) {
      float r = _initialColors[i];
      float g = _initialColors[i+1];
      float b = _initialColors[i+2];
      float a = _initialColors[i+3];
      
      r *= factor;
      r = mu->clamp(r, 0.0f, 1.0f);
      
      g *= factor;
      g = mu->clamp(g, 0.0f, 1.0f);
      
      b *= 1.0f / factor;
      b = mu->clamp(b, 0.0f, 1.0f);
      
//      colors->put(i, r);
//      colors->put(i+1, g);
//      colors->put(i+2, b);
//      colors->put(i+3, a);
      
      
      newColors[i] = r;
      newColors[i+1] = g;
      newColors[i+2] = b;
      newColors[i+3] = a;
    }
    
    colors->put(0, &fb);
    
    
    //Label
    _step++;
    int min = _step % 60;
    int hour = (_step / 60) % 24;
    std::string s = context->getStringUtils()->toString(hour) + ":" + context->getStringUtils()->toString(min);
    _labelBuilder->setText(s);
  }
  
};

class ColorChangingMeshTask: public GTask{
  
  AbstractMesh* _abstractMesh;
  int _step;
  std::vector<IFloatBuffer*> _colors;
public:
  
  ColorChangingMeshTask(AbstractMesh* abstractMesh, std::vector<IFloatBuffer*> colors):
  _abstractMesh(abstractMesh),
  _step(0),
  _colors(colors)
  {
    IFloatBuffer* meshColors = _abstractMesh->getColorsFloatBuffer();
    
    for (size_t i = 0; i < _colors.size(); i++) {
      if (colors[i]->size() != meshColors->size()){
        THROW_EXCEPTION("WRONG NUMBER OF COLORS");
      }
    }
  }
  
  void run(const G3MContext* context){
    IFloatBuffer* colors = _abstractMesh->getColorsFloatBuffer();
    IFloatBuffer* newColors = _colors[_step];
    
    colors->put(0, newColors);
    
    _step++; //Advance
  }
  
};


class PointCloudBDL : public IBufferDownloadListener {
private:
  G3MCityGMLDemoScene* _demo;
  ElevationData* _ed;
public:
  PointCloudBDL(G3MCityGMLDemoScene* demo, ElevationData* ed) :
  _demo(demo),
  _ed(ed)
  {
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    delete buffer;
    _demo->createPointCloud(_ed, s);
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
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


void G3MCityGMLDemoScene::colorBuildings(CityGMLBuildingColorProvider* cp){
  
  for (size_t i = 0; i < _buildings.size(); i++) {
    CityGMLBuilding* b = _buildings.at(i);
    Color c = cp->getColor(b);
    CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(b, c);
  }
  
}

class MyCityGMLListener: public CityGMLListener{
  
private:
  G3MCityGMLDemoScene* _demo;
  ElevationData* _ed;
public:
  
  MyCityGMLListener(G3MCityGMLDemoScene* demo, ElevationData* ed):_demo(demo), _ed(ed){
    
  }
  
  virtual void onBuildingsCreated(const std::vector<CityGMLBuilding*>& buildings){
    _demo->addBuildings(buildings, _ed);
  }
  
  virtual void onError(){
    
  }
};

class MyEDListener: public IElevationDataListener{
  
  
private:
  G3MCityGMLDemoScene* _demo;
  
public:
  
  MyEDListener(G3MCityGMLDemoScene* demo):_demo(demo){}
  
  virtual void onData(const Sector& sector,
                      const Vector2I& extent,
                      ElevationData* elevationData){
    _demo->requestPointCloud(elevationData);
    _demo->loadCityModel(elevationData);
  }
  
  virtual void onError(const Sector& sector,
                       const Vector2I& extent){
    
  }
  
  virtual void onCancel(const Sector& sector,
                        const Vector2I& extent){
    
  }
};

void G3MCityGMLDemoScene::addBuildings(const std::vector<CityGMLBuilding*>& buildings, const ElevationData* ed){
  
  _modelsLoadedCounter++;
  
  for (int i = 0; i < buildings.size(); i++) {
    _buildings.push_back(buildings[i]);
  }
  
  getModel()->getG3MWidget()->getG3MContext()
  ->getDownloader()->requestBuffer(URL("file:///karlsruhe_data.geojson"), 1000, TimeInterval::forever(), true,
                                   new ColouringCityGMLDemoSceneBDL(this, buildings),
                                   true);
  
  bool createCityMeshAndMarks = true;
  if (createCityMeshAndMarks){
    
    MarksRenderer* marksR = getModel()->getMarksRenderer();
    MeshRenderer* meshR = getModel()->getMeshRenderer();
    
    
    //Adding marks
    for (size_t i = 0; i < buildings.size(); i++) {
      marksR->addMark( CityGMLBuildingTessellator::createMark(buildings[i], false) );
    }
    
    //Checking walls visibility
    int n = CityGMLBuilding::checkWallsVisibility(buildings);
    ILogger::instance()->logInfo("Removed %d invisible walls from the model.", n);
    
    //Creating mesh model
    Mesh* mesh = CityGMLBuildingTessellator::createMesh(buildings,
                                                        *getModel()->getG3MWidget()->getG3MContext()->getPlanet(),
                                                        false, false, NULL,
                                                        ed);
    meshR->addMesh(mesh);
  }
  
  if (_modelsLoadedCounter == _cityGMLFiles.size()){
    ILogger::instance()->logInfo("City Model Loaded");
    
    //Whole city!
    getModel()->getG3MWidget()->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                                          Geodetic3D::fromDegrees(49.07139214735035182, 8.134019638291379195, 22423.46165080198989),
                                                          Angle::fromDegrees(-109.452892),
                                                          Angle::fromDegrees(-44.938813)
                                                          );
  }
}

void G3MCityGMLDemoScene::requestPointCloud(ElevationData* ed){
  getModel()->getG3MWidget()->getG3MContext()
  ->getDownloader()->requestBuffer(URL("file:///random_cluster.geojson"), 1000, TimeInterval::forever(), true,
                                   new PointCloudBDL(this, ed),
                                   true);
}

void G3MCityGMLDemoScene::createPointCloud(ElevationData* ed, const std::string& pointCloudDescriptor){
  
  
  Mesh* m = BuildingDataParser::createPointCloudMesh(pointCloudDescriptor, getModel()->getG3MWidget()->getG3MContext()->getPlanet(), ed);
  getModel()->getMeshRenderer()->addMesh(m);
  getModel()->getG3MWidget()->addPeriodicalTask(new PeriodicalTask(TimeInterval::fromSeconds(0.1),
                                                                   new TimeEvolutionTask((AbstractMesh*)m, _labelBuilder)));
}


void G3MCityGMLDemoScene::loadCityModel(ElevationData* ed){
  
  
  const G3MContext* context = getModel()->getG3MWidget()->getG3MContext();
  IDownloader* downloader = context->getDownloader();
  
  for (size_t i = 0; i < _cityGMLFiles.size(); i++) {
    CityGMLParser::parseFromURL(URL(_cityGMLFiles[i]),
                                downloader,
                                new MyCityGMLListener(this, ed),
                                true);
  }
}


void G3MCityGMLDemoScene::rawActivate(const G3MContext* context) {
  
  G3MDemoModel* model     = getModel();
  
  //  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
  //                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
  //                                           TimeInterval::fromDays(30));
  
  OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
  
  getModel()->getLayerSet()->addLayer(layer);
  
  getModel()->getPlanetRenderer()->addTerrainTouchListener(new MyTerrainTL(getModel()->getG3MWidget(), !_useDEM));
  
  if (_useDEM){
    Sector karlsruheSector = Sector::fromDegrees(48.9397891179, 8.27643508429, 49.0930546874, 8.5431344933);
    SingleBilElevationDataProvider* edp = new SingleBilElevationDataProvider(URL("file:///ka_31467.bil"),
                                                                             karlsruheSector,
                                                                             Vector2I(308, 177));
    getModel()->getPlanetRenderer()->setElevationDataProvider(edp, true);
    edp->requestElevationData(karlsruheSector, Vector2I(308, 177), new MyEDListener(this), true);
  } else{
    
    requestPointCloud(NULL);
    loadCityModel(NULL);
  }
  
  HUDRenderer* hudRenderer = model->getHUDRenderer();
  
  _labelBuilder = new LabelImageBuilder("00:00",               // text
                                        GFont::monospaced(38), // font
                                        6,                     // margin
                                        Color::yellow(),       // color
                                        Color::black(),        // shadowColor
                                        3,                     // shadowBlur
                                        1,                     // shadowOffsetX
                                        -1,                    // shadowOffsetY
                                        Color::fromRGBA255(102, 255, 51, 255),          // backgroundColor
                                        10,                     // cornerRadius
                                        true                   // mutable
                                        );
  
  HUDQuadWidget* label = new HUDQuadWidget(_labelBuilder,
                                           new HUDAbsolutePosition(10),
                                           new HUDAbsolutePosition(60),
                                           new HUDRelativeSize(1, HUDRelativeSize::BITMAP_WIDTH),
                                           new HUDRelativeSize(1, HUDRelativeSize::BITMAP_HEIGHT) );
  
  HUDQuadWidget* logo = new HUDQuadWidget(new DownloaderImageBuilder(URL("file:///eifer_logo.png")),
                                          new HUDAbsolutePosition(0),
                                          new HUDRelativePosition(0.82,
                                                                  HUDRelativePosition::VIEWPORT_HEIGHT,
                                                                  HUDRelativePosition::MIDDLE),
                                          new HUDRelativeSize(0.5,
                                                              HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                          new HUDRelativeSize(0.25,
                                                              HUDRelativeSize::VIEWPORT_MIN_AXIS));
  
  hudRenderer->addWidget(label);
  hudRenderer->addWidget(logo);
}

void G3MCityGMLDemoScene::deactivate(const G3MContext* context) {
  
  G3MDemoScene::deactivate(context);
}

void G3MCityGMLDemoScene::rawSelectOption(const std::string& option,
                                          int optionIndex) {
  
  if (option == "Random Colors"){
    RandomBuildingColorPicker* rcp = new RandomBuildingColorPicker();
    colorBuildings(rcp);
    delete rcp;
  }
  
  
  if (option == "Heat Demand"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::blue(), 6336.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::red(), 70000.0));
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("Heat_Dem_1", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
  if (option == "Volume"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(213,62,79, 255), 2196.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89, 255), 6816.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(254,224,139, 255), 17388.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(230,245,152, 255), 33165.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(153,213,148, 255), 62472.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(50,136,189, 255), 122553.0));
    ColorLegend* cl = new ColorLegend(legend);
    
    BuildingDataColorProvider* colorProvider = new BuildingDataColorProvider("Bui_Volu_1", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
  }
  
  if (option == "QCL"){
    
    int nClasses = 18;
    int colors[] = {103,0,31,
      178,24,43,
      214,96,77,
      244,165,130,
      253,219,199,
      247,247,247,
      209,229,240,
      146,197,222,
      67,147,195,
      33,102,172,
      5,48,97,
      171,221,164,
      102,194,165,
      50,136,189,
      94,79,162,
      227,26,28,
      253,191,111,
      255,127,0};
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    for (int i = 0; i < nClasses;i++) {
      legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(colors[i*3],
                                                                         colors[i*3+1],
                                                                         colors[i*3+2],
                                                                         255),
                                                      (double)(i+1)));
    }
    
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("QCL_1", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
  if (option == "SOM Cluster"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    
    int nClasses = 15;
    int colors[] = {103,0,31,
      178,24,43,
      214,96,77,
      244,165,130,
      253,219,199,
      247,247,247,
      209,229,240,
      146,197,222,
      67,147,195,
      33,102,172,
      5,48,97,
      166,219,160,
      90,174,97,
      27,120,55,
      0,68,27};
    
    for (int i = 0; i < nClasses;i++) {
      legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(colors[i*3],
                                                                         colors[i*3+1],
                                                                         colors[i*3+2],
                                                                         255),
                                                      (double)(i+1)));
    }
    
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("SOMcluster", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
  if (option == "Field 2"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(213,62,79, 255), -0.05));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89, 255), 0.05));
    
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("Field2_12", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
}