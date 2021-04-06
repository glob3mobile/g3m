//
//  XPCParsing.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/16/21.
//

#ifndef XPCParsing_hpp
#define XPCParsing_hpp

#include <vector>

class Sector;
class JSONArray;


class XPCParsing {
public:

  static const Sector*                   parseSector(const JSONArray* jsonArray);
  static const std::vector<std::string>* parseStrings(const JSONArray* jsonArray);


private:
  XPCParsing() {

  }


};

#endif
