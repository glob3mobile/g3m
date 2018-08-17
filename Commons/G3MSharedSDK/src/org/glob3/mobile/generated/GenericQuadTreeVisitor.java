package org.glob3.mobile.generated;import java.util.*;

public abstract class GenericQuadTreeVisitor
{

  private int _comparisonsDone;

  public GenericQuadTreeVisitor()
  {
	  _comparisonsDone = 0;
  }
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void addComparisonsDoneWhileVisiting(int n) const
  public final void addComparisonsDoneWhileVisiting(int n)
  {
	_comparisonsDone += n;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getNComparisonsDone() const
  public final int getNComparisonsDone()
  {
	return _comparisonsDone;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean visitElement(const Sector& sector, const Object* element) const = 0;
  public abstract boolean visitElement(Sector sector, Object element);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean visitElement(const Geodetic2D& geodetic, const Object* element) const = 0;
  public abstract boolean visitElement(Geodetic2D geodetic, Object element);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void endVisit(boolean aborted) const = 0;
  public abstract void endVisit(boolean aborted);

}
