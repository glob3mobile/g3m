package org.glob3.mobile.generated;import java.util.*;

public class SurfaceElevationProvider_Visitor extends GenericQuadTreeVisitor
{
  private final ElevationData _elevationData;
  private final double _verticalExaggeration;

  public SurfaceElevationProvider_Visitor(ElevationData ed, double verticalExaggeration)
  {
	  _elevationData = ed;
	  _verticalExaggeration = verticalExaggeration;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean visitElement(const Sector& sector, const Object* element) const
  public final boolean visitElement(Sector sector, Object element)
  {
	SurfaceElevationListener listener = (SurfaceElevationListener) element;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: listener->elevationChanged(sector, _elevationData, _verticalExaggeration);
	listener.elevationChanged(new Sector(sector), _elevationData, _verticalExaggeration);
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean visitElement(const Geodetic2D& geodetic, const Object* element) const
  public final boolean visitElement(Geodetic2D geodetic, Object element)
  {
	SurfaceElevationListener listener = (SurfaceElevationListener) element;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double height = _elevationData->getElevationAt(geodetic);
	double height = _elevationData.getElevationAt(new Geodetic2D(geodetic));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: listener->elevationChanged(geodetic, height, _verticalExaggeration);
	listener.elevationChanged(new Geodetic2D(geodetic), height, _verticalExaggeration);
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void endVisit(boolean aborted) const
  public final void endVisit(boolean aborted)
  {
  }
}
