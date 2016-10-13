//
//  TerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#ifndef TerrainElevationProvider_hpp
#define TerrainElevationProvider_hpp

class G3MRenderContext;
class G3MContext;


class TerrainElevationProvider {
public:
  virtual ~TerrainElevationProvider() {
  }

  virtual bool isReadyToRender(const G3MRenderContext* rc) = 0;

  virtual void initialize(const G3MContext* context) = 0;

};

#endif
