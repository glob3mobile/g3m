//
//  ISearchDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 23/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ISearchDownloaderListener_hpp
#define G3MiOSSDK_ISearchDownloaderListener_hpp

#include <iostream>

#include "IBufferDownloadListener.hpp"
#include "JSONArray.hpp"

class ISearchDownloadListener : public IBufferDownloadListener {
    
    void* _unknownObject;
    
public:

    ISearchDownloadListener(void* unknownObject):_unknownObject(unknownObject){
        
    }
    
    virtual void onDownload(const URL& url,
                    const IByteBuffer* buffer) = 0;
    
    virtual void onError(const URL& url) = 0;
    
    virtual void onCancel(const URL& url) = 0;
    
    virtual void onCanceledDownload(const URL& url,
                                    const IByteBuffer* data) = 0;
    
    virtual void updateResults(JSONArray json) = 0;
    
    virtual ~ISearchDownloadListener(){}
    
};

#endif

