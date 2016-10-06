//
//  BILDownloader.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/16.
//
//

#ifndef BILDownloader_hpp
#define BILDownloader_hpp

class G3MContext;
class URL;
class Sector;
class Vector2I;
class TimeInterval;
class ShortBufferTerrainElevationGrid;


class BILDownloader {
private:
  BILDownloader() {}

public:

  class Handler {
  public:
    virtual ~Handler() {
    }

    virtual void onDownloadError(const URL& url) = 0;

    virtual void onParseError() = 0;

    virtual void onBIL(ShortBufferTerrainElevationGrid* result) = 0;
  };

  static void request(const G3MContext*       context,
                      const URL&              url,
                      long long               priority,
                      const TimeInterval&     timeToCache,
                      bool                    readExpired,
                      const Sector&           sector,
                      const Vector2I&         extent,
                      const double            deltaHeight,
                      const short             noDataValue,
                      BILDownloader::Handler* handler,
                      const bool              deleteHandler);


};

#endif
