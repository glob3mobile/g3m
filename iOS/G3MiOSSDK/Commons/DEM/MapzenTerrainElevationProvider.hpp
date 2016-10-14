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


class MapzenTerrainElevationProvider : public TerrainElevationProvider {
private:
  const long long    _downloadPriority;
  const TimeInterval _timeToCache;
  const bool         _readExpired;

protected:
  ~MapzenTerrainElevationProvider();

public:

  static MapzenTerrainElevationProvider* createDefault(long long           downloadPriority = DownloadPriority::HIGHER,
                                                       const TimeInterval& timeToCache      = TimeInterval::fromDays(30),
                                                       bool                readExpired      = true);

  MapzenTerrainElevationProvider(long long           downloadPriority,
                                 const TimeInterval& timeToCache,
                                 bool                readExpired);

  RenderState getRenderState();

  void initialize(const G3MContext* context);

  void cancel();

};

#endif
