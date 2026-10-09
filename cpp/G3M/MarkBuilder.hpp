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
#include "MarkTransitionMode.hpp"

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

  /** in order of preference: a decluttering renderer gives the mark the first one that fits; anchor NULL: the mark keeps its own anchor */
  void addOutfit(IImageFactory* imageFactory,
                 MarkAnchor*    anchor);

  /** an outfit with its own way of coming in and going away, instead of the renderer's */
  void addOutfit(IImageFactory*     imageFactory,
                 MarkAnchor*        anchor,
                 MarkTransitionMode transitionMode);

  /** detailLevel: outfits with the same level are alternatives (the label on one side or the other), a higher one shows more; without it, each outfit is a level of its own, from the first down */
  void addOutfit(IImageFactory* imageFactory,
                 MarkAnchor*    anchor,
                 int            detailLevel);

  void addOutfit(IImageFactory*     imageFactory,
                 MarkAnchor*        anchor,
                 int                detailLevel,
                 MarkTransitionMode transitionMode);

  void addOutfit(IImageFactory* imageFactory);

  void setUserData(MarkUserData* userData,
                   bool          autoDeleteUserData);

  void setTouchListener(MarkTouchListener* touchListener,
                        bool               autoDeleteTouchListener);

  /** the order among the marks that compete for space: the higher, the earlier; without it, the renderer's order */
  void setPriority(double priority);

  /** the mark's own hint, drawn when nothing else fits; without it, the renderer's; anchor NULL: the mark keeps its own anchor, as in addOutfit */
  void setHint(IImageFactory* imageFactory,
               MarkAnchor*    anchor);

  /** a hint centred on the position */
  void setHint(IImageFactory* imageFactory);

  /** needs a position and at least one outfit */
  Mark* build();

};

#endif
