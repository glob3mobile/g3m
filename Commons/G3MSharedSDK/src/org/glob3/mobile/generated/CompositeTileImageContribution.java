package org.glob3.mobile.generated; 
public class CompositeTileImageContribution extends TileImageContribution
{
  private final TileImageContribution _contribution0 = new TileImageContribution();
  private final TileImageContribution _contribution1 = new TileImageContribution();
  private final TileImageContribution _contribution2 = new TileImageContribution();
  private final TileImageContribution _contribution3 = new TileImageContribution();

  private CompositeTileImageContribution(TileImageContribution contribution0, TileImageContribution contribution1, TileImageContribution contribution2)
  {
     this(contribution0, contribution1, contribution2, TileImageContribution.none());
  }
  private CompositeTileImageContribution(TileImageContribution contribution0, TileImageContribution contribution1)
  {
     this(contribution0, contribution1, TileImageContribution.none(), TileImageContribution.none());
  }
  private CompositeTileImageContribution(TileImageContribution contribution0, TileImageContribution contribution1, TileImageContribution contribution2, TileImageContribution contribution3)
  {
     super(false, 1);
     _contribution0 = new TileImageContribution(contribution0);
     _contribution1 = new TileImageContribution(contribution1);
     _contribution2 = new TileImageContribution(contribution2);
     _contribution3 = new TileImageContribution(contribution3);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  }

  private static void deleteContributions(java.util.ArrayList<TileImageContribution> contributions)
  {
    final int size = contributions.size();
    for (int i = 0; i < size; i++)
    {
      TileImageContribution contribution = contributions.get(i);
      if (contribution != null)
         contribution.dispose();
    }
  }

  public static TileImageContribution create(java.util.ArrayList<TileImageContribution> contributions)
  {
    final int contributionsSize = contributions.size();
    if (contributionsSize == 0)
    {
      return TileImageContribution.none();
    }
    else if (contributionsSize == 1)
    {
      TileImageContribution contribution = contributions.get(0);
      TileImageContribution result = contribution;
      if (contribution != null)
         contribution.dispose();
      return result;
    }
    if (contributionsSize == 2)
    {
      final TileImageContribution contribution0 = new TileImageContribution(*(contributions.get(0)));
      final TileImageContribution contribution1 = new TileImageContribution(*(contributions.get(1)));
  
      deleteContributions(contributions);
  
      return new CompositeTileImageContribution(contribution0, contribution1);
  
    }
    else if (contributionsSize == 3)
    {
      final TileImageContribution contribution0 = new TileImageContribution(*(contributions.get(0)));
      final TileImageContribution contribution1 = new TileImageContribution(*(contributions.get(1)));
      final TileImageContribution contribution2 = new TileImageContribution(*(contributions.get(2)));
  
      deleteContributions(contributions);
  
      return new CompositeTileImageContribution(contribution0, contribution1, contribution2);
    }
    else
    {
      if (contributionsSize > 4)
      {
        ILogger.instance().logWarning("Maximum 4 contributions, ignoring all but the first 4");
      }
      final TileImageContribution contribution0 = new TileImageContribution(*(contributions.get(0)));
      final TileImageContribution contribution1 = new TileImageContribution(*(contributions.get(1)));
      final TileImageContribution contribution2 = new TileImageContribution(*(contributions.get(2)));
      final TileImageContribution contribution3 = new TileImageContribution(*(contributions.get(3)));
  
      deleteContributions(contributions);
  
      return new CompositeTileImageContribution(contribution0, contribution1, contribution2, contribution3);
    }
  }
}