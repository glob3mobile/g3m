//
//  CompositeTileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/26/14.
//
//

#include "CompositeTileImageContribution.hpp"


const TileImageContribution* CompositeTileImageContribution::create(const std::vector<const TileImageContribution*>& contributions) {
  const int contributionsSize = contributions.size();
  if (contributionsSize == 0) {
    return TileImageContribution::none();
  }
  else if (contributionsSize == 1) {
    return contributions[0];
  }
  else {
    return new CompositeTileImageContribution(contributions);
  }
}
