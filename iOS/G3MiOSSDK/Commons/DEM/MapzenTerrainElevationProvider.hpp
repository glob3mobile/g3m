//
//  MapzenTerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#ifndef MapzenTerrainElevationProvider_hpp
#define MapzenTerrainElevationProvider_hpp

#include "TerrainElevationProvider.hpp"

#include "TimeInterval.hpp"
#include "DownloadPriority.hpp"
#include <string>


class MapzenTerrainElevationProvider : public TerrainElevationProvider {
private:
  static int _idCounter;

  const std::string _apiKey;

  const long long    _downloadPriority;
#ifdef C_CODE
  const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
  private final TimeInterval _timeToCache;
#endif
  const bool         _readExpired;

  const std::string _instanceID;

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

protected:
  ~MapzenTerrainElevationProvider();

public:

  MapzenTerrainElevationProvider(const std::string&  apiKey,
                                 long long           downloadPriority,
                                 const TimeInterval& timeToCache,
                                 bool                readExpired);

  RenderState getRenderState();

  void initialize(const G3MContext* context);

  void cancel();
  
};

#endif
