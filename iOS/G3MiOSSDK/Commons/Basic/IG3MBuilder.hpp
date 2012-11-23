//
//  IG3MBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//

#ifndef __G3MiOSSDK__IG3MBuilder__
#define __G3MiOSSDK__IG3MBuilder__

#include <vector>
#include "CameraRenderer.hpp"
#include "CameraConstraints.hpp"
#include "Color.hpp"
#include "LayerSet.hpp"
#include "TilesRenderParameters.hpp"
#include "TileRenderer.hpp"
#include "GTask.hpp"
#include "PeriodicalTask.hpp"
#include "G3MWidget.hpp"



class IG3MBuilder {

private:
    INativeGL* _nativeGL;
    const Planet* _planet;
    std::vector<ICameraConstrainer*> _cameraConstraints;
    CameraRenderer* _cameraRenderer;
    Color* _backgroundColor;
    LayerSet* _layerSet;
    TilesRenderParameters* _parameters;
    TileRenderer* _tileRenderer;
    Renderer* _busyRenderer;
    std::vector<Renderer*> _renderers;
    GTask* _initializationTask;
    bool _autoDeleteInitializationTask;
    std::vector<PeriodicalTask*> _periodicalTasks;
    bool _logFPS;
    bool _logDownloaderStatistics;
    UserData* _userData;
    
    std::vector<ICameraConstrainer*> createCameraConstraints();
    CameraRenderer* createCameraRenderer();
    
protected:
    G3MWidget* create();
    
public:
    IG3MBuilder();
    ~IG3MBuilder();
    void setNativeGL(INativeGL* nativeGL);
    void setPlanet(const Planet* planet);
    void addCameraConstraint(ICameraConstrainer* cameraConstraint);
    void setCameraRenderer(CameraRenderer* cameraRenderer);
    void setBackgroundColor(Color* backgroundColor);
    void setLayerSet(LayerSet* layerSet);
    void setTileRendererParameters(TilesRenderParameters* parameters);
    void setTileRenderer(TileRenderer * tileRenderer);
    void setBusyRenderer(Renderer* busyRenderer);
    void addRenderer(Renderer* renderer);
    void setInitializationTask(GTask* initializationTask);
    void setAutoDeleteInitializationTask(const bool autoDeleteInitializationTask);
    void addPeriodicalTask(PeriodicalTask* periodicalTask);
    void setLogFPS(const bool logFPS);
    void setLogDownloaderStatistics(const bool logDownloaderStatistics);
    void setUserData(UserData* userData);
    
};


#endif /* defined(__G3MiOSSDK__IG3MBuilder__) */
