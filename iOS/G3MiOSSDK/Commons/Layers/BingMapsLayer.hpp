//
//  BingMapsLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/13.
//
//

#ifndef __G3MiOSSDK__BingMapsLayer__
#define __G3MiOSSDK__BingMapsLayer__

#include "Layer.hpp"

class BingMapType {
public:

  static std::string Aerial() {
    return "Aerial";
  }

  static std::string AerialWithLabels() {
    return "AerialWithLabels";
  }

  static std::string Road() {
    return "Road";
  }

  static std::string OrdnanceSurvey() {
    return "OrdnanceSurvey";
  }

  static std::string CollinsBart() {
    return "CollinsBart";
  }

};


class BingMapsLayer : public Layer {
private:
  const std::string _imagerySet;
  const std::string _culture;
  const std::string _key;

  const int _initialLevel;

  bool _isInitialized;

  std::string _brandLogoUri;
  std::string _copyright;
  std::string _imageUrl;
  std::vector<std::string> _imageUrlSubdomains;

  void processMetadata(const std::string& brandLogoUri,
                       const std::string& copyright,
                       const std::string& imageUrl,
                       std::vector<std::string> imageUrlSubdomains,
                       const int imageWidth,
                       const int imageHeight,
                       const int zoomMin,
                       const int zoomMax);

  const std::string getQuadkey(const int level,
                               const int column,
                               const int row) const;

protected:
  std::string getLayerType() const {
    return "BingMaps";
  }

  std::string getBrandLogoUri() const {
    return _brandLogoUri;
  }

  std::string getCopyright() const {
    return _copyright;
  }

  bool rawIsEquals(const Layer* that) const;

public:

  /**
   imagerySet: "Aerial", "AerialWithLabels", "Road", "OrdnanceSurvey" or "CollinsBart". See class BingMapType for constants.
   key: Bing Maps key. See http://msdn.microsoft.com/en-us/library/gg650598.aspx
   */
  BingMapsLayer(const std::string& imagerySet,
                const std::string& key,
                const TimeInterval& timeToCache,
                bool readExpired = true,
                int initialLevel = 2,
                LayerCondition* condition = NULL,
                float transparency = (float)1.0);

  BingMapsLayer(const std::string& imagerySet,
                const std::string& culture,
                const std::string& key,
                const TimeInterval& timeToCache,
                bool readExpired = true,
                int initialLevel = 2,
                LayerCondition* condition = NULL,
                float transparency = (float)1.0);

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                const Tile* tile) const;

  bool isReady() const;

  void initialize(const G3MContext* context);

  void onDowloadMetadata(IByteBuffer* buffer);
  void onDownloadErrorMetadata();

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  BingMapsLayer* copy() const;
  
  RenderState getRenderState();
};

#endif
