//
//  TileTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by fpulido on 26/03/14.
//
//

#include "TileTextureBuilder.hpp"
#include "IDownloader.hpp"
#include "RectangleI.hpp"
#include "TextureIDReference.hpp"
#include "IImageUtils.hpp"
#include "IImageListener.hpp"
#include "TexturesHandler.hpp"
#include "IFactory.hpp"
#include "LayerTilesRenderParameters.hpp"


class BuilderDownloadStepDownloadListener : public IImageDownloadListener {
private:
    TileTextureBuilder* _builder;
    const int           _position;
    
public:
    BuilderDownloadStepDownloadListener(TileTextureBuilder* builder,
                                        int position);
    
    void onDownload(const URL& url,
                    IImage* image,
                    bool expired);
    
    void onError(const URL& url);
    
    void onCanceledDownload(const URL& url,
                            IImage* image,
                            bool expired) {
    }
    
    void onCancel(const URL& url);
    
    virtual ~BuilderDownloadStepDownloadListener();
    
};

class LTMInitializer : public LazyTextureMappingInitializer {
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
    
    const bool _mercator;
    
public:
    LTMInitializer(const Vector2I& resolution,
                   const Tile* tile,
                   const Tile* ancestor,
                   const TileTessellator* tessellator,
                   bool mercator) :
    _resolution(resolution),
    _tile(tile),
    _ancestor(ancestor),
    _tessellator(tessellator),
    _translationU(0),
    _translationV(0),
    _scaleU(1),
    _scaleV(1),
    _mercator(mercator)
    {
        
    }
    
    virtual ~LTMInitializer() {
#ifdef JAVA_CODE
        super.dispose();
#endif
        
    }
    
    void initialize() {
        // The default scale and translation are ok when (tile == _ancestor)
        if (_tile != _ancestor) {
            const Sector tileSector = _tile->_sector;
            
            const Vector2F lowerTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                         tileSector._lower,
                                                                         _mercator);
            
            const Vector2F upperTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                         tileSector._upper,
                                                                         _mercator);
            
            //      _scale       = MutableVector2D(upperTextCoordUV._x - lowerTextCoordUV._x,
            //                                     lowerTextCoordUV._y - upperTextCoordUV._y);
            //
            //      _translation = MutableVector2D(lowerTextCoordUV._x,
            //                                     upperTextCoordUV._y);
            
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
        return _tessellator->createTextCoords(_resolution, _tile, _mercator);
    }
    
};


//class TextureUploader : public IImageListener {
//private:
//    TileTextureBuilder* _builder;
//    const Tile* _tile;
//    const bool  _mercator;
//    
//    TileRasterizer* _tileRasterizer;
//    
//#ifdef C_CODE
//    const std::vector<RectangleF*> _srcRects;
//    const std::vector<RectangleF*> _dstRects;
//#endif
//#ifdef JAVA_CODE
//    private final java.util.ArrayList<RectangleF> _srcRects;
//    private final java.util.ArrayList<RectangleF> _dstRects;
//#endif
//    
//    const std::string _textureId;
//    
//public:
//    TextureUploader(TileTextureBuilder* builder,
//                    const Tile* tile,
//                    bool mercator,
//                    TileRasterizer* tileRasterizer,
//                    std::vector<RectangleF*> srcRects,
//                    std::vector<RectangleF*> dstRects,
//                    const std::string& textureId) :
//    _builder(builder),
//    _tile(tile),
//    _mercator(mercator),
//    _tileRasterizer(tileRasterizer),
//    _srcRects(srcRects),
//    _dstRects(dstRects),
//    _textureId(textureId)
//    {
//        ILogger::instance()->logInfo("nace TextureUploader: %p, [%d,%d]",this, _tile->_row,_tile->_column );//fpulido
//    }
//    
//    void imageCreated(const IImage* image);
//    
//    
//    ~TextureUploader(){
//        ILogger::instance()->logInfo("muere TextureUploader: %p, [%d,%d]",this, _tile->_row,_tile->_column );//fpulido
//    }
//    
//};

class TextureUploader : public IImageListener {
private:
    TileTextureBuilder* _builder;
    const Tile* _tile;
    const bool  _mercator;
    
    TileRasterizer* _tileRasterizer;
    
#ifdef C_CODE
    const std::vector<RectangleF*> _srcRects;
    const std::vector<RectangleF*> _dstRects;
#endif
#ifdef JAVA_CODE
    private final java.util.ArrayList<RectangleF> _srcRects;
    private final java.util.ArrayList<RectangleF> _dstRects;
#endif
    
    const std::string _textureId;
    
    const TileRasterizerContext* _trc;//fpulido
    
public:
    TextureUploader(TileTextureBuilder* builder,
//                    const Tile* tile,
//                    bool mercator,
                    const TileRasterizerContext* trc,
                    TileRasterizer* tileRasterizer,
                    std::vector<RectangleF*> srcRects,
                    std::vector<RectangleF*> dstRects,
                    const std::string& textureId) :
    _builder(builder),
    _tile(trc->_tile),
    _mercator(trc->_mercator),
//    _trc(tile,mercator),
    _trc(trc),
    _tileRasterizer(tileRasterizer),
    _srcRects(srcRects),
    _dstRects(dstRects),
    _textureId(textureId)
    {
        ILogger::instance()->logInfo("nace TextureUploader: %p, [%d,%d]",this, _tile->_row,_tile->_column );//fpulido
        //_trc = new TileRasterizerContext(_tile,_mercator);
    }
    
    void imageCreated(const IImage* image);
    
    ~TextureUploader(){
        ILogger::instance()->logInfo("muere TextureUploader: %p, [%d,%d]",this, _tile->_row,_tile->_column );//fpulido
    }
    
};


TileTextureBuilder::~TileTextureBuilder() {
    if (!_finalized && !_canceled) {
        cancel();
    }
    ILogger::instance()->logInfo("MUERE TileTextureBuilder: %p, [%d,%d]",this, _tile->_row,_tile->_column );//fpulido
    deletePetitions();
#ifdef JAVA_CODE
    super.dispose();
#endif
    
}

const std::vector<Petition*> TileTextureBuilder::cleanUpPetitions(const std::vector<Petition*>& petitions) const {
    
    const int petitionsSize = petitions.size();
    if (petitionsSize <= 1) {
        return petitions;
    }
    
    std::vector<Petition*> result;
    for (int i = 0; i < petitionsSize; i++) {
        Petition* currentPetition = petitions[i];
        const Sector currentSector = currentPetition->getSector();
        
        bool coveredByFollowingPetition = false;
        for (int j = i+1; j < petitionsSize; j++) {
            Petition* followingPetition = petitions[j];
            
            // only opaque petitions can cover
            if (!followingPetition->isTransparent()) {
                if (followingPetition->getSector().fullContains(currentSector)) {
                    coveredByFollowingPetition = true;
                    break;
                }
            }
        }
        
        if (coveredByFollowingPetition) {
            delete currentPetition;
        }
        else {
            result.push_back(currentPetition);
        }
    }
    
    return result;
}


//fpulido, remove later
void TileTextureBuilder::_retain() const {
//    _contador++;
//    _maxcontador++;
    RCObject::_retain();
    //ILogger::instance()->logInfo("called _retain");
}

//fpulido, remove later
void TileTextureBuilder::_release() const {
//    _contador--;
//    if(_contador == 0){
//        ILogger::instance()->logInfo("MAX CONTADOR [%d,%d]= %d", _tile->_row, _tile->_column, _maxcontador);
//    }
    RCObject::_release();
    //ILogger::instance()->logInfo("called _release");
}


TileTextureBuilder::TileTextureBuilder(MultiLayerTileTexturizer*         texturizer,
                                       TileRasterizer*                   tileRasterizer,
                                       const G3MRenderContext*           rc,
                                       const LayerTilesRenderParameters* layerTilesRenderParameters,
                                       const std::vector<Petition*>&     petitions,
                                       IDownloader*                      downloader,
                                       Tile*                             tile,
                                       const Mesh*                       tessellatorMesh,
                                       const TileTessellator*            tessellator,
                                       long long                         texturePriority,
                                       bool                              logTilesPetitions) :
_texturizer(texturizer),
_tileRasterizer(tileRasterizer),
_texturesHandler(rc->getTexturesHandler()),
_tileTextureResolution( layerTilesRenderParameters->_tileTextureResolution ),
_tileMeshResolution( layerTilesRenderParameters->_tileMeshResolution ),
_mercator( layerTilesRenderParameters->_mercator ),
_downloader(downloader),
_tile(tile),
_tessellatorMesh(tessellatorMesh),
_stepsDone(0),
_mesh(NULL),
_tessellator(tessellator),
_finalized(false),
_canceled(false),
_alreadyStarted(false),
_texturePriority(texturePriority),
_logTilesPetitions(logTilesPetitions),
_textureComposedAndUploaded(false)
{
    _petitions = cleanUpPetitions( petitions );
    
    _petitionsCount = _petitions.size();
    
    for (int i = 0; i < _petitionsCount; i++) {
        _status.push_back(STATUS_PENDING);
    }
    
    _mesh = createMesh();
}

void TileTextureBuilder::start() {
    if (_canceled) {
        return;
    }
    if (_alreadyStarted) {
        return;
    }
    _alreadyStarted = true;
    
    if (_tile == NULL) {
        return;
    }
    
    for (int i = 0; i < _petitionsCount; i++) {
        const Petition* petition = _petitions[i];
        
        const long long priority = _texturePriority + _tile->_level;
        
        if (_logTilesPetitions) {
            ILogger::instance()->logInfo("Tile petition \"%s\"", petition->getURL().getPath().c_str());
        }
        
        const long long requestId = _downloader->requestImage(URL(petition->getURL()),
                                                              priority,
                                                              petition->getTimeToCache(),
                                                              petition->getReadExpired(),
                                                              new BuilderDownloadStepDownloadListener(this, i),
                                                              true);
        if (requestId >= 0) {
            _requestsIds.push_back(requestId);
        }
    }
}

RectangleF* TileTextureBuilder::getInnerRectangle(int wholeSectorWidth, int wholeSectorHeight,
                                                  const Sector& wholeSector,
                                                  const Sector& innerSector) const {
    //printf("%s - %s\n", wholeSector.description().c_str(), innerSector.description().c_str());
    
    const double widthFactor  = innerSector._deltaLongitude.div(wholeSector._deltaLongitude);
    const double heightFactor = innerSector._deltaLatitude.div(wholeSector._deltaLatitude);
    
    const Vector2D lowerUV = wholeSector.getUVCoordinates(innerSector.getNW());
    
    return new RectangleF((float) (lowerUV._x   * wholeSectorWidth),
                          (float) (lowerUV._y   * wholeSectorHeight),
                          (float) (widthFactor  * wholeSectorWidth),
                          (float) (heightFactor * wholeSectorHeight));
}

bool TileTextureBuilder::composeAndUploadTexture() {
#ifdef JAVA_CODE
    synchronized (this) {
#endif
        
        if (_mesh == NULL) {
            return false;
        }
        
        std::vector<const IImage*>     images;
        std::vector<RectangleF*> sourceRects;
        std::vector<RectangleF*> destRects;
        std::vector<float> transparencies;
        std::string textureId = _tile->getKey().tinyDescription();
        
        const Sector tileSector = _tile->_sector;
        
        for (int i = 0; i < _petitionsCount; i++) {
            const Petition* petition = _petitions[i];
            IImage* image = petition->getImage();
            
            if (image != NULL) {
                const Sector imageSector = petition->getSector();
                //Finding intersection image sector - tile sector = srcReq
                const Sector intersectionSector = tileSector.intersection(imageSector);
                
                RectangleF* sourceRect = NULL;
                if (!intersectionSector.isEquals(imageSector)) {
                    sourceRect = getInnerRectangle(image->getWidth(), image->getHeight(),
                                                   imageSector,
                                                   intersectionSector);
                }
                else {
                    sourceRect = new RectangleF(0, 0,
                                                image->getWidth(), image->getHeight());
                }
                
                //Part of the image we are going to draw
                sourceRects.push_back(sourceRect);
                
                images.push_back(image);
                
                //Where we are going to draw the image
                destRects.push_back(getInnerRectangle(_tileTextureResolution._x,
                                                      _tileTextureResolution._y,
                                                      tileSector,
                                                      intersectionSector));
                textureId += petition->getURL().getPath();
                textureId += "_";
                
                //Layer transparency set by user
                transparencies.push_back(petition->getLayerTransparency());
            }
            else{
                return false;
            }
        }
        
        if (images.size() > 0) {
            
            if (_tileRasterizer != NULL) {
                textureId += "_";
                textureId += _tileRasterizer->getId();
            }
            
            if (images.size() != transparencies.size()) {
                ILogger::instance()->logError("Wrong number of transparencies");
            }
            
            IImageUtils::combine(_tileTextureResolution,
                                 images,
                                 sourceRects,
                                 destRects,
                                 transparencies,
                                 new TextureUploader(this,
                                                     new TileRasterizerContext(_tile, _mercator),
                                                     _tileRasterizer,
                                                     sourceRects,
                                                     destRects,
                                                     textureId),
                                 true);
            return true;
        }
        
        return false;
        
#ifdef JAVA_CODE
    }
#endif
}

void TileTextureBuilder::imageCreated(const IImage* image,
                                      std::vector<RectangleF*> srcRects,
                                      std::vector<RectangleF*> dstRects,
                                      const std::string& textureId) {
#ifdef JAVA_CODE
    synchronized (this) {
#endif
        
        if (_mesh != NULL) {

            const bool isMipmap = false;
            
            const TextureIDReference* glTextureId = _texturesHandler->getTextureIDReference(image,
                                                                                            GLFormat::rgba(),
                                                                                            textureId,
                                                                                            isMipmap);
            
            if (glTextureId != NULL) {
                if (!_mesh->setGLTextureIdForLevel(0, glTextureId)) {
                    delete glTextureId;
                    //_texturesHandler->releaseGLTextureId(glTextureId);
                }
            }
        }
        
        IFactory::instance()->deleteImage(image);
        
        for (int i = 0; i < srcRects.size(); i++) {
            delete srcRects[i];
        }
        
        for (int i = 0; i < dstRects.size(); i++) {
            delete dstRects[i];
        }
        
#ifdef JAVA_CODE
    }
#endif
}

void TileTextureBuilder::done() {
    if (!_finalized) {
        _finalized = true;
        
        if (!_canceled && (_tile != NULL) && (_mesh != NULL)) {
            if (composeAndUploadTexture()) {
//                if(!_tileRasterizer->backgroundRasterize()){
//                     //If the image could be properly turn into texture
//                    _tile->setTextureSolved(true); //fpulido
//                    deletePetitions();    //We must release the petitions so we can get rid off no longer needed images
//                }
//                else{
                    _textureComposedAndUploaded = true;
//                }
            }
        }
        
    }
}

void TileTextureBuilder::deletePetitions() {
    for (int i = 0; i < _petitionsCount; i++) {
        Petition* petition = _petitions[i];
        delete petition;
    }
    _petitions.clear();
    _petitionsCount = 0;
}

void TileTextureBuilder::stepDone() {
    _stepsDone++;
    
    if (_stepsDone == _petitionsCount) {
        //      if (_anyCanceled) {
        //        ILogger::instance()->logInfo("Completed with cancelation\n");
        //      }
        
        done();
    }
}

void TileTextureBuilder::cancel() {
    if (_canceled) {
        return;
    }
    
    _canceled = true;
    
    if (!_finalized) {
        for (int i = 0; i < _requestsIds.size(); i++) {
            const long long requestId = _requestsIds[i];
            _downloader->cancelRequest(requestId);
        }
    }
    _requestsIds.clear();
}

bool TileTextureBuilder::isCanceled() const {
    return _canceled;
}

//  void checkIsPending(int position) const {
//    if (_status[position] != STATUS_PENDING) {
//      ILogger::instance()->logError("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n",
//                                    position,
//                                    _status[position]);
//    }
//  }

void TileTextureBuilder::stepDownloaded(int position,
                                        IImage* image) {
    if (_canceled) {
        IFactory::instance()->deleteImage(image);
        return;
    }
    //checkIsPending(position);
    
    _status[position]  = STATUS_DOWNLOADED;
    _petitions[position]->setImage( image );
    
    stepDone();
}

void TileTextureBuilder::stepCanceled(int position) {
    if (_canceled) {
        return;
    }
    //checkIsPending(position);
    
    //    _anyCanceled = true;
    _status[position] = STATUS_CANCELED;
    
    stepDone();
}

LeveledTexturedMesh* TileTextureBuilder::createMesh() const {
    std::vector<LazyTextureMapping*>* mappings = new std::vector<LazyTextureMapping*>();
    
    Tile* ancestor = _tile;
    bool fallbackSolved = false;
    while (ancestor != NULL && !fallbackSolved) {
        const bool ownedTexCoords = true;
        const bool transparent    = false;
        LazyTextureMapping* mapping = new LazyTextureMapping(new LTMInitializer(_tileMeshResolution,
                                                                                _tile,
                                                                                ancestor,
                                                                                _tessellator,
                                                                                _mercator),
                                                             ownedTexCoords,
                                                             transparent);
        
        if (ancestor != _tile) {
            const TextureIDReference* glTextureId = _texturizer->getTopLevelTextureIdForTile(ancestor);
            if (glTextureId != NULL) {
                TextureIDReference* glTextureIdRetainedCopy = glTextureId->createCopy();
                
                //          _texturesHandler->retainGLTextureId(glTextureId);
                mapping->setGLTextureId(glTextureIdRetainedCopy);
                fallbackSolved = true;
            }
        }
        
        mappings->push_back(mapping);
        
        ancestor = ancestor->getParent();
    }
    
    return new LeveledTexturedMesh(_tessellatorMesh,
                                   false,
                                   mappings);
}

LeveledTexturedMesh* TileTextureBuilder::getMesh() {
    return _mesh;
}

void TileTextureBuilder::cleanMesh() {
#ifdef JAVA_CODE
    synchronized (this) {
#endif
        
        if (_mesh != NULL) {
            _mesh = NULL;
        }
        
#ifdef JAVA_CODE
    }
#endif
}

void TileTextureBuilder::cleanTile() {
    if (_tile != NULL) {
        _tile = NULL;
    }
}


TileTextureBuilderHolder::~TileTextureBuilderHolder() {
    ILogger::instance()->logInfo("MUERE TileTextureBuilderHolder: %p", this->get());//fpulido
    if (_builder != NULL) {
        _builder->_release();
    }
}

BuilderDownloadStepDownloadListener::BuilderDownloadStepDownloadListener(TileTextureBuilder* builder,
                                                                         int position) :
_builder(builder),
_position(position)
//_onDownload(0),
//_onError(0),
//_onCancel(0)
{
    _builder->_retain();
}

BuilderDownloadStepDownloadListener::~BuilderDownloadStepDownloadListener() {
    //  testState();
    
    if (_builder != NULL) {
        _builder->_release();
    }
    
#ifdef JAVA_CODE
    super.dispose();
#endif
    
}

//TileTextureBuilderHolder::~TileTextureBuilderHolder() {
//  if (_builder != NULL) {
//    _builder->_release();
//  }
//}

void BuilderDownloadStepDownloadListener::onDownload(const URL& url,
                                                     IImage* image,
                                                     bool expired) {
    //  _onDownload++;
    _builder->stepDownloaded(_position, image);
}

void BuilderDownloadStepDownloadListener::onError(const URL& url) {
    //  _onError++;
    _builder->stepCanceled(_position);
    ILogger::instance()->logError("Error downloading tile texture from \"%s\"", url.getPath().c_str());
}

void BuilderDownloadStepDownloadListener::onCancel(const URL& url) {
    //  _onCancel++;
    _builder->stepCanceled(_position);
}

void TextureUploader::imageCreated(const IImage* image) {

    if (_tileRasterizer == NULL) {
        _builder->imageCreated(image,
                               _srcRects,
                               _dstRects,
                               _textureId);
        
        _builder->composedAndUploaded();
    }
    else {
        //const TileRasterizerContext trc(_tile, _mercator);//fpulido
        const TileRasterizerContext* trc = new TileRasterizerContext(_tile, _mercator);
        _tileRasterizer->rasterize(image,
                                   *trc,
                                   new TextureUploader(_builder,
                                                       trc,
                                                       NULL,
                                                       _srcRects,
                                                       _dstRects,
                                                       _textureId),
                                   true);
        
    }
}

void TileTextureBuilder::composedAndUploaded() {
    //if (_textureComposedAndUploaded){
        //ILogger::instance()->logInfo("COMPOSED AND UPLOADED: [%d,%d]", _tile->_row, _tile->_column); //fpulido
        _tile->setTextureSolved(true);
        deletePetitions();
    //}
}
