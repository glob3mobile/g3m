//
//  CompositeTileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/26/14.
//
//

#include "CompositeTileImageContribution.hpp"


CompositeTileImageContribution::ChildContribution::ChildContribution(const int                    childIndex,
                                                                     const TileImageContribution* contribution) :
_childIndex(childIndex),
_contribution(contribution)
{
}

CompositeTileImageContribution::ChildContribution::~ChildContribution() {
  TileImageContribution::deleteContribution( _contribution );
}


const TileImageContribution* CompositeTileImageContribution::create(const std::vector<const ChildContribution*>& contributions) {
  const int contributionsSize = contributions.size();
  if (contributionsSize == 0) {
    return NULL;
  }
  return new CompositeTileImageContribution(contributions);
}
