//
//  BuildingRenderer.hpp
//  G3MiOSSDK
//
//  Created by Pratik Prakash on 2/27/15.
//
//

#ifndef __G3MiOSSDK__BuildingRenderer__
#define __G3MiOSSDK__BuildingRenderer__

class Tile;
class TileTessellator;
class LayerSet;
class VisibleSectorListenerEntry;
class VisibleSectorListener;
class ElevationDataProvider;
class LayerTilesRenderParameters;
class TerrainTouchListener;
class ChangedInfoListener;

//I'm not 100% sure if I need all of these imports
#include <stdio.h>
#include "IStringBuilder.hpp"
#include "DefaultRenderer.hpp"
#include "Sector.hpp"
#include "Tile.hpp"
#include "Camera.hpp"
#include "LayerSet.hpp"
#include "ITileVisitor.hpp"
#include "SurfaceElevationProvider.hpp"
#include "ChangedListener.hpp"
#include "TouchEvent.hpp"
#include "TileRenderingListener.hpp"

/*
 * BuildingRenderer renders a given building mesh
 */
class BuildingRenderer : public DefaultRenderer {
public:
    BuildingRenderer();
    ~BuildingRenderer();
    
    void setEnable(bool enable);
    void onChangedContext();
    void onLostContext();
    void render(const G3MRenderContext* rc,
                GLState* glState);
    bool onTouchEvent(const G3MEventContext* ec,
                              const TouchEvent* touchEvent);
    void onResizeViewportEvent(const G3MEventContext* ec,
                                       int width, int height);
    
    void start(const G3MRenderContext* rc);
    
    void stop(const G3MRenderContext* rc);
    
    TileRenderingListener* getTileRenderingListener();
    
private:

};

#endif /* defined(__G3MiOSSDK__BuildingRenderer__) */
