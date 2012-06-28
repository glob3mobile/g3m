//
//  DummyDownload.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 27/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_DummyDownload_hpp
#define G3MiOSSDK_DummyDownload_hpp

#include "FileSystemStorage.hpp"
#include "Downloader.hpp"
#include "IDownloadListener.hpp"

#include "IFactory.hpp"

class DummyDownload: public IDownloadListener
{
  
  FileSystemStorage * _fss;
  Downloader * _downloader;
  
public:
  
  DummyDownload(IFactory *fac)
  {
    _fss = new FileSystemStorage();
    _downloader = new Downloader(_fss, 5, fac->createNetwork());
  }
  
  void run()
  {
    std::string url ="http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?Request=GetCapabilities&SERVICE=WMS";
    
    _downloader->request(url, 10, this);
    
    _downloader->request(url, 15, this);
    
  }
  
  void onDownload(const Response& response)
  {
    unsigned char *data = response.getData();
    
    std::string resp = (char*)data;
    
    printf("GETTING RESPONSE %s", resp.c_str());
  }
  
  void onError(const Response& response)
  {
    
  }
  
};

#endif
