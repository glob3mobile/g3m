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

void CityGMLRenderer::addBuildings(const std::vector<CityGMLBuilding*>& buildings,
                                   CityGMLRendererListener* listener, bool autoDelete){
  
  for (size_t i = 0; i < buildings.size(); i++) {
    _buildings.push_back(buildings[i]);
  }
  
  bool createCityMeshAndMarks = true;
  if (createCityMeshAndMarks){
    _context->getThreadUtils()->invokeAsyncTask(new TessellationTask(this, buildings, listener, autoDelete), true);
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
  _marksRenderer->render(rc, glState);
}

class CityGMLParsingListener: public CityGMLListener{
  
private:
  CityGMLRenderer* _demo;
  const IThreadUtils* _threadUtils;
  CityGMLRendererListener *_listener;
  bool _autoDelete;
public:
  
  CityGMLParsingListener(CityGMLRenderer* demo,
                         CityGMLRendererListener *listener, bool autoDelete):
  _demo(demo), _listener(listener), _autoDelete(autoDelete){
    
  }
  
  class MyLis: public CityGMLRendererListener{
  public:
    virtual void onBuildingsLoaded(const std::vector<CityGMLBuilding*>& buildings){}
    
  };
  
  virtual void onBuildingsCreated(const std::vector<CityGMLBuilding*>& buildings){
    _demo->addBuildings(buildings, _listener, _autoDelete);
  }
  
  virtual void onError(){
    
  }
};

void CityGMLRenderer::addBuildingsFromURL(const URL& url,
                                          CityGMLRendererListener* listener, bool autoDelete){
  
  CityGMLParser::parseFromURL(url,
                              new CityGMLParsingListener(this, listener, autoDelete),
                              true);
  
}


bool CityGMLRenderer::onTouchEvent(const G3MEventContext* ec,
                                  const TouchEvent* touchEvent) {
  
  if (_lastCamera == NULL) {
    return false;
  }
  
  if ( touchEvent->getType() == LongPress ) {
    const Vector2F pixel = touchEvent->getTouch(0)->getPos();
    const Vector3D ray = _lastCamera->pixel2Ray(pixel);
    const Vector3D origin = _lastCamera->getCartesianPosition();
    
    const Planet* planet = ec->getPlanet();
    
    const Vector3D positionCartesian = planet->closestIntersection(origin, ray);
    if (positionCartesian.isNan()) {
      ILogger::instance()->logWarning("PlanetRenderer::onTouchEvent: positionCartesian ( - planet->closestIntersection(origin, ray) - ) is NaN");
      return false;
    }
    
  }
  
  return false;
}
