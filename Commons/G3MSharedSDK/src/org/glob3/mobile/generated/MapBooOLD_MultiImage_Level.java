package org.glob3.mobile.generated;import java.util.*;

public class MapBooOLD_MultiImage_Level
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final URL _url = new URL();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final URL _url = new internal();
//#endif
  private final int _width;
  private final int _height;

  public MapBooOLD_MultiImage_Level(URL url, int width, int height)
  {
	  _url = new URL(url);
	  _width = width;
	  _height = height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL getUrl() const
  public final URL getUrl()
  {
	return _url;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getWidth() const
  public final int getWidth()
  {
	return _width;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getHeight() const
  public final int getHeight()
  {
	return _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

  public void dispose()
  {

  }

}
