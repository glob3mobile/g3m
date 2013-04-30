//
//  CompositeElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/04/13.
//
//

#include "CompositeElevationDataProvider.hpp"

#include "Vector2I.hpp"
#include "CompositeElevationData.hpp"
#include "SubviewElevationData.hpp"

void CompositeElevationDataProvider::addElevationDataProvider(ElevationDataProvider* edp){
  _providers.push_back(edp);
  if (_context != NULL){
    edp->initialize(_context);
  }
}

bool CompositeElevationDataProvider::isReadyToRender(const G3MRenderContext* rc){
  int size = _providers.size();
  for (int i = 0; i < size; i++) {
    if (!_providers[i]->isReadyToRender(rc)){
      return false;
    }
  }
  return true;
}

void CompositeElevationDataProvider::initialize(const G3MContext* context){
  _context = context;
  int size = _providers.size();
  for (int i = 0; i < size; i++) {
    _providers[i]->initialize(context);
  }
}

std::vector<ElevationDataProvider*> CompositeElevationDataProvider::getProviders(const Sector& s) const{
  int size = _providers.size();
  std::vector<ElevationDataProvider*> providers;
  
  for (int i = 0; i < size; i++) {
    
    ElevationDataProvider* edp = _providers[i];
    
    std::vector<const Sector*> sectorsI = edp->getSectors();
    int sizeI = sectorsI.size();
    for (int j = 0; j < sizeI; j++) {
      if (sectorsI[j]->touchesWith(s)){ //This provider contains the sector
        providers.push_back(edp);
      }
    }
  }
  return providers;
}

const long long CompositeElevationDataProvider::requestElevationData(const Sector& sector,
                                                                     const Vector2I& resolution,
                                                                     IElevationDataListener* listener,
                                                                     bool autodeleteListener){
  
  CompositeElevationDataProvider_Request* req = new CompositeElevationDataProvider_Request(this,
                                                                                           sector,
                                                                                           resolution,
                                                                                           listener,
                                                                                           autodeleteListener);
  _currentID++;
  _requests[_currentID] =  req;
  
  req->launchNewStep();
  
  return _currentID;
}

std::vector<const Sector*> CompositeElevationDataProvider::getSectors() const{
  std::vector<const Sector*> sectors;
  int size = _providers.size();
  for (int i = 0; i < size; i++) {
    std::vector<const Sector*> sectorsI = _providers[i]->getSectors();
    int sizeI = sectorsI.size();
    for (int j = 0; j < sizeI; j++) {
      sectors.push_back(sectorsI[j]);
    }
  }
  return sectors;
}

Vector2I CompositeElevationDataProvider::getMinResolution() const{
  
  int size = _providers.size();
  double minD = 9999999999;
  int x= -1, y= -1;
  
  for (int i = 0; i < size; i++) {
    
    Vector2I res = _providers[i]->getMinResolution();
    double d = res.squaredLength();
    
    if (minD > d){
      minD = d;
      x = res._x;
      y = res._y;
    }
  }
  return Vector2I(x,y);
}

void CompositeElevationDataProvider::cancelRequest(const long long requestId){
  std::map<long long, CompositeElevationDataProvider_Request*>::iterator it = _requests.find(requestId);
  if (it != _requests.end()) {
    CompositeElevationDataProvider_Request* req = it->second;
    req->cancel();
    _requests.erase(requestId);
    delete req;
  }
}

void CompositeElevationDataProvider::requestFinished(CompositeElevationDataProvider_Request* req){
  
  req->respondToListener();
  
  std::map<long long, CompositeElevationDataProvider_Request*>::iterator it;
  for (it =  _requests.begin(); it !=  _requests.end(); it++) {
    const CompositeElevationDataProvider_Request* reqI = it->second;
    if (reqI == req){
      _requests.erase(it);
      
      delete req;

      return;
    }
  }
  
  ILogger::instance()->logError("Deleting nonexisting request in CompositeElevationDataProvider.");
}


ElevationData* CompositeElevationDataProvider::createSubviewOfElevationData(ElevationData* elevationData,
                                                                            const Sector& sector,
                                                                            const Vector2I& resolution) const{
  return new SubviewElevationData(elevationData,
                                  false,
                                  sector,
                                  resolution,
                                  false);
}

#pragma mark Request

CompositeElevationDataProvider::CompositeElevationDataProvider_Request::
CompositeElevationDataProvider_Request(CompositeElevationDataProvider* provider,
                                       const Sector& sector,
                                       const Vector2I &resolution,
                                       IElevationDataListener *listener,
                                       bool autodelete):
_providers(provider->getProviders(sector)),
_sector(sector),
_resolution(resolution),
_listener(listener),
_autodelete(autodelete),
_compProvider(provider),
_compData(NULL),
_currentStep(NULL){
}

ElevationDataProvider* CompositeElevationDataProvider::
CompositeElevationDataProvider_Request::
popBestProvider(std::vector<ElevationDataProvider*>& ps, const Vector2I& resolution) const{
  std::vector<ElevationDataProvider*>::iterator edp = (std::vector<ElevationDataProvider*>::iterator) ps.end();
  double bestRes = resolution.squaredLength();
  double selectedRes = IMathUtils::instance()->maxDouble();
  double selectedResDistance = IMathUtils::instance()->maxDouble();
  IMathUtils *mu = IMathUtils::instance();
  for (std::vector<ElevationDataProvider*>::iterator it = ps.begin() ; it != ps.end(); ++it){
    double res = (*it)->getMinResolution().squaredLength();
    double newResDistance = mu->abs(bestRes - res);
    
    if (newResDistance < selectedResDistance || //Closer Resolution
        (newResDistance == selectedResDistance && res < selectedRes)){ //or equal and higher resolution
      selectedResDistance = newResDistance;
      selectedRes = res;
      edp = it;
    }
  }
  
  ElevationDataProvider* provider = NULL;
  if (edp != ps.end()){
    provider = *edp;
    ps.erase(edp);
  }
  return provider;
}

bool CompositeElevationDataProvider::CompositeElevationDataProvider_Request::launchNewStep(){
  ElevationDataProvider* bestProvider = popBestProvider(_providers, _resolution);
  if (bestProvider != NULL){
    _currentStep = new CompositeElevationDataProvider_RequestStep(this, bestProvider, _sector, _resolution);
    _currentStep->send();
    return true;
  } else{
    _currentStep = NULL; //Waiting for no request
    return false;
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::onData(const Sector& sector,
                                                                                    const Vector2I& resolution,
                                                                                    ElevationData* elevationData){
  _currentStep = NULL;
  if (_compData == NULL){
    _compData = new CompositeElevationData(elevationData);
  } else{
    ((CompositeElevationData*)_compData)->addElevationData(elevationData);
  }
  
  if (!_compData->hasNoData()){
    _compProvider->requestFinished(this);//If this data is enough we respond
  } else{
    if (!launchNewStep()){//If there are no more providers we respond
      _compProvider->requestFinished(this);
    }
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::cancel(){
  
  if (_currentStep != NULL){
    _currentStep->cancel();
    _currentStep = NULL;
  }
  
  _listener->onCancel(_sector, _resolution);
  if (_autodelete){
    delete _listener;
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::onError(const Sector& sector,
                                                                                     const Vector2I& resolution){
  _currentStep = NULL;
  if (!launchNewStep()){
    //If there are no more providers we respond
    _compProvider->requestFinished(this);
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::respondToListener() const{
  
  if (_compData == NULL){
    _listener->onError(_sector, _resolution);
    if (_autodelete){
      delete _listener;
    }
  } else{
    _listener->onData(_sector, _resolution, _compData);
    if (_autodelete){
      delete _listener;
    }
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::onCancel(const Sector& sector,
                                                                                      const Vector2I& resolution){
  _currentStep = NULL;
}

#pragma mark RequestStep

CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStep::
CompositeElevationDataProvider_RequestStep(CompositeElevationDataProvider_Request* request,
                                           ElevationDataProvider* provider,
                                           const Sector& sector,
                                           const Vector2I &resolution):
_request(request),
_provider(provider),
_sector(sector),
_resolution(resolution){
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStep::send(){
    _id = _provider->requestElevationData(_sector, _resolution, this, false);
}


void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStep::onData(const Sector& sector,
                                                                                        const Vector2I& resolution,
                                                                                        ElevationData* elevationData){
  if (_request != NULL){
    _request->onData(sector, resolution, elevationData);
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStep::cancel(){
  _provider->cancelRequest(_id);
  _request = NULL;
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStep::onError(const Sector& sector,
                                                                                         const Vector2I& resolution){
  if (_request != NULL){
    _request->onError(sector, resolution);
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStep::onCancel(const Sector& sector,
                                                                                          const Vector2I& resolution){
  if (_request != NULL){
    _request->onCancel(sector, resolution);
  }
}
