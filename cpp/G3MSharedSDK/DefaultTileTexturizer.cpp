//
//  DefaultTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/16/14.
//
//

#include "DefaultTileTexturizer.hpp"

#include "LayerSet.hpp"
#include "Tile.hpp"
#include "LeveledTexturedMesh.hpp"
#include "TextureIDReference.hpp"
#include "FrameTasksExecutor.hpp"
#include "FrameTask.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "TileImageProvider.hpp"
#include "TileImageListener.hpp"
#include "TexturesHandler.hpp"
#include "ITexturizerData.hpp"
#include "TileImageContribution.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "RectangleF.hpp"
#include "IImageListener.hpp"
#include "IImageBuilder.hpp"
#include "IImageBuilderListener.hpp"
#include "PlanetRenderContext.hpp"
#include "TilesRenderParameters.hpp"
#include "G3MRenderContext.hpp"
#include "ILogger.hpp"


class DTT_LTMInitializer : public LazyTextureMappingInitializer {
private:
  const Tile* _tile;
  const Tile* _ancestor;

  float _translationU;
  float _translationV;
  float _scaleU;
  float _scaleV;

  const TileTessellator* _tessellator;
  const Vector2S _resolution;

public:
  DTT_LTMInitializer(const Vector2S& resolution,
                     const Tile* tile,
                     const Tile* ancestor,
                     const TileTessellator* tessellator) :
  _resolution(resolution),
  _tile(tile),
  _ancestor(ancestor),
  _tessellator(tessellator),
  _translationU(0),
  _translationV(0),
  _scaleU(1),
  _scaleV(1)
  {

  }

  virtual ~DTT_LTMInitializer() {
#ifdef JAVA_CODE
    super.dispose();
#endif

  }

  void initialize() {
    // The default scale and translation are ok when (tile == _ancestor)
    if (_tile != _ancestor) {
      const Sector tileSector = _tile->_sector;

      const Vector2F lowerTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                   tileSector._lower);

      const Vector2F upperTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                   tileSector._upper);

      _translationU = lowerTextCoordUV._x;
      _translationV = upperTextCoordUV._y;

      _scaleU = upperTextCoordUV._x - lowerTextCoordUV._x;
      _scaleV = lowerTextCoordUV._y - upperTextCoordUV._y;
    }
  }

  const Vector2F getTranslation() const {
    return Vector2F(_translationU, _translationV);
  }

  const Vector2F getScale() const {
    return Vector2F(_scaleU, _scaleV);
  }

  IFloatBuffer* createTextCoords() const {
    return _tessellator->createTextCoords(Vector2S(_resolution._x, _resolution._y), _tile);
  }

};

class DTT_TileTextureBuilder;





class DTT_TileImageListener : public TileImageListener {
private:
  DTT_TileTextureBuilder* _builder;
  const Sector            _tileSector;
  const IImage*           _backgroundTileImage;
  const std::string       _backgroundTileImageName;

  const Vector2S          _tileTextureResolution;

public:
  DTT_TileImageListener(DTT_TileTextureBuilder* builder,
                        const Tile*             tile,
                        const Vector2S&         tileTextureResolution,
                        const IImage*           backgroundTileImage,
                        const std::string&      backgroundTileImageName);

  virtual ~DTT_TileImageListener();

  void imageCreated(const std::string&           tileID,
                    const IImage*                image,
                    const std::string&           imageID,
                    const TileImageContribution* contribution);

  void imageCreationError(const std::string& tileID,
                          const std::string& error);

  void imageCreationCanceled(const std::string& tileID);


};


class DTT_TileTextureBuilder : public RCObject {
private:
  LeveledTexturedMesh* _texturedMesh;
  Tile*                _tile;
  const std::string    _tileID;
  TileImageProvider*   _tileImageProvider;
  TexturesHandler*     _texturesHandler;
  const Vector2S       _tileTextureResolution;
  const bool          _logTilesPetitions;
  const long long     _tileTextureDownloadPriority;
  bool                _canceled;
  FrameTasksExecutor* _frameTasksExecutor;
  const IImage* _backgroundTileImage;
  const std::string _backgroundTileImageName;
  const bool _verboseErrors;


  static const TextureIDReference* getTopLevelTextureIDForTile(Tile* tile) {
    LeveledTexturedMesh* mesh = (LeveledTexturedMesh*) tile->getTexturizedMesh();

    return (mesh == NULL) ? NULL : mesh->getTopLevelTextureID();
  }

  static LeveledTexturedMesh* createMesh(Tile* tile,
                                         const Mesh* tessellatorMesh,
                                         const Vector2S tileMeshResolution,
                                         const TileTessellator* tessellator,
                                         TexturesHandler*     texturesHandler,
                                         const IImage*  backgroundTileImage,
                                         const std::string& backgroundTileImageName,
                                         const bool ownedTexCoords,
                                         const bool transparent,
                                         const bool generateMipmap,
                                         const int wrapS,
                                         const int wrapT) {
    std::vector<LazyTextureMapping*>* mappings = new std::vector<LazyTextureMapping*>();

    Tile* ancestor = tile;
    bool fallbackSolved = false;
    while (ancestor != NULL && !fallbackSolved) {

      LazyTextureMapping* mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution,
                                                                                  tile,
                                                                                  ancestor,
                                                                                  tessellator),
                                                           ownedTexCoords,
                                                           transparent);

      if (ancestor != tile) {
        const TextureIDReference* glTextureID = getTopLevelTextureIDForTile(ancestor);
        if (glTextureID != NULL) {
          TextureIDReference* glTextureIDRetainedCopy = glTextureID->createCopy();

          mapping->setGLTextureID(glTextureIDRetainedCopy);
          fallbackSolved = true;
        }
      }

      mappings->push_back(mapping);

      ancestor = ancestor->getParent();
    }

    if (!fallbackSolved && backgroundTileImage != NULL) {
      LazyTextureMapping* mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution,
                                                                                  tile,
                                                                                  tile,
                                                                                  tessellator),
                                                           true,
                                                           false);
      const TextureIDReference* glTextureID = texturesHandler->getTextureIDReference(backgroundTileImage,
                                                                                     GLFormat::rgba(),
                                                                                     backgroundTileImageName,
                                                                                     generateMipmap,
                                                                                     wrapS,
                                                                                     wrapT);
      mapping->setGLTextureID(glTextureID); //Mandatory to active mapping

      mappings->push_back(mapping);

    }

    return new LeveledTexturedMesh(tessellatorMesh,
                                   false,
                                   mappings);
  }

public:

  DTT_TileTextureBuilder(const G3MRenderContext*           rc,
                         const LayerTilesRenderParameters* layerTilesRenderParameters,
                         TileImageProvider*                tileImageProvider,
                         Tile*                             tile,
                         const Mesh*                       tessellatorMesh,
                         const TileTessellator*            tessellator,
                         long long                         tileTextureDownloadPriority,
                         bool                              logTilesPetitions,
                         FrameTasksExecutor*               frameTasksExecutor,
                         const IImage*                     backgroundTileImage,
                         const std::string&                backgroundTileImageName,
                         const bool                        verboseErrors) :
  _tileImageProvider(tileImageProvider),
  _texturesHandler(rc->getTexturesHandler()),
  _tileTextureResolution( layerTilesRenderParameters->_tileTextureResolution ),
  _tile(tile),
  _tileID(tile->_id),
  _texturedMesh( NULL ),
  _canceled(false),
  _tileTextureDownloadPriority(tileTextureDownloadPriority),
  _logTilesPetitions(logTilesPetitions),
  _frameTasksExecutor(frameTasksExecutor),
  _backgroundTileImage(backgroundTileImage),
  _backgroundTileImageName(backgroundTileImageName),
  _verboseErrors(verboseErrors)
  {
    _tileImageProvider->_retain();

    _texturedMesh = createMesh(tile,
                               tessellatorMesh,
                               layerTilesRenderParameters->_tileMeshResolution,
                               tessellator,
                               _texturesHandler,
                               backgroundTileImage,
                               backgroundTileImageName,
                               true,  // ownedTexCoords,
                               false, // transparent,
                               true,  // generateMipmap
                               GLTextureParameterValue::clampToEdge(),
                               GLTextureParameterValue::clampToEdge());
  }

  LeveledTexturedMesh* getTexturedMesh() {
    return _texturedMesh;
  }

  void start() {
    if (!_canceled) {
      const TileImageContribution* contribution = _tileImageProvider->contribution(_tile);
      if (contribution == NULL) {
        if (_tile != NULL) {
          imageCreated(_backgroundTileImage->shallowCopy(), _backgroundTileImageName, TileImageContribution::fullCoverageOpaque());
          //_tile->setTextureSolved(true);
        }
      }
      else {
        _tileImageProvider->create(_tile,
                                   contribution,
                                   _tileTextureResolution,
                                   _tileTextureDownloadPriority,
                                   _logTilesPetitions,
                                   new DTT_TileImageListener(this,
                                                             _tile,
                                                             _tileTextureResolution,
                                                             _backgroundTileImage,
                                                             _backgroundTileImageName),
                                   true,
                                   _frameTasksExecutor);
      }
    }
  }

  void cancel(bool cleanTile) {
    _texturedMesh = NULL;
    if (cleanTile) {
      _tile = NULL;
    }
    if (!_canceled) {
      _canceled = true;
      _tileImageProvider->cancel(_tileID);
    }
  }

  bool isCanceled() const {
    return _canceled;
  }

  ~DTT_TileTextureBuilder() {
    _tileImageProvider->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  bool uploadTexture(const IImage*      image,
                     const std::string& imageID) {

    const TextureIDReference* glTextureID = _texturesHandler->getTextureIDReference(image,
                                                                                    GLFormat::rgba(),
                                                                                    imageID,
                                                                                    true, // generateMipmap
                                                                                    GLTextureParameterValue::clampToEdge(), // wrapS
                                                                                    GLTextureParameterValue::clampToEdge()  // wrapT
                                                                                    );
    if (glTextureID == NULL) {
      return false;
    }

    if (!_texturedMesh->setGLTextureIDForLevel(0, glTextureID)) {
      delete glTextureID;
    }

    return true;
  }

  void imageCreated(const IImage*                image,
                    const std::string&           imageID,
                    const TileImageContribution* contribution) {
    if (!contribution->isFullCoverageAndOpaque()) {
      ILogger::instance()->logWarning("Contribution isn't full covearge and opaque before to upload texture");
    }

    if (!_canceled && (_tile != NULL) && (_texturedMesh != NULL)) {
      if (uploadTexture(image, imageID)) {
        _tile->setTextureSolved(true);
      }
    }

    delete image;
    TileImageContribution::releaseContribution( contribution );
  }

  void imageCreationError(const std::string& error) {
    // TODO: #warning propagate the error to the texturizer and change the render state if is necessary
    if (_verboseErrors) {
      ILogger::instance()->logError("%s", error.c_str());
    }
  }

  void imageCreationCanceled() {
  }
};

class DTT_NotFullProviderImageListener : public IImageListener {
private:
  DTT_TileTextureBuilder* _builder;
  const std::string& _imageID;

public:
  DTT_NotFullProviderImageListener(DTT_TileTextureBuilder* builder,
                                   const std::string& imageID) :
  _builder(builder),
  _imageID(imageID)
  {
    _builder->_retain();
  }

  ~DTT_NotFullProviderImageListener() {
    if (_builder != NULL) {
      _builder->_release();
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void imageCreated(const IImage* image) {
    _builder->imageCreated(image,_imageID,TileImageContribution::fullCoverageOpaque());
  }
};




DTT_TileImageListener::DTT_TileImageListener(DTT_TileTextureBuilder* builder,
                                             const Tile*             tile,
                                             const Vector2S&         tileTextureResolution,
                                             const IImage*           backgroundTileImage,
                                             const std::string&      backgroundTileImageName) :
_builder(builder),
_tileSector(tile->_sector),
_tileTextureResolution(tileTextureResolution),
_backgroundTileImage(backgroundTileImage),
_backgroundTileImageName(backgroundTileImageName)
{
  _builder->_retain();
}

DTT_TileImageListener::~DTT_TileImageListener() {
  if (_builder != NULL) {
    _builder->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void DTT_TileImageListener::imageCreated(const std::string&           tileID,
                                         const IImage*                image,
                                         const std::string&           imageID,
                                         const TileImageContribution* contribution) {

  if (!contribution->isFullCoverageAndOpaque()) {

    IStringBuilder* auxImageID = IStringBuilder::newStringBuilder();

    //ILogger::instance()->logInfo("DTT_TileImageListener received image that does not fit tile. Building new Image....");

    ICanvas* canvas = IFactory::instance()->createCanvas(false);

    const int width  =  _tileTextureResolution._x;
    const int height =  _tileTextureResolution._y;

    //ILogger::instance()->logInfo("Tile " + _tile->description());

    canvas->initialize(width, height);

    if (_backgroundTileImage != NULL) {
      auxImageID->addString(_backgroundTileImageName);
      auxImageID->addString("|");
      canvas->drawImage(_backgroundTileImage, 0, 0, width, height);
    }

    auxImageID->addString(imageID);
    auxImageID->addString("|");

    const float   alpha = contribution->_alpha;

    if (contribution->isFullCoverage()) {
      auxImageID->addFloat(alpha);
      auxImageID->addString("|");
      canvas->drawImage(image, 0, 0, width, height, alpha);
    }
    else {
      const Sector* imageSector = contribution->getSector();

      const Sector visibleContributionSector = imageSector->intersection(_tileSector);

      auxImageID->addString(visibleContributionSector.id());
      auxImageID->addString("|");

      const RectangleF* srcRect = RectangleF::calculateInnerRectangleFromSector(image->getWidth(), image->getHeight(),
                                                                                *imageSector,
                                                                                visibleContributionSector);

      const RectangleF* destRect = RectangleF::calculateInnerRectangleFromSector(width, height,
                                                                                 _tileSector,
                                                                                 visibleContributionSector);


      //We add "destRect->id()" to "auxImageID" for to differentiate cases of same "visibleContributionSector" at different levels of tiles
      auxImageID->addString(destRect->id());
      auxImageID->addString("|");


      //ILogger::instance()->logInfo("destRect " + destRect->description());


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

    canvas->createImage(new DTT_NotFullProviderImageListener(_builder,
                                                             auxImageID->getString()),
                        true);

    delete auxImageID;
    delete canvas;
    delete image;
    TileImageContribution::releaseContribution( contribution );

  } else {
    _builder->imageCreated(image, imageID, contribution);
  }
}

void DTT_TileImageListener::imageCreationError(const std::string& tileID,
                                               const std::string& error) {
  _builder->imageCreationError(error);
}

void DTT_TileImageListener::imageCreationCanceled(const std::string& tileID) {
  _builder->imageCreationCanceled();
}

class DTT_TileTextureBuilderHolder : public ITexturizerData {
private:
  DTT_TileTextureBuilder* _builder;

public:
  DTT_TileTextureBuilderHolder(DTT_TileTextureBuilder* builder) :
  _builder(builder)
  {
  }

  DTT_TileTextureBuilder* get() const {
    return _builder;
  }

  ~DTT_TileTextureBuilderHolder() {
    if (_builder != NULL) {
      _builder->_release();
    }
  }

#ifndef C_CODE
  void unusedMethod() { }
#endif
};


class DTT_TileTextureBuilderStartTask : public FrameTask {
private:
  DTT_TileTextureBuilder* _builder;

public:
  DTT_TileTextureBuilderStartTask(DTT_TileTextureBuilder* builder) :
  _builder(builder)
  {
    _builder->_retain();
  }

  virtual ~DTT_TileTextureBuilderStartTask() {
    _builder->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void execute(const G3MRenderContext* rc) {
    _builder->start();
  }

  bool isCanceled(const G3MRenderContext* rc) {
    return _builder->isCanceled();
  }
};

DefaultTileTexturizer::DefaultTileTexturizer(IImageBuilder* defaultBackgroundImageBuilder,
                                             const bool verboseErrors) :
_defaultBackgroundImageBuilder(defaultBackgroundImageBuilder),
_defaultBackgroundImageLoaded(false),
_verboseErrors(verboseErrors)
{
  ILogger::instance()->logInfo("Create texturizer...");

}


LeveledTexturedMesh* DefaultTileTexturizer::getMesh(Tile* tile) const {
  DTT_TileTextureBuilderHolder* tileBuilderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();
  return (tileBuilderHolder == NULL) ? NULL : tileBuilderHolder->get()->getTexturedMesh();
}


RenderState DefaultTileTexturizer::getRenderState(LayerSet* layerSet) {
  //ILogger::instance()->logInfo("Check render state texturizer...");
  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  if (!_defaultBackgroundImageLoaded) {
    return RenderState::busy();
  }
  if (layerSet != NULL) {
    return layerSet->getRenderState();
  }
  return RenderState::ready();
}

class DTT_IImageBuilderListener: public IImageBuilderListener {

private:

  DefaultTileTexturizer* _defaultTileTexturizer;

public:

  DTT_IImageBuilderListener(DefaultTileTexturizer* defaultTileTexturizer) :
  _defaultTileTexturizer(defaultTileTexturizer)
  {
  }

  ~DTT_IImageBuilderListener() {
  }

  void imageCreated(const IImage* image,
                    const std::string& imageName) {
    _defaultTileTexturizer->setDefaultBackgroundImage(image);
    _defaultTileTexturizer->setDefaultBackgroundImageName(imageName);
    _defaultTileTexturizer->setDefaultBackgroundImageLoaded(true);
    ILogger::instance()->logInfo("Default Background Image loaded...");

  }

  void onError(const std::string& error) {
    ILogger::instance()->logError(error);
    _defaultTileTexturizer->_errors.push_back("Can't download background image default");
    _defaultTileTexturizer->_errors.push_back(error);

  }
};

void DefaultTileTexturizer::initialize(const G3MContext* context,
                                       const TilesRenderParameters* parameters) {
  ILogger::instance()->logInfo("Initializing texturizer...");

  _defaultBackgroundImageBuilder->build(context, new DTT_IImageBuilderListener(this), true);

  // do nothing
}

Mesh* DefaultTileTexturizer::texturize(const G3MRenderContext*    rc,
                                       const PlanetRenderContext* prc,
                                       Tile* tile,
                                       Mesh* tessellatorMesh,
                                       Mesh* previousMesh) {
  DTT_TileTextureBuilderHolder* builderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();

  TileImageProvider* tileImageProvider = prc->_layerSet->getTileImageProvider(rc,
                                                                              prc->_layerTilesRenderParameters);

  if (tileImageProvider == NULL) {
    tile->setTextureSolved(true);
    tile->setTexturizerDirty(false);
    return NULL;
  }

  DTT_TileTextureBuilder* builder;
  if (builderHolder == NULL) {
    const long long tileTextureDownloadPriority = prc->getTileTextureDownloadPriority(tile->_level);

    builder = new DTT_TileTextureBuilder(rc,
                                         prc->_layerTilesRenderParameters,
                                         tileImageProvider,
                                         tile,
                                         tessellatorMesh,
                                         prc->_tessellator,
                                         tileTextureDownloadPriority,
                                         prc->_logTilesPetitions,
                                         rc->getFrameTasksExecutor(),
                                         _defaultBackgroundImage,
                                         _defaultBackgroundImageName,
                                         _verboseErrors);
    builderHolder = new DTT_TileTextureBuilderHolder(builder);
    tile->setTexturizerData(builderHolder);
  }
  else {
    builder = builderHolder->get();
  }

  // get the mesh just before calling start(), if not... the start(ed) task can finish immediately
  // and as one consequence the builder got deleted and the "builder" pointer becomes a dangling pointer
  Mesh* texturizedMesh = builder->getTexturedMesh();

  rc->getFrameTasksExecutor()->addPreRenderTask( new DTT_TileTextureBuilderStartTask(builder) );

  tile->setTexturizerDirty(false);

  return texturizedMesh;
}

void DefaultTileTexturizer::tileToBeDeleted(Tile* tile,
                                            Mesh* mesh) {
  DTT_TileTextureBuilderHolder* builderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();
  if (builderHolder != NULL) {
    DTT_TileTextureBuilder* builder = builderHolder->get();
    builder->cancel(true /* cleanTile */);
  }
}

void DefaultTileTexturizer::tileMeshToBeDeleted(Tile* tile,
                                                Mesh* mesh) {
  DTT_TileTextureBuilderHolder* builderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();
  if (builderHolder != NULL) {
    DTT_TileTextureBuilder* builder = builderHolder->get();
    builder->cancel(false /* cleanTile */);
  }
}

void DefaultTileTexturizer::justCreatedTopTile(const G3MRenderContext* rc,
                                               Tile* tile,
                                               LayerSet* layerSet) {
  // do nothing
}

void DefaultTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
                                                          Tile* ancestorTile,
                                                          bool textureSolved) {
  if (!textureSolved) {
    return;
  }

  if (tile->isTextureSolved()) {
    return;
  }

  LeveledTexturedMesh* ancestorMesh = getMesh(ancestorTile);
  if (ancestorMesh == NULL) {
    return;
  }

  const TextureIDReference* glTextureID = ancestorMesh->getTopLevelTextureID();
  if (glTextureID == NULL) {
    return;
  }

  LeveledTexturedMesh* tileMesh = getMesh(tile);
  if (tileMesh == NULL) {
    return;
  }

  const TextureIDReference* glTextureIDRetainedCopy = glTextureID->createCopy();

  const int level = tile->_level - ancestorTile->_level;
  if (!tileMesh->setGLTextureIDForLevel(level, glTextureIDRetainedCopy)) {
    delete glTextureIDRetainedCopy;
  }
}

bool DefaultTileTexturizer::onTerrainTouchEvent(const G3MEventContext* ec,
                                                const Geodetic3D& position,
                                                const Tile* tile,
                                                LayerSet* layerSet) {
  return (layerSet == NULL) ? false : layerSet->onTerrainTouchEvent(ec, position, tile);
}

void DefaultTileTexturizer::setDefaultBackgroundImage(const IImage* defaultBackgroundImage) {
  _defaultBackgroundImage = defaultBackgroundImage;
}

void DefaultTileTexturizer::setDefaultBackgroundImageName(const std::string& defaultBackgroundImageName) {
  _defaultBackgroundImageName = defaultBackgroundImageName;
}

void DefaultTileTexturizer::setDefaultBackgroundImageLoaded(const bool defaultBackgroundImageLoaded) {
  _defaultBackgroundImageLoaded = defaultBackgroundImageLoaded;
}
