//
//  BILDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/16.
//
//

#include "BILDownloader.hpp"

#include "G3MContext.hpp"
#include "IDownloader.hpp"


void BILDownloader::request(const G3MContext*       context,
                            const URL&              url,
                            long long               priority,
                            const TimeInterval&     timeToCache,
                            bool                    readExpired,
                            const Sector&           sector,
                            const Vector2I&         extent,
                            const double            deltaHeight,
                            const short             noDataValue,
                            BILDownloader::Handler* handler,
                            const bool              deleteHandler) {

//  context->getDownloader()->requestBuffer(url,
//                                          priority,
//                                          timeToCache,
//                                          readExpired,
//                                          <#IBufferDownloadListener *listener#>,
//                                          <#bool deleteListener#>);
#error Diego at work!
}
