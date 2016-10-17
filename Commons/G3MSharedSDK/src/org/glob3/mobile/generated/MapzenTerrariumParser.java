package org.glob3.mobile.generated; 
//
//  MapzenTerrariumParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

//
//  MapzenTerrariumParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//


//class FloatBufferTerrainElevationGrid;
//class IImage;
//class Sector;


public class MapzenTerrariumParser
{
  private MapzenTerrariumParser()
  {
  }

  public static FloatBufferTerrainElevationGrid parse(IImage image, Sector sector, double deltaHeight)
  {
    MutableColor255 pixel = new MutableColor255((byte) 0, (byte) 0, (byte) 0, (byte) 0);
  
    final int width = image.getWidth();
    final int height = image.getHeight();
  
    final int bufferSize = width * height;
  
    float[] buffer = new float[bufferSize];
  
    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++)
      {
        image.getPixel(x, y, pixel);
        final float elevation = ((pixel._red * 256.0f) + pixel._green + (pixel._blue / 256.0f)) - 32768.0f;
        final int index = ((height-1-y) * width) + x;
        buffer[index] = elevation;
      }
    }
  
    if (image != null)
       image.dispose();
  
    return new FloatBufferTerrainElevationGrid(sector, new Vector2I(width, height), buffer, bufferSize, deltaHeight);
  }



}