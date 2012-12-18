//
//  SceneParserDownloadListener.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 26/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SceneParserDownloadListener_hpp
#define G3MiOSSDK_SceneParserDownloadListener_hpp

#include "IBufferDownloadListener.hpp"
#include "ILogger.hpp"
#include "JSONObject.hpp"

class MarksRenderer;
class GEOJSONDownloadListener : public IBufferDownloadListener {
    
    static const std::string FEATURES;
    static const std::string GEOMETRY;
    static const std::string TYPE;
    static const std::string COORDINATES;
    static const std::string PROPERTIES;
    static const std::string DENOMINATION;
    static const std::string CLASE;
    static const std::string URLICON;
    static const std::string URLWEB;

    
    MarksRenderer* _marksRenderer;
    std::string _icon;
    
public:
    GEOJSONDownloadListener(MarksRenderer* _marksRenderer, std::string icon);
    
    void onDownload(const URL& url,
                    const IByteBuffer* buffer);
    
    void onError(const URL& url){
        ILogger::instance()->logError("The requested geojson file could not be found!");    }
    
    void onCancel(const URL& url){}
    void onCanceledDownload(const URL& url,
                            const IByteBuffer* data) {}
    
    ~GEOJSONDownloadListener(){}
private:
    void parseGEOJSON(const JSONObject* jsonCustomData);
    void parsePointObject(const JSONObject* point);

};

#endif
