//
//  Downloader.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Downloader_h
#define G3MiOSSDK_Downloader_h


#include "IDownloadListener.hpp"
#include "INetwork.hpp"
#include "IStorage.hpp"

#include <string>
#include <vector>

struct ListenerID{
  long _id;
  IDownloadListener * _listener;
};

class Download{
  
  static long _currentID;
  
public:
  std::string             _url;
  int                     _priority;
  std::vector<ListenerID> _listeners;
  
  Download(const URL& url,
           int priority):
  _url(url.getPath()),
  _priority(priority)
  {
  }

  bool cancel(long id) {
    const URL url(_url);
    
    for (int j = 0; j < _listeners.size(); j++) {
      if (_listeners[j]._id == id){
        _listeners[j]._listener->onCancel(&url); //CANCELING OPERATION
        _listeners.erase(_listeners.begin() + j);
        return true;
      }
    }
    return false;
  }
  
  long addListener(IDownloadListener* listener) {
    ListenerID lid; 
    lid._listener = listener; 
    lid._id = _currentID++;
    _listeners.push_back(lid);
    return lid._id;
  }
  
  const URL getURL() const {
    return URL(_url);
  }
};

class Downloader: public IDownloadListener
{
private:
  IStorage* const    _storage;               //CACHE
  const unsigned int _maxSimultaneous;      //MAX NUMBER OF SIMOULTANEOUS DOWNLOADS
  INetwork * const   _network;
  
  std::vector<Download> _petitions;
  unsigned int          _simultaneousDownloads; 
  
  void startDownload();
  
public:
  Downloader(IStorage *storage, unsigned int maxSimultaneous, INetwork * const net);
  
  const ByteBuffer* getByteBufferFromCache(const URL& urlOfFile) const;
  
  long request(const URL& urlOfFile, int priority, IDownloadListener* listener);
  
  void onDownload(const Response* e);
  
  void onError(const Response* e);
  
  void onCancel(const URL* url) {}
  
  void cancelRequest(long id);
  
};

#endif
