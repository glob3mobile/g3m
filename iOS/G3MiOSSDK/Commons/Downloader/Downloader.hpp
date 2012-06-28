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
#include "IFactory.hpp"

#include <string>
#include <vector>

class Download{
public:
  std::string _url;
  int _priority;
  std::vector<IDownloadListener *> _listeners;
  
  Download(std::string& url, int priority, IDownloadListener* listener):
  _url(url), _priority(priority)
  {
    _listeners.push_back(listener);
  }
  
  void addListener(IDownloadListener* listener){ _listeners.push_back(listener);}
};

class Storage;

class Downloader: public IDownloadListener
{
private:
  const Storage* _storage;                  //CACHE
  const unsigned int _maxSimultaneous;      //MAX NUMBER OF SIMOULTANEOUS DOWNLOADS
  INetwork * const _network;
  
  std::vector<Download> _petitions;
  unsigned int _simultaneousDownloads; 
  
  void startDownload();
  
public:
  Downloader(Storage *storage, unsigned int maxSimultaneous, INetwork * const net);
  
  void request(std::string& urlOfFile, int priority, IDownloadListener *listener);
  
  void onDownload(const Response& e);
  
  void onError(const Response& e);
  
};

#endif
