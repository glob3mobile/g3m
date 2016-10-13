//
//  PlanetRendererBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#ifndef __G3MiOSSDK__PlanetRendererBuilder__
#define __G3MiOSSDK__PlanetRendererBuilder__

#include <vector>
#include "TilesRenderParameters.hpp"
#include "TouchEvent.hpp"
#include "TimeInterval.hpp"

class TileTessellator;
class TileTexturizer;
class GEOVectorLayer;
class TileLODTester;
class TileVisibilityTester;
class LayerSet;
class VisibleSectorListener;
class ElevationDataProvider;
class TerrainElevationProvider;
class Sector;
class ChangedRendererInfoListener;
class IImageBuilder;
class PlanetRenderer;


class PlanetRendererBuilder {

private:
  TileTessellator* _tileTessellator;
  TileTexturizer* _texturizer;
  std::vector<GEOVectorLayer*> _geoVectorLayers;
  TileLODTester* _tileLODTester;
  TileVisibilityTester* _tileVisibilityTester;

  LayerSet* _layerSet;
  TilesRenderParameters* _parameters;
  bool _showStatistics;
  bool _renderDebug;
  bool _incrementalTileQuality;
  Quality _quality;
  std::vector<VisibleSectorListener*>* _visibleSectorListeners;
  std::vector<long long>* _stabilizationMilliSeconds;
  long long _tileTextureDownloadPriority;

  ElevationDataProvider*    _elevationDataProvider;
  TerrainElevationProvider* _terrainElevationProvider;
  float _verticalExaggeration;

  TileTessellator* getTileTessellator();
  TileTexturizer* getTexturizer();

  LayerSet* getLayerSet();
  TilesRenderParameters* getParameters();
  bool getShowStatistics();
  bool getRenderDebug();
  bool getIncrementalTileQuality();
  std::vector<VisibleSectorListener*>* getVisibleSectorListeners();
  std::vector<long long>* getStabilizationMilliSeconds();
  long long getTileTextureDownloadPriority();

  bool _logTilesPetitions;

  LayerSet* createLayerSet();
  TilesRenderParameters* createPlanetRendererParameters();
  TileTessellator* createTileTessellator();

  ElevationDataProvider*    getElevationDataProvider();
  TerrainElevationProvider* getTerrainElevationProvider();

  float getVerticalExaggeration();

  Sector* _renderedSector;
  Sector getRenderedSector();

  bool _renderTileMeshes;
  bool getRenderTileMeshes();

  bool getLogTilesPetitions();

  ChangedRendererInfoListener* _changedInfoListener;
  
  TouchEventType _touchEventTypeOfTerrainTouchListener;
  
  TouchEventType getTouchEventTypeOfTerrainTouchListener();
  
  IImageBuilder* _defaultTileBackgroundImage = NULL;
  
  IImageBuilder* getDefaultTileBackgroundImageBuilder() const;
  
  TileLODTester* createDefaultTileLODTester() const;

  TileVisibilityTester* createDefaultTileVisibilityTester() const;


public:
  PlanetRendererBuilder();
  ~PlanetRendererBuilder();
  PlanetRenderer* create();
  void setTileTessellator(TileTessellator* tileTessellator);
  void setTileTexturizer(TileTexturizer* tileTexturizer);
  void setLayerSet(LayerSet* layerSet);
  void setPlanetRendererParameters(TilesRenderParameters* parameters);
  void setShowStatistics(const bool showStatistics);
  void setRenderDebug(const bool renderDebug);
  void setIncrementalTileQuality(const bool incrementalTileQuality);
  void addVisibleSectorListener(VisibleSectorListener* listener,
                                const TimeInterval& stabilizationInterval);
  void addVisibleSectorListener(VisibleSectorListener* listener) {
    addVisibleSectorListener(listener, TimeInterval::zero());
  }
  void setTileTextureDownloadPriority(long long tileTextureDownloadPriority);

  void setElevationDataProvider(ElevationDataProvider* elevationDataProvider);

  void setTerrainElevationProvider(TerrainElevationProvider* terrainElevationProvider);

  void setVerticalExaggeration(float verticalExaggeration);

  void setRenderedSector(const Sector& sector);

  GEOVectorLayer* createGEOVectorLayer();

  Quality getQuality() const;
  void setQuality(Quality quality);

  void setRenderTileMeshes(bool renderTileMeshes);

  void setLogTilesPetitions(bool logTilesPetitions);
  
  ChangedRendererInfoListener* getChangedRendererInfoListener();
  
  void setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener);
  
  void setTouchEventTypeOfTerrainTouchListener(TouchEventType _touchEventTypeOfTerrainTouchListener);
  
  void setDefaultTileBackgroundImage(IImageBuilder* defaultTileBackgroundImage);
  
  void setTileLODTester(TileLODTester* tlt);
  
  TileLODTester* getTileLODTester();

  TileVisibilityTester* getTileVisibilityTester();

};

#endif
