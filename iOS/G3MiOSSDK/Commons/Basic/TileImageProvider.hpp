//
//  TileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#ifndef __G3MiOSSDK__TileImageProvider__
#define __G3MiOSSDK__TileImageProvider__

class TileImageContribution;
class Tile;
class Vector2I;
class TileImageListener;

#include "RCObject.hpp"
#include <string>

class FrameTasksExecutor;

class TileImageProvider : public RCObject {
protected:
  virtual ~TileImageProvider() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

  virtual const TileImageContribution* contribution(const Tile* tile) = 0;

  virtual void create(const Tile* tile,
                      const TileImageContribution* contribution,
                      const Vector2I& resolution,
                      long long tileDownloadPriority,
                      bool logDownloadActivity,
                      TileImageListener* listener,
                      bool deleteListener,
                      FrameTasksExecutor* frameTasksExecutor) = 0;

  virtual void cancel(const std::string& tileId) = 0;

};

#endif
