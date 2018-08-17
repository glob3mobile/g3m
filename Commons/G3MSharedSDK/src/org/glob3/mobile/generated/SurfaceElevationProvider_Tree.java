package org.glob3.mobile.generated;import java.util.*;

//Every SurfaceElevationProvider should store petitions in a SurfaceElevationProvider_Tree
public class SurfaceElevationProvider_Tree extends GenericQuadTree
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void notifyListeners(const ElevationData* ed, double verticalExaggeration) const
  public final void notifyListeners(ElevationData ed, double verticalExaggeration)
  {
	SurfaceElevationProvider_Visitor visitor = new SurfaceElevationProvider_Visitor(ed, verticalExaggeration);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: acceptVisitor(ed->getSector(), visitor);
	acceptVisitor(ed.getSector(), new SurfaceElevationProvider_Visitor(visitor));
  }
}
