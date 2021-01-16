//
//  XPCNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCNode_hpp
#define XPCNode_hpp

#include <vector>
#include <string>

class JSONObject;
class JSONArray;
class Sector;


class XPCNode {
private:

  static XPCNode* fromJSON(const JSONObject* jsonObject);


  const std::string _id;

  const Sector* _sector;

  const int _pointsCount;

  const double _minZ;
  const double _maxZ;

  const std::vector<std::string>* _childIDs;


  XPCNode(const std::string& id,
          const Sector* sector,
          const int pointsCount,
          const double minZ,
          const double maxZ,
          const std::vector<std::string>* childIDs);

public:
  
  static const std::vector<XPCNode*>* fromJSON(const JSONArray* jsonArray);

  ~XPCNode();

};

#endif
