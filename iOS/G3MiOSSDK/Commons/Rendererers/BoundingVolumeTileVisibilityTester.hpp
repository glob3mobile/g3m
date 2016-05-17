//
//  BoundingVolumeTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujilo on 05/08/16.
//
//

#ifndef BoundingVolumeTileVisibilityTester_hpp
#define BoundingVolumeTileVisibilityTester_hpp

#include "TileVisibilityTester.hpp"


class BoundingVolumeTileVisibilityTester : public TileVisibilityTester {
public:

  ~BoundingVolumeTileVisibilityTester() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  bool isVisible(const G3MRenderContext* rc,
                 const PlanetRenderContext* prc,
                 Tile* tile) const;

  void onTileHasChangedMesh(const Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

  void renderStarted() const;

};

#endif
