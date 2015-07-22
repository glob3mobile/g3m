//
//  WMSLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_WMSLayer
#define G3MiOSSDK_WMSLayer

#include "RasterLayer.hpp"
#include "URL.hpp"
#include "Sector.hpp"

enum WMSServerVersion {
  WMS_1_1_0,
  WMS_1_3_0
};


class WMSLayer: public RasterLayer {
private:

#ifdef C_CODE
  const URL _mapServerURL;
  const URL _queryServerURL;
#endif
#ifdef JAVA_CODE
  private final URL _mapServerURL;
  private final URL _queryServerURL;
#endif

  const std::string      _mapLayer;
  const WMSServerVersion _mapServerVersion;
  const std::string      _queryLayer;
  const WMSServerVersion _queryServerVersion;
  const Sector           _dataSector;
  const std::string      _format;
  const std::string      _srs;
  const std::string      _style;
  const bool             _isTransparent;
  std::string            _extraParameter;

  inline double toBBOXLongitude(const Angle& longitude) const;
  inline double toBBOXLatitude (const Angle& latitude)  const;

protected:
  std::string getLayerType() const {
    return "WMS";
  }

  bool rawIsEquals(const Layer* that) const;


  const TileImageContribution* rawContribution(const Tile* tile) const;

  const URL createURL(const Tile* tile) const;

public:

  static WMSLayer* newMercator(const std::string&        mapLayer,
                               const URL&                mapServerURL,
                               const WMSServerVersion    mapServerVersion,
                               const std::string&        queryLayer,
                               const URL&                queryServerURL,
                               const WMSServerVersion    queryServerVersion,
                               const Sector&             dataSector,
                               const std::string&        format,
                               const std::string&        style,
                               const bool                isTransparent,
                               const int                 firstLevel   = 2,
                               const int                 maxLevel     = 17,
                               const LayerCondition*     condition    = NULL,
                               const TimeInterval&       timeToCache  = TimeInterval::fromDays(30),
                               const bool                readExpired  = true,
                               const float               transparency = 1,
                               std::vector<const Info*>* layerInfo    = new std::vector<const Info*>());

  static WMSLayer* newMercator(const std::string&        mapLayer,
                               const URL&                mapServerURL,
                               const WMSServerVersion    mapServerVersion,
                               const Sector&             dataSector,
                               const std::string&        format,
                               const std::string&        style,
                               const bool                isTransparent,
                               const int                 firstLevel   = 2,
                               const int                 maxLevel     = 17,
                               const LayerCondition*     condition    = NULL,
                               const TimeInterval&       timeToCache  = TimeInterval::fromDays(30),
                               const bool                readExpired  = true,
                               const float               transparency = 1,
                               std::vector<const Info*>* layerInfo    = new std::vector<const Info*>());

  static WMSLayer* newWGS84(const std::string&        mapLayer,
                            const URL&                mapServerURL,
                            const WMSServerVersion    mapServerVersion,
                            const std::string&        queryLayer,
                            const URL&                queryServerURL,
                            const WMSServerVersion    queryServerVersion,
                            const Sector&             dataSector,
                            const std::string&        format,
                            const std::string&        style,
                            const bool                isTransparent,
                            const int                 firstLevel   = 1,
                            const int                 maxLevel     = 17,
                            const LayerCondition*     condition    = NULL,
                            const TimeInterval&       timeToCache  = TimeInterval::fromDays(30),
                            const bool                readExpired  = true,
                            const float               transparency = 1,
                            std::vector<const Info*>* layerInfo    = new std::vector<const Info*>());

  static WMSLayer* newWGS84(const std::string&        mapLayer,
                            const URL&                mapServerURL,
                            const WMSServerVersion    mapServerVersion,
                            const Sector&             dataSector,
                            const std::string&        format,
                            const std::string&        style,
                            const bool                isTransparent,
                            const int                 firstLevel   = 1,
                            const int                 maxLevel     = 17,
                            const LayerCondition*     condition    = NULL,
                            const TimeInterval&       timeToCache  = TimeInterval::fromDays(30),
                            const bool                readExpired  = true,
                            const float               transparency = 1,
                            std::vector<const Info*>* layerInfo    = new std::vector<const Info*>());

  WMSLayer(const std::string&                mapLayer,
           const URL&                        mapServerURL,
           const WMSServerVersion            mapServerVersion,
           const std::string&                queryLayer,
           const URL&                        queryServerURL,
           const WMSServerVersion            queryServerVersion,
           const Sector&                     dataSector,
           const std::string&                format,
           const std::string&                srs,
           const std::string&                style,
           const bool                        isTransparent,
           const LayerCondition*             condition,
           const TimeInterval&               timeToCache,
           const bool                        readExpired,
           const LayerTilesRenderParameters* parameters   = NULL,
           const float                       transparency = 1,
           std::vector<const Info*>*         layerInfo    = new std::vector<const Info*>());

  WMSLayer(const std::string&                mapLayer,
           const URL&                        mapServerURL,
           const WMSServerVersion            mapServerVersion,
           const Sector&                     dataSector,
           const std::string&                format,
           const std::string&                srs,
           const std::string&                style,
           const bool                        isTransparent,
           const LayerCondition*             condition,
           const TimeInterval&               timeToCache,
           const bool                        readExpired,
           const LayerTilesRenderParameters* parameters    = NULL,
           const float                       transparency  = 1,
           std::vector<const Info*>*         layerInfo     = new std::vector<const Info*>());

  URL getFeatureInfoURL(const Geodetic2D& g,
                        const Sector& sector) const;

  void setExtraParameter(const std::string& extraParameter) {
    _extraParameter = extraParameter;
    notifyChanges();
  }

  const std::string description() const;

  WMSLayer* copy() const;

  RenderState getRenderState();

  const Sector getDataSector() const {
    return _dataSector;
  }

};

#endif
