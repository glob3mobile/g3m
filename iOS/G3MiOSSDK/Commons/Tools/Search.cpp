//
//  Search.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 23/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "Search.h"
#include "IDownloader.hpp"


void Search::launchSearch(const std::string searchItem)const{
    IDownloader::instance()->requestBuffer(URL(_url, false), 100000000L, _searchDownloadListener, true);
}
