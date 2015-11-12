//
//  BingMapsLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/13.
//
//

#include "BingMapsLayer.hpp"

#include "Vector2I.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"
#include "IStringUtils.hpp"
#include "IDownloader.hpp"
#include "DownloadPriority.hpp"
#include "IBufferDownloadListener.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONNumber.hpp"
#include "LayerCondition.hpp"
#include "Context.hpp"
#include "RenderState.hpp"
#include "Info.hpp"

BingMapsLayer::BingMapsLayer(const std::string&    imagerySet,
                             const std::string&    key,
                             const TimeInterval&   timeToCache,
                             const bool            readExpired,
                             const int             initialLevel,
                             const int             maxLevel,
                             const float           transparency,
                             const LayerCondition* condition,
                             std::vector<const Info*>*  layerInfo) :
RasterLayer(timeToCache,
            readExpired,
            NULL,
            transparency,
            condition,
            layerInfo),
_imagerySet(imagerySet),
_culture("en-US"),
_key(key),
_initialLevel(initialLevel),
_maxLevel(maxLevel),
_isInitialized(false)
{
}

BingMapsLayer::BingMapsLayer(const std::string&    imagerySet,
                             const std::string&    culture,
                             const std::string&    key,
                             const TimeInterval&   timeToCache,
                             const bool            readExpired,
                             const int             initialLevel,
                             const int             maxLevel,
                             const float           transparency,
                             const LayerCondition* condition,
                             std::vector<const Info*>*  layerInfo) :
RasterLayer(timeToCache,
            readExpired,
            NULL,
            transparency,
            condition,
            layerInfo),
_imagerySet(imagerySet),
_culture(culture),
_key(key),
_initialLevel(initialLevel),
_maxLevel(maxLevel),
_isInitialized(false)
{
}


class BingMapsLayer_MetadataBufferDownloadListener : public IBufferDownloadListener {
private:
  BingMapsLayer* _bingMapsLayer;
  
public:
  BingMapsLayer_MetadataBufferDownloadListener(BingMapsLayer* bingMapsLayer) :
  _bingMapsLayer(bingMapsLayer)
  {
    
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    _bingMapsLayer->onDowloadMetadata(buffer);
  }
  
  void onError(const URL& url) {
    _bingMapsLayer->onDownloadErrorMetadata();
  }
  
  void onCancel(const URL& url) {
    // do nothing, the request won't be cancelled
  }
  
  void onCanceledDownload(const URL& url,
                          IByteBuffer* data,
                          bool expired) {
    // do nothing, the request won't be cancelled
  }
};


void BingMapsLayer::onDownloadErrorMetadata() {
  _metadataErrors.push_back("BingMapsLayer: Error while downloading metadata. Please review your key");
  notifyChanges();
  ILogger::instance()->logError("BingMapsLayer: Error while downloading metadata.");
}

void BingMapsLayer::onDowloadMetadata(IByteBuffer* buffer) {
  IJSONParser* parser = IJSONParser::instance();
  
  const JSONBaseObject* jsonBaseObject = parser->parse(buffer);
  if (jsonBaseObject == NULL) {
    _metadataErrors.push_back("BingMapsLayer: Can't parse json metadata.");
    ILogger::instance()->logError("BingMapsLayer: Can't parse json metadata.");
    return;
  }
  
  const JSONObject* jsonObject = jsonBaseObject->asObject();
  if (jsonObject == NULL) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, root object is not an json-object.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, root object is not an json-object.");
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  const std::string brandLogoUri = jsonObject->getAsString("brandLogoUri", "");
  const std::string copyright    = jsonObject->getAsString("copyright",    "");
  
  const JSONArray* resourceSets = jsonObject->getAsArray("resourceSets");
  if (resourceSets == NULL) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, resourceSets field not found.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, resourceSets field not found.");
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  if (resourceSets->size() != 1) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, resourceSets has more elements than the current implementation can handle.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, resourceSets has %d elements (the current implementation can only handle 1 element).",
                                  resourceSets->size());
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  const JSONObject* resource = resourceSets->getAsObject(0);
  if (resource == NULL) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, can't find resource jsonobject.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, can't find resource jsonobject.");
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  const JSONArray* resources = resource->getAsArray("resources");
  if (resources->size() != 1) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, resources has more elements than the current implementation can handle.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, resources has %d elements (the current implementation can only handle 1 element).",
                                  resources->size());
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  const JSONObject* meanfulResource = resources->getAsObject(0);
  if (meanfulResource == NULL) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, can't find a meanfulResource JSONObject.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, can't find a meanfulResource JSONObject.");
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  const std::string imageUrl = meanfulResource->getAsString("imageUrl", "");
  if (imageUrl.size() == 0) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, can't find a imageUrl String.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, can't find a imageUrl String.");
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  const int imageWidth  = (int) meanfulResource->getAsNumber("imageWidth",  256);
  const int imageHeight = (int) meanfulResource->getAsNumber("imageHeight", 256);
  
  const int zoomMin = (int) meanfulResource->getAsNumber("zoomMin", 1);
  const int zoomMax = (int) meanfulResource->getAsNumber("zoomMax", 1);
  
  const JSONArray* imageUrlSubdomainsJS = meanfulResource->getAsArray("imageUrlSubdomains");
  if (imageUrlSubdomainsJS == NULL) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, can't find a imageUrlSubdomains JSONArray.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, can't find a imageUrlSubdomains JSONArray.");
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  std::vector<std::string> imageUrlSubdomains;
  for (int i = 0; i < imageUrlSubdomainsJS->size(); i++) {
    const std::string imageUrlSubdomain = imageUrlSubdomainsJS->getAsString(i, "");
    if (imageUrlSubdomain.size() != 0) {
      imageUrlSubdomains.push_back(imageUrlSubdomain);
    }
  }
  
  if (imageUrlSubdomains.size() == 0) {
    _metadataErrors.push_back("BingMapsLayer: Error while parsing json metadata, can't find any imageUrlSubdomain String.");
    ILogger::instance()->logError("BingMapsLayer: Error while parsing json metadata, can't find any imageUrlSubdomain String.");
    parser->deleteJSONData(jsonBaseObject);
    return;
  }
  
  processMetadata(brandLogoUri,
                  copyright,
                  imageUrl,
                  imageUrlSubdomains,
                  imageWidth, imageHeight,
                  zoomMin, zoomMax);
  
  //  http://ecn.{subdomain}.tiles.virtualearth.net/tiles/h{quadkey}.jpeg?g=1180&mkt={culture}
  
  
  parser->deleteJSONData(jsonBaseObject);
  delete buffer;
}

void BingMapsLayer::processMetadata(const std::string& brandLogoUri,
                                    const std::string& copyright,
                                    const std::string& imageUrl,
                                    std::vector<std::string> imageUrlSubdomains,
                                    const int imageWidth,
                                    const int imageHeight,
                                    const int zoomMin,
                                    const int zoomMax) {
  _brandLogoUri = brandLogoUri;
  _copyright = copyright;
  addInfo(new Info(copyright));
  _imageUrl = imageUrl;
  _imageUrlSubdomains = imageUrlSubdomains;
  
  _isInitialized = true;
  
  const IMathUtils* mu = IMathUtils::instance();
  
  setParameters(new LayerTilesRenderParameters(Sector::fullSphere(),
                                               1,
                                               1,
                                               mu->max(zoomMin, _initialLevel),
                                               mu->min(zoomMax, _maxLevel),
                                               Vector2I(imageWidth, imageHeight),
                                               LayerTilesRenderParameters::defaultTileMeshResolution(),
                                               true));
}

void BingMapsLayer::initialize(const G3MContext* context) {
  _metadataErrors.clear();
  const URL url("http://dev.virtualearth.net/REST/v1/Imagery/Metadata/" + _imagerySet + "?key=" + _key, false);
  
  context->getDownloader()->requestBuffer(url,
                                          DownloadPriority::HIGHEST,
                                          TimeInterval::fromDays(1),
                                          true,
                                          new BingMapsLayer_MetadataBufferDownloadListener(this),
                                          true);
}

URL BingMapsLayer::getFeatureInfoURL(const Geodetic2D& position,
                                     const Sector& sector) const {
  return URL();
}

const std::string BingMapsLayer::getQuadKey(const int zoom,
                                            const int column,
                                            const int row) {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  
  for (int i = 1; i <= zoom; i++) {
    const int zoom_i = (zoom - i);
    const int t = (((row >> zoom_i) & 1) << 1) | ((column >> zoom_i) & 1);
    isb->addInt(t);
  }
  
  const std::string result = isb->getString();
  
  delete isb;
  
  return result;
}

const std::string BingMapsLayer::getQuadKey(const Tile* tile) {
  const int level   = tile->_level;
  const int numRows = (int) IMathUtils::instance()->pow(2.0, level);
  const int row     = numRows - tile->_row - 1;
  return getQuadKey(level, tile->_column, row);
}

const URL BingMapsLayer::createURL(const Tile* tile) const {
  const IStringUtils* su = IStringUtils::instance();
  
  const int level   = tile->_level;
  const int column  = tile->_column;
  const int numRows = (int) IMathUtils::instance()->pow(2.0, level);
  const int row     = numRows - tile->_row - 1;
  
  const size_t subdomainsSize = _imageUrlSubdomains.size();
  std::string subdomain = "";
  if (subdomainsSize > 0) {
    // select subdomain based on fixed data (instead of round-robin) to be cache friendly
    const size_t subdomainsIndex =  IMathUtils::instance()->abs(level + column + row) % subdomainsSize;
    subdomain = _imageUrlSubdomains[subdomainsIndex];
  }
  
  const std::string quadkey = getQuadKey(level, column, row);
  
  std::string path = _imageUrl;
  path = su->replaceAll(path, "{subdomain}", subdomain);
  path = su->replaceAll(path, "{quadkey}",   quadkey);
  path = su->replaceAll(path, "{culture}",   _culture);
  
  return URL(path, false);
}

const std::string BingMapsLayer::description() const {
  return "[BingMapsLayer]";
}

bool BingMapsLayer::rawIsEquals(const Layer* that) const {
  BingMapsLayer* t = (BingMapsLayer*) that;
  
  if (_imagerySet != t->_imagerySet) {
    return false;
  }
  
  if (_key != t->_key) {
    return false;
  }
  
  if (_initialLevel != t->_initialLevel) {
    return false;
  }

  if (_maxLevel != t->_maxLevel) {
    return false;
  }

  return true;
}

BingMapsLayer* BingMapsLayer::copy() const {
  return new BingMapsLayer(_imagerySet,
                           _culture,
                           _key,
                           _timeToCache,
                           _readExpired,
                           _initialLevel,
                           _maxLevel,
                           _transparency,
                           (_condition == NULL) ? NULL : _condition->copy(),
                           _layerInfo);
}

RenderState BingMapsLayer::getRenderState() {
  _errors.clear();
  if (_metadataErrors.size() > 0) {
#ifdef C_CODE
    _errors.insert(_errors.end(),
                   _metadataErrors.begin(),
                   _metadataErrors.end());
#endif
#ifdef JAVA_CODE
    _errors.addAll(_metadataErrors);
#endif
  }
  
  if (_imagerySet.compare("") == 0) {
    _errors.push_back("Missing layer parameter: imagerySet");
  }
  if (_key.compare("") == 0) {
    _errors.push_back("Missing layer parameter: key");
  }

  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }

  return _isInitialized ? RenderState::ready() : RenderState::busy();
}

const TileImageContribution* BingMapsLayer::rawContribution(const Tile* tile) const {
  const Tile* tileP = getParentTileOfSuitableLevel(tile);
  if (tileP == NULL) {
    return NULL;
  }
  else if (tile == tileP) {
    //Most common case tile of suitable level being fully coveraged by layer
    return ((_transparency < 1)
            ? TileImageContribution::fullCoverageTransparent(_transparency)
            : TileImageContribution::fullCoverageOpaque());
  }
  else {
    const Sector requestedImageSector = tileP->_sector;
    return ((_transparency < 1)
            ? TileImageContribution::partialCoverageTransparent(requestedImageSector, _transparency)
            : TileImageContribution::partialCoverageOpaque(requestedImageSector));
  }
}
