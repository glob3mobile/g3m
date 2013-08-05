//
//  TileRendererBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#ifndef __G3MiOSSDK__TileRendererBuilder__
#define __G3MiOSSDK__TileRendererBuilder__

#include "LayerSet.hpp"
#include "TilesRenderParameters.hpp"
#include "TileRenderer.hpp"


class TileRendererBuilder {

private:
  TileTessellator* _tileTessellator;
  TileTexturizer* _texturizer;
  TileRasterizer* _tileRasterizer;

  LayerSet* _layerSet;
  TilesRenderParameters* _parameters;
  bool _showStatistics;
  bool _renderDebug;
  bool _useTilesSplitBudget;
  bool _forceFirstLevelTilesRenderOnStart;
  bool _incrementalTileQuality;
  std::vector<VisibleSectorListener*>* _visibleSectorListeners;
  std::vector<long long>* _stabilizationMilliSeconds;
  long long _texturePriority;

  ElevationDataProvider* _elevationDataProvider;
  float _verticalExaggeration;
  
  TileTessellator* getTileTessellator();
  TileTexturizer* getTexturizer();
  TileRasterizer* getTileRasterizer();

  LayerSet* getLayerSet();
  TilesRenderParameters* getParameters();
  bool getShowStatistics();
  bool getRenderDebug();
  bool getUseTilesSplitBudget();
  bool getForceFirstLevelTilesRenderOnStart();
  bool getIncrementalTileQuality();
  std::vector<VisibleSectorListener*>* getVisibleSectorListeners();
  std::vector<long long>* getStabilizationMilliSeconds();
  long long getTexturePriority();

  LayerSet* createLayerSet();
  TilesRenderParameters* createTileRendererParameters();
  TileTessellator* createTileTessellator();

  ElevationDataProvider* getElevationDataProvider();
  float getVerticalExaggeration();

public:
  TileRendererBuilder();
  ~TileRendererBuilder();
  TileRenderer* create();
  void setTileTessellator(TileTessellator* tileTessellator);
  void setTileTexturizer(TileTexturizer* tileTexturizer);
  void setTileRasterizer(TileRasterizer* tileRasterizer);
  void setLayerSet(LayerSet* layerSet);
  void setTileRendererParameters(TilesRenderParameters* parameters);
  void setShowStatistics(const bool showStatistics);
  void setRenderDebug(const bool renderDebug);
  void setUseTilesSplitBuget(const bool useTilesSplitBudget);
  void setForceFirstLevelTilesRenderOnStart(const bool forceFirstLevelTilesRenderOnStart);
  void setIncrementalTileQuality(const bool incrementalTileQuality);
  void addVisibleSectorListener(VisibleSectorListener* listener,
                                const TimeInterval& stabilizationInterval);
  void addVisibleSectorListener(VisibleSectorListener* listener) {
    addVisibleSectorListener(listener, TimeInterval::zero());
  }
  void setTexturePriority(long long texturePriority);

  void setElevationDataProvider(ElevationDataProvider* elevationDataProvider);

  void setVerticalExaggeration(float verticalExaggeration);

};

#endif
