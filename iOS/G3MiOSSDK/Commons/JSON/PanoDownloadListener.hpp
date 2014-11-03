//
//  PanoDownloadListener.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 7/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_PanoDownloadListener_hpp
#define G3MiOSSDK_PanoDownloadListener_hpp

#include "IBufferDownloadListener.hpp"
#include "ILogger.hpp"
#include "JSONObject.hpp"
#include "MarkTouchListener.hpp"
#include "Mark.hpp"

class PanoMarkUserData : public MarkUserData {
  const std::string _name;
  
#ifdef C_CODE
  const URL* _url;
#endif
#ifdef JAVA_CODE
  private URL _url;
#endif
  
public:
  
  PanoMarkUserData(const std::string name, const URL* url):
  _name(name),
  _url(url){};
  
  const std::string getName(){
    return _name;
  }
  
  const URL* getUrl(){
    return _url;
  }
  
  ~PanoMarkUserData() {
    delete _url;
  }
};

class MarksRenderer;
class PanoDownloadListener : public IBufferDownloadListener {
    
    static const std::string NAME;
    static const std::string POSITION;
    static const std::string LAT;
    static const std::string LON;
    
    MarksRenderer* _marksRenderer;
    MarkTouchListener* _panoTouchListener;
    std::string _urlIcon;
  
public:
    PanoDownloadListener(MarksRenderer* _marksRenderer, MarkTouchListener* panoTouchListener, std::string urlIcon);
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);
    
    void onError(const URL& url){
        ILogger::instance()->logError("The requested pano could not be found! ->"+url.description());
    }
    
    void onCancel(const URL& url){}
    void onCanceledDownload(const URL& url,
                            IByteBuffer* data,
                            bool expired) {}
    
    ~PanoDownloadListener(){}
private:
    void parseMETADATA(std::string url, const JSONObject* jsonCustomData);
};

#endif
