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
  
  CompositeTileImageContribution(const std::vector<const ChildContribution*>& contributions, const bool transparent) :
  TileImageContribution(transparent, 1.0f),
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
    return _contributions[index];
#endif
#ifdef JAVA_CODE
    return _contributions.get(index);
#endif
  }
};

#endif
