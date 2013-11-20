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

void CompositeElevationDataProvider::addElevationDataProvider(ElevationDataProvider* edp) {
  _providers.push_back(edp);
  if (_context != NULL) {
    edp->initialize(_context);
  }

  edp->setChangedListener(_changedListener); //Setting Changed Listener on child

  onChanged();
}

bool CompositeElevationDataProvider::isReadyToRender(const G3MRenderContext* rc) {
  int size = _providers.size();
  for (int i = 0; i < size; i++) {
    if (!_providers[i]->isReadyToRender(rc)) {
      return false;
    }
  }
  return true;
}

void CompositeElevationDataProvider::initialize(const G3MContext* context) {
  _context = context;
  int size = _providers.size();
  for (int i = 0; i < size; i++) {
    _providers[i]->initialize(context);
  }
}

std::vector<ElevationDataProvider*> CompositeElevationDataProvider::getProviders(const Sector& s) const {
  int size = _providers.size();
  std::vector<ElevationDataProvider*> providers;

  for (int i = 0; i < size; i++) {

    ElevationDataProvider* edp = _providers[i];

    std::vector<const Sector*> sectorsI = edp->getSectors();
    int sizeI = sectorsI.size();
    for (int j = 0; j < sizeI; j++) {
      if (sectorsI[j]->touchesWith(s)) { //This provider contains the sector
        providers.push_back(edp);
      }
    }
  }
  return providers;
}

const long long CompositeElevationDataProvider::requestElevationData(const Sector& sector,
                                                                     const Vector2I& extent,
                                                                     IElevationDataListener* listener,
                                                                     bool autodeleteListener) {

  CompositeElevationDataProvider_Request* req = new CompositeElevationDataProvider_Request(this,
                                                                                           sector,
                                                                                           extent,
                                                                                           listener,
                                                                                           autodeleteListener);
  _currentID++;
  _requests[_currentID] =  req;

  req->launchNewStep();

  return _currentID;
}

std::vector<const Sector*> CompositeElevationDataProvider::getSectors() const {
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

const Vector2I CompositeElevationDataProvider::getMinResolution() const {
  const int size = _providers.size();
  double minD = IMathUtils::instance()->maxDouble();
  int x = -1;
  int y = -1;

  for (int i = 0; i < size; i++) {
    const Vector2I res = _providers[i]->getMinResolution();
    const double d = res.squaredLength();

    if (minD > d) {
      minD = d;
      x = res._x;
      y = res._y;
    }
  }
  return Vector2I(x,y);
}

void CompositeElevationDataProvider::cancelRequest(const long long requestId) {
#ifdef C_CODE
  std::map<long long, CompositeElevationDataProvider_Request*>::iterator it = _requests.find(requestId);
  if (it != _requests.end()) {
    CompositeElevationDataProvider_Request* req = it->second;
    req->cancel();
    _requests.erase(requestId);
    delete req;
  }
#endif
#ifdef JAVA_CODE
  final CompositeElevationDataProvider_Request req = _requests.remove(requestId);
  if (req != null) {
    req.cancel();
    req.dispose();
  }
#endif
}

void CompositeElevationDataProvider::requestFinished(CompositeElevationDataProvider_Request* req) {
  
  req->respondToListener();

#ifdef C_CODE
  std::map<long long, CompositeElevationDataProvider_Request*>::iterator it;
  for (it =  _requests.begin(); it !=  _requests.end(); it++) {
    const CompositeElevationDataProvider_Request* reqI = it->second;
    if (reqI == req) {
      _requests.erase(it);
      
      delete req;

      return;
    }
  }

  if (it == _requests.end()) {
    ILogger::instance()->logError("Deleting nonexisting request in CompositeElevationDataProvider.");
  }
#endif

#ifdef JAVA_CODE
  for (final java.util.Map.Entry<Long, CompositeElevationDataProvider_Request> entry : _requests.entrySet()) {
    final CompositeElevationDataProvider_Request reqI = entry.getValue();
    if (reqI == req) {
      _requests.remove(entry.getKey());
      reqI.dispose();
      return;
    }
  }

  ILogger.instance().logError("Deleting nonexisting request in CompositeElevationDataProvider.");
#endif

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
_currentStep(NULL) {
}

ElevationDataProvider* CompositeElevationDataProvider::
CompositeElevationDataProvider_Request::
popBestProvider(std::vector<ElevationDataProvider*>& ps, const Vector2I& extent) const{

  double bestRes = extent.squaredLength();
  double selectedRes = IMathUtils::instance()->maxDouble();
  double selectedResDistance = IMathUtils::instance()->maxDouble();
  IMathUtils *mu = IMathUtils::instance();


  ElevationDataProvider* provider = NULL;
  
  const int psSize = ps.size();
  int selectedIndex = -1;
  for (int i = 0; i < psSize; i++) {
    ElevationDataProvider* each = ps[i];

    const double res = each->getMinResolution().squaredLength();
    const double newResDistance = mu->abs(bestRes - res);

    if (newResDistance < selectedResDistance || //Closer Resolution
        (newResDistance == selectedResDistance && res < selectedRes)) { //or equal and higher resolution
      selectedResDistance = newResDistance;
      selectedRes = res;
      selectedIndex = i;
      provider = each;
    }
  }

  if (provider != NULL) {
#ifdef C_CODE
    ps.erase(ps.begin() + selectedIndex);
#endif
#ifdef JAVA_CODE
    ps.remove(selectedIndex);
#endif
  }
  
  return provider;
}

bool CompositeElevationDataProvider::CompositeElevationDataProvider_Request::launchNewStep() {
  _currentProvider = popBestProvider(_providers, _resolution);
  if (_currentProvider != NULL) {
    _currentStep = new CompositeElevationDataProvider_RequestStepListener(this);

    _currentID = _currentProvider->requestElevationData(_sector, _resolution, _currentStep, true);

    return true;
  }

  _currentStep = NULL; //Waiting for no request
  return false;
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::onData(const Sector& sector,
                                                                                    const Vector2I& extent,
                                                                                    ElevationData* elevationData) {
  _currentStep = NULL;
  if (_compData == NULL) {
    _compData = new CompositeElevationData(elevationData);
  } else{
    ((CompositeElevationData*)_compData)->addElevationData(elevationData);
  }
  
  if (!_compData->hasNoData()) {
    _compProvider->requestFinished(this);//If this data is enough we respond
  } else{
    if (!launchNewStep()) {//If there are no more providers we respond
      _compProvider->requestFinished(this);
    }
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::cancel() {
  
  if (_currentStep != NULL) {
    _currentStep->_request = NULL;
    _currentProvider->cancelRequest(_currentID);
    _currentStep = NULL;
  }
  
  _listener->onCancel(_sector, _resolution);
  if (_autodelete) {
    delete _listener;
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::onError(const Sector& sector,
                                                                                     const Vector2I& extent) {
  _currentStep = NULL;
  if (!launchNewStep()) {
    //If there are no more providers we respond
    _compProvider->requestFinished(this);
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::respondToListener() const {
  
  if (_compData == NULL) {
    _listener->onError(_sector, _resolution);
    if (_autodelete) {
      delete _listener;
    }
  } else{
    _listener->onData(_sector, _resolution, _compData);
    if (_autodelete) {
      delete _listener;
    }
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_Request::onCancel(const Sector& sector,
                                                                                      const Vector2I& extent) {
  _currentStep = NULL;
}

#pragma mark RequestStep

CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStepListener::
CompositeElevationDataProvider_RequestStepListener(CompositeElevationDataProvider_Request* request):
_request(request) {
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStepListener::onData(const Sector& sector,
                                                                                                const Vector2I& extent,
                                                                                                ElevationData* elevationData) {
  if (_request != NULL) {
    _request->onData(sector, extent, elevationData);
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStepListener::onError(const Sector& sector,
                                                                                                 const Vector2I& extent) {
  if (_request != NULL) {
    _request->onError(sector, extent);
  }
}

void CompositeElevationDataProvider::CompositeElevationDataProvider_RequestStepListener::onCancel(const Sector& sector,
                                                                                                  const Vector2I& extent) {
  if (_request != NULL) {
    _request->onCancel(sector, extent);
  }
}
