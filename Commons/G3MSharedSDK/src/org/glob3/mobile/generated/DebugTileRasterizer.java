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



public class DebugTileRasterizer extends TileRasterizer
{


  private String getTileLabel1(Tile tile)
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

  private String getTileLabel2(Tile tile)
  {
    return "Lower lat: " + tile.getSector().lower().latitude().description();
  }
  private String getTileLabel3(Tile tile)
  {
    return "Lower lon: " + tile.getSector().lower().longitude().description();
  }
  private String getTileLabel4(Tile tile)
  {
    return "Upper lat: " + tile.getSector().upper().latitude().description();
  }
  private String getTileLabel5(Tile tile)
  {
    return "Upper lon: " + tile.getSector().upper().longitude().description();
  }

  public final String getId()
  {
    return "DebugTileRasterizer";
  }

  public final void rasterize(IImage image, Tile tile, IImageListener listener, boolean autodelete)
  {
  
    final int width = image.getWidth();
    final int height = image.getHeight();
  
    ICanvas canvas = IFactory.instance().createCanvas();
    canvas.initialize(width, height);
  
    canvas.drawImage(image, 0, 0);
  
    canvas.setStrokeColor(Color.yellow());
    canvas.setStrokeWidth(2);
    canvas.strokeRectangle(0, 0, width, height);
  
  
    ColumnCanvasElement col = new ColumnCanvasElement();
    col.add(new TextCanvasElement(getTileLabel1(tile), GFont.serif(), Color.yellow()));
  
    final GFont sectorFont = GFont.monospaced(14);
    final Color sectorColor = Color.yellow();
    col.add(new TextCanvasElement(getTileLabel2(tile), sectorFont, sectorColor));
    col.add(new TextCanvasElement(getTileLabel3(tile), sectorFont, sectorColor));
    col.add(new TextCanvasElement(getTileLabel4(tile), sectorFont, sectorColor));
    col.add(new TextCanvasElement(getTileLabel5(tile), sectorFont, sectorColor));
  
    final Vector2F colExtent = col.getExtent(canvas);
    col.drawAt((width - colExtent._x) / 2, (height - colExtent._y) / 2, canvas);
  
    canvas.createImage(listener, autodelete);
  
    if (canvas != null)
       canvas.dispose();
  
    if (image != null)
       image.dispose();
  }

}