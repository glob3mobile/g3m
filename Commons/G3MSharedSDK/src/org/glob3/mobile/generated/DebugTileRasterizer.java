package org.glob3.mobile.generated; 
//
//  DebugTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

//
//  DebugTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//



//class ICanvas;
//class Sector;

public class DebugTileRasterizer extends TileRasterizer
{
  private ICanvas _canvas;
  private int _canvasWidth;
  private int _canvasHeight;

  private final GFont _font = new GFont();
  private final Color _color ;


  private String getTileKeyLabel(Tile tile)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("L:");
    isb.addInt(tile.getLevel());
  
    isb.addString(", C:");
    isb.addInt(tile.getColumn());
  
    isb.addString(", R:");
    isb.addInt(tile.getRow());
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  private String getSectorLabel1(Sector sector)
  {
    return "Lower lat: " + sector.lower().latitude().description();
  }
  private String getSectorLabel2(Sector sector)
  {
    return "Lower lon: " + sector.lower().longitude().description();
  }
  private String getSectorLabel3(Sector sector)
  {
    return "Upper lat: " + sector.upper().latitude().description();
  }
  private String getSectorLabel4(Sector sector)
  {
    return "Upper lon: " + sector.upper().longitude().description();
  }

  private ICanvas getCanvas(int width, int height)
  {
    if ((_canvas == null) || (_canvasWidth != width) || (_canvasHeight != height))
    {
      if (_canvas != null)
         _canvas.dispose();
  
      _canvas = IFactory.instance().createCanvas();
      _canvas.initialize(width, height);
  
      _canvasWidth = width;
      _canvasHeight = height;
    }
    else
    {
      _canvas.setFillColor(Color.transparent());
      _canvas.fillRectangle(0, 0, width, height);
    }
    return _canvas;
  }

  public DebugTileRasterizer()
  {
     _canvas = null;
     _canvasWidth = -1;
     _canvasHeight = -1;
     _font = new GFont(GFont.monospaced(15));
     _color = new Color(Color.white());
  }

  public void dispose()
  {
    if (_canvas != null)
       _canvas.dispose();
  }

  public final String getId()
  {
    return "DebugTileRasterizer";
  }

  public final void rasterize(IImage image, Tile tile, IImageListener listener, boolean autodelete)
  {
  
    final int width = image.getWidth();
    final int height = image.getHeight();
  
    ICanvas canvas = getCanvas(width, height);
  
    canvas.removeShadow();
  
    canvas.drawImage(image, 0, 0);
  
    canvas.setStrokeColor(_color);
    canvas.setStrokeWidth(1);
    canvas.strokeRectangle(0, 0, width, height);
  
  
    canvas.setShadow(Color.black(), 2, 1, -1);
    ColumnCanvasElement col = new ColumnCanvasElement();
    col.add(new TextCanvasElement(getTileKeyLabel(tile), _font, _color));
  
    final Sector sectorTile = tile.getSector();
    col.add(new TextCanvasElement(getSectorLabel1(sectorTile), _font, _color));
    col.add(new TextCanvasElement(getSectorLabel2(sectorTile), _font, _color));
    col.add(new TextCanvasElement(getSectorLabel3(sectorTile), _font, _color));
    col.add(new TextCanvasElement(getSectorLabel4(sectorTile), _font, _color));
  
    final Vector2F colExtent = col.getExtent(canvas);
    col.drawAt((width - colExtent._x) / 2, (height - colExtent._y) / 2, canvas);
  
  
    canvas.createImage(listener, autodelete);
  
    if (image != null)
       image.dispose();
  }

}