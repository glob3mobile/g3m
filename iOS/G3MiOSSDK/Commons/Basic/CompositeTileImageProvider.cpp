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
#include "IImage.hpp"
#include "FrameTasksExecutor.hpp"
#include "Sector.hpp"
#include "Vector2S.hpp"
#include "IStringBuilder.hpp"


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
      const size_t childrenContributionsSize = childrenContributions.size();
      if ((childrenContributionsSize > 0) &&
          childContribution->isFullCoverageAndOpaque()) {
        for (size_t j = 0; j < childrenContributionsSize; j++) {
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

CompositeTileImageProvider::ChildResult::ChildResult(const bool                   isError,
                                                     const bool                   isCanceled,
                                                     const IImage*                image,
                                                     const std::string&           imageID,
                                                     const TileImageContribution* contribution,
                                                     const std::string&           error) :
_isError(isError),
_isCanceled(isCanceled),
_image(image),
_imageID(imageID),
_contribution(contribution),
_error(error)
{
}


CompositeTileImageProvider::ChildResult::~ChildResult() {
  delete _image;
  TileImageContribution::releaseContribution(_contribution);
}

const CompositeTileImageProvider::ChildResult* CompositeTileImageProvider::ChildResult::image(const IImage*                image,
                                                                                              const std::string&           imageID,
                                                                                              const TileImageContribution* contribution) {
  return new CompositeTileImageProvider::ChildResult(false ,       // isError
                                                     false,        // isCanceled
                                                     image,
                                                     imageID,
                                                     contribution,
                                                     ""            // error
                                                     );
}

const CompositeTileImageProvider::ChildResult* CompositeTileImageProvider::ChildResult::error(const std::string& error) {
  return new CompositeTileImageProvider::ChildResult(true,  // isError
                                                     false, // isCanceled
                                                     NULL,  // image
                                                     "",    // imageID
                                                     NULL,  // contribution
                                                     error);
}

const CompositeTileImageProvider::ChildResult* CompositeTileImageProvider::ChildResult::cancelation() {
  return new CompositeTileImageProvider::ChildResult(false, // isError
                                                     true,  // isCanceled
                                                     NULL,  // image
                                                     "",    // imageID
                                                     NULL,  // contribution
                                                     ""     // error
                                                     );
}

CompositeTileImageProvider::Composer::Composer(int width,
                                               int height,
                                               CompositeTileImageProvider* compositeTileImageProvider,
                                               const std::string& tileID,
                                               const Sector& tileSector,
                                               TileImageListener* listener,
                                               bool deleteListener,
                                               const CompositeTileImageContribution* compositeContribution,
                                               FrameTasksExecutor* frameTasksExecutor) :
_width(width),
_height(height),
_compositeTileImageProvider(compositeTileImageProvider),
_tileID(tileID),
_listener(listener),
_deleteListener(deleteListener),
_compositeContribution(compositeContribution),
_contributionsSize( compositeContribution->size() ),
_frameTasksExecutor(frameTasksExecutor),
_stepsDone(0),
_anyError(false),
_anyCancelation(false),
_canceled(false),
_tileSector(tileSector)
{
  _compositeTileImageProvider->_retain();

  for (int i = 0; i < _contributionsSize; i++) {
    _results.push_back( NULL );
  }
}

CompositeTileImageProvider::Composer::~Composer() {
  for (int i = 0; i < _contributionsSize; i++) {
    const ChildResult* result = _results[i];
    delete result;
  }

  TileImageContribution::releaseContribution(_compositeContribution);

  _compositeTileImageProvider->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void CompositeTileImageProvider::Composer::cleanUp() {
  if (_deleteListener) {
    delete _listener;
    _listener = NULL;
  }

  _compositeTileImageProvider->composerDone(this);
}

void CompositeTileImageProvider::Composer::done() {
  if (_canceled) {
    cleanUp();
    return;
  }

  bool simpleCase;
  if (_contributionsSize == 1) {
    const ChildResult* singleResult = _results[0];
    // check for (singleResult->_contribution == NULL). It's NULL when error or cancelation.
    const Sector* contributionSector = (singleResult->_contribution == NULL) ? NULL : singleResult->_contribution->getSector();
    simpleCase = ((contributionSector == NULL) ||
                  contributionSector->isNan()  ||
                  _tileSector.isEquals(*contributionSector));
  }
  else {
    simpleCase = false;
  }

  if (simpleCase) { // One image covers fits tile
    const ChildResult* singleResult = _results[0];

    if (singleResult->_isError) {
      _listener->imageCreationError(_tileID,
                                    singleResult->_error);
    }
    else if (singleResult->_isCanceled) {
      _listener->imageCreationCanceled(_tileID);
    }
    else {
      // retain the singleResult->_contribution as the _listener take full ownership of the contribution
      TileImageContribution::retainContribution(singleResult->_contribution);
      _listener->imageCreated(singleResult->_imageID,
                              singleResult->_image->shallowCopy(),
                              singleResult->_imageID,
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

      _listener->imageCreationError(_tileID,
                                    composedError);

      cleanUp();
    }
    else if (_anyCancelation) {
      _listener->imageCreationCanceled(_tileID);
      cleanUp();
    }
    else {
      _frameTasksExecutor->addPreRenderTask(new ComposerFrameTask(this));
    }
  }
}

void CompositeTileImageProvider::Composer::mixResult() {
  if (_canceled) {
    cleanUp();
    return;
  }

  ICanvas* canvas = IFactory::instance()->createCanvas(false);

  canvas->initialize(_width, _height);
  
  IStringBuilder* imageID = IStringBuilder::newStringBuilder();
  
  for (int i = 0; i < _contributionsSize; i++) {
    const ChildResult* result = _results[i];

    
    imageID->addString(result->_imageID);
    imageID->addString("|");

    const IImage* image = result->_image;
    
    const float alpha = result->_contribution->_alpha;

    if (result->_contribution->isFullCoverage()) {
      if ( result->_contribution->isOpaque() ) {
        canvas->drawImage(image, 0, 0, _width, _height);
      } else {
        imageID->addFloat(alpha);
        imageID->addString("|");
        
        canvas->drawImage(image, 0, 0, _width, _height, alpha);
      }
    }
    else {
      const Sector* imageSector = result->_contribution->getSector();
      const Sector visibleContributionSector = imageSector->intersection(_tileSector);
      
      imageID->addString(visibleContributionSector.id());
      imageID->addString("|");

      const RectangleF* srcRect = RectangleF::calculateInnerRectangleFromSector(image->getWidth(), image->getHeight(),
                                                                                *imageSector,
                                                                                visibleContributionSector);
      
      const RectangleF* destRect = RectangleF::calculateInnerRectangleFromSector(_width, _height,
                                                                                _tileSector,
                                                                                visibleContributionSector);
      
      //We add "destRect->id()" to "imageID" for to differentiate cases of same "visibleContributionSector" at different levels of tiles
      
      imageID->addString(destRect->id());
      imageID->addString("|");
      
      canvas->drawImage(image,
                        //SRC RECT
                        srcRect->_x, srcRect->_y,
                        srcRect->_width, srcRect->_height,
                        //DEST RECT
                        destRect->_x, destRect->_y,
                        destRect->_width, destRect->_height,
                        alpha);
      
      delete destRect;
      delete srcRect;
    }
  }
  _imageID = imageID->getString();
  
  delete imageID;

  canvas->createImage(new ComposerImageListener(this), true);

  delete canvas;
}

bool CompositeTileImageProvider::ComposerFrameTask::isCanceled(const G3MRenderContext* rc) {
  return false;
}

void CompositeTileImageProvider::ComposerFrameTask::execute(const G3MRenderContext* rc) {
  _composer->mixResult();
}

void CompositeTileImageProvider::Composer::imageCreated(const IImage* image) {
  const CompositeTileImageContribution* compositeContribution = _compositeContribution;
  _compositeContribution = NULL; // the _compositeContribution ownership moved to the listener
  _listener->imageCreated(_tileID,
                          image,
                          _imageID,
                          compositeContribution);
  cleanUp();
}

void CompositeTileImageProvider::Composer::stepDone() {
  _stepsDone++;
  if (_stepsDone == _contributionsSize) {
    done();
  }
}

void CompositeTileImageProvider::Composer::imageCreated(const std::string&           tileID,
                                                        const IImage*                image,
                                                        const std::string&           imageID,
                                                        const TileImageContribution* contribution,
                                                        const size_t                 index) {
  _results[index] = ChildResult::image(image, imageID, contribution);
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationError(const std::string& error,
                                                              const size_t       index) {
  _results[index] = ChildResult::error(error);
  _anyError = true;
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationCanceled(const size_t index) {
  _results[index] = ChildResult::cancelation();
  _anyCancelation = true;
  stepDone();
}

void CompositeTileImageProvider::Composer::cancel(const std::string& tileID) {
  _canceled = true;
  _compositeTileImageProvider->cancelChildren(tileID, _compositeContribution);
}

void CompositeTileImageProvider::ChildTileImageListener::imageCreated(const std::string&           tileID,
                                                                      const IImage*                image,
                                                                      const std::string&           imageID,
                                                                      const TileImageContribution* contribution) {
  _composer->imageCreated(tileID, image, imageID, contribution, _index);
}

void CompositeTileImageProvider::ChildTileImageListener::imageCreationError(const std::string& tileID,
                                                                            const std::string& error) {
  _composer->imageCreationError(error, _index);
}

void CompositeTileImageProvider::ChildTileImageListener::imageCreationCanceled(const std::string& tileID) {
  _composer->imageCreationCanceled(_index);
}

void CompositeTileImageProvider::create(const Tile* tile,
                                        const TileImageContribution* contribution,
                                        const Vector2S& resolution,
                                        long long tileTextureDownloadPriority,
                                        bool logDownloadActivity,
                                        TileImageListener* listener,
                                        bool deleteListener,
                                        FrameTasksExecutor* frameTasksExecutor) {

  const CompositeTileImageContribution* compositeContribution = (const CompositeTileImageContribution*) contribution;

  const std::string tileID = tile->_id;

  Composer* composer = new Composer(resolution._x,
                                    resolution._y,
                                    this,
                                    tileID,
                                    tile->_sector,
                                    listener,
                                    deleteListener,
                                    compositeContribution,
                                    frameTasksExecutor);

  _composers[ tileID ] = composer;

  const size_t contributionsSize = compositeContribution->size();
  for (size_t i = 0; i < contributionsSize; i++) {
    const CompositeTileImageContribution::ChildContribution* childContribution = compositeContribution->get(i);

    TileImageProvider* child = _children[ childContribution->_childIndex ];

    // retain the childContribution before calling the child, as the child take full ownership of the contribution
    TileImageContribution::retainContribution(childContribution->_contribution);

    child->create(tile,
                  childContribution->_contribution,
                  resolution,
                  tileTextureDownloadPriority,
                  logDownloadActivity,
                  new ChildTileImageListener(composer, i),
                  true,
                  frameTasksExecutor);
  }
}

void CompositeTileImageProvider::cancel(const std::string& tileID) {
#ifdef C_CODE
  if (_composers.find(tileID) != _composers.end()) {
    Composer* composer = _composers[tileID];
    composer->cancel(tileID);
    _composers.erase(tileID);
  }
#endif
#ifdef JAVA_CODE
  final Composer composer = _composers.remove(tileID);
  if (composer != null) {
    composer.cancel(tileID);
  }
#endif
}

void CompositeTileImageProvider::composerDone(Composer* composer) {
  _composers.erase( composer->_tileID );
  composer->_release();
}

void CompositeTileImageProvider::cancelChildren(const std::string& tileID,
                                                const CompositeTileImageContribution* compositeContribution) {
  const size_t contributionsSize = compositeContribution->size();

  // store all the indexes before calling child->cancel().
  // child->cancel() can force the deletion of the builder (and in order the deletion of compositeContribution)
  size_t* indexes = new size_t[contributionsSize];
  for (size_t i = 0; i < contributionsSize; i++) {
    indexes[i] = compositeContribution->get(i)->_childIndex;
  }

  for (size_t i = 0; i < contributionsSize; i++) {
    TileImageProvider* child = _children[ indexes[i] ];
    child->cancel(tileID);
  }
  
  delete [] indexes;
}
