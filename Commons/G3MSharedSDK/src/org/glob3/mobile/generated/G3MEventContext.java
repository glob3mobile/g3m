package org.glob3.mobile.generated;
//
//  G3MEventContext.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

//
//  G3MEventContext.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//



//class Camera;

public class G3MEventContext extends G3MContext
{

  public final Camera _currentCamera;

  public G3MEventContext(IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, IDownloader downloader, EffectsScheduler scheduler, IStorage storage, SurfaceElevationProvider surfaceElevationProvider, ViewMode viewMode, Camera currentCamera)
  {
     super(factory, stringUtils, threadUtils, logger, mathUtils, jsonParser, planet, downloader, scheduler, storage, surfaceElevationProvider, viewMode);
     _currentCamera = currentCamera;
  }

}