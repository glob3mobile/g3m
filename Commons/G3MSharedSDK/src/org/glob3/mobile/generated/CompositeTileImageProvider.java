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

//  class ChildContribution {
//  public:
//    ChildContribution(const TileImageContribution& contribution);
//  };

  private java.util.ArrayList<TileImageProvider> _children = new java.util.ArrayList<TileImageProvider>();
  private int _childrenSize;

//  static const int MAX_CHILDREN_CONTRIBUTIONS = 4;
//  ChildContribution _childrenContributions[MAX_CHILDREN_CONTRIBUTIONS];


  //class CompositeTileImageProvider_ChildContribution {
  //
  //};
  
  //CompositeTileImageProvider::ChildContribution(const TileImageContribution& contribution) {
  //
  //}
  
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
    java.util.ArrayList<TileImageContribution> childrenContributions = new java.util.ArrayList<TileImageContribution>();
  
    for (int i = 0; i < _childrenSize; i++)
    {
      TileImageProvider child = _children.get(i);
      TileImageContribution childContribution = child.contribution(tile);
      if (!childContribution.isNone())
      {
  
        // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
        final int childrenContributionsSize = childrenContributions.size();
        if ((childrenContributionsSize > 0) && childContribution.isFullCoverageAndOpaque())
        {
          for (int j = 0; j < childrenContributionsSize; j++)
          {
            if (childrenContributions.get(j) != null)
               childrenContributions.get(j).dispose();
          }
          childrenContributions.clear();
        }
  
        childrenContributions.add( childContribution );
      }
    }
  
    return CompositeTileImageContribution.create(childrenContributions);
  
  //  bool hasContribution = false;
  //  for (int i = 0; i < _childrenSize; i++) {
  //    TileImageProvider* child = _children[i];
  //    TileImageContribution childContribution = child->contribution(tile);
  //    if (!childContribution.isNone()) {
  //      hasContribution = true;
  //      break;
  //    }
  //  }
  //
  ///#warning Diego at work!
  //  return hasContribution ? TileImageContribution::fullCoverageOpaque() : TileImageContribution::none();
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  
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