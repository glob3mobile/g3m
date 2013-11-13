//
//  G3MAppInitializationTask.mm
//  G3MApp
//
//  Created by Mari Luz Mateo on 23/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MAppInitializationTask.hpp"

#import <G3MiOSSDK/G3MWidget_iOS.h>
#import <G3MiOSSDK/Context.hpp>
#import <G3MiOSSDK/TimeInterval.hpp>
#import <G3MiOSSDK/Downloader_iOS.hpp>
#import <G3MiOSSDK/IThreadUtils.hpp>
#import <G3MiOSSDK/DownloadPriority.hpp>

#import "G3MWikiDownloadListener.hpp"
#import "G3MWeatherDownloadListener.hpp"
#import "G3MPlaneParseTask.hpp"

void G3MAppInitializationTask::run(const G3MContext* context) {
  // Download Markers data
  Downloader_iOS* downloader = (Downloader_iOS*) context->getDownloader();

  // wikiMarkers data
  downloader->requestBuffer(URL("http://poiproxy.mapps.es/browseByLonLat?service=wikilocation&lon=-122.415985&lat=37.766372&dist=50000"),
                            DownloadPriority::HIGHEST,
                            TimeInterval::forever(),
                            true,
                            new G3MWikiDownloadListener(this, _widget),
                            true);

  // weatherMarkers data
  downloader->requestBuffer(URL("http://openweathermap.org/data/2.1/find/city?bbox=-180,-90,180,90,2&cluster=yes"),
                            DownloadPriority::HIGHEST,
                            TimeInterval::fromHours(1.0),
                            true,
                            new G3MWeatherDownloadListener(this, _widget),
                            true);

  // Parse 3D model
  context->getThreadUtils()->invokeInBackground(new G3MPlaneParseTask(_widget),
                                                true);
}

bool G3MAppInitializationTask::isDone(const G3MContext* context) {
  return (_wikiMarkersParsed && _weatherMarkersParsed);
}

void G3MAppInitializationTask::setWikiMarkersParsed(bool parsed) {
  _wikiMarkersParsed = parsed;
}

void G3MAppInitializationTask::setWeatherMarkersParsed(bool parsed) {
  _weatherMarkersParsed = parsed;
}
