//
//  DefaultTileTexturizer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 4/16/14.
//
//

#ifndef __G3M__DefaultTileTexturizer__
#define __G3M__DefaultTileTexturizer__

#include "TileTexturizer.hpp"

class LeveledTexturedMesh;
class TextureIDReference;
class IImageBuilder;
class IImage;


class DefaultTileTexturizer : public TileTexturizer {
private:

  IImageBuilder* _defaultBackgroundImageBuilder;
  bool _defaultBackgroundImageLoaded;
#ifdef C_CODE
  const IImage* _defaultBackgroundImage;
#endif
#ifdef JAVA_CODE
  private IImage _defaultBackgroundImage;
#endif
  std::string _defaultBackgroundImageName;

  const bool _verboseErrors;

  LeveledTexturedMesh* getMesh(Tile* tile) const;

public:

  std::vector<std::string> _errors;


  DefaultTileTexturizer(IImageBuilder* defaultBackgroundImageBuilder,
                        const bool verboseErrors);

  virtual ~DefaultTileTexturizer() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  RenderState getRenderState(LayerSet* layerSet);

  void initialize(const G3MContext* context,
                  const TilesRenderParameters* parameters);

  Mesh* texturize(const G3MRenderContext*    rc,
                  const PlanetRenderContext* prc,
                  Tile* tile,
                  Mesh* tessellatorMesh,
                  Mesh* previousMesh);

  void tileToBeDeleted(Tile* tile,
                       Mesh* mesh);

  void tileMeshToBeDeleted(Tile* tile,
                           Mesh* mesh);

  void justCreatedTopTile(const G3MRenderContext* rc,
                          Tile* tile,
                          LayerSet* layerSet);

  void ancestorTexturedSolvedChanged(Tile* tile,
                                     Tile* ancestorTile,
                                     bool textureSolved);

  bool onTerrainTouchEvent(const G3MEventContext* ec,
                           const Geodetic3D& position,
                           const Tile* tile,
                           LayerSet* layerSet);

  const IImageBuilder* getDefaultBackgroundImageBuilder() const {
    return _defaultBackgroundImageBuilder;
  }

  const IImage* getDefaultBackgroundImage() const {
    return _defaultBackgroundImage;
  }

  void setDefaultBackgroundImage(const IImage* defaultBackgroundImage);

  const std::string getDefaultBackgroundImageName() const {
    return _defaultBackgroundImageName;
  }

  void setDefaultBackgroundImageName(const std::string& defaultBackgroundImageName);

  void setDefaultBackgroundImageLoaded(const bool defaultBackgroundImageLoaded);
  
};

#endif
