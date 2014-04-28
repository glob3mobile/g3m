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
  std::vector<CompositeTileImageContribution::ChildContribution*> childrenContributions;

  for (int i = 0; i < _childrenSize; i++) {
    TileImageProvider* child = _children[i];
    const TileImageContribution* childContribution = child->contribution(tile);
//    if (childContribution->isNone()) {
//      TileImageContribution::deleteContribution( childContribution );
//    }
//    else {
    if (childContribution != NULL) {
      // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
      const int childrenContributionsSize = childrenContributions.size();
      if ((childrenContributionsSize > 0) &&
          childContribution->isFullCoverageAndOpaque()) {
        for (int j = 0; j < childrenContributionsSize; j++) {
          CompositeTileImageContribution::ChildContribution* previousContribution = childrenContributions[j];
          delete previousContribution;
        }
        childrenContributions.clear();
      }

      childrenContributions.push_back( new CompositeTileImageContribution::ChildContribution(i, childContribution) );
    }
  }

  return CompositeTileImageContribution::create(childrenContributions);
}

const CompositeTileImageProvider::ChildContribution* CompositeTileImageProvider::ChildContribution::image(const IImage*                image,
                                                                                                          const std::string&           imageId,
                                                                                                          const TileImageContribution* contribution) {
  return new CompositeTileImageProvider::ChildContribution(false ,        // isError
                                                           false,         // isCanceled
                                                           image,
                                                           imageId,
                                                           contribution,
                                                           ""             // error
                                                           );
}

const CompositeTileImageProvider::ChildContribution* CompositeTileImageProvider::ChildContribution::error(const std::string& error) {
  return new CompositeTileImageProvider::ChildContribution(true,  // isError
                                                           false, // isCanceled
                                                           NULL,  // image
                                                           "",    // imageId
                                                           NULL,  // contribution
                                                           error);
}

const CompositeTileImageProvider::ChildContribution* CompositeTileImageProvider::ChildContribution::cancelation() {
  return new CompositeTileImageProvider::ChildContribution(false, // isError
                                                           true,  // isCanceled
                                                           NULL,  // image
                                                           "",    // imageId
                                                           NULL,  // contribution
                                                           ""     // error
                                                           );
}

CompositeTileImageProvider::Composer::Composer(const std::string& tileId,
                                               TileImageListener* listener,
                                               bool deleteListener,
                                               const CompositeTileImageContribution* compositeContribution) :
_tileId(tileId),
_listener(listener),
_deleteListener(deleteListener),
_compositeContribution(compositeContribution),
_contributionsSize( compositeContribution->size() ),
_stepsDone(0),
_anyError(false),
_anyCancelation(false)
{
  for (int i = 0; i < _contributionsSize; i++) {
    _contributions.push_back( NULL );
  }
}

CompositeTileImageProvider::Composer::~Composer() {
#warning Diego at work!
  for (int i = 0; i < _contributionsSize; i++) {
    const ChildContribution* contribution = _contributions[i];
    delete contribution;
  }
}

void CompositeTileImageProvider::Composer::done() {

  if (_contributionsSize == 1) {
    const ChildContribution* singleContribution = _contributions[0];

    if (singleContribution->_isError) {
      _listener->imageCreationError(_tileId,
                                    singleContribution->_error);
    }
    else if (singleContribution->_isCanceled) {
      _listener->imageCreationCanceled(_tileId);
    }
    else {
      _listener->imageCreated(singleContribution->_imageId,
                              singleContribution->_image,
                              singleContribution->_imageId,
                              singleContribution->_contribution);
    }
    if (_deleteListener) {
      delete _listener;
      _listener = NULL;
    }
  }

#warning TODODODODODODO
}

void CompositeTileImageProvider::Composer::stepDone() {
  _stepsDone++;
  if (_stepsDone == _contributionsSize) {
    done();
  }
}

void CompositeTileImageProvider::Composer::imageCreated(const std::string&           tileId,
                                                        const IImage*                image,
                                                        const std::string&           imageId,
                                                        const TileImageContribution* contribution,
                                                        int                          index) {
  if (_contributions[index] != NULL) {
    printf("Logic error 1\n");
  }

  _contributions[index] = ChildContribution::image(image, imageId, contribution);
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationError(const std::string& error,
                                                              const int          index) {
  if (_contributions[index] != NULL) {
    printf("Logic error 2\n");
  }
  _contributions[index] = ChildContribution::error(error);
  _anyError = true;
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationCanceled(const int index) {
  if (_contributions[index] != NULL) {
    printf("Logic error 3\n");
  }
  _contributions[index] = ChildContribution::cancelation();
  _anyCancelation = true;
  stepDone();
}

void CompositeTileImageProvider::ChildTileImageListener::imageCreated(const std::string&           tileId,
                                                                      const IImage*                image,
                                                                      const std::string&           imageId,
                                                                      const TileImageContribution* contribution) {
  _composer->imageCreated(tileId, image, imageId, contribution, _index);
}

void CompositeTileImageProvider::ChildTileImageListener::imageCreationError(const std::string& tileId,
                                                                            const std::string& error) {
  _composer->imageCreationError(error, _index);
}

void CompositeTileImageProvider::ChildTileImageListener::imageCreationCanceled(const std::string& tileId) {
  _composer->imageCreationCanceled(_index);
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

  const int contributionsSize = compositeContribution->size();

  Composer* composer = new Composer(tile->_id,
                                    listener,
                                    deleteListener,
                                    compositeContribution);
  for (int i = 0; i < contributionsSize; i++) {
    const CompositeTileImageContribution::ChildContribution* singleContribution = compositeContribution->get(i);

    TileImageProvider* child = _children[ singleContribution->_childIndex ];
    const TileImageContribution* contribution = singleContribution->_contribution;

    child->create(tile,
                  contribution,
                  resolution,
                  tileDownloadPriority,
                  logDownloadActivity,
                  new ChildTileImageListener(composer, i),
                  true);
  }


  /*

//  if (contributionsSize == 1) {
//    const CompositeTileImageContribution::ChildContribution* singleContribution = compositeContribution->get(0);
//
//    TileImageProvider* child = _children[ singleContribution->_childIndex ];
//    const TileImageContribution* contribution = singleContribution->_contribution;
//
//    child->create(tile,
//                  contribution,
//                  resolution,
//                  tileDownloadPriority,
//                  logDownloadActivity,
//                  listener,
//                  deleteListener);
//  }
//  else {
    // temporary error code
    TileImageContribution::deleteContribution( contribution );
    listener->imageCreationError(tile->_id, "Not yet implemented");
    if (deleteListener) {
      delete listener;
    }
//  }
   */
}

void CompositeTileImageProvider::cancel(const Tile* tile) {
#warning Diego at work!
#warning TODO cancel the (posible) single request

}
