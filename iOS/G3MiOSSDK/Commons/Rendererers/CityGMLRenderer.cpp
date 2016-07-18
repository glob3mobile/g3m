//
//  CityGMLRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/4/16.
//
//

#include "CityGMLRenderer.hpp"

#include "GLState.hpp"
#include "Camera.hpp"
#include "Mesh.hpp"
#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "CityGMLParser.hpp"
#include "IBufferDownloadListener.hpp"
#include "BuildingDataParser.hpp"
#include "TouchEvent.hpp"
#include "G3MEventContext.hpp"
#include "DirectMesh.hpp"

void CityGMLRenderer::addBuildings(const std::vector<CityGMLBuilding*>& buildings,
                                   bool fixOnGround,
                                   CityGMLRendererListener* listener, bool autoDelete){
  
  std::vector<CityGMLBuilding*> notRepeatedBuildings;
  
  for (size_t i = 0; i < buildings.size(); i++) {
    
    CityGMLBuilding* b = buildings[i];
    for (size_t j = 0; j < _buildings.size(); j++) {
      if (_buildings[j]->_name == b->_name){
        delete b;
        b = NULL;
        break;
      }
    }
    
    if (b != NULL){
      notRepeatedBuildings.push_back(b);
    }
  }
  
  for (size_t i = 0; i < notRepeatedBuildings.size(); i++) {
    _buildings.push_back(notRepeatedBuildings[i]);
  }
  
  bool createCityMeshAndMarks = true;
  if (createCityMeshAndMarks){
    _context->getThreadUtils()->invokeAsyncTask(new TessellationTask(this, notRepeatedBuildings, fixOnGround, listener, autoDelete), true);
  }
}

class BuildingDataBDL : public IBufferDownloadListener {
private:
  std::vector<CityGMLBuilding*> _buildings;
  
  class DataParsingTask: public GAsyncTask {
    const std::vector<CityGMLBuilding*> _buildings;
    const std::string _s;
  public:
    
    DataParsingTask(const std::string& s,
                    const std::vector<CityGMLBuilding*>& buildings):
    _s(s),
    _buildings(buildings)
    {}
    
    virtual void runInBackground(const G3MContext* context){
      BuildingDataParser::includeDataInBuildingSet(_s, _buildings);
    }
    
    virtual void onPostExecute(const G3MContext* context){
    }
  };
  
  const G3MContext* _context;
  
public:
  BuildingDataBDL(const std::vector<CityGMLBuilding*>& buildings, const G3MContext* context) :
  _buildings(buildings),
  _context(context)
  {
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    delete buffer;
    
    _context->getThreadUtils()->invokeAsyncTask(new DataParsingTask(s, _buildings), true);
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());
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




void CityGMLRenderer::addBuildingDataFromURL(const URL& url){
  
  
  _context->getDownloader()->requestBuffer(url,
                                           1000,
                                           TimeInterval::forever(), true,
                                           new BuildingDataBDL(_buildings, _context),
                                           true);
}



void CityGMLRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  _lastCamera = rc->getCurrentCamera();
  
  _meshRenderer->render(rc, glState);
  if (_marksRenderer != NULL){
    _marksRenderer->render(rc, glState);
  }
  
//  //Rendering Spheres
//  Color red = Color::red();
//  for (size_t i = 0; i < _buildings.size(); i++) {
//    const Sphere* s = CityGMLBuildingTessellator::getSphereOfBuilding(_buildings[i]);
//    if (s != NULL){
//      s->render(rc, glState, red);
//    }
//  }
}

class CityGMLParsingListener: public CityGMLListener{
  
private:
  CityGMLRenderer* _demo;
  const IThreadUtils* _threadUtils;
  CityGMLRendererListener *_listener;
  bool _autoDelete;
  const bool _fixBuidlingsOnGround;
public:
  
  CityGMLParsingListener(CityGMLRenderer* demo,
                         bool fixBuidlingsOnGround,
                         CityGMLRendererListener *listener, bool autoDelete):
  _demo(demo), _listener(listener), _autoDelete(autoDelete),
  _fixBuidlingsOnGround(fixBuidlingsOnGround){
    
  }
  
  class MyLis: public CityGMLRendererListener{
  public:
    virtual void onBuildingsLoaded(const std::vector<CityGMLBuilding*>& buildings){}
    
  };
  
  virtual void onBuildingsCreated(const std::vector<CityGMLBuilding*>& buildings){
    _demo->addBuildings(buildings, _fixBuidlingsOnGround, _listener, _autoDelete);
  }
  
  virtual void onError(){
    
  }
};

void CityGMLRenderer::addBuildingsFromURL(const URL& url,
                                          bool fixBuildingsOnGround,
                                          CityGMLRendererListener* listener,
                                          bool autoDelete){
  
  CityGMLParser::parseFromURL(url,
                              new CityGMLParsingListener(this, fixBuildingsOnGround, listener, autoDelete),
                              true);
  
}


bool CityGMLRenderer::onTouchEvent(const G3MEventContext* ec,
                                   const TouchEvent* touchEvent) {
  
  if (_lastCamera == NULL || _touchListener == NULL) {
    return false;
  }
  
  if ( touchEvent->getType() == LongPress ) {
    const Vector2F pixel = touchEvent->getTouch(0)->getPos();
    const Vector3D ray = _lastCamera->pixel2Ray(pixel);
    const Vector3D origin = _lastCamera->getCartesianPosition();
    
//    FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
//    fbb->add(origin);
//    fbb->add(origin.add(ray.times(10)));
//    DirectMesh* dm = new DirectMesh(GLPrimitive::lines(),
//                                    true,
//                                    fbb->getCenter(),
//                                    fbb->create(),
//                                    4.0f,
//                                    1.0f,
//                                    new Color(Color::green()));
//    _meshRenderer->addMesh(dm);
//    
//    delete fbb;
//    
//    fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
//    fbb->add(origin);
//    
//    DirectMesh* dm2 = new DirectMesh(GLPrimitive::lines(),
//                                     true,
//                                     fbb->getCenter(),
//                                     fbb->create(),
//                                     4.0f,
//                                     1.0f,
//                                     new Color(Color::blue()));
//    
//    
//    delete fbb;
//    _meshRenderer->addMesh(dm2);
    
    double minDis = 1e20;
    CityGMLBuilding* touchedB = NULL;
    
    for (size_t i = 0; i < _buildings.size(); i++) {
      const Sphere* s = CityGMLBuildingTessellator::getSphereOfBuilding(_buildings[i]);
      
      if (s != NULL){
        const std::vector<double> dists = s->intersectionsDistances(origin._x, origin._y, origin._z,
                                                                    ray._x, ray._y, ray._z);
        for (size_t j = 0; j < dists.size(); j++){
          if (dists[j] < minDis){
            minDis = dists[j];
            touchedB = _buildings[i];
          }
        }
        
      }
    }
    
    if (touchedB != NULL){
      _touchListener->onBuildingTouched(touchedB);
    }
  }
  
  return false;
}

