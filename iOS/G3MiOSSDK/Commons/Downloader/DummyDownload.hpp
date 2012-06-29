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
  
  IStorage * _fss;
  Downloader * _downloader;
  
public:
  
  DummyDownload(IFactory *fac, const std::string& root)
  {
    _fss = new FileSystemStorage(root);
    _downloader = new Downloader(_fss, 5, fac->createNetwork());
  }
  
  void run()
  {
    std::string url ="http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?Request=GetCapabilities&SERVICE=WMS";
    std::string url2 ="http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx?request=capabilities";
    
    _downloader->request(url, 10, this);
     
    _downloader->request(url, 10, this);
    
    _downloader->request(url2, 20, this);
   
    _downloader->request(".....", 15, this); //THIS SHOULD PRODUCE AN ERROR
    _downloader->request(url, 30, this);
  }
  
  void onDownload(const Response& response)
  {
    const unsigned char *data = response.getByteBuffer().getData();
    std::string resp = (char*)data;
    resp = resp.substr(0,10);
    printf("GETTING RESPONSE %s\n", resp.c_str());
  }
  
  void onError(const Response& response)
  {
    printf("GETTING ERROR IN URL: %s\n", response.getURL().getPath().c_str());
  }
  
};

#endif
