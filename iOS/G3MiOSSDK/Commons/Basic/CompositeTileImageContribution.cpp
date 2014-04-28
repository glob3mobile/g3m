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


const TileImageContribution* CompositeTileImageContribution::create(const std::vector<ChildContribution*>& contributions) {
  const int contributionsSize = contributions.size();
  if (contributionsSize == 0) {
    //return TileImageContribution::none();
    return NULL;
  }
//  else if (contributionsSize == 1) {
//    const ChildContribution* singleContribution = contributions[0];
//    const TileImageContribution* result = singleContribution->_contribution;
//    delete singleContribution;
//    return result;
//  }
  else {
    return new CompositeTileImageContribution(contributions);
  }
}
