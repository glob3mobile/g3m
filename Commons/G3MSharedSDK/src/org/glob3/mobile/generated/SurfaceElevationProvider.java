package org.glob3.mobile.generated;import java.util.*;

public abstract class SurfaceElevationProvider
{






//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void dispose();
//#endif

  public abstract void addListener(Angle latitude, Angle longitude, SurfaceElevationListener listener);

  public abstract void addListener(Geodetic2D position, SurfaceElevationListener listener);

  public abstract boolean removeListener(SurfaceElevationListener listener);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float getVerticalExaggeration() const = 0;
  public abstract float getVerticalExaggeration();

}
