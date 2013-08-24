package org.glob3.mobile.generated; 
public class MapBoo_MultiImage
{
  private final Color _averageColor ;
  private java.util.ArrayList<MapBoo_MultiImage_Level> _levels = new java.util.ArrayList<MapBoo_MultiImage_Level>();

  public MapBoo_MultiImage(Color averageColor, java.util.ArrayList<MapBoo_MultiImage_Level> levels)
  {
     _averageColor = new Color(averageColor);
     _levels = levels;
  }

  public final Color getAverageColor()
  {
    return _averageColor;
  }

  public final java.util.ArrayList<MapBoo_MultiImage_Level> getLevels()
  {
    return _levels;
    return unmodificable list;
  }

  public final MapBoo_MultiImage_Level getBestLevel(int width)
  {
    final int levelsSize = _levels.size();
    if (levelsSize == 0)
    {
      return null;
    }
  
    for (int i = 0; i < levelsSize; i++)
    {
      MapBoo_MultiImage_Level level = _levels.get(i);
      final int levelWidth = level.getWidth();
      if (levelWidth <= width)
      {
        if ((levelWidth < width) && (i > 0))
        {
          return _levels.get(i - 1);
        }
        return level;
      }
    }
  
    // all levels are widther than width, so select the level with the less resolution
    return _levels.get(levelsSize - 1);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("[MultiImage averageColor=");
    isb.addString(_averageColor.description());
    isb.addString(", _levels=[");
    final int levelsSize = _levels.size();
    for (int i = 0; i < levelsSize; i++)
    {
      if (i > 0)
      {
        isb.addString(", ");
      }
      isb.addString(_levels.get(i).description());
    }
    isb.addString("]]");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}