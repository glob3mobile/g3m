package org.glob3.mobile.generated; 
//
//  GFont.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/10/13.
//
//

//
//  GFont.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/10/13.
//
//




public class GFont
{
  private static final String SERIF = "serif";
  private static final String SANS_SERIF = "sans-serif";
  private static final String MONOSPACED = "monospaced";

  private final String _name;
  private final float _size;
  private final boolean _bold;
  private final boolean _italic;

  private GFont(String name, float size, boolean bold, boolean italic)
  {
     _name = name;
     _size = size;
     _bold = bold;
     _italic = italic;

  }



  public static GFont serif(float size, boolean bold)
  {
     return serif(size, bold, false);
  }
  public static GFont serif(float size)
  {
     return serif(size, false, false);
  }
  public static GFont serif()
  {
     return serif(20, false, false);
  }
  public static GFont serif(float size, boolean bold, boolean italic)
  {
    return new GFont(GFont.SERIF, size, bold, italic);
  }

  public static GFont sansSerif(float size, boolean bold)
  {
     return sansSerif(size, bold, false);
  }
  public static GFont sansSerif(float size)
  {
     return sansSerif(size, false, false);
  }
  public static GFont sansSerif()
  {
     return sansSerif(20, false, false);
  }
  public static GFont sansSerif(float size, boolean bold, boolean italic)
  {
    return new GFont(GFont.SANS_SERIF, size, bold, italic);
  }

  public static GFont monospaced(float size, boolean bold)
  {
     return monospaced(size, bold, false);
  }
  public static GFont monospaced(float size)
  {
     return monospaced(size, false, false);
  }
  public static GFont monospaced()
  {
     return monospaced(20, false, false);
  }
  public static GFont monospaced(float size, boolean bold, boolean italic)
  {
    return new GFont(GFont.MONOSPACED, size, bold, italic);
  }

  public GFont(GFont that)
  {
     _name = that._name;
     _size = that._size;
     _bold = that._bold;
     _italic = that._italic;

  }

  public void dispose()
  {
  }

  public final boolean isSerif()
  {
    return (_name.compareTo(GFont.SERIF) == 0);
  }
  public final boolean isSansSerif()
  {
    return (_name.compareTo(GFont.SANS_SERIF) == 0);
  }
  public final boolean isMonospaced()
  {
    return (_name.compareTo(GFont.MONOSPACED) == 0);
  }

  public final float getSize()
  {
    return _size;
  }

  public final boolean isBold()
  {
    return _bold;
  }

  public final boolean isItalic()
  {
    return _italic;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(GFont name=\"");
    isb.addString(_name);
    isb.addString("\", size=");
    isb.addFloat(_size);
    if (_bold)
    {
      isb.addString(", bold");
    }
    if (_italic)
    {
      isb.addString(", italic");
    }
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}