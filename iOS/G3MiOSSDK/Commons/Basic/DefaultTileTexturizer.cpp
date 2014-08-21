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

class DTT_LTMInitializer : public LazyTextureMappingInitializer {
private:
  const Tile* _tile;
  const Tile* _ancestor;

  float _translationU;
  float _translationV;
  float _scaleU;
  float _scaleV;

  const TileTessellator* _tessellator;
#ifdef C_CODE
  const Vector2I _resolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _resolution;
#endif

public:
  DTT_LTMInitializer(const Vector2I& resolution,
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
    return _tessellator->createTextCoords(_resolution, _tile);
  }

};

class DTT_TileTextureBuilder;





class DTT_TileImageListener : public TileImageListener {
private:
  DTT_TileTextureBuilder* _builder;
  const Tile* _tile;
#ifdef C_CODE
  const Vector2I       _tileTextureResolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _tileTextureResolution;
#endif
  
  RectangleF* getInnerRectangle(int wholeSectorWidth,
                                int wholeSectorHeight,
                                const Sector& wholeSector,
                                const Sector& innerSector) const;
  
public:
  DTT_TileImageListener(DTT_TileTextureBuilder* builder, const Tile* tile, const Vector2I& tileTextureResolution);

  virtual ~DTT_TileImageListener();

  void imageCreated(const std::string&           tileId,
                    const IImage*                image,
                    const std::string&           imageId,
                    const TileImageContribution* contribution);

  void imageCreationError(const std::string& tileId,
                          const std::string& error);

  void imageCreationCanceled(const std::string& tileId);
  
  
};


class DTT_TileTextureBuilder : public RCObject {
private:
  LeveledTexturedMesh* _texturedMesh;
  Tile*                _tile;
  const std::string    _tileId;
  TileImageProvider*   _tileImageProvider;
  TexturesHandler*     _texturesHandler;
#ifdef C_CODE
  const Vector2I       _tileTextureResolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _tileTextureResolution;
#endif
  const bool          _logTilesPetitions;
  const long long     _tileDownloadPriority;
  bool                _canceled;
  FrameTasksExecutor* _frameTasksExecutor;


  static const TextureIDReference* getTopLevelTextureIdForTile(Tile* tile) {
    LeveledTexturedMesh* mesh = (LeveledTexturedMesh*) tile->getTexturizedMesh();

    return (mesh == NULL) ? NULL : mesh->getTopLevelTextureId();
  }

  static LeveledTexturedMesh* createMesh(Tile* tile,
                                         const Mesh* tessellatorMesh,
                                         const Vector2I tileMeshResolution,
                                         const TileTessellator* tessellator) {
    std::vector<LazyTextureMapping*>* mappings = new std::vector<LazyTextureMapping*>();

    Tile* ancestor = tile;
    bool fallbackSolved = false;
    while (ancestor != NULL && !fallbackSolved) {
      const bool ownedTexCoords = true;
      const bool transparent    = false;
      LazyTextureMapping* mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution,
                                                                                  tile,
                                                                                  ancestor,
                                                                                  tessellator),
                                                           ownedTexCoords,
                                                           transparent);

      if (ancestor != tile) {
        const TextureIDReference* glTextureId = getTopLevelTextureIdForTile(ancestor);
        if (glTextureId != NULL) {
          TextureIDReference* glTextureIdRetainedCopy = glTextureId->createCopy();

          mapping->setGLTextureId(glTextureIdRetainedCopy);
          fallbackSolved = true;
        }
      }

      mappings->push_back(mapping);

      ancestor = ancestor->getParent();
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
                         long long                         tileDownloadPriority,
                         bool                              logTilesPetitions,
                         FrameTasksExecutor*               frameTasksExecutor) :
  _tileImageProvider(tileImageProvider),
  _texturesHandler(rc->getTexturesHandler()),
  _tileTextureResolution( layerTilesRenderParameters->_tileTextureResolution ),
  _tile(tile),
  _tileId(tile->_id),
  _texturedMesh( NULL ),
  _canceled(false),
  _tileDownloadPriority(tileDownloadPriority),
  _logTilesPetitions(logTilesPetitions),
  _frameTasksExecutor(frameTasksExecutor)
  {
    _tileImageProvider->_retain();

    _texturedMesh = createMesh(tile,
                               tessellatorMesh,
                               layerTilesRenderParameters->_tileMeshResolution,
                               tessellator);
  }

  LeveledTexturedMesh* getTexturedMesh() {
    return _texturedMesh;
  }

  void start() {
    if (!_canceled) {
      const TileImageContribution* contribution = _tileImageProvider->contribution(_tile);
      if (contribution == NULL) {
        if (_tile != NULL) {
          _tile->setTextureSolved(true);
        }
      }
      else {
        _tileImageProvider->create(_tile,
                                   contribution,
                                   _tileTextureResolution,
                                   _tileDownloadPriority,
                                   _logTilesPetitions,
                                   new DTT_TileImageListener(this, _tile, _tileTextureResolution),
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
      _tileImageProvider->cancel(_tileId);
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
                     const std::string& imageId) {
    const bool generateMipmap = true;

    const TextureIDReference* glTextureId = _texturesHandler->getTextureIDReference(image,
                                                                                    GLFormat::rgba(),
                                                                                    imageId,
                                                                                    generateMipmap);
    if (glTextureId == NULL) {
      return false;
    }

    if (!_texturedMesh->setGLTextureIdForLevel(0, glTextureId)) {
      delete glTextureId;
    }

    return true;
  }

  void imageCreated(const IImage*                image,
                    const std::string&           imageId,
                    const TileImageContribution* contribution) {
    if (!_canceled && (_tile != NULL) && (_texturedMesh != NULL)) {
      if (uploadTexture(image, imageId)) {
        _tile->setTextureSolved(true);
      }
    }

    delete image;
    TileImageContribution::releaseContribution( contribution );
  }

  void imageCreationError(const std::string& error) {
#warning Diego at work
    ILogger::instance()->logError("%s", error.c_str());
  }

  void imageCreationCanceled() {
#warning Diego at work
  }
};

class DTT_NotFullProviderImageListener : public IImageListener {
private:
  DTT_TileTextureBuilder* _builder;
  const IImage* _image;
  const std::string& _imageId;
  const TileImageContribution* _contribution;
  
public:
  DTT_NotFullProviderImageListener(DTT_TileTextureBuilder* builder,
                                   const std::string& imageId,
                                   const TileImageContribution* contribution) :
  _builder(builder),
  _imageId(imageId),
  _contribution(contribution)
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
    ILogger::instance()->logInfo("Image %s", image->description().c_str());
    _builder->imageCreated(image,_imageId,_contribution);
  }
};




DTT_TileImageListener::DTT_TileImageListener(DTT_TileTextureBuilder* builder, const Tile* tile, const Vector2I& tileTextureResolution) :
_builder(builder),
_tile(tile),
_tileTextureResolution(tileTextureResolution)
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

void DTT_TileImageListener::imageCreated(const std::string&           tileId,
                                         const IImage*                image,
                                         const std::string&           imageId,
                                         const TileImageContribution* contribution) {
    
#warning JM at WORK: CREAR CANVAS SOURCE RECTANGLE Y DEST RECTANGLE. EL LISTENER DEL CANVAS TIENE QUE HACER UN RETAIN Y UN RELEASE DEL BUILDER. METER DEFAULTIMAGEPROVIDER (CLASE QUE GENERA UNA IMAGEN). QUE RENDER STATE DEVUELVA BUSY HASTA QUE NO TENGA LA IMAGEN DEFAULT, PARA QUE PUEDA ARRANCAR EL GLOBO.
  
  
  
  
  if (!contribution->isFullCoverageAndOpaque()){

      std::string auxImageId = imageId + "|";
      
      // retain the singleResult->_contribution as the _listener take full ownership of the contribution
      TileImageContribution::retainContribution(contribution);
      
      ILogger::instance()->logInfo("DTT_TileImageListener received image that does not fit tile. Building new Image....");

      ICanvas* canvas = IFactory::instance()->createCanvas();
      
      const int _width =  _tileTextureResolution._x;
      
      const int _height =  _tileTextureResolution._y;

      const Sector tileSector = _tile->_sector;

      ILogger::instance()->logInfo("Tile " + _tile->description());

      canvas->initialize(_width, _height);
      
      const float   alpha = contribution->_alpha;
      const Sector* imageSector = contribution->getSector();
      
      const Sector visibleContributionSector = imageSector->intersection(tileSector);
      
      auxImageId += "_" + visibleContributionSector.description();

      
      const RectangleF* srcRect = getInnerRectangle(_width, _height,
                                                    *imageSector,
                                                    visibleContributionSector);
      
      const RectangleF* destRect = getInnerRectangle(_width, _height,
                                                     tileSector,
                                                     *imageSector);
    
      //We add "destRect->description()" to "auxImageId" for to differentiate cases of same "visibleContributionSector" at different levels of tiles
      auxImageId += "_" + destRect->description();

    
      ILogger::instance()->logInfo("destRect " + destRect->description());

      
      canvas->drawImage(image,
                        //SRC RECT
                        srcRect->_x, srcRect->_y,
                        srcRect->_width, srcRect->_height,
                        //DEST RECT
                        destRect->_x, destRect->_y,
                        destRect->_width, destRect->_height,
                        alpha);
      
      canvas->setLineColor(Color::magenta());
      canvas->strokeRectangle(destRect->_x, destRect->_y,
                              destRect->_width, destRect->_height);
      
      
      delete destRect;
      delete srcRect;
      
      canvas->createImage(new DTT_NotFullProviderImageListener(_builder, auxImageId, contribution), true);
      
      delete canvas;
    } else {
      _builder->imageCreated(image, imageId, contribution);
    }
}

void DTT_TileImageListener::imageCreationError(const std::string& tileId,
                                               const std::string& error) {
  _builder->imageCreationError(error);
}

void DTT_TileImageListener::imageCreationCanceled(const std::string& tileId) {
  _builder->imageCreationCanceled();
}

RectangleF* DTT_TileImageListener::getInnerRectangle(int wholeSectorWidth,
                                                                    int wholeSectorHeight,
                                                                    const Sector& wholeSector,
                                                                    const Sector& innerSector) const {
  if (wholeSector.isNan() || innerSector.isNan() || wholeSector.isEquals(innerSector)){
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

DefaultTileTexturizer::DefaultTileTexturizer(const IImageBuilder* defaultBackGroundImage) : _defaultBackGroundImage(defaultBackGroundImage) {
  
}


LeveledTexturedMesh* DefaultTileTexturizer::getMesh(Tile* tile) const {
  DTT_TileTextureBuilderHolder* tileBuilderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();
  return (tileBuilderHolder == NULL) ? NULL : tileBuilderHolder->get()->getTexturedMesh();
}


RenderState DefaultTileTexturizer::getRenderState(LayerSet* layerSet) {
  if (layerSet != NULL) {
    return layerSet->getRenderState();
  }
  return RenderState::ready();
}

void DefaultTileTexturizer::initialize(const G3MContext* context,
                                       const TilesRenderParameters* parameters) {
  // do nothing
}

Mesh* DefaultTileTexturizer::texturize(const G3MRenderContext* rc,
                                       const TileTessellator* tessellator,
//                                       TileRasterizer* tileRasterizer,
                                       const LayerTilesRenderParameters* layerTilesRenderParameters,
                                       const LayerSet* layerSet,
                                       bool forceFullRender,
                                       long long tileDownloadPriority,
                                       Tile* tile,
                                       Mesh* tessellatorMesh,
                                       Mesh* previousMesh,
                                       bool logTilesPetitions) {
  DTT_TileTextureBuilderHolder* builderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();

  //  TileImageProvider* tileImageProvider = new DebugTileImageProvider();
  //  TileImageProvider* tileImageProvider = new ChessboardTileImageProvider();

#warning TODO: creates the TileImageProvider from the LayerSet (and Rasterizer?)
  TileImageProvider* tileImageProvider = layerSet->getTileImageProvider(rc,
                                                                        layerTilesRenderParameters);

  if (tileImageProvider == NULL) {
#warning TODO: error callback
    tile->setTextureSolved(true);
    tile->setTexturizerDirty(false);
    return NULL;
  }

  DTT_TileTextureBuilder* builder;
  if (builderHolder == NULL) {
    builder = new DTT_TileTextureBuilder(rc,
                                         layerTilesRenderParameters,
                                         tileImageProvider,
                                         tile,
                                         tessellatorMesh,
                                         tessellator,
                                         tileDownloadPriority,
                                         logTilesPetitions,
                                         rc->getFrameTasksExecutor() );
    builderHolder = new DTT_TileTextureBuilderHolder(builder);
    tile->setTexturizerData(builderHolder);
  }
  else {
    builder = builderHolder->get();
  }

  // get the mesh just before calling start(), if not... the start(ed) task can finish immediately
  // and as one consequence the builder got deleted and the "builder" pointer becomes a dangling pointer
  Mesh* texturizedMesh = builder->getTexturedMesh();

  if (forceFullRender) {
    builder->start();
  }
  else {
    rc->getFrameTasksExecutor()->addPreRenderTask( new DTT_TileTextureBuilderStartTask(builder) );
  }

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

bool DefaultTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return false;
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

  const TextureIDReference* glTextureId = ancestorMesh->getTopLevelTextureId();
  if (glTextureId == NULL) {
    return;
  }

  LeveledTexturedMesh* tileMesh = getMesh(tile);
  if (tileMesh == NULL) {
    return;
  }

  const TextureIDReference* glTextureIdRetainedCopy = glTextureId->createCopy();

  const int level = tile->_level - ancestorTile->_level;
  if (!tileMesh->setGLTextureIdForLevel(level, glTextureIdRetainedCopy)) {
    delete glTextureIdRetainedCopy;
  }
}

bool DefaultTileTexturizer::onTerrainTouchEvent(const G3MEventContext* ec,
                                                const Geodetic3D& position,
                                                const Tile* tile,
                                                LayerSet* layerSet) {
  return (layerSet == NULL) ? false : layerSet->onTerrainTouchEvent(ec, position, tile);
}
