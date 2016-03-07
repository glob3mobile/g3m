//
//  PyramidElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 4/3/16.
//
//

#include "PyramidElevationDataProvider.hpp"
#include "ShortBufferElevationData.hpp"
#include "IStringBuilder.hpp"
#include "DownloadPriority.hpp"
#include "G3MContext.hpp"
#include "TimeInterval.hpp"
#include <sstream>


class PyramidElevationDataProvider_BufferDownloadListener : public IBufferDownloadListener {
private:
    
    const Sector &_sector;
    int _width, _height;
    IElevationDataListener *_listener;
    bool _autodeleteListener;
    double _deltaHeight;
    
#ifdef C_CODE
    const Vector2I* getResolution(const JSONObject *data){
        return new Vector2I((int) data->getAsNumber("width",0),(int) data->getAsNumber("height",0));
    }
#endif
#ifdef JAVA_CODE
    private Vector2I getResolution(JSONObject data){
        return new Vector2I((int) data.getAsNumber("width",0),(int) data.getAsNumber("height",0));
    }
#endif
    
    ShortBufferElevationData* getElevationData(const Sector& sector,
                                               const Vector2I& extent,
                                               const JSONObject *data,
                                               double deltaHeight){
        const short minValue = IMathUtils::instance()->minInt16();
        const int size = extent._x * extent._y;
        const JSONArray *dataArray = data->getAsArray("data");
        short *shortBuffer = new short[size];
        for (int i = 0; i < size; i++)
        {
            short height = (short) dataArray->getAsNumber(i, minValue);
            
            if (height == 15000) //Our own NODATA, since -9999 is a valid height.
            {
                height = ShortBufferElevationData::NO_DATA_VALUE;
            }
            else if (height == minValue)
            {
                height = ShortBufferElevationData::NO_DATA_VALUE;
            }
            
            shortBuffer[i] = height;
        }
        
        short max = (short) data->getAsNumber("max",IMathUtils::instance()->minInt16());
        short min = (short) data->getAsNumber("min",IMathUtils::instance()->maxInt16());
        short children = (short) data->getAsNumber("withChildren",0);
        short similarity = (short) data->getAsNumber("similarity",0);
        
        return new ShortBufferElevationData(sector, extent, sector, extent, shortBuffer,
                                            size, deltaHeight,max,min,children,similarity);
    }
    
public:
    PyramidElevationDataProvider_BufferDownloadListener(const Sector& sector,
                                                        const Vector2I& extent,
                                                        IElevationDataListener *listener,
                                                        bool autodeleteListener,
                                                        double deltaHeight):
    _sector(sector),
    _width(extent._x),
    _height(extent._y),
    _listener(listener),
    _autodeleteListener(autodeleteListener),
    _deltaHeight(deltaHeight){
        
    }
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired){
        
        ShortBufferElevationData *elevationData;
        
        std::string contents = buffer->getAsString();
        const JSONObject *jsonContent = IJSONParser::instance()->parse(contents)->asObject();
        const Vector2I *resolution = getResolution(jsonContent);
        elevationData = getElevationData(_sector, *resolution, jsonContent, _deltaHeight);
        
        if (buffer != NULL) delete buffer;
        
        if (elevationData == NULL)
        {
            _listener->onError(_sector, *resolution);
        }
        else
        {
            _listener->onData(_sector, *resolution, elevationData);
            //elevationData->_release();
        }
        
        
        if (_autodeleteListener)
        {
            if (_listener != NULL) delete _listener;
            _listener = NULL;
        }
    }
    
    void onError(const URL& url){
        const Vector2I resolution = Vector2I(_width, _height);
        
        _listener->onError(_sector, resolution);
        if (_autodeleteListener)
        {
            if (_listener != NULL) delete _listener;
            _listener = NULL;
        }
    }

    void onCancel(const URL& url){
        if (_listener != NULL)
        {
            const Vector2I resolution = Vector2I(_width, _height);
            _listener->onCancel(_sector, resolution);
            if (_autodeleteListener)
            {
                if (_listener != NULL) delete _listener;
                _listener = NULL;
            }
        }
    }

    
    void onCanceledDownload(const URL& url,
                            IByteBuffer* data,
                            bool expired){
        if (_autodeleteListener)
        {
            if (_listener != NULL) delete _listener;
            _listener = NULL;
        }
    }

    
};




PyramidElevationDataProvider::PyramidElevationDataProvider(const std::string &layer, const Sector& sector,
                                                           double deltaHeight): _sector(sector), _layer(layer){
  _pyrComposition = new std::vector<PyramidComposition>();
  _deltaHeight = deltaHeight;
}

PyramidElevationDataProvider::~PyramidElevationDataProvider(){
  _pyrComposition->clear();
  delete _pyrComposition;
  _pyrComposition = NULL;
  
}

void PyramidElevationDataProvider::getMetadata() const{
  
  _downloader->requestBuffer(URL(requestMetadataPath(),false), DownloadPriority::HIGHER, TimeInterval::fromDays(30), true, new MetadataListener(_pyrComposition), true);
}

void PyramidElevationDataProvider::initialize(const G3MContext* context ){
  _downloader = context->getDownloader();
  getMetadata();
}

const long long PyramidElevationDataProvider::requestElevationData(const Sector &sector, const Vector2I &extent, IElevationDataListener *listener, bool autodeleteListener){
  //This requester is not necessary, but we are forced to implement it, so -1.
  return -1;
}

const long long PyramidElevationDataProvider::requestElevationData(const Sector &sector, int level, int row, int column, const Vector2I &extent, IElevationDataListener *listener, bool autodeleteListener){
  
  if ((_downloader == NULL) || (aboveLevel(sector, level))){
    return -1;
  }
  
  std::string path = requestStringPath(_layer,level,row,column);
  
  return _downloader->requestBuffer(URL(path,false), DownloadPriority::HIGHEST - level, TimeInterval::fromDays(30), true, new PyramidElevationDataProvider_BufferDownloadListener(sector, extent, listener, autodeleteListener, _deltaHeight), true );
}

std::string PyramidElevationDataProvider::requestStringPath(const std::string & layer, int level, int row, int column){
  
    IStringBuilder *istr = IStringBuilder::newStringBuilder();
    istr->addString(_layer);
    istr->addInt(level);
    istr->addString("/");
    istr->addInt(column);
    istr->addString("/");
    istr->addInt(row);
    istr->addString(".json");
    std::string res = istr->getString();
    delete istr;
    return res;
}

std::string PyramidElevationDataProvider::requestMetadataPath() const{
  return _layer + "meta.json";
}

void PyramidElevationDataProvider::cancelRequest(const long long requestId){
  _downloader->cancelRequest(requestId);
}

std::vector<const Sector*> PyramidElevationDataProvider::getSectors() const{
  std::vector<const Sector*> sectors;
  sectors.push_back(&_sector);
  return sectors;
}

const Vector2I PyramidElevationDataProvider::getMinResolution() const{
  //    int WORKING_JM;
  return Vector2I::zero();
}

bool PyramidElevationDataProvider::aboveLevel(const Sector &sector, int level){
  int maxLevel = 0;
  for (unsigned int i=0; i< _pyrComposition->size(); i++)
    if (sector.touchesWith(_pyrComposition->at(i).getSector()))
      maxLevel = IMathUtils::instance()->max(maxLevel,_pyrComposition->at(i)._pyramidLevel);
  
  if (level > maxLevel) return true;
  else {
    if (!sector.touchesWith(_sector)) return true;
    return false;
  }
}



