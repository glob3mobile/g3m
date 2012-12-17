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
    LayerSet* _layerSet;
    TilesRenderParameters* _parameters;
    bool _showStatistics;
    bool _renderDebug;
    bool _useTilesSplitBudget;
    bool _forceTopLevelTilesRenderOnStart;
    bool _incrementalTileQuality;
    
    LayerSet* createLayerSet();
    TilesRenderParameters* createTileRendererParameters();
    TileTessellator* createTileTessellator();
    
public:
    TileRendererBuilder();
    ~TileRendererBuilder();
    TileRenderer* create();
    void setTileTessellator(TileTessellator* tileTessellator);
    void setTileTexturizer(TileTexturizer* tileTexturizer);
    void setLayerSet(LayerSet* layerSet);
    void setTileRendererParameters(TilesRenderParameters* parameters);
    void setShowStatistics(const bool showStatistics);
    void setRenderDebug(const bool renderDebug);
    void setUseTilesSplitBuget(const bool useTilesSplitBudget);
    void setForceTopLevelTilesRenderOnStart(const bool forceTopLevelTilesRenderOnStart);
    void setIncrementalTileQuality(const bool incrementalTileQuality);

};

#endif /* defined(__G3MiOSSDK__TileRendererBuilder__) */
