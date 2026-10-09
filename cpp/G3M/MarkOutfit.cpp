//
//  MarkOutfit.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#include "MarkOutfit.hpp"

#include "IImageFactory.hpp"
#include "MarkAnchor.hpp"


MarkOutfit::MarkOutfit(IImageFactory* imageFactory,
                       MarkAnchor*    anchor) :
_imageFactory(imageFactory),
_anchor(anchor),
_hasTransitionMode(false),
_transitionMode(SCALE_AND_ALPHA),
_hasDetailLevel(false),
_detailLevel(0)
{
}

MarkOutfit::MarkOutfit(IImageFactory*     imageFactory,
                       MarkAnchor*        anchor,
                       MarkTransitionMode transitionMode) :
_imageFactory(imageFactory),
_anchor(anchor),
_hasTransitionMode(true),
_transitionMode(transitionMode),
_hasDetailLevel(false),
_detailLevel(0)
{
}

MarkOutfit::MarkOutfit(IImageFactory* imageFactory,
                       MarkAnchor*    anchor,
                       int            detailLevel) :
_imageFactory(imageFactory),
_anchor(anchor),
_hasTransitionMode(false),
_transitionMode(SCALE_AND_ALPHA),
_hasDetailLevel(true),
_detailLevel(detailLevel)
{
}

MarkOutfit::MarkOutfit(IImageFactory*     imageFactory,
                       MarkAnchor*        anchor,
                       int                detailLevel,
                       MarkTransitionMode transitionMode) :
_imageFactory(imageFactory),
_anchor(anchor),
_hasTransitionMode(true),
_transitionMode(transitionMode),
_hasDetailLevel(true),
_detailLevel(detailLevel)
{
}

MarkOutfit::~MarkOutfit() {
  delete _imageFactory;
  delete _anchor;
}

IImageFactory* MarkOutfit::takeImageFactory() {
  IImageFactory* imageFactory = _imageFactory;
  _imageFactory = NULL;
  return imageFactory;
}
