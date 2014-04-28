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
      TileImageContribution.deleteContribution(_contribution);
    }
  }


  public static TileImageContribution create(java.util.ArrayList<ChildContribution> contributions)
  {
    final int contributionsSize = contributions.size();
    if (contributionsSize == 0)
    {
      //return TileImageContribution::none();
      return null;
    }
  //  else if (contributionsSize == 1) {
  //    const ChildContribution* singleContribution = contributions[0];
  //    const TileImageContribution* result = singleContribution->_contribution;
  //    delete singleContribution;
  //    return result;
  //  }
    else
    {
      return new CompositeTileImageContribution(contributions);
    }
  }


  private final java.util.ArrayList<ChildContribution> _contributions;

  private CompositeTileImageContribution(java.util.ArrayList<ChildContribution> contributions)
  {
     super(false, 1);
     _contributions = contributions;
  }

  public void dispose()
  {
    final int size = _contributions.size();
    for (int i = 0; i < size; i++)
    {
      _contributions[i] = null;
    }
  }

  public final int size()
  {
    return _contributions.size();
  }

  public final ChildContribution get(int index)
  {
    return _contributions[index];
  }

}