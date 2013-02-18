//
//  ElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3MiOSSDK__ElevationDataProvider__
#define __G3MiOSSDK__ElevationDataProvider__

class Sector;
class Vector2I;
class ElevationData;
class G3MContext;

class IElevationDataListener {
public:
  virtual ~IElevationDataListener() {}

  virtual void onData(const Sector& sector,
                      const Vector2I& resolution,
                      ElevationData* elevationData) = 0;

  virtual void onError(const Sector& sector,
                       const Vector2I& resolution) = 0;

};


class ElevationDataProvider {
public:

  virtual ~ElevationDataProvider() {

  }

  virtual void initialize(const G3MContext* context) = 0;

  virtual const long long requestElevationData(const Sector& sector,
                                               const Vector2I& resolution,
                                               IElevationDataListener* listener,
                                               bool autodeleteListener) = 0;

  virtual void cancelRequest(const long long requestId) = 0;

};

#endif
