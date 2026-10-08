//
//  MarkBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#ifndef G3M_MarkBuilder
#define G3M_MarkBuilder

#include <vector>

#include "AltitudeMode.hpp"

class Mark;
class MarkOutfit;
class MarkAnchor;
class IImageFactory;
class Geodetic3D;
class MarkUserData;
class MarkTouchListener;


/**
 * Builds Marks step by step. What belongs to one mark (position, outfits, user
 * data, touch listener) is consumed by build(); the rest stays as the default
 * for the next marks.
 */
class MarkBuilder {
private:
  AltitudeMode _altitudeMode;
  double       _minDistanceToCamera;
  double       _maxDistanceToCamera;
  bool         _zoomInAppears;

  Geodetic3D*               _position;
  std::vector<MarkOutfit*>  _outfits;
  MarkUserData*             _userData;
  bool                      _autoDeleteUserData;
  MarkTouchListener*        _touchListener;
  bool                      _autoDeleteTouchListener;
  double                    _priority;
  MarkOutfit*               _hint;

  void clearMarkProperties();

public:
  /** defaults: ABSOLUTE, no distance limits, zoom in on appearing */
  MarkBuilder();

  ~MarkBuilder();

  void setAltitudeMode(AltitudeMode altitudeMode);

  /** the mark is hidden farther than this; 0: no limit */
  void setMinDistanceToCamera(double minDistanceToCamera);

  /** the mark is hidden nearer than this; 0: no limit */
  void setMaxDistanceToCamera(double maxDistanceToCamera);

  void setZoomInAppears(bool zoomInAppears);

  void setPosition(const Geodetic3D& position);

  /** the first outfit added is the largest; anchor NULL: the mark keeps its own anchor */
  void addOutfit(IImageFactory* imageFactory,
                 MarkAnchor*    anchor);

  void addOutfit(IImageFactory* imageFactory);

  void setUserData(MarkUserData* userData,
                   bool          autoDeleteUserData);

  void setTouchListener(MarkTouchListener* touchListener,
                        bool               autoDeleteTouchListener);

  /** the order among the marks that compete for space: the higher, the earlier; without it, the renderer's order */
  void setPriority(double priority);

  /** the mark's own hint, drawn when nothing else fits; without it, the renderer's; anchor NULL: centred */
  void setHint(IImageFactory* imageFactory,
               MarkAnchor*    anchor);

  /** needs a position and at least one outfit */
  Mark* build();

};

#endif
