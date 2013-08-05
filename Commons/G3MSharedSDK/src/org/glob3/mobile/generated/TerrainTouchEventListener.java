package org.glob3.mobile.generated; 
public interface TerrainTouchEventListener
{

  /**
   Process terrain touch event, return true if the event was processed.
   */
  boolean onTerrainTouch(G3MEventContext context, TerrainTouchEvent ev);

  public void dispose();

}