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

struct Download{
  std::string url;
  int priority;
  std::vector<IDownloadListener *> listeners;
};

class Storage;

class Downloader
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
  
};

#endif
