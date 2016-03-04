//
//  PyramidElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 4/3/16.
//
//

#import "PyramidElevationDataProvider.hpp"
#include "PyramidElevationDataProvider_BufferDownloadListener.hpp"
#include "IJSONParser.hpp"
#include "DownloadPriority.hpp"
#include "JSONObject.hpp"
#include "JSONInteger.hpp"
#include "JSONDouble.hpp"
#include "JSONArray.hpp"
#include "G3MContext.hpp"
#include "TimeInterval.hpp"
#include <sstream>

class PyramidElevationDataProvider::MetadataListener : public IBufferDownloadListener {
public:
    MetadataListener(std::vector<PyramidComposition>* itself): _itself(itself) {}
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired) {
        
        const std::string str = buffer->getAsString();
        
        IJSONParser* parser = IJSONParser::instance();
        const JSONArray* array = parser->parse(str)->asObject()->getAsArray("sectors");
        
        for (unsigned int i=0; i<array->size(); i++){
            _itself->push_back(PyramidComposition(getLowerLat(array,i),getLowerLon(array,i),getUpperLat(array,i),getUpperLon(array,i),getLevel(array,i)));
        }
    }
    void onError(const URL& url) {}
    void onCancel(const URL& url) {}
    
    void onCanceledDownload(const URL& url,
                            IByteBuffer* data,
                            bool expired) {}
    
private:
    std::vector<PyramidComposition>* _itself;
    const G3MContext *_context;
    
    double getUpperLat(const JSONArray *array, int index){
        JSONDouble *doble = (JSONDouble*) array->getAsObject(index)->getAsObject("sector")->getAsObject("upper")->getAsNumber("lat");
        return doble->value();
    }
    
    double getLowerLat(const JSONArray *array, int index){
        JSONDouble *doble = (JSONDouble*) array->getAsObject(index)->getAsObject("sector")->getAsObject("lower")->getAsNumber("lat");
        return doble->value();
    }
    
    double getUpperLon(const JSONArray *array, int index){
        JSONDouble *doble = (JSONDouble*) array->getAsObject(index)->getAsObject("sector")->getAsObject("upper")->getAsNumber("lon");
        return doble->value();
    }
    
    double getLowerLon(const JSONArray *array, int index){
        JSONDouble *doble = (JSONDouble*)array->getAsObject(index)->getAsObject("sector")->getAsObject("lower")->getAsNumber("lon");
        return doble->value();
    }
    
    int getLevel(const JSONArray *array,int index){
        JSONInteger *integer = (JSONInteger *) array->getAsObject(index)->getAsNumber("pyrLevel");
        return integer->intValue();
    }
};

PyramidElevationDataProvider::PyramidElevationDataProvider(const std::string &layer, const Sector& sector,
                                                           bool isMercator,double deltaHeight): _sector(sector), _layer(layer){
    _pyrComposition = new std::vector<PyramidComposition>();
    _deltaHeight = deltaHeight;
    _isMercator = isMercator;
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

std::string PyramidElevationDataProvider::requestStringPath(const Sector &sector, const Vector2I &extent){
    // TODO: Esta en principio no debería usarse, pero ...
    ILogger::instance()->logError("BAD STRING PATH REQUESTED!");
    return "";
}

std::string PyramidElevationDataProvider::requestStringPath(const std::string & layer, int level, int row, int column){
    std::ostringstream strs;
    strs << _layer << level << "/" << column << "/" << row << ".json"; //".bil";
    std::string res = strs.str();
    //ILogger::instance()->logInfo(res);
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



