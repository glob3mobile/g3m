//
//  CityGMLParser.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//

#ifndef CityGMLParser_hpp
#define CityGMLParser_hpp

#include "IXMLNode.hpp"
#include "CityGMLBuilding.hpp"

#include <vector>
#include <string>

class CityGMLParser{
  
public:
  static std::vector<CityGMLBuilding*> parseLOD2Buildings2(IXMLNode* cityGMLDoc);
  
};

#endif /* CityGMLParser_hpp */
