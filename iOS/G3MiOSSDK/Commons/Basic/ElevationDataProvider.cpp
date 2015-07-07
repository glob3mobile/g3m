//
//  ElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#include "ElevationDataProvider.hpp"

#include "Sector.hpp"


bool ElevationDataProvider::containsSector(const Sector& sector) const{
  
  std::vector<const Sector*> ss = getSectors();
  for (int i = 0; i < ss.size(); i++) {
    if (ss[i]->touchesWith(sector)){
      return true;
    }
  }
  return false;
  
}
