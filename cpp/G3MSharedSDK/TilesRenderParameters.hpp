//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#ifndef G3MiOSSDK_TilesRenderParameters
#define G3MiOSSDK_TilesRenderParameters



enum Quality {
  QUALITY_LOW,
  QUALITY_MEDIUM,
  QUALITY_HIGH
};



class TilesRenderParameters {
public:
  const bool    _renderDebug;
  bool          _incrementalTileQuality;
  double        _texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
  const Quality _quality;

  TilesRenderParameters(const bool    renderDebug,
                        const bool    incrementalTileQuality,
                        const Quality quality) :
  _renderDebug(renderDebug),
  _incrementalTileQuality(incrementalTileQuality),
  _quality(quality)
  {
    switch (quality) {
      case QUALITY_LOW:
        _texturePixelsPerInch = 64;
        break;
      case QUALITY_MEDIUM:
        _texturePixelsPerInch = 128;
        break;
      default: //HIGH
        _texturePixelsPerInch = 256;
        break;
    }
  }

  ~TilesRenderParameters() {
  }

};

#endif
