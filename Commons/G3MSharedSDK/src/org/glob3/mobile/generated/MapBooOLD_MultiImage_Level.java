package org.glob3.mobile.generated; 
public class MapBooOLD_MultiImage_Level
{
  private final URL _url;
  private final int _width;
  private final int _height;

  public MapBooOLD_MultiImage_Level(URL url, int width, int height)
  {
     _url = url;
     _width = width;
     _height = height;
  }

  public final URL getUrl()
  {
    return _url;
  }

  public final int getWidth()
  {
    return _width;
  }

  public final int getHeight()
  {
    return _height;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("[Level size=");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString(", url=");
    isb.addString(_url.description());
    isb.addString("]");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

  public void dispose()
  {

  }

}