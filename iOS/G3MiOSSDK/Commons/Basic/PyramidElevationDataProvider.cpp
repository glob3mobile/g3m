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
    
    const Sector *_sector;
    int _width, _height;
    IElevationDataListener *_listener;
    bool _autodeleteListener;
    double _deltaHeight;
    short _noDataValue;
    
public:
    PyramidElevationDataProvider_BufferDownloadListener(const Sector* sector,
                                                        const Vector2I& extent,
                                                        IElevationDataListener *listener,
                                                        bool autodeleteListener,
                                                        short noDataValue,
                                                        double deltaHeight):
    _sector(sector),
    _width(extent._x),
    _height(extent._y),
    _listener(listener),
    _autodeleteListener(autodeleteListener),
    _deltaHeight(deltaHeight),
    _noDataValue(noDataValue){
        
    }
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired){
        
        ShortBufferElevationData *elevationData;
        std::string contents = buffer->getAsString();
        const Vector2I *resolution = JSONDemParser::getResolution(buffer);
        
#warning refactor 15000
        elevationData = JSONDemParser::parseJSONDemElevationData(*_sector, *resolution, buffer,_noDataValue, _deltaHeight);
        
        if (buffer != NULL){
            delete buffer;
        }
        
        if (elevationData == NULL)
        {
            _listener->onError(*_sector, *resolution);
        }
        else
        {
            _listener->onData(*_sector, *resolution, elevationData);
        }
        
        
        if (_autodeleteListener)
        {
            if (_listener != NULL){
               delete _listener;
            }
            _listener = NULL;
        }
        delete resolution;
    }
    
    void onError(const URL& url){
        const Vector2I resolution = Vector2I(_width, _height);
        
        _listener->onError(*_sector, resolution);
        if (_autodeleteListener)
        {
            if (_listener != NULL){
              delete _listener;
            }
            _listener = NULL;
        }
    }

    void onCancel(const URL& url){
        if (_listener != NULL)
        {
            const Vector2I resolution = Vector2I(_width, _height);
            _listener->onCancel(*_sector, resolution);
            if (_autodeleteListener)
            {
                if (_listener != NULL) {
                  delete _listener;
                }
                _listener = NULL;
            }
        }
    }

    
    void onCanceledDownload(const URL& url,
                            IByteBuffer* data,
                            bool expired){
        if (_autodeleteListener)
        {
            if (_listener != NULL){
              delete _listener;
            }
            _listener = NULL;
        }
    }

    ~PyramidElevationDataProvider_BufferDownloadListener(){
        delete _sector;
        
#ifdef JAVA_CODE
        super.dispose();
#endif
    }
    
};




PyramidElevationDataProvider::PyramidElevationDataProvider(const std::string &layer, const Sector& sector,
                                                           short noDataValue,
                                                           double deltaHeight): _sector(sector), _layer(layer), _noDataValue(noDataValue){
  _pyrComposition = new std::vector<PyramidComposition>();
  _deltaHeight = deltaHeight;
}

PyramidElevationDataProvider::~PyramidElevationDataProvider(){
  _pyrComposition->clear();
  delete _pyrComposition;
  _pyrComposition = NULL;
    
#ifdef JAVA_CODE
    super.dispose();
#endif
  
}

void PyramidElevationDataProvider::getMetadata() const{
  
  _downloader->requestBuffer(URL(requestMetadataPath(),false), DownloadPriority::HIGHER, TimeInterval::fromDays(30), true, new MetadataListener(_pyrComposition), true);
}

void PyramidElevationDataProvider::initialize(const G3MContext* context ){
  _downloader = context->getDownloader();
  getMetadata();
}

const long long PyramidElevationDataProvider::requestElevationData(const Sector& sector,
                                             const Vector2I& extent,
                                             const Tile * tile,
                                             IElevationDataListener* listener,
                                             bool autodeleteListener){
    const int level = tile->_level;
    const int row = tile->_row;
    const int column = tile->_column;

    if ((_downloader == NULL) || (aboveLevel(sector, level))){
        return -1;
    }
    
    Sector * sectorCopy = new Sector(sector);
    std::string path = requestStringPath(_layer,level,row,column);
    
    return _downloader->requestBuffer(URL(path,false), DownloadPriority::HIGHEST - level, TimeInterval::fromDays(30), true, new PyramidElevationDataProvider_BufferDownloadListener(sectorCopy, extent, listener, autodeleteListener,_noDataValue, _deltaHeight), true );
    
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

bool PyramidElevationDataProvider::aboveLevel(const Sector &sector, int level){
  int maxLevel = 0;
  for (size_t i=0; i< _pyrComposition->size(); i++) {
    if (sector.touchesWith(_pyrComposition->at(i).getSector())) {
      maxLevel = IMathUtils::instance()->max(maxLevel,_pyrComposition->at(i)._pyramidLevel);
    }
  }

  return ((level > maxLevel) || (!sector.touchesWith(_sector)));
}

const Vector2I PyramidElevationDataProvider::getMinResolution() const {
#warning En apariencia, es forzoso implementar esta función. Solo la necesita realmente el popBestProvider de Composite.
    return Vector2I::zero();
}



