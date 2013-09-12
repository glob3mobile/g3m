package org.glob3.mobile.generated; 
//
//  CompositeTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/12/13.
//
//

//
//  CompositeTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/12/13.
//
//



public class CompositeTileRasterizer extends CanvasTileRasterizer
{
  private java.util.ArrayList<TileRasterizer> _children = new java.util.ArrayList<TileRasterizer>();

  public void dispose()
  {
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      TileRasterizer child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  }

  public final String getId()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("CompositeTileRasterizer");
  
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      isb.addString("-");
      TileRasterizer child = _children.get(i);
      isb.addString(child.getId());
    }
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final void rasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodeleteListener)
  {
    if (_children.size() == 0)
    {
      listener.imageCreated(image);
      if (autodeleteListener)
      {
        if (listener != null)
           listener.dispose();
      }
    }
    else
    {
      final int width = image.getWidth();
      final int height = image.getHeight();
  
      ICanvas canvas = getCanvas(width, height);
  
      canvas.drawImage(image, 0, 0);
  
      canvas.createImage(new CompositeTileRasterizer_ChildImageListener(0, _children, trc, listener, autodeleteListener), true);
    }
  
    if (image != null)
       image.dispose();
  }

  public final void addTileRasterizer(TileRasterizer tileRasterizer)
  {
    if (tileRasterizer != null)
    {
      _children.add(tileRasterizer);
    }
  }

}