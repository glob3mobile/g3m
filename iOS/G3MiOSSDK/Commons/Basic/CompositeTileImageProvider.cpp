//
//  CompositeTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

#include "CompositeTileImageProvider.hpp"
#include "TileImageListener.hpp"
#include "Tile.hpp"
#include "CompositeTileImageContribution.hpp"

CompositeTileImageProvider::~CompositeTileImageProvider() {
  for (int i = 0; i < _childrenSize; i++) {
    TileImageProvider* child = _children[i];
    //    delete child;
    child->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const TileImageContribution* CompositeTileImageProvider::contribution(const Tile* tile) {
  std::vector<const TileImageContribution*> childrenContributions;

  for (int i = 0; i < _childrenSize; i++) {
    TileImageProvider* child = _children[i];
    const TileImageContribution* childContribution = child->contribution(tile);
    if (childContribution->isNone()) {
      TileImageContribution::deleteContribution( childContribution );
    }
    else {
      // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
      const int childrenContributionsSize = childrenContributions.size();
      if ((childrenContributionsSize > 0) &&
          childContribution->isFullCoverageAndOpaque()) {
        for (int j = 0; j < childrenContributionsSize; j++) {
          TileImageContribution::deleteContribution( childrenContributions[j] );
        }
        childrenContributions.clear();
      }

      childrenContributions.push_back( childContribution );
    }
  }

  return CompositeTileImageContribution::create(childrenContributions);
}

void CompositeTileImageProvider::create(const Tile* tile,
                                        const TileImageContribution* contribution,
                                        const Vector2I& resolution,
                                        long long tileDownloadPriority,
                                        bool logDownloadActivity,
                                        TileImageListener* listener,
                                        bool deleteListener) {
#warning Diego at work!

  const CompositeTileImageContribution* compositeContribution = (const CompositeTileImageContribution*) contribution;


  // temporary error code
  TileImageContribution::deleteContribution( contribution );
  listener->imageCreationError(tile->_id, "Not yet implemented");
  if (deleteListener) {
    delete listener;
  }
}

void CompositeTileImageProvider::cancel(const Tile* tile) {
#warning Diego at work!
}
