//
//  Search.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 23/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Search_h
#define G3MiOSSDK_Search_h

#include "ISearchDownloadListener.hpp"
#include "URL.hpp"

class Search {
  
    std::string _url;
    ISearchDownloadListener* _searchDownloadListener;
    
public:
    
    Search(ISearchDownloadListener* searchDL, std::string url):_searchDownloadListener(searchDL),_url(url){};
    void launchSearch(const std::string searchItem)const;
    ~Search();
    
};    
    

#endif
