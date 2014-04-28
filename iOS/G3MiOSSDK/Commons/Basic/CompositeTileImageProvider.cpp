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
#include "IFactory.hpp"
#include "ICanvas.hpp"

CompositeTileImageProvider::~CompositeTileImageProvider() {
  for (int i = 0; i < _childrenSize; i++) {
    TileImageProvider* child = _children[i];
    child->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const TileImageContribution* CompositeTileImageProvider::contribution(const Tile* tile) {
  std::vector<const CompositeTileImageContribution::ChildContribution*> childrenContributions;

  for (int i = 0; i < _childrenSize; i++) {
    TileImageProvider* child = _children[i];
    const TileImageContribution* childContribution = child->contribution(tile);
    if (childContribution != NULL) {
      // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
      const int childrenContributionsSize = childrenContributions.size();
      if ((childrenContributionsSize > 0) &&
          childContribution->isFullCoverageAndOpaque()) {
        for (int j = 0; j < childrenContributionsSize; j++) {
          const CompositeTileImageContribution::ChildContribution* previousContribution = childrenContributions[j];
#ifdef C_CODE
          delete previousContribution;
#endif
#ifdef JAVA_CODE
          previousContribution.dispose();
#endif
        }
        childrenContributions.clear();
      }

      childrenContributions.push_back( new CompositeTileImageContribution::ChildContribution(i, childContribution) );
    }
  }

  return CompositeTileImageContribution::create(childrenContributions);
}

const CompositeTileImageProvider::ChildResult* CompositeTileImageProvider::ChildResult::image(const IImage*                image,
                                                                                              const std::string&           imageId,
                                                                                              const TileImageContribution* contribution) {
  return new CompositeTileImageProvider::ChildResult(false ,        // isError
                                                     false,         // isCanceled
                                                     image,
                                                     imageId,
                                                     contribution,
                                                     ""             // error
                                                     );
}

const CompositeTileImageProvider::ChildResult* CompositeTileImageProvider::ChildResult::error(const std::string& error) {
  return new CompositeTileImageProvider::ChildResult(true,  // isError
                                                     false, // isCanceled
                                                     NULL,  // image
                                                     "",    // imageId
                                                     NULL,  // contribution
                                                     error);
}

const CompositeTileImageProvider::ChildResult* CompositeTileImageProvider::ChildResult::cancelation() {
  return new CompositeTileImageProvider::ChildResult(false, // isError
                                                     true,  // isCanceled
                                                     NULL,  // image
                                                     "",    // imageId
                                                     NULL,  // contribution
                                                     ""     // error
                                                     );
}

CompositeTileImageProvider::Composer::Composer(int width,
                                               int height,
                                               CompositeTileImageProvider* compositeTileImageProvider,
                                               const std::string& tileId,
                                               TileImageListener* listener,
                                               bool deleteListener,
                                               const CompositeTileImageContribution* compositeContribution) :
_width(width),
_height(height),
_compositeTileImageProvider(compositeTileImageProvider),
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
    _results.push_back( NULL );
  }
}

CompositeTileImageProvider::Composer::~Composer() {
#warning Diego at work!
  for (int i = 0; i < _contributionsSize; i++) {
    const ChildResult* result = _results[i];
    delete result;
  }

  delete _compositeContribution;
}

void CompositeTileImageProvider::Composer::cleanUp() {
  if (_deleteListener) {
    delete _listener;
    _listener = NULL;
  }

  _compositeTileImageProvider->composerDone(this);
}

void CompositeTileImageProvider::Composer::done() {

  if (_contributionsSize == 1) {
    const ChildResult* singleResult = _results[0];

    if (singleResult->_isError) {
      _listener->imageCreationError(_tileId,
                                    singleResult->_error);
    }
    else if (singleResult->_isCanceled) {
      _listener->imageCreationCanceled(_tileId);
    }
    else {
      _listener->imageCreated(singleResult->_imageId,
                              singleResult->_image,
                              singleResult->_imageId,
                              singleResult->_contribution);
    }
    cleanUp();
  }
  else {
    if (_anyError) {
      std::string composedError = "";
      for (int i = 0; i < _contributionsSize; i++) {
        const ChildResult* childResult = _results[i];
        if (childResult->_isError) {
          composedError += childResult->_error + " ";
        }
      }

      _listener->imageCreationError(_tileId,
                                    composedError);

      cleanUp();
    }
    else if (_anyCancelation) {
      _listener->imageCreationCanceled(_tileId);
      cleanUp();
    }
    else {
      ICanvas* canvas = IFactory::instance()->createCanvas();

      canvas->initialize(_width, _height);

      std::string imageId = "";

      for (int i = 0; i < _contributionsSize; i++) {
       const ChildResult* result = _results[i];

        imageId += result->_imageId + "|";
        canvas->drawImage(result->_image, 0, 0);
      }
      _imageId = imageId;

      canvas->createImage(this, false);

      delete canvas;
    }
  }

#warning TODODODODODODO
}

void CompositeTileImageProvider::Composer::imageCreated(const IImage* image) {
  _listener->imageCreated(_tileId,
                          image,
                          _imageId,
                          _compositeContribution);
  _compositeContribution = NULL;
  cleanUp();
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
                                                        const int                    index) {
  if (_results[index] != NULL) {
    printf("Logic error 1\n");
  }

  _results[index] = ChildResult::image(image, imageId, contribution);
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationError(const std::string& error,
                                                              const int          index) {
  if (_results[index] != NULL) {
    printf("Logic error 2\n");
  }
  _results[index] = ChildResult::error(error);
  _anyError = true;
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationCanceled(const int index) {
  if (_results[index] != NULL) {
    printf("Logic error 3\n");
  }
  _results[index] = ChildResult::cancelation();
  _anyCancelation = true;
  stepDone();
}

void CompositeTileImageProvider::Composer::cancel() {
#warning TODO cancel children
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

  const std::string tileId = tile->_id;

  Composer* composer = new Composer(resolution._x,
                                    resolution._y,
                                    this,
                                    tileId,
                                    listener,
                                    deleteListener,
                                    compositeContribution);

  _composers[ tileId ] = composer;

  const int contributionsSize = compositeContribution->size();
  for (int i = 0; i < contributionsSize; i++) {
    const CompositeTileImageContribution::ChildContribution* childContribution = compositeContribution->get(i);

    TileImageProvider* child = _children[ childContribution->_childIndex ];

    child->create(tile,
                  childContribution->_contribution,
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
  const std::string tileId = tile->_id;
#ifdef C_CODE
  if (_composers.find(tileId) != _composers.end()) {
    Composer* composer = _composers[tileId];

    composer->cancel();

    _composers.erase(tileId);
  }
#endif
#ifdef JAVA_CODE
  final Composer composer = _composers.remove(tileId);
  if (composer != null) {
    composer.cancel();
  }
#endif
}

void CompositeTileImageProvider::composerDone(Composer* composer) {
  const std::string tileId = composer->_tileId;
#ifdef C_CODE
  if (_composers.find(tileId) != _composers.end()) {
    _composers.erase(tileId);
  }
#endif
#ifdef JAVA_CODE
  _composers.remove(tileId);
#endif
  delete composer;
}
