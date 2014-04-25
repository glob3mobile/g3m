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

//class CompositeTileImageProvider_ChildContribution {
//
//};

//CompositeTileImageProvider::ChildContribution(const TileImageContribution& contribution) {
//
//}

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

class CompositeTileImageContribution : public TileImageContribution {
private:
  const TileImageContribution _contribution0;
  const TileImageContribution _contribution1;
  const TileImageContribution _contribution2;
  const TileImageContribution _contribution3;

  CompositeTileImageContribution(const TileImageContribution& contribution0,
                                 const TileImageContribution& contribution1,
                                 const TileImageContribution& contribution2 = TileImageContribution::none(),
                                 const TileImageContribution& contribution3 = TileImageContribution::none()) :
  TileImageContribution(false, 1),
  _contribution0(contribution0),
  _contribution1(contribution1),
  _contribution2(contribution2),
  _contribution3(contribution3)
  {
#warning Diego at work!
  }

  static void deleteContributions(const std::vector<TileImageContribution*>& contributions) {
    const int size = contributions.size();
    for (int i = 0; i < size; i++) {
      TileImageContribution* contribution = contributions[i];
      delete contribution;
    }
  }

public:
  static TileImageContribution create(const std::vector<TileImageContribution*>& contributions);
};

TileImageContribution CompositeTileImageContribution::create(const std::vector<TileImageContribution*>& contributions) {
  const int contributionsSize = contributions.size();
  if (contributionsSize == 0) {
    return TileImageContribution::none();
  }
  else if (contributionsSize == 1) {
    TileImageContribution* contribution = contributions[0];
    TileImageContribution result = *contribution;
    delete contribution;
    return result;
  }
  if (contributionsSize == 2) {
    const TileImageContribution contribution0( *(contributions[0]) );
    const TileImageContribution contribution1( *(contributions[1]) );

    deleteContributions( contributions );

    return CompositeTileImageContribution(contribution0,
                                          contribution1);

  }
  else if (contributionsSize == 3) {
    const TileImageContribution contribution0( *(contributions[0]) );
    const TileImageContribution contribution1( *(contributions[1]) );
    const TileImageContribution contribution2( *(contributions[2]) );

    deleteContributions( contributions );

    return CompositeTileImageContribution(contribution0,
                                          contribution1,
                                          contribution2);
  }
  else {
    if (contributionsSize > 4) {
      ILogger::instance()->logWarning("Maximum 4 contributions, ignoring all but the first 4");
    }
    const TileImageContribution contribution0( *(contributions[0]) );
    const TileImageContribution contribution1( *(contributions[1]) );
    const TileImageContribution contribution2( *(contributions[2]) );
    const TileImageContribution contribution3( *(contributions[3]) );

    deleteContributions( contributions );

    return CompositeTileImageContribution(contribution0,
                                          contribution1,
                                          contribution2,
                                          contribution3);
  }
}

TileImageContribution CompositeTileImageProvider::contribution(const Tile* tile) {
  std::vector<TileImageContribution*> childrenContributions;

  for (int i = 0; i < _childrenSize; i++) {
    TileImageProvider* child = _children[i];
    TileImageContribution childContribution = child->contribution(tile);
    if (!childContribution.isNone()) {

      // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
      const int childrenContributionsSize = childrenContributions.size();
      if ((childrenContributionsSize > 0) &&
          childContribution.isFullCoverageAndOpaque()) {
        for (int j = 0; j < childrenContributionsSize; j++) {
          delete childrenContributions[j];
        }
        childrenContributions.clear();
      }

#ifdef C_CODE
      childrenContributions.push_back( new TileImageContribution(childContribution) );
#endif
#ifdef JAVA_CODE
      childrenContributions.add( childContribution );
#endif
    }
  }

  return CompositeTileImageContribution::create(childrenContributions);

//  bool hasContribution = false;
//  for (int i = 0; i < _childrenSize; i++) {
//    TileImageProvider* child = _children[i];
//    TileImageContribution childContribution = child->contribution(tile);
//    if (!childContribution.isNone()) {
//      hasContribution = true;
//      break;
//    }
//  }
//
//#warning Diego at work!
//  return hasContribution ? TileImageContribution::fullCoverageOpaque() : TileImageContribution::none();
}

void CompositeTileImageProvider::create(const Tile* tile,
                                        const TileImageContribution& contribution,
                                        const Vector2I& resolution,
                                        long long tileDownloadPriority,
                                        bool logDownloadActivity,
                                        TileImageListener* listener,
                                        bool deleteListener) {
#warning Diego at work!

  listener->imageCreationError(tile->_id, "Not yet implemented");
  if (deleteListener) {
    delete listener;
  }
}

void CompositeTileImageProvider::cancel(const Tile* tile) {
#warning Diego at work!
}
