//
//  XPCNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCNode.hpp"

#include "JSONArray.hpp"
#include "JSONObject.hpp"
#include "JSONNumber.hpp"
#include "JSONString.hpp"

#include "Sector.hpp"
#include "XPCParsing.hpp"


const std::vector<XPCNode*>* XPCNode::fromJSON(const JSONArray* jsonArray) {
  if (jsonArray == NULL) {
    return NULL;
  }
  
  std::vector<XPCNode*>* result = new std::vector<XPCNode*>();

  const size_t size = jsonArray->size();

  for (size_t i = 0; i < size; i++) {
    const JSONObject* jsonObject = jsonArray->getAsObject(i);
    XPCNode* dimension = XPCNode::fromJSON(jsonObject);
    if (dimension == NULL) {
      // release the memory allocated up to here
      for (size_t j = 0; j < result->size(); j++) {
        delete result->at(j);
      }
      delete result;

      return NULL;
    }

    result->push_back( dimension );
  }

  return result;

}


XPCNode* XPCNode::fromJSON(const JSONObject* jsonObject) {

  const std::string id = jsonObject->getAsString("id")->value();

  const Sector* sector = XPCParsing::parseSector( jsonObject->getAsArray("sector") );

  const int pointsCount = (int) jsonObject->getAsNumber("pointsCount")->value();

  const double minZ = jsonObject->getAsNumber("minZ")->value();
  const double maxZ = jsonObject->getAsNumber("maxZ")->value();

  const std::vector<std::string>* childIDs = XPCParsing::parseStrings( jsonObject->getAsArray("childIDs")  );
  if (childIDs == NULL) {
    delete sector;
    return NULL;
  }

  return new XPCNode(id,
                     sector,
                     pointsCount,
                     minZ,
                     maxZ,
                     childIDs);
}


XPCNode::XPCNode(const std::string& id,
                 const Sector* sector,
                 const int pointsCount,
                 const double minZ,
                 const double maxZ,
                 const std::vector<std::string>* childIDs) :
_id(id),
_sector(sector),
_pointsCount(pointsCount),
_minZ(minZ),
_maxZ(maxZ),
_childIDs(childIDs)
{

}


XPCNode::~XPCNode() {
  delete _sector;

#ifdef C_CODE
  delete _childIDs;
#endif
}
