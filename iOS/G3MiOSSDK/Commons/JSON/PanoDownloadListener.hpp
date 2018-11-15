//
//  PanoDownloadListener.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 7/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_PanoDownloadListener_hpp
#define G3MiOSSDK_PanoDownloadListener_hpp

#import "IBufferDownloadListener.hpp"
#import "Mark.hpp"

class JSONObject;


class PanoMarkUserData : public MarkUserData {
private:
  const std::string _name;
#ifdef C_CODE
  const URL* _url;
#endif
#ifdef JAVA_CODE
  private URL _url;
#endif
  
public:
  PanoMarkUserData(const std::string& name,
                   const URL* url):
  _name(name),
  _url(url) {
  }

  ~PanoMarkUserData() {
    delete _url;
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  const std::string getName(){
    return _name;
  }
  
  const URL* getUrl() {
    return _url;
  }
  
};


class PanoDownloadListener : public IBufferDownloadListener {
private:
  MarksRenderer* _panoMarksRenderer;
  const std::string _urlIcon;

  void parseMETADATA(const std::string& url,
                     const JSONObject* json);

public:
  PanoDownloadListener(MarksRenderer* panoMarksRenderer,
                       const std::string& urlIcon);

  ~PanoDownloadListener() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired);

  void onError(const URL& url){
    ILogger::instance()->logError("The requested pano could not be found! ->"+url.description());
  }

  void onCancel(const URL& url) {
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* data,
                          bool expired) {
  }

};

#endif
