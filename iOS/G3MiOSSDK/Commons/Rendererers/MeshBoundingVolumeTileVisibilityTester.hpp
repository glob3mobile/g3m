//
//  MeshBoundingVolumeTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#ifndef MeshBoundingVolumeTileVisibilityTester_hpp
#define MeshBoundingVolumeTileVisibilityTester_hpp

#include "TileVisibilityTester.hpp"


class MeshBoundingVolumeTileVisibilityTester : public TileVisibilityTester {
public:

  ~MeshBoundingVolumeTileVisibilityTester() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  virtual bool isVisible(const G3MRenderContext* rc,
                 const PlanetRenderContext* prc,
                 Tile* tile) const = 0;

  void onTileHasChangedMesh(const Tile* tile) const {}

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {}

  void renderStarted() const {}

};


class MeshBoundingBoxTileVisibilityTester : public MeshBoundingVolumeTileVisibilityTester {
public:
  bool isVisible(const G3MRenderContext* rc,
                 const PlanetRenderContext* prc,
                 Tile* tile) const;
};

class MeshBoundingOrientedBoxTileVisibilityTester : public MeshBoundingVolumeTileVisibilityTester {
public:
  bool isVisible(const G3MRenderContext* rc,
                 const PlanetRenderContext* prc,
                 Tile* tile) const;
};

class MeshBoundingSimpleOrientedBoxTileVisibilityTester : public MeshBoundingVolumeTileVisibilityTester {
public:
  bool isVisible(const G3MRenderContext* rc,
                 const PlanetRenderContext* prc,
                 Tile* tile) const;
};



#endif
