//
//  Downloader.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Downloader.hpp"

#include "IStorage.hpp"

#include "INetwork.hpp"

long Download::_currentID = 0;

Downloader::Downloader(IStorage *storage, unsigned int maxSimultaneous, INetwork * const net):
_storage(storage), _maxSimultaneous(maxSimultaneous), _network(net), _simultaneousDownloads(0)
{
}

ByteBuffer* Downloader::getByteBufferFromCache(const std::string& urlOfFile) const
{
  return _storage->read(urlOfFile);
}

long Downloader::request(const URL& url, int priority, IDownloadListener* listener)
{
  std::string urlOfFile = url.getPath();
  
  //First we check in storage
  if (_storage->contains(urlOfFile)){
    ByteBuffer *bb = _storage->read(urlOfFile);
    Response r(url , bb);
    if (listener != NULL){
      listener->onDownload(r); 
    }
    delete bb;
    return -1;
  }
  
  long currentID = -1;
  //We look for repeated petitions
  for (int i = 0; i < _petitions.size(); i++)
  {
    if (urlOfFile == _petitions[i]._url){ //IF WE FOUND THE SAME PETITION
      if (priority > _petitions[i]._priority){ //MAX PRIORITY
        _petitions[i]._priority = priority;
      }
      currentID = _petitions[i].addListener(listener); //NEW LISTENER
    }
  }
  
  if (currentID == -1){
    //NEW DOWNLOAD
    Download d(urlOfFile, priority);
    currentID = d.addListener(listener);
    _petitions.push_back(d);
  }
  
  //When a new petition comes, we try to throw a download
  startDownload();
  
  return currentID;
}

void Downloader::startDownload()
{
  if (_maxSimultaneous > _simultaneousDownloads) { //If we are able to throw more dowloads
    
    //Selecting download
    int maxPrior = -99999999;
    int downloadIndex = -1;
    for (int i = 0; i < _petitions.size(); i++)
    {
      if (_petitions[i]._priority > maxPrior){
        maxPrior = _petitions[i]._priority;
        downloadIndex = i;
      }
    }
    if (downloadIndex < 0) return;
    
    //Downloading
    _network->request(_petitions[downloadIndex]._url, this);
    
    //One more in the net
    _simultaneousDownloads++;
  }
}

void Downloader::onDownload(const Response& e)
{
  //Saving on storage
#ifdef C_CODE
  _storage->save(e.getURL().getPath(), *e.getByteBuffer());
#endif
#ifdef JAVA_CODE
    _storage.save(e.getURL().getPath(), e.getByteBuffer());
#endif
  
  for (int i = 0; i < _petitions.size(); i++)
  {
    if (_petitions[i]._url == e.getURL().getPath()) //RECEIVED RESPONSE
    {
      Download& pet = _petitions[i];
      for (int j = 0; j < pet._listeners.size(); j++) {
        IDownloadListener *dl = pet._listeners[j]._listener;
        if (dl != NULL){
          dl->onDownload(e);
        }
      }
#ifdef C_CODE
      _petitions.erase(_petitions.begin() + i);
#endif
#ifdef JAVA_CODE
      _petitions.remove(i);
#endif
      
      break;
    }
  }
  
  //One less in the net
  _simultaneousDownloads--;
  startDownload();          //CHECK IF WE CAN THROW A NEW PETITION TO THE NET
}

void Downloader::onError(const Response& e)
{
  for (int i = 0; i < _petitions.size(); i++)
  {
    if (_petitions[i]._url == e.getURL().getPath()) //RECEIVED RESPONSE
    {
      Download& pet = _petitions[i];
      for (int j = 0; j < pet._listeners.size(); j++) {
        IDownloadListener *dl = pet._listeners[j]._listener;
        if (dl != NULL){
          dl->onError(e);
        }
      }
#ifdef C_CODE
      _petitions.erase(_petitions.begin() + i);
#endif
#ifdef JAVA_CODE
      _petitions.remove(i);
#endif
      return;
    }
  }
  
  //One less in the net
  _simultaneousDownloads--;
  startDownload();          //CHECK IF WE CAN THROW A NEW PETITION TO THE NET
}

void Downloader::cancelRequest(long id)
{
  for (int i = 0; i < _petitions.size(); i++)
  {
    if (_petitions[i].cancel(id)){
      if (_petitions[i]._listeners.size() < 1){
        _petitions.erase(_petitions.begin() + i);
      }
      break;
    }
  }
}