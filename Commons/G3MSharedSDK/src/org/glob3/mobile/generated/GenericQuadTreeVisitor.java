package org.glob3.mobile.generated; 
public abstract class GenericQuadTreeVisitor extends Disposable
{

  private int _comparisonsDone;

  public GenericQuadTreeVisitor()
  {
     _comparisonsDone = 0;
  }
  public void dispose()
  {
  super.dispose();

  }

  public final void addComparisonsDoneWhileVisiting(int n)
  {
     _comparisonsDone += n;
  }
  public final int getNComparisonsDone()
  {
     return _comparisonsDone;
  }

  public abstract boolean visitElement(Sector sector, Object element);
  public abstract boolean visitElement(Geodetic2D geodetic, Object element);

  public abstract void endVisit(boolean aborted);

}