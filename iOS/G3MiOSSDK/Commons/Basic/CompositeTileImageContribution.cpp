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
  TileImageContribution::releaseContribution(_contribution);
}

CompositeTileImageContribution::~CompositeTileImageContribution() {
  const int contributionsSize = _contributions.size();
  for (int i = 0; i < contributionsSize; i++) {
#ifdef C_CODE
    const ChildContribution* contribution = _contributions[i];
    delete contribution;
#endif
#ifdef JAVA_CODE
    final ChildContribution contribution = _contributions.get(i);
    contribution.dispose();
#endif
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}


const TileImageContribution* CompositeTileImageContribution::create(const std::vector<const ChildContribution*>& contributions) {
  if (contributions.empty()) {
    return NULL;
  }
  
  //if first contribution is opaque, then composite is opaque.
#ifdef C_CODE
  const ChildContribution* firsChild = contributions[0];
#endif
#ifdef JAVA_CODE
  final ChildContribution firsChild = contributions.get(0);
#endif
  
  return new CompositeTileImageContribution(contributions, firsChild->_contribution->isOpaque());
}


