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
class DEMProvider;
class Sector;
class ChangedRendererInfoListener;
class IImageBuilder;
class PlanetRenderer;


class PlanetRendererBuilder {

private:
  mutable TileTessellator* _tileTessellator;
  mutable TileTexturizer* _texturizer;
  mutable std::vector<GEOVectorLayer*> _geoVectorLayers;
  mutable TileLODTester* _tileLODTester;
  mutable TileVisibilityTester* _tileVisibilityTester;

  mutable LayerSet* _layerSet;
  mutable TilesRenderParameters* _parameters;
  bool _showStatistics;
  bool _renderDebug;
  bool _incrementalTileQuality;
  Quality _quality;
  mutable std::vector<VisibleSectorListener*>* _visibleSectorListeners;
  mutable std::vector<long long>* _stabilizationMilliSeconds;
  long long _tileTextureDownloadPriority;

  bool _verboseTileTexturizerErrors;

  ElevationDataProvider* _elevationDataProvider;
  DEMProvider* _demProvider;
  mutable float _verticalExaggeration;

  TileTessellator* getTileTessellator() const;
  TileTexturizer* getTexturizer() const;

  LayerSet* getLayerSet() const;
  TilesRenderParameters* getParameters() const;
  bool getShowStatistics() const;
  bool getVerboseTileTexturizerErrors() const;

  bool getRenderDebug() const;
  bool getIncrementalTileQuality() const;
  std::vector<VisibleSectorListener*>* getVisibleSectorListeners() const;
  std::vector<long long>* getStabilizationMilliSeconds() const;
  long long getTileTextureDownloadPriority() const;

  bool _logTilesPetitions;

  LayerSet* createLayerSet() const;
  TilesRenderParameters* createPlanetRendererParameters() const;
  TileTessellator* createTileTessellator() const;

  ElevationDataProvider*    getElevationDataProvider() const;
  DEMProvider* getDEMProvider() const;

  float getVerticalExaggeration() const;

  Sector* _renderedSector;
  Sector getRenderedSector() const;

  bool _renderTileMeshes;
  bool getRenderTileMeshes() const;

  bool getLogTilesPetitions() const;

  ChangedRendererInfoListener* _changedInfoListener;
  
  TouchEventType _touchEventTypeOfTerrainTouchListener;
  
  TouchEventType getTouchEventTypeOfTerrainTouchListener() const;
  
  mutable IImageBuilder* _defaultTileBackgroundImage = NULL;
  
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
  void setVerboseTileTexturizerErrors(const bool verboseTileTexturizerErrors);
  void setRenderDebug(const bool renderDebug);
  void setIncrementalTileQuality(const bool incrementalTileQuality);
  void addVisibleSectorListener(VisibleSectorListener* listener,
                                const TimeInterval& stabilizationInterval);
  void addVisibleSectorListener(VisibleSectorListener* listener) {
    addVisibleSectorListener(listener, TimeInterval::zero());
  }
  void setTileTextureDownloadPriority(long long tileTextureDownloadPriority);

  void setElevationDataProvider(ElevationDataProvider* elevationDataProvider);

  void setDEMProvider(DEMProvider* demProvider);

  void setVerticalExaggeration(float verticalExaggeration);

  void setRenderedSector(const Sector& sector);

  GEOVectorLayer* createGEOVectorLayer() const;

  Quality getQuality() const;
  void setQuality(Quality quality);

  void setRenderTileMeshes(bool renderTileMeshes);

  void setLogTilesPetitions(bool logTilesPetitions);
  
  ChangedRendererInfoListener* getChangedRendererInfoListener() const;
  
  void setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener);
  
  void setTouchEventTypeOfTerrainTouchListener(TouchEventType touchEventTypeOfTerrainTouchListener);
  
  void setDefaultTileBackgroundImage(IImageBuilder* defaultTileBackgroundImage);
  
  void setTileLODTester(TileLODTester* tlt);
  
  TileLODTester* getTileLODTester() const;

  TileVisibilityTester* getTileVisibilityTester() const;

};

#endif
