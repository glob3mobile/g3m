package org.glob3.mobile.generated; 
public interface LayerTouchEventListener
{

  /**
   Process terrain touch event, return true if the event was processed.
   */
  boolean onTerrainTouch(G3MEventContext context, LayerTouchEvent ev);

  void dispose();

}