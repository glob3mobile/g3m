//
//  CityGMLRenderer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/4/16.
//
//

#ifndef CityGMLRenderer_hpp
#define CityGMLRenderer_hpp

#include "DefaultRenderer.hpp"

#include "CityGMLBuilding.hpp"
#include "MeshRenderer.hpp"
#include "MarksRenderer.hpp"
#include <vector>


#include "CityGMLBuildingTessellator.hpp"
#include "IThreadUtils.hpp"

#include "GEO2DLineRasterStyle.hpp"
#include "GEOLineRasterSymbol.hpp"
#include "ColorLegendHelper.hpp"
#include "GEOVectorLayer.hpp"

class Mesh;

class CityGMLRendererListener{
public:
  virtual ~CityGMLRendererListener(){}
  virtual void onBuildingsLoaded(const std::vector<CityGMLBuilding*>& buildings) = 0;
  
};

class CityGMLBuildingTouchedListener{
public:
  virtual ~CityGMLBuildingTouchedListener(){}
  virtual void onBuildingTouched(CityGMLBuilding* building) = 0;
  
};

class CityGMLRenderer: public DefaultRenderer{
  
  std::vector<CityGMLBuilding*> _buildings;
  
  ElevationData* _elevationData;
  MeshRenderer* _meshRenderer;
  MarksRenderer* _marksRenderer;
  GEOVectorLayer* _vectorLayer;
  
  const Camera* _lastCamera;
  
  CityGMLBuildingTouchedListener* _touchListener;
  
  class TessellationTask: public GAsyncTask {
    CityGMLRenderer* _vc;
    std::vector<CityGMLBuilding*> _buildings;
    std::vector<Mesh*> _buildingMeshes;
    
    CityGMLRendererListener* _listener;
    const bool _autoDelete;
    
    std::vector<Mark*> _marks;
    std::vector<GEORasterSymbol*> _geoSymbol;
    Mesh* _mesh;
    
    const bool _fixOnGround;
    
    
    
    GEORasterSymbol* createBuildingFootprints(const CityGMLBuilding* b){
      
      
      float dashLengths[] = {};
      int dashCount = 0;
      
      GEO2DLineRasterStyle style(Color::red(),
                                 3,
                                 CAP_ROUND,
                                 JOIN_ROUND,
                                 1,
                                 dashLengths,
                                 dashCount,
                                 0);
      
      for (size_t j = 0; j < b->_surfaces.size(); j++) {
        CityGMLBuildingSurface* s  = b->_surfaces[j];
        if (s->getType() == GROUND){
          
          std::vector<Geodetic2D*>* coordinates = new std::vector<Geodetic2D*>();
          for (size_t k = 0; k < s->_geodeticCoordinates.size(); k++) {
            const Geodetic2D g2D = s->_geodeticCoordinates[k]->asGeodetic2D();
            coordinates->push_back(new Geodetic2D(g2D));
          }
          
          return new GEOLineRasterSymbol(new GEO2DCoordinatesData(coordinates), style);
        }
        
      }
      return NULL;
    }
  public:
    
    TessellationTask(CityGMLRenderer* vc,
                     const std::vector<CityGMLBuilding*>& buildings,
                     bool fixOnGround,
                     CityGMLRendererListener* listener, bool autoDelete):
    _vc(vc),
    _buildings(buildings),
    _listener(listener),
    _autoDelete(autoDelete),
    _mesh(NULL),
    _fixOnGround(fixOnGround)
    {}
    
    
    
    virtual void runInBackground(const G3MContext* context){
      //Adding marks
      if (_vc->_marksRenderer != NULL){
        for (size_t i = 0; i < _buildings.size(); i++) {
          _marks.push_back( CityGMLBuildingTessellator::createMark(_buildings[i], false) );
        }
      }
      
      if (_vc->_vectorLayer!= NULL){
        for (size_t i = 0; i < _buildings.size(); i++) {
          GEORasterSymbol* s = createBuildingFootprints(_buildings[i]);
          if (s != NULL){
            _geoSymbol.push_back(s);
          }
        }
      }
      
      //Checking walls visibility
      //      int n = CityGMLBuilding::checkWallsVisibility(_buildings);
      //      ILogger::instance()->logInfo("Removed %d invisible walls from the model.", n);
      
      
      //Creating mesh model
            const bool checkSurfacesVisibility = true;
            _mesh = CityGMLBuildingTessellator::createMesh(_buildings,
                                                           *_vc->_context->getPlanet(),
                                                           _fixOnGround, checkSurfacesVisibility, NULL,
                                                           _vc->_elevationData);
      _buildingMeshes.push_back(_mesh);
      
#warning TEST CODE
//      for (size_t i = 0; i < _buildings.size(); i++) {
//        std::vector<CityGMLBuilding*> bs;
//        bs.push_back(_buildings[i]);
//        _mesh = CityGMLBuildingTessellator::createMesh(bs,
//                                                       *_vc->_context->getPlanet(),
//                                                       _fixOnGround, checkSurfacesVisibility, NULL,
//                                                       _vc->_elevationData);
//        _buildingMeshes.push_back(_mesh);
//      }
      
    }
    
    virtual void onPostExecute(const G3MContext* context){
      
      //Including elements must be done in the rendering thread
      //      _vc->_meshRenderer->addMesh(_mesh);
      
      for (size_t i = 0; i < _buildingMeshes.size(); i++) {
        _vc->_meshRenderer->addMesh(_buildingMeshes[i]);
      }
      
      //Uncomment for seeing spheres
      //      for (size_t i = 0; i < _buildings.size(); i++) {
      //      _vc->_meshRenderer->addMesh(CityGMLBuildingTessellator::getSphereOfBuilding(_buildings[i])->createWireframeMesh(Color::red(), 5));
      //      }
      
      for (size_t i = 0; i < _marks.size(); i++) {
        _vc->_marksRenderer->addMark( _marks[i] );
      }
      
      for (size_t i = 0; i < _geoSymbol.size(); i++) {
        _vc->_vectorLayer->addSymbol(_geoSymbol[i]);
      }
      
      _listener->onBuildingsLoaded(_buildings);
      if (_autoDelete){
        delete _listener;
      }
    }
  };
  
public:
  
  CityGMLRenderer(MeshRenderer* meshRenderer,
                  MarksRenderer* marksRenderer,
                  GEOVectorLayer* vectorLayer):
  _elevationData(NULL),
  _meshRenderer(meshRenderer),
  _marksRenderer(marksRenderer),
  _lastCamera(NULL),
  _vectorLayer(vectorLayer),
  _touchListener(NULL){}
  
  void setElevationData(ElevationData* ed){
    _elevationData = ed;
  }
  
  virtual void initialize(const G3MContext* context) {
    DefaultRenderer::initialize(context);
    _meshRenderer->initialize(context);
    if (_marksRenderer != NULL){
      _marksRenderer->initialize(context);
    }
  }
  
  const std::vector<CityGMLBuilding*>* getBuildings() const{
    return &_buildings;
  }
  
  void addBuildingsFromURL(const URL& url,
                           bool fixBuildingsOnGround,
                           CityGMLRendererListener* listener, bool autoDelete);
  
  void addBuildingDataFromURL(const URL& url);
  
  void addBuildings(const std::vector<CityGMLBuilding*>& buildings,
                    bool fixOnGround,
                    CityGMLRendererListener* listener, bool autoDelete);
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height){}
  
  virtual void render(const G3MRenderContext* rc,
                      GLState* glState);
  
  //  virtual bool onTouchEvent(const G3MEventContext* ec,
  //                            const TouchEvent* touchEvent) {
  //    return false;
  //  }
  
  void colorBuildings(CityGMLBuildingColorProvider* cp){
    for (size_t i = 0; i < _buildings.size(); i++) {
      CityGMLBuilding* b = _buildings.at(i);
      Color c = cp->getColor(b);
      CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(b, c);
    }
  }
  
  std::vector<double> getAllValuesOfProperty(const std::string& name){
    std::vector<double> v;
    for (size_t i = 0; i < _buildings.size(); i++) {
      double value = _buildings[i]->getNumericProperty(name);
      if (!ISNAN(value)){
        v.push_back(value);
      }
    }
    return v;
  }
  
  void colorBuildingsWithColorBrewer(const std::string& propertyName,
                                     const std::string& colorScheme,
                                     int nClasses){
    
    std::vector<double> vs = getAllValuesOfProperty(propertyName);
    
    ColorLegend* cl = ColorLegendHelper::createColorBrewLegendWithNaturalBreaks(vs, colorScheme, nClasses);
    
    BuildingDataColorProvider* colorProvider = new BuildingDataColorProvider(propertyName, cl);
    
    colorBuildings(colorProvider);
    
    delete colorProvider;
    
  }
  
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);
  
  void setTouchListener(CityGMLBuildingTouchedListener* touchListener){
    _touchListener = touchListener;
  }
  
  
};

#endif /* CityGMLRenderer_hpp */
