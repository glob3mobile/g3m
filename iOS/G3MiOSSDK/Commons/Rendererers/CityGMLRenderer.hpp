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

class Mesh;

class CityGMLRendererListener{
public:
  virtual ~CityGMLRendererListener(){}
  virtual void onBuildingsLoaded(const std::vector<CityGMLBuilding*>& buildings) = 0;
  
};

class CityGMLRenderer: public DefaultRenderer{
  
  std::vector<CityGMLBuilding*> _buildings;
  
  ElevationData* _elevationData;
  MeshRenderer* _meshRenderer;
  MarksRenderer* _marksRenderer;
  
  class TessellationTask: public GAsyncTask {
    CityGMLRenderer* _vc;
    std::vector<CityGMLBuilding*> _buildings;
    
    CityGMLRendererListener* _listener;
    bool _autoDelete;
  public:
    
    TessellationTask(CityGMLRenderer* vc,
                     const std::vector<CityGMLBuilding*>& buildings,
                     CityGMLRendererListener* listener, bool autoDelete):
    _vc(vc),
    _buildings(buildings),
    _listener(listener),
    _autoDelete(autoDelete)
    {}
    
    virtual void runInBackground(const G3MContext* context){
      //Adding marks
      for (size_t i = 0; i < _buildings.size(); i++) {
        _vc->_marksRenderer->addMark( CityGMLBuildingTessellator::createMark(_buildings[i], false) );
      }
      
      //Checking walls visibility
      int n = CityGMLBuilding::checkWallsVisibility(_buildings);
      ILogger::instance()->logInfo("Removed %d invisible walls from the model.", n);
      
      //Creating mesh model
      Mesh* mesh = CityGMLBuildingTessellator::createMesh(_buildings,
                                                          *_vc->_context->getPlanet(),
                                                          false, false, NULL,
                                                          _vc->_elevationData);
      _vc->_meshRenderer->addMesh(mesh);
    }
    
    virtual void onPostExecute(const G3MContext* context){
      _listener->onBuildingsLoaded(_buildings);
      if (_autoDelete){
        delete _listener;
      }
    }
  };
  
public:
  
  CityGMLRenderer(MeshRenderer* meshRenderer,
                  MarksRenderer* marksRenderer):
  _elevationData(NULL),
  _meshRenderer(meshRenderer),
  _marksRenderer(marksRenderer){}
  
  void setElevationData(ElevationData* ed){
    _elevationData = ed;
  }
  
  virtual void initialize(const G3MContext* context) {
    DefaultRenderer::initialize(context);
    _meshRenderer->initialize(context);
    _marksRenderer->initialize(context);
  } 
  
  void addBuildingsFromURL(const URL& url,
                    CityGMLRendererListener* listener, bool autoDelete);
  
  void addBuildingDataFromURL(const URL& url);
  
  void addBuildings(const std::vector<CityGMLBuilding*>& buildings,
                    CityGMLRendererListener* listener, bool autoDelete);
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height){}
  
  virtual void render(const G3MRenderContext* rc,
                      GLState* glState);
  
  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) {
    return false;
  }
  
  void colorBuildings(CityGMLBuildingColorProvider* cp){
    
    for (size_t i = 0; i < _buildings.size(); i++) {
      CityGMLBuilding* b = _buildings.at(i);
      Color c = cp->getColor(b);
      CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(b, c);
    }
    
  }
  
};

#endif /* CityGMLRenderer_hpp */
