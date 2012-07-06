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
#include "SQLiteStorage_iOS.hpp"
#include "Downloader.hpp"
#include "IDownloadListener.hpp"

#include "IFactory.hpp"


#include <fstream>

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
    
    DummyDownload(IFactory *fac, const std::string database, const std::string table)
    {
        _fss = new SQLiteStorage_iOS(database, table);
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
    
    void runSqlite(std::string root, std::string filename)
    {
        printf("\nFileName: %s;", (root+filename).c_str());
        NSString *documentsDirectory = [NSString stringWithCString:root.c_str() 
                                                    encoding:[NSString defaultCStringEncoding]];

        FileSystemStorage * fssAux = new FileSystemStorage([documentsDirectory cStringUsingEncoding:NSUTF8StringEncoding]);
  
        
        
        if (_fss->contains(filename.c_str())){
            ByteBuffer bb = _fss->getByteBuffer(filename.c_str());
            std::string resp = (char*)bb.getData();
            printf("\nFileName: %s;\nData: %s;\nDataLength:%i;\n\n",(root+filename).c_str(), resp.c_str(), bb.getDataLength());
            fssAux->save(("_(1)" + filename).c_str(), bb);
            //WE MUST DELETE THE BYTE BUFFER WE HAVE CREATED
            delete [] bb.getData();
        }else{  
            ByteBuffer bb = fssAux->getByteBuffer(filename);
            if(bb.getData() != NULL){
                fssAux->save(("_withoutsaveinsqlite_" + filename).c_str(), bb);
                _fss->save(filename, bb); 
            }
            delete [] bb.getData();
        }

        delete fssAux;
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
