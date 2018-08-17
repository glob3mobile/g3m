//
//  TileElevationDataListener.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//

#include "TileElevationDataRequest.hpp"

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
    _provider->cancelRequest(_requestID);
  }
}

void TileElevationDataRequest::sendRequest() {
  _listener = new TileElevationDataRequestListener(this);
  _requestID = _provider->requestElevationData(_tile->_sector,
                                               _resolution,
                                               _listener, true);
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
