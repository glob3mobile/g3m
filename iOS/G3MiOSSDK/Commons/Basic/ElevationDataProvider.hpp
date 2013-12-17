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
//class Vector2I;
class ElevationData;
class G3MContext;
class G3MRenderContext;

#include <vector>
#include "Vector2I.hpp"

#include "ChangedListener.hpp"

class IElevationDataListener {
public:
#ifdef C_CODE
  virtual ~IElevationDataListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  /**
   Callback method for ElevationData creation. Pointer is owned by Listener
   */
  virtual void onData(const Sector& sector,
                      const Vector2I& extent,
                      ElevationData* elevationData) = 0;

  virtual void onError(const Sector& sector,
                       const Vector2I& extent) = 0;

  virtual void onCancel(const Sector& sector,
                       const Vector2I& extent) = 0;

};


class ElevationDataProvider {
protected:

  ChangedListener* _changedListener;

  bool _enabled;

public:

  ElevationDataProvider():_changedListener(NULL), _enabled(true) {}

  virtual ~ElevationDataProvider() {

  }

  virtual bool isReadyToRender(const G3MRenderContext* rc) = 0;

  virtual void initialize(const G3MContext* context) = 0;

  virtual const long long requestElevationData(const Sector& sector,
                                               const Vector2I& extent,
                                               IElevationDataListener* listener,
                                               bool autodeleteListener) = 0;

  virtual void cancelRequest(const long long requestId) = 0;

  virtual std::vector<const Sector*> getSectors() const = 0;

  virtual const Vector2I getMinResolution() const = 0;

  void setChangedListener(ChangedListener* changedListener) {
    _changedListener = changedListener;
  }

  void onChanged() const {
    if (_changedListener != NULL) {
      _changedListener->changed();
    }
  }

  void setEnabled(bool enabled) {
    if (_enabled != enabled) {
      _enabled = enabled;
      onChanged();
    }
  }

  bool isEnabled() const{
    return _enabled;
  }
  
};

#endif
