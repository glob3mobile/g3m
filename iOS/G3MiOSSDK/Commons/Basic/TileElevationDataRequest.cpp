//
//  TileElevationDataListener.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//

#include "TileElevationDataRequest.hpp"

#include "PlanetRenderContext.hpp"
#include "LayerTilesRenderParameters.hpp"

#pragma mark TileElevationDataRequest

TileElevationDataRequest::TileElevationDataRequest(Tile* tile,
                                                   const Vector2I& resolution,
                                                   ElevationDataProvider* provider) :
_tile(tile),
_resolution(resolution),
_provider(provider),
_requestID(-1),
_listener(NULL)
{

}

void TileElevationDataRequest::onData(const Sector& sector,
                                      const Vector2I& resolution,
                                      ElevationData* elevationData) {
  _listener = NULL;
  if (_tile != NULL) {
    _tile->setElevationData(elevationData, _tile->_level);
  }
}

void TileElevationDataRequest::onError(const Sector& sector,
                                       const Vector2I& resolution) {
  _listener = NULL;
}

void TileElevationDataRequest::onCancel(const Sector& sector,
                                        const Vector2I& resolution) {
  _listener = NULL;
}

void TileElevationDataRequest::cancelRequest() {
  if (_listener != NULL) {
    _listener->_request = NULL;
    if (_requestID > -1){
      _provider->cancelRequest(_requestID);
    }
  }
}

void TileElevationDataRequest::sendRequest(const G3MRenderContext *rc, const PlanetRenderContext *prc) {
  _listener = new TileElevationDataRequestListener(this);
  _requestID = _provider->requestElevationData(_tile->_sector,
                                               _resolution,
                                               _tile->_level,
                                               _tile->_row,
                                               _tile->_column,
                                               _listener,
                                               true);
  if (_requestID < -1){
    //A requestID lower than -1 is defined to represent a tile which won't have elevationData due to the pyramid being shorter than needed.
    //That case, we will try to get ElevData from ancestor and define it as the one needed in the level.
    long long maxLevel = - (_requestID);
    Tile *theLastAncestor = NULL;
    Tile *theAncestor = _tile->getParent();
    while ( theAncestor != NULL){
      if (theAncestor->_level == maxLevel) {
        theLastAncestor = theAncestor;
        break;
      }
      theAncestor = theAncestor->getParent();
    }
    if (theLastAncestor != NULL){
      if (theLastAncestor->getElevationData() == NULL) {
        //Ensure lastAncestor to have an ElevData.
        theLastAncestor->initializeElevationData(rc, prc);
      }
      if (theLastAncestor->getElevationData() != NULL) {
        ElevationData* subView = _tile->createElevationDataSubviewFromAncestor(theLastAncestor);
        _tile->setElevationData(subView, _tile->_level);
      }
    }
    _requestID = -1;
  }
}

#pragma mark TileElevationDataRequestListener

TileElevationDataRequestListener::TileElevationDataRequestListener(TileElevationDataRequest* request) :
_request(request)
{
}

void TileElevationDataRequestListener::onData(const Sector& sector,
                                              const Vector2I& resolution,
                                              ElevationData* elevationData) {
  if (_request != NULL) {
    _request->onData(sector, resolution, elevationData);
  }
}

void TileElevationDataRequestListener::onError(const Sector& sector,
                                               const Vector2I& resolution) {
  if (_request != NULL) {
    _request->onError(sector, resolution);
  }
}

void TileElevationDataRequestListener::onCancel(const Sector& sector,
                                                const Vector2I& resolution) {
  if (_request != NULL) {
    _request->onCancel(sector, resolution);
  }
}
