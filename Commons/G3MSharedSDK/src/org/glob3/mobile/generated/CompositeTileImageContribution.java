package org.glob3.mobile.generated; 
public class CompositeTileImageContribution extends TileImageContribution
{
  private final java.util.ArrayList<TileImageContribution> _contributions = new java.util.ArrayList<TileImageContribution>();

  private CompositeTileImageContribution(java.util.ArrayList<TileImageContribution> contributions)
  {
     super(false, 1);
     _contributions = contributions;
  }

  public void dispose()
  {
    final int size = _contributions.size();
    for (int i = 0; i < size; i++)
    {
      TileImageContribution.deleteContribution(_contributions.get(i));
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
      return contributions.get(0);
    }
    else
    {
      return new CompositeTileImageContribution(contributions);
    }
  }
}