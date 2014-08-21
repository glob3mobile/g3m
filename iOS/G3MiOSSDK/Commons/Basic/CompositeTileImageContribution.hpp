//
//  CompositeTileImageContribution.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/26/14.
//
//

#ifndef __G3MiOSSDK__CompositeTileImageContribution__
#define __G3MiOSSDK__CompositeTileImageContribution__

#include "TileImageContribution.hpp"
#include <vector>


class CompositeTileImageContribution : public TileImageContribution {
public:

  class ChildContribution {
  public:
    const int                    _childIndex;
    const TileImageContribution* _contribution;

    ChildContribution(const int                    childIndex,
                      const TileImageContribution* contribution);

    ~ChildContribution();
  };


  static const TileImageContribution* create(const std::vector<const ChildContribution*>& contributions);


private:
#ifdef C_CODE
  const std::vector<const ChildContribution*> _contributions;
#endif
#ifdef JAVA_CODE
  private final java.util.ArrayList<ChildContribution> _contributions;
#endif
  
  CompositeTileImageContribution(const std::vector<const ChildContribution*>& contributions) :
  TileImageContribution(false, 1.0f),
  _contributions(contributions)
  {
    
    
  }
  
  
//  CompositeTileImageContribution(const std::vector<const ChildContribution*>& contributions,
//                                 const Sector& sector,
//                                 bool isFullCoverage,
//                                 bool isTransparent,
//                                 float alpha) :
//  TileImageContribution(sector, isFullCoverage, isTransparent, alpha),
//  _contributions(contributions)
//  {
//    
//    
//  }

protected:
  ~CompositeTileImageContribution();

public:

  const int size() const {
    return _contributions.size();
  }

  const ChildContribution* get(int index) const {
#ifdef C_CODE
    return _contributions.at(index);//[index];
#endif
#ifdef JAVA_CODE
    return _contributions.get(index);
#endif
  }
  
  static bool isFullCoverage(const std::vector<const ChildContribution*>& contributions) {
    const int size = contributions.size();
    for (int i = 0; i < size; i++) {
      const ChildContribution* child = contributions.at(i);
      if(child->_contribution->isFullCoverage()) {
        return true;
      }
    }
    
    return false;
  }
  
  static bool isFullCoverageAndOpaque(const std::vector<const ChildContribution*>& contributions) {
    const int size = contributions.size();
    for (int i = 0; i < size; i++) {
      const ChildContribution* child = contributions.at(i);
      const TileImageContribution* contribution = child->_contribution;
      if(contribution->isFullCoverage() && !contribution->isTransparent() && contribution->isOpaque()) {
        return true;
      }
    }
    
    return false;
  }
  
  
  static bool isOpaque(const std::vector<const ChildContribution*>& contributions) {
    const int size = contributions.size();
    for (int i = 0; i < size; i++) {
      const ChildContribution* child = contributions.at(i);
      const TileImageContribution* contribution = child->_contribution;
      if(contribution->isOpaque()) {
        return true;
      }
    }
    
    return false;
  }
  
  static bool isTransparent(const std::vector<const ChildContribution*>& contributions) {
    const int size = contributions.size();
    for (int i = 0; i < size; i++) {
      const ChildContribution* child = contributions.at(i);
      const TileImageContribution* contribution = child->_contribution;
      if(!contribution->isTransparent()) {
        return false;
      }
    }
    
    return true;
  }
  
  static const Sector* getSector(const std::vector<const ChildContribution*>& contributions) {
    Sector* result = NULL;
    const int size = contributions.size();
    for (int i = 0; i < size; i++) {
      const ChildContribution* child = contributions.at(i);
      const TileImageContribution* contribution = child->_contribution;
      if(result == NULL) {
        result = new Sector(*contribution->getSector());
      } else {
        result->mergedWith(*contribution->getSector());
      }
    }
    
    return result;
  }
  
};

#endif
