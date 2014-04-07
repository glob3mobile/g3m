//
//  TileTextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by fpulido on 26/03/14.
//
//

#ifndef __G3MiOSSDK__TileTextureBuilder__
#define __G3MiOSSDK__TileTextureBuilder__

#include "ITexturizerData.hpp"
#include "RCObject.hpp"
#include "Petition.hpp"
#include "RectangleF.hpp"
#include "MultiLayerTileTexturizer.hpp"
#include "LeveledTexturedMesh.hpp"
#include "Tile.hpp"
#include "TileRasterizer.hpp"
#include "IImageDownloadListener.hpp"


enum TileTextureBuilder_PetitionStatus {
    STATUS_PENDING,
    STATUS_DOWNLOADED,
    STATUS_CANCELED
};


class TileTextureBuilder : public RCObject {
    
private:
    MultiLayerTileTexturizer* _texturizer;
    TileRasterizer*           _tileRasterizer;
    Tile*                     _tile;
    
    std::vector<Petition*> _petitions;
    int                    _petitionsCount;
    int                    _stepsDone;
    
    TexturesHandler* _texturesHandler;
//    mutable int _contador = 1;//fpulido
//    mutable int _maxcontador = 1;//fpulido
    
#ifdef C_CODE
    const Vector2I   _tileTextureResolution;
    const Vector2I   _tileMeshResolution;
#endif
#ifdef JAVA_CODE
    private final Vector2I _tileTextureResolution;
    private final Vector2I _tileMeshResolution;
#endif
    const bool       _mercator;
    
    IDownloader*     _downloader;
    
    const Mesh* _tessellatorMesh;
    
    const TileTessellator* _tessellator;
    
    const bool _logTilesPetitions;
    
    std::vector<TileTextureBuilder_PetitionStatus> _status;
    std::vector<long long>                         _requestsIds;
    
    
    bool _finalized;
    bool _canceled;
    bool _alreadyStarted;
    
    long long _texturePriority;
    
    bool _isComposedAndUploaded;
    
    ~TileTextureBuilder();
    
    const std::vector<Petition*> cleanUpPetitions(const std::vector<Petition*>& petitions) const;
    
public:
    LeveledTexturedMesh* _mesh;
    
    
    TileTextureBuilder(MultiLayerTileTexturizer*         texturizer,
                       TileRasterizer*                   tileRasterizer,
                       const G3MRenderContext*           rc,
                       const LayerTilesRenderParameters* layerTilesRenderParameters,
                       const std::vector<Petition*>&     petitions,
                       IDownloader*                      downloader,
                       Tile*                             tile,
                       const Mesh*                       tessellatorMesh,
                       const TileTessellator*            tessellator,
                       long long                         texturePriority,
                       bool                              logTilesPetitions);
    
    void _retain() const;
    void _release() const;
    
    
    void start();
    
    RectangleF* getInnerRectangle(int wholeSectorWidth, int wholeSectorHeight,
                                  const Sector& wholeSector,
                                  const Sector& innerSector) const;
    
    bool composeAndUploadTexture();
    
    void imageCreated(const IImage* image,
                      std::vector<RectangleF*> srcRects,
                      std::vector<RectangleF*> dstRects,
                      const std::string& textureId);
    
    void done();
    
    void deletePetitions();
    
    void stepDone();
    
    void cancel();
    
    bool isCanceled() const;
    
    void stepDownloaded(int position,
                        IImage* image);
    
    void stepCanceled(int position);
    
    LeveledTexturedMesh* createMesh() const;
    
    LeveledTexturedMesh* getMesh();
    
    void cleanMesh();
    void cleanTile();
    
    void composedAndUploaded();
    
};


class TileTextureBuilderHolder : public ITexturizerData {
private:
    TileTextureBuilder* _builder;
    
public:
    TileTextureBuilderHolder(TileTextureBuilder* builder) :
    _builder(builder)
    {
    }
    
    TileTextureBuilder* get() const {
        return _builder;
    }
    
    virtual ~TileTextureBuilderHolder();
    
};





#endif /* defined(__G3MiOSSDK__TileTextureBuilder__) */
