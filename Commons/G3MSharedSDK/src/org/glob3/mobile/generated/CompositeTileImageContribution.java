package org.glob3.mobile.generated; 
//
//  CompositeTileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/26/14.
//
//

//
//  CompositeTileImageContribution.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/26/14.
//
//




public class CompositeTileImageContribution extends TileImageContribution
{

  public static class ChildContribution
  {
    public final int _childIndex;
    public final TileImageContribution _contribution;

    public ChildContribution(int childIndex, TileImageContribution contribution)
    {
       _childIndex = childIndex;
       _contribution = contribution;
    }

    public void dispose()
    {
      TileImageContribution.releaseContribution(_contribution);
    }
  }


  public static TileImageContribution create(java.util.ArrayList<ChildContribution> contributions)
  {
    if (contributions.isEmpty())
    {
      return null;
    }
  
    //if first contribution is opaque, then composite is opaque.
    final ChildContribution firsChild = contributions.get(0);
  
    return new CompositeTileImageContribution(contributions, firsChild._contribution.isOpaque());
  }


  private final java.util.ArrayList<ChildContribution> _contributions;

  private CompositeTileImageContribution(java.util.ArrayList<ChildContribution> contributions, boolean transparent)
  {
     super(transparent, 1.0f);
     _contributions = contributions;


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

  public void dispose()
  {
    final int contributionsSize = _contributions.size();
    for (int i = 0; i < contributionsSize; i++)
    {
      final ChildContribution contribution = _contributions.get(i);
      contribution.dispose();
    }
    super.dispose();
  }


  public final int size()
  {
    return _contributions.size();
  }

  public final ChildContribution get(int index)
  {
    return _contributions.get(index);
  }
}