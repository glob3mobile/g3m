//
//  G3MElevationsDemoScene.hpp
//  G3MApp
//
//  Created by Sebastian Ortega Trujillo on 10/3/16.
//

#ifndef __G3MApp__G3MElevationsDemoScene_hpp__
#define __G3MApp__G3MElevationsDemoScene_hpp__

#include "G3MDemoScene.hpp"
#include "G3MiOSSDK/Sector.hpp"
#include <G3MiOSSDK/SGShape.hpp>

class G3MElevationsDemoScene : public G3MDemoScene {
private:
    void loadElevs(std::string layerServer,const Sector &layerSector, float vertEx);

protected:
    void rawActivate(const G3MContext* context);
    
    void rawSelectOption(const std::string& option,
                         int optionIndex);

public:
    G3MElevationsDemoScene(G3MDemoModel* model) :
    G3MDemoScene(model, "Elevation layers", "", 0)
    {
        _options.push_back("World layer");
        _options.push_back("World variable layer");
        _options.push_back("European layer");
        _options.push_back("European variable layer");
    }
    
};


#endif /* G3MElevationsDemoScene_hpp */
