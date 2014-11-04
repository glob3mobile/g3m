//
//  BingMapsLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/13.
//
//

#ifndef __G3MiOSSDK__BingMapsLayer__
#define __G3MiOSSDK__BingMapsLayer__

#include "RasterLayer.hpp"
class IByteBuffer;


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


class BingMapsLayer : public RasterLayer {
private:
  const std::string _imagerySet;
  const std::string _culture;
  const std::string _key;
  
  const int _initialLevel;
  const int _maxLevel;

  bool _isInitialized;
  
  std::string _brandLogoUri;
  std::string _copyright;
  std::string _imageUrl;
  std::vector<std::string> _imageUrlSubdomains;
  
  std::vector<std::string>              _metadataErrors;
  
  void processMetadata(const std::string& brandLogoUri,
                       const std::string& copyright,
                       const std::string& imageUrl,
                       std::vector<std::string> imageUrlSubdomains,
                       const int imageWidth,
                       const int imageHeight,
                       const int zoomMin,
                       const int zoomMax);
  
  static const std::string getQuadKey(const int level,
                                      const int column,
                                      const int row);
  
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
  
  const TileImageContribution* rawContribution(const Tile* tile) const;
  
  const URL createURL(const Tile* tile) const;
  
public:
  static const std::string getQuadKey(const Tile* tile);

  /**
   imagerySet: "Aerial", "AerialWithLabels", "Road", "OrdnanceSurvey" or "CollinsBart". See class BingMapType for constants.
   key: Bing Maps key. See http://msdn.microsoft.com/en-us/library/gg650598.aspx
   */
  BingMapsLayer(const std::string&    imagerySet,
                const std::string&    key,
                const TimeInterval&   timeToCache,
                const bool            readExpired    = true,
                const int             initialLevel   = 2,
                const int             maxLevel       = 25,
                const float           transparency   = 1,
                const LayerCondition* condition      = NULL,
                std::vector<const Info*>*  layerInfo = new std::vector<const Info*>());
  
  BingMapsLayer(const std::string&    imagerySet,
                const std::string&    culture,
                const std::string&    key,
                const TimeInterval&   timeToCache,
                const bool            readExpired    = true,
                const int             initialLevel   = 2,
                const int             maxLevel       = 25,
                const float           transparency   = 1,
                const LayerCondition* condition      = NULL,
                std::vector<const Info*>*  layerInfo = new std::vector<const Info*>());
  
  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;
    
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
  
  const Sector getDataSector() const {
    return Sector::fullSphere();
  }
  
};

#endif
