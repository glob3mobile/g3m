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




public class CompositeTileRasterizer extends CanvasTileRasterizer implements ChangedListener
{
  private java.util.ArrayList<TileRasterizer> _children = new java.util.ArrayList<TileRasterizer>();

  private G3MContext _context;

  public CompositeTileRasterizer()
  {
     _context = null;

  }

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

  public final void initialize(G3MContext context)
  {
    _context = context;
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      TileRasterizer child = _children.get(i);
      child.initialize(context);
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

  public final void rawRasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodeleteListener)
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
  
      canvas.drawImage(image, 0, 0, width, height);
  
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
      tileRasterizer.setChangeListener(this);
      if (_context != null)
      {
        tileRasterizer.initialize(_context);
      }
      notifyChanges();
    }
  }

  public final void changed()
  {
    notifyChanges();
  }

}