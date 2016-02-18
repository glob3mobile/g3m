//
//  PyramidElevationDataProvider.h
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 18/2/16.
//
//

#ifndef G3MiOSSDK_PyramidElevationDataProvider_h
#define G3MiOSSDK_PyramidElevationDataProvider_h

#include <stdio.h>
#include "ElevationDataProvider.hpp"
#include "IDownloader.hpp"
#include "Sector.hpp"
#include "URL.hpp"

class PyramidElevationDataProvider : public ElevationDataProvider {
private:
    IDownloader * _downloader;
    const Sector _sector;
    double _deltaHeight;
    bool _isMercator, _variableSized;
    const std::string _layer;
    
    class MetadataListener;
    
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
    
    std::vector<PyramidComposition> *_pyrComposition;
    
    bool aboveLevel(const Sector &sector, int level);
public:
    
    PyramidElevationDataProvider(const std::string &layer, const Sector& sector, bool isMercator, bool variableSized, double deltaHeight = 0);
    
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
