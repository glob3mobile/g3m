//
//  MarkBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#include "MarkBuilder.hpp"

#include "Mark.hpp"
#include "MarkOutfit.hpp"
#include "MarkTouchListener.hpp"
#include "Geodetic3D.hpp"
#include "ErrorHandling.hpp"
#include "IMathUtils.hpp"
#include "FixedMarkAnchor.hpp"


MarkBuilder::MarkBuilder() :
_altitudeMode(ABSOLUTE),
_minDistanceToCamera(0),
_maxDistanceToCamera(0),
_zoomInAppears(true),
_position(NULL),
_userData(NULL),
_autoDeleteUserData(false),
_touchListener(NULL),
_autoDeleteTouchListener(false),
_priority(NAND),
_hint(NULL)
{
}

MarkBuilder::~MarkBuilder() {
  delete _position;
  delete _hint;

  for (size_t i = 0; i < _outfits.size(); i++) {
    delete _outfits[i];
  }

  if (_autoDeleteUserData) {
    delete _userData;
  }
  if (_autoDeleteTouchListener) {
    delete _touchListener;
  }
}

void MarkBuilder::setAltitudeMode(AltitudeMode altitudeMode) {
  _altitudeMode = altitudeMode;
}

void MarkBuilder::setMinDistanceToCamera(double minDistanceToCamera) {
  _minDistanceToCamera = minDistanceToCamera;
}

void MarkBuilder::setMaxDistanceToCamera(double maxDistanceToCamera) {
  _maxDistanceToCamera = maxDistanceToCamera;
}

void MarkBuilder::setZoomInAppears(bool zoomInAppears) {
  _zoomInAppears = zoomInAppears;
}

void MarkBuilder::setPosition(const Geodetic3D& position) {
  delete _position;
  _position = new Geodetic3D(position);
}

void MarkBuilder::addOutfit(IImageFactory* imageFactory,
                            MarkAnchor*    anchor) {
  _outfits.push_back(new MarkOutfit(imageFactory, anchor));
}

void MarkBuilder::addOutfit(IImageFactory*     imageFactory,
                            MarkAnchor*        anchor,
                            MarkTransitionMode transitionMode) {
  _outfits.push_back(new MarkOutfit(imageFactory, anchor, transitionMode));
}

void MarkBuilder::addOutfit(IImageFactory* imageFactory,
                            MarkAnchor*    anchor,
                            int            detailLevel) {
  _outfits.push_back(new MarkOutfit(imageFactory, anchor, detailLevel));
}

void MarkBuilder::addOutfit(IImageFactory*     imageFactory,
                            MarkAnchor*        anchor,
                            int                detailLevel,
                            MarkTransitionMode transitionMode) {
  _outfits.push_back(new MarkOutfit(imageFactory, anchor, detailLevel, transitionMode));
}

void MarkBuilder::addOutfit(IImageFactory* imageFactory) {
  addOutfit(imageFactory, NULL);
}

void MarkBuilder::setUserData(MarkUserData* userData,
                              bool          autoDeleteUserData) {
  if (_autoDeleteUserData) {
    delete _userData;
  }
  _userData           = userData;
  _autoDeleteUserData = autoDeleteUserData;
}

void MarkBuilder::setTouchListener(MarkTouchListener* touchListener,
                                   bool               autoDeleteTouchListener) {
  if (_autoDeleteTouchListener) {
    delete _touchListener;
  }
  _touchListener           = touchListener;
  _autoDeleteTouchListener = autoDeleteTouchListener;
}

void MarkBuilder::setPriority(double priority) {
  _priority = priority;
}

void MarkBuilder::setHint(IImageFactory* imageFactory,
                          MarkAnchor*    anchor) {
  delete _hint;
  _hint = new MarkOutfit(imageFactory, anchor);
}

void MarkBuilder::setHint(IImageFactory* imageFactory) {
  setHint(imageFactory, new FixedMarkAnchor(0.5f, 0.5f));
}

// what belongs to the mark just built goes with it
void MarkBuilder::clearMarkProperties() {
  delete _position;
  _position = NULL;

  _outfits.clear();

  _userData           = NULL;
  _autoDeleteUserData = false;

  _touchListener           = NULL;
  _autoDeleteTouchListener = false;

  _priority = NAND;

  _hint = NULL;
}

Mark* MarkBuilder::build() {
  if (_position == NULL) {
    THROW_EXCEPTION("MarkBuilder: the mark has no position");
  }
  if (_outfits.empty()) {
    THROW_EXCEPTION("MarkBuilder: the mark has no outfit");
  }

  Mark* mark = new Mark(_outfits,
                        *_position,
                        _altitudeMode,
                        _minDistanceToCamera,
                        _maxDistanceToCamera,
                        _userData,
                        _autoDeleteUserData,
                        _touchListener,
                        _autoDeleteTouchListener,
                        _zoomInAppears);
  mark->setPriority(_priority);
  if (_hint != NULL) {
    mark->addHint(_hint);
  }

  clearMarkProperties();

  return mark;
}
