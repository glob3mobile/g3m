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
      return TileImageContribution.none();
    }
    else if (contributionsSize == 1)
    {
      final ChildContribution singleContribution = contributions.get(0);
      final TileImageContribution result = singleContribution._contribution;
      if (singleContribution != null)
         singleContribution.dispose();
      return result;
    }
    else
    {
      return new CompositeTileImageContribution(contributions);
    }
  }


  private final java.util.ArrayList<ChildContribution> _contributions = new java.util.ArrayList<ChildContribution>();

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
      if (_contributions.get(i) != null)
         _contributions.get(i).dispose();
    }
  }

}