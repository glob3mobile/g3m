//
//  TileMillLayer.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 19/02/13.
//
//

#include "TileMillLayer.hpp"

#include "Tile.hpp"
#include "Petition.hpp"
#include "IMathUtils.hpp"
#include "IDownloader.hpp"

#include "IBufferDownloadListener.hpp"

#include "IJSONParser.hpp"
#include "JSONBaseObject.hpp"
#include "JSONNumber.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "JSONBoolean.hpp"

#include "IStringUtils.hpp"
#include "IStringBuilder.hpp"
#include "TimeInterval.hpp"
#include "ILogger.hpp"

std::vector<Petition*> TileMillLayer::getMapPetitions(const G3MRenderContext* rc,
                                                      const Tile* tile,
                                                      int width,
                                                      int height)
const{                                                    
    std::vector<Petition*> petitions;
    const Sector tileSector = tile->getSector();
                                                                
    if (!_sector.touchesWith(tileSector)) {
        return petitions;
    }
    const Sector sector = tileSector.intersection(_sector);
    std::string req = _mapServerURL.getPath();
    //If the server refer to itself as localhost...
    int pos = req.find("localhost");

    if (pos != -1) {
        req = req.substr(pos+9);
        int pos2 = req.find("/", 8);
        std::string newHost = req.substr(0, pos2);
        req = newHost + req;
    }
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString(req);
    isb->addString("db=");
    isb->addString(_dataBaseMBTiles);
    isb->addString("&z=");
    isb->addInt(tile->getLevel()+1);
    isb->addString("&x=");
    isb->addInt(tile->getColumn());
    isb->addString("&y=");
    isb->addInt(tile->getRow());
    
    ILogger::instance()->logInfo("%s",isb->getString().c_str());
    
    petitions.push_back(new Petition(sector, URL(isb->getString(), false), _timeToCache));
    
    return petitions;
}



URL TileMillLayer::getFeatureInfoURL(const Geodetic2D& g,
                                 const IFactory* factory,
                                 const Sector& tileSector,
                                 int width, int height) const {
    return URL::nullURL();
    
}