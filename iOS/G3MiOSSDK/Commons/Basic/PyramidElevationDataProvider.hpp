//
//  PyramidElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 4/3/16.
//
//

#ifndef G3MiOSSDK_PyramidElevationDataProvider_h
#define G3MiOSSDK_PyramidElevationDataProvider_h

#include <stdio.h>
#include "ElevationDataProvider.hpp"
#include "IDownloader.hpp"
#include "IJSONParser.hpp"
#include "Sector.hpp"
#include "JSONArray.hpp"
#include "JSONObject.hpp"
#include "JSONInteger.hpp"
#include "JSONDouble.hpp"
#include "URL.hpp"
#include "ErrorHandling.hpp"
#include "IBufferDownloadListener.hpp"

class PyramidElevationDataProvider : public ElevationDataProvider {
private:
    IDownloader * _downloader;
    const Sector _sector;
    double _deltaHeight;
    bool _isMercator;
    const std::string _layer;

    class PyramidComposition {
    public:
        double _upperLat, _upperLon, _lowerLat, _lowerLon;
        int _pyramidLevel;
        
        PyramidComposition(double lowerLat, double lowerLon, double upperLat, double upperLon, const int pyramidLevel){
            _lowerLat = lowerLat;
            _lowerLon = lowerLon;
            _upperLat = upperLat;
            _upperLon = upperLon;
            _pyramidLevel = pyramidLevel;
        }
        
        std::string description(){
            //TODO: implement this better.
            return "";
            //return _sector.description() + ", pyramidLevel: "+_pyramidLevel;
        }
        
        Sector getSector() {
            return Sector::fromDegrees(_lowerLat, _lowerLon, _upperLat, _upperLon);
        }
    };
    
    class MetadataListener : public IBufferDownloadListener {
    public:
        MetadataListener(std::vector<PyramidComposition>* itself): _itself(itself) {}
        
        void onDownload(const URL& url,
                        IByteBuffer* buffer,
                        bool expired) {
            
            const std::string str = buffer->getAsString();
            
            IJSONParser* parser = IJSONParser::instance();
            const JSONArray* array = parser->parse(str)->asObject()->getAsArray("sectors");
            if (array == NULL){
                THROW_EXCEPTION("Problem parsing at PyramidElevationDataProvider::MetadataListener::onDownload().");
            }
            
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
    
    std::vector<PyramidComposition> *_pyrComposition;
    
    bool aboveLevel(const Sector &sector, int level);
public:
    
    PyramidElevationDataProvider(const std::string &layer, const Sector& sector, bool isMercator, double deltaHeight = 0);
    
    ~PyramidElevationDataProvider();
    
    bool isReadyToRender (const G3MRenderContext *rc) {return true;}
    void getMetadata() const;
    
    void initialize(const G3MContext* context);
    const long long requestElevationData(const Sector &sector, const Vector2I &extent, IElevationDataListener *listener, bool autodeleteListener);
    const long long requestElevationData(const Sector &sector, int level, int row, int column, const Vector2I &extent, IElevationDataListener *listener, bool autodeleteListener);
    
    std::string requestStringPath(const Sector &sector, const Vector2I &extent);
    std::string requestStringPath(const std::string & layer, int level, int row, int column);
    std::string requestMetadataPath() const;
    
    void cancelRequest(const long long requestId);
    std::vector<const Sector*> getSectors() const;
    const Vector2I getMinResolution() const;
    
};

#endif
