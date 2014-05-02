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

CompositeTileImageProvider::ChildResult::~ChildResult() {
  delete _image;
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
                                               const Sector& tileSector,
                                               TileImageListener* listener,
                                               bool deleteListener,
                                               const CompositeTileImageContribution* compositeContribution,
                                               FrameTasksExecutor* frameTasksExecutor) :
_width(width),
_height(height),
_compositeTileImageProvider(compositeTileImageProvider),
_tileId(tileId),
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
  for (int i = 0; i < _contributionsSize; i++) {
    _results.push_back( NULL );
  }
}

CompositeTileImageProvider::Composer::~Composer() {
  for (int i = 0; i < _contributionsSize; i++) {
    const ChildResult* result = _results[i];
    delete result;
  }
  delete _compositeContribution;

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
                              singleResult->_image->shallowCopy(),
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
      //      ICanvas* canvas = IFactory::instance()->createCanvas();
      //
      //      canvas->initialize(_width, _height);
      //
      //      std::string imageId = "";
      //
      //      for (int i = 0; i < _contributionsSize; i++) {
      //       const ChildResult* result = _results[i];
      //
      //        imageId += result->_imageId + "|";
      //#warning JM: consider sector and transparency
      //        canvas->drawImage(result->_image, 0, 0);
      //      }
      //      _imageId = imageId;
      //
      //      canvas->createImage(new ComposerImageListener(this), true);
      //
      //      delete canvas;

      _frameTasksExecutor->addPreRenderTask(new ComposerFrameTask(this));
    }
  }
}

RectangleF* CompositeTileImageProvider::Composer::getInnerRectangle(int wholeSectorWidth,
                                                                    int wholeSectorHeight,
                                                                    const Sector& wholeSector,
                                                                    const Sector& innerSector) const {
  if (wholeSector.isEquals(innerSector)){
    return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
  }

  const double widthFactor  = innerSector._deltaLongitude.div(wholeSector._deltaLongitude);
  const double heightFactor = innerSector._deltaLatitude.div(wholeSector._deltaLatitude);

  const Vector2D lowerUV = wholeSector.getUVCoordinates(innerSector.getNW());

  return new RectangleF((float) (lowerUV._x   * wholeSectorWidth),
                        (float) (lowerUV._y   * wholeSectorHeight),
                        (float) (widthFactor  * wholeSectorWidth),
                        (float) (heightFactor * wholeSectorHeight));
}

void CompositeTileImageProvider::Composer::mixResult() {
  if (_canceled) {
    cleanUp();
    return;
  }

  ICanvas* canvas = IFactory::instance()->createCanvas();

  canvas->initialize(_width, _height);

  std::string imageId = "";

  for (int i = 0; i < _contributionsSize; i++) {
    const ChildResult* result = _results[i];
    imageId += result->_imageId + "|";

#warning //For now, we consider the whole image will appear on the tile (no source rect needed)
    const IImage* image = result->_image;
    const float   alpha = result->_contribution->_alpha;

    if (result->_contribution->isFullCoverageAndOpaque()) {
      canvas->drawImage(image, 0, 0);
    }
    else {
      if (result->_contribution->isFullCoverage()) {
        canvas->drawImage(image,
                          //SRC RECT
                          0,0,
                          image->getWidth(), image->getHeight(),
                          //DEST RECT
                          0, 0,
                          _width, _height,
                          alpha);
      }
      else {
        const Sector* imageSector = result->_contribution->getSector();

        const RectangleF* destRect = getInnerRectangle(_width, _height,
                                                       _tileSector,
                                                       *imageSector);

        //TEST MADRID
        //      if (_tileSector.contains(Angle::fromDegrees(40.41677540051771), Angle::fromDegrees(-3.7037901976145804))){
        //      printf("TS: %s\nIS: %s\nR: %s\n",_tileSector.description().c_str(),
        //             imageSector->description().c_str(),
        //             rect->description().c_str());
        //      }

        canvas->drawImage(image,
                          //SRC RECT
                          0,0,
                          image->getWidth(), image->getHeight(),
                          //DEST RECT
                          destRect->_x, destRect->_y,
                          destRect->_width, destRect->_height,
                          alpha);

        delete destRect;
      }
    }
  }
  _imageId = imageId;

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
  _listener->imageCreated(_tileId,
                          image,
                          _imageId,
                          compositeContribution);
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
  _results[index] = ChildResult::image(image, imageId, contribution);
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationError(const std::string& error,
                                                              const int          index) {
  _results[index] = ChildResult::error(error);
  _anyError = true;
  stepDone();
}

void CompositeTileImageProvider::Composer::imageCreationCanceled(const int index) {
  _results[index] = ChildResult::cancelation();
  _anyCancelation = true;
  stepDone();
}

void CompositeTileImageProvider::Composer::cancel(const std::string& tileId) {
  _canceled = true;
  _compositeTileImageProvider->cancelChildren(tileId, _compositeContribution);
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
                                        bool deleteListener,
                                        FrameTasksExecutor* frameTasksExecutor) {

  const CompositeTileImageContribution* compositeContribution = (const CompositeTileImageContribution*) contribution;

  const std::string tileId = tile->_id;

  Composer* composer = new Composer(resolution._x,
                                    resolution._y,
                                    this,
                                    tileId,
                                    tile->_sector,
                                    listener,
                                    deleteListener,
                                    compositeContribution,
                                    frameTasksExecutor);

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
                  true,
                  frameTasksExecutor);
  }
}

void CompositeTileImageProvider::cancel(const std::string& tileId) {
#ifdef C_CODE
  if (_composers.find(tileId) != _composers.end()) {
    Composer* composer = _composers[tileId];
    composer->cancel(tileId);
    _composers.erase(tileId);
  }
#endif
#ifdef JAVA_CODE
  final Composer composer = _composers.remove(tileId);
  if (composer != null) {
    composer.cancel(tileId);
  }
#endif
}

void CompositeTileImageProvider::composerDone(Composer* composer) {
  _composers.erase( composer->_tileId );
  composer->_release();
}

void CompositeTileImageProvider::cancelChildren(const std::string& tileId,
                                                const CompositeTileImageContribution* compositeContribution) {
  const int contributionsSize = compositeContribution->size();

  // store all the indexes before calling child->cancel().
  // child->cancel() can force the deletion of the builder (and in order the deletion of compositeContribution)
  int* indexes = new int[contributionsSize];
  for (int i = 0; i < contributionsSize; i++) {
    indexes[i] = compositeContribution->get(i)->_childIndex;
  }
  
  for (int i = 0; i < contributionsSize; i++) {
    TileImageProvider* child = _children[ indexes[i] ];
    child->cancel(tileId);
  }
  
  delete [] indexes;
}
