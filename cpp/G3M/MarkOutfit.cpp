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
_anchor(anchor)
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
