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
#include "Sector.hpp"
#include "JSONDemParser.hpp"
#include "URL.hpp"
#include "ErrorHandling.hpp"
#include "IBufferDownloadListener.hpp"

class PyramidElevationDataProvider : public ElevationDataProvider {
private:
    IDownloader * _downloader;
    const Sector _sector;
    double _deltaHeight;
    const std::string _layer;

    class PyramidComposition {
    public:
        double _upperLat, _upperLon, _lowerLat, _lowerLon;
        int _pyramidLevel;
        
        PyramidComposition(double lowerLat, double lowerLon, double upperLat, double upperLon, double pyramidLevel){
            _lowerLat = lowerLat;
            _lowerLon = lowerLon;
            _upperLat = upperLat;
            _upperLon = upperLon;
            _pyramidLevel = (int) pyramidLevel;
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
            
            std::vector<double> array = JSONDemParser::parseDemMetadata(buffer);
            
            
            if (array.size() == 0){
                THROW_EXCEPTION("Problem parsing at PyramidElevationDataProvider::MetadataListener::onDownload().");
            }
            
            for (size_t i=1; i<array[0]; i+=5){
                _itself->push_back(PyramidComposition(array[i],array[i+1],array[i+2],array[i+3],array[i+4]));
            }
        }
        void onError(const URL& url) {}
        void onCancel(const URL& url) {}
        
        void onCanceledDownload(const URL& url,
                                IByteBuffer* data,
                                bool expired) {}
        
    private:
        std::vector<PyramidComposition>* _itself;
    };
    
    std::vector<PyramidComposition> *_pyrComposition;
    int _noDataValue;
    
    bool aboveLevel(const Sector &sector, int level);
public:
    
    PyramidElevationDataProvider(const std::string &layer, const Sector& sector, int noDataValue = 15000, double deltaHeight = 0);
    
    ~PyramidElevationDataProvider();
    
    bool isReadyToRender (const G3MRenderContext *rc) {return true;}
    void getMetadata() const;
    
    void initialize(const G3MContext* context);
    
    const long long requestElevationData(const Sector& sector,
                                         const Vector2I& extent,
                                         const Tile * tile,
                                         IElevationDataListener* listener,
                                         bool autodeleteListener);
    
    std::string requestStringPath(const std::string & layer, int level, int row, int column);
    std::string requestMetadataPath() const;
    
    void cancelRequest(const long long requestId);
    std::vector<const Sector*> getSectors() const;
    
    const Vector2I getMinResolution() const;
    
};

#endif
