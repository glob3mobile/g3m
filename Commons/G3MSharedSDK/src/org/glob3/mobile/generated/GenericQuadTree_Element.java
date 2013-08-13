package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////
public abstract class GenericQuadTree_Element extends Disposable
{
  public final Object _element;

  public GenericQuadTree_Element(Object element)
  {
     _element = element;
  }

  public abstract boolean isSectorElement();
  public abstract Geodetic2D getCenter();
  public abstract Sector getSector();

  public void dispose()
  {
    JAVA_POST_DISPOSE
  }
}