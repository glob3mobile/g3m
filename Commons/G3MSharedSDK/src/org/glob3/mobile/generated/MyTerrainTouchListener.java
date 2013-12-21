package org.glob3.mobile.generated; 
public class MyTerrainTouchListener implements TerrainTouchListener
{
  private ShapesEditorRenderer _renderer;

  public MyTerrainTouchListener(ShapesEditorRenderer renderer)
  {
     _renderer = renderer;
  }

  public MyTerrainTouchListener()
  {
  }

  public final boolean onTerrainTouch(G3MEventContext ec, Vector2I pixel, Camera camera, Geodetic3D position, Tile tile)
  {
    _renderer.onTouch(position);
    return true;
  }
}