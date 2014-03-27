//
//  PlanetRendererBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#ifndef __G3MiOSSDK__PlanetRendererBuilder__
#define __G3MiOSSDK__PlanetRendererBuilder__

#include "LayerSet.hpp"
#include "TilesRenderParameters.hpp"
#include "PlanetRenderer.hpp"


class PlanetRendererBuilder {

private:
  TileTessellator* _tileTessellator;
  TileTexturizer* _texturizer;
  std::vector<TileRasterizer*> _tileRasterizers;

  LayerSet* _layerSet;
  TilesRenderParameters* _parameters;
  bool _showStatistics;
  bool _renderDebug;
  bool _useTilesSplitBudget;
  bool _forceFirstLevelTilesRenderOnStart;
  bool _incrementalTileQuality;
  Quality _quality;
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

  bool _logTilesPetitions;

  LayerSet* createLayerSet();
  TilesRenderParameters* createPlanetRendererParameters();
  TileTessellator* createTileTessellator();

  ElevationDataProvider* getElevationDataProvider();
  float getVerticalExaggeration();

  Sector* _renderedSector;
  Sector getRenderedSector();

  bool _renderTileMeshes;
  bool getRenderTileMeshes();

  bool getLogTilesPetitions();

  TileRenderingListener* _tileRenderingListener;

public:
  PlanetRendererBuilder();
  ~PlanetRendererBuilder();
  PlanetRenderer* create();
  void setTileTessellator(TileTessellator* tileTessellator);
  void setTileTexturizer(TileTexturizer* tileTexturizer);
  void addTileRasterizer(TileRasterizer* tileRasterizer);
  void setLayerSet(LayerSet* layerSet);
  void setPlanetRendererParameters(TilesRenderParameters* parameters);
  void setShowStatistics(const bool showStatistics);
  void setRenderDebug(const bool renderDebug);
  void setUseTilesSplitBudget(const bool useTilesSplitBudget);
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

  void setRenderedSector(const Sector& sector);

  GEOTileRasterizer* createGEOTileRasterizer();

  Quality getQuality() const;
  void setQuality(Quality quality);

  void setRenderTileMeshes(bool renderTileMeshes);

  void setLogTilesPetitions(bool logTilesPetitions);

  void setTileRenderingListener(TileRenderingListener* tileRenderingListener);

  TileRenderingListener* getTileRenderingListener();

};

#endif
