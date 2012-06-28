//
//  Downloader.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Downloader.hpp"

#include "Storage.hpp"

#include "INetwork.hpp"

Downloader::Downloader(Storage *storage, unsigned int maxSimultaneous, INetwork * const net):
_storage(storage), _maxSimultaneous(maxSimultaneous), _network(net)
{
}

void Downloader::request(std::string& urlOfFile, int priority, IDownloadListener *listener)
{
  
  for (int i = 0; i < _petitions.size(); i++)
  {
    if (urlOfFile == _petitions[i].url){ //IF WE FOUND THE SAME PETITION
      
      if (priority > _petitions[i].priority){ //MAX PRIORITY
        _petitions[i].priority = priority;
      }
      _petitions[i].listeners.push_back(listener); //NEW LISTENER
      return;
    }
  }
  
  //NEW DOWNLOAD
  Download d;
  d.url = urlOfFile;
  d.priority = priority;
  d.listeners.push_back(listener);
  _petitions.push_back(d);
  
  //When a new petition comes, we try to throw a download
  startDownload();
}

void Downloader::startDownload()
{
  if (_maxSimultaneous > _simultaneousDownloads) { //If we are able to throw more dowloads
    
    //Selecting download
    int maxPrior = -99999999;
    Download petition;
    for (int i = 0; i < _petitions.size(); i++)
    {
      if (_petitions[i].priority > maxPrior){
        maxPrior = _petitions[i].priority;
        petition = _petitions[i];
      }
    }
    
    //Downloading
    std::vector<IDownloadListener *>* dls = &petition.listeners;
    _network->request(petition.url, dls);
    
    //One more in the net
    _simultaneousDownloads++;
  }
}