package org.glob3.mobile.generated; 
//
//  CompositeTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

//
//  CompositeTileImageProvider.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//



public class CompositeTileImageProvider extends CanvasTileImageProvider
{
  private java.util.ArrayList<TileImageProvider> _children = new java.util.ArrayList<TileImageProvider>();
  private int _childrenSize;

  public void dispose()
  {
    for (int i = 0; i < _childrenSize; i++)
    {
      TileImageProvider child = _children.get(i);
      //    delete child;
      child._release();
    }
    super.dispose();
  }

  public CompositeTileImageProvider()
  {
     _childrenSize = 0;
  }

  public final void addProvider(TileImageProvider child)
  {
    _children.add(child);
    _childrenSize = _children.size();
  }

  public final TileImageContribution contribution(Tile tile)
  {
    java.util.ArrayList<CompositeTileImageContribution.ChildContribution> childrenContributions = new java.util.ArrayList<CompositeTileImageContribution.ChildContribution>();
  
    for (int i = 0; i < _childrenSize; i++)
    {
      TileImageProvider child = _children.get(i);
      final TileImageContribution childContribution = child.contribution(tile);
      if (childContribution.isNone())
      {
        TileImageContribution.deleteContribution(childContribution);
      }
      else
      {
        // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
        final int childrenContributionsSize = childrenContributions.size();
        if ((childrenContributionsSize > 0) && childContribution.isFullCoverageAndOpaque())
        {
          for (int j = 0; j < childrenContributionsSize; j++)
          {
            CompositeTileImageContribution.ChildContribution previousContribution = childrenContributions.get(j);
            previousContribution = null;
          }
          childrenContributions.clear();
        }
  
        childrenContributions.add(new CompositeTileImageContribution.ChildContribution(i, childContribution));
      }
    }
  
    return CompositeTileImageContribution.create(childrenContributions);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  
    final CompositeTileImageContribution compositeContribution = (CompositeTileImageContribution) contribution;
  
  
    // temporary error code
    TileImageContribution.deleteContribution(contribution);
    listener.imageCreationError(tile._id, "Not yet implemented");
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

  public final void cancel(Tile tile)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  }

}