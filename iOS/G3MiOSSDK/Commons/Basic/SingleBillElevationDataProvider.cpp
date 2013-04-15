//
//  SingleBillElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "SingleBillElevationDataProvider.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "Vector2I.hpp"
#include "IBufferDownloadListener.hpp"
#include "TimeInterval.hpp"
#include "BilParser.hpp"
#include "SubviewElevationData.hpp"

SingleBillElevationDataProvider::SingleBillElevationDataProvider(const URL& bilUrl,
                                                                 const Sector& sector,
                                                                 const Vector2I& resolution,
                                                                 const double noDataValue,
                                                                 const bool useFloat) :
_bilUrl(bilUrl),
_sector(sector),
_resolutionWidth(resolution._x),
_resolutionHeight(resolution._y),
_noDataValue(noDataValue),
_elevationData(NULL),
_elevationDataResolved(false),
_useFloat(useFloat)
{

}

class SingleBillElevationDataProvider_BufferDownloadListener : public IBufferDownloadListener {
private:
  SingleBillElevationDataProvider* _singleBillElevationDataProvider;
  const Sector _sector;
  const int _resolutionWidth;
  const int _resolutionHeight;
  const double _noDataValue;
  const bool _useFloat;

public:
  SingleBillElevationDataProvider_BufferDownloadListener(SingleBillElevationDataProvider* singleBillElevationDataProvider,
                                                         const Sector& sector,
                                                         int resolutionWidth,
                                                         int resolutionHeight,
                                                         double noDataValue,
                                                         const bool useFloat) :
  _singleBillElevationDataProvider(singleBillElevationDataProvider),
  _sector(sector),
  _resolutionWidth(resolutionWidth),
  _resolutionHeight(resolutionHeight),
  _noDataValue(noDataValue),
  _useFloat(useFloat)
  {

  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer) {
    const Vector2I resolution(_resolutionWidth, _resolutionHeight);
    
    ElevationData* elevationData = NULL;
    //TODO: NECESARY USE FLOAT?????
    if (!_useFloat){
    elevationData = BilParser::parseBil16(_sector, resolution, (short)_noDataValue, -9999, buffer);
    } else{
    elevationData = BilParser::parseBil16ToFloatElevationData(_sector, resolution, (short)_noDataValue, -9999, buffer);
    }
    
    delete buffer;

    _singleBillElevationDataProvider->onElevationData(elevationData);
  }

  void onError(const URL& url) {
    _singleBillElevationDataProvider->onElevationData(NULL);
  }

  void onCancel(const URL& url) {

  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* data) {

  }
};

void SingleBillElevationDataProvider::onElevationData(ElevationData* elevationData) {
  _elevationData = elevationData;
  _elevationDataResolved = true;
  if (_elevationData == NULL) {
    ILogger::instance()->logError("Can't download Elevation-Data from %s",
                                  _bilUrl.getPath().c_str());
  }

  drainQueue();
}

void SingleBillElevationDataProvider::initialize(const G3MContext* context) {
  if (!_elevationDataResolved) {
    context->getDownloader()->requestBuffer(_bilUrl,
                                            2000000000,
                                            TimeInterval::fromDays(30),
                                            new SingleBillElevationDataProvider_BufferDownloadListener(this,
                                                                                                       _sector,
                                                                                                       _resolutionWidth,
                                                                                                       _resolutionHeight,
                                                                                                       _noDataValue,
                                                                                                       _useFloat),
                                            true);
  }
}

const long long SingleBillElevationDataProvider::requestElevationData(const Sector& sector,
                                                                      const Vector2I& resolution,
                                                                      IElevationDataListener* listener,
                                                                      bool autodeleteListener) {
  if (!_elevationDataResolved) {
    return queueRequest(sector,
                        resolution,
                        listener,
                        autodeleteListener);
  }

  if (_elevationData == NULL) {
    listener->onError(sector, resolution);
  }
  else {
    int _DGD_working_on_terrain;
    const bool useDecimation = false;
    ElevationData *elevationData = new SubviewElevationData(_elevationData,
                                                            false,
                                                            sector,
                                                            resolution,
                                                            useDecimation);
    listener->onData(sector,
                     resolution,
                     elevationData);
  }

  if (autodeleteListener) {
    delete listener;
  }

  return -1;
}

void SingleBillElevationDataProvider::cancelRequest(const long long requestId) {
  if (requestId >= 0) {
    removeQueueRequest(requestId);
  }
}

void SingleBillElevationDataProvider::drainQueue() {
  int _DGD_working_on_terrain;
}

const long long SingleBillElevationDataProvider::queueRequest(const Sector& sector,
                                                              const Vector2I& resolution,
                                                              IElevationDataListener* listener,
                                                              bool autodeleteListener) {
  int _DGD_working_on_terrain;
  return -1;
}

void SingleBillElevationDataProvider::removeQueueRequest(const long long requestId) {
  int _DGD_working_on_terrain;
}
