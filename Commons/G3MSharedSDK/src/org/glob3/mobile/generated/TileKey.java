package org.glob3.mobile.generated; 
//
//  TileKey.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

//
//  TileKey.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//




public class TileKey
{
  private final int _level;
  private final int _row;
  private final int _column;


  public TileKey(int level, int row, int column)
  {
     _level = level;
     _row = row;
     _column = column;

  }

  public TileKey(TileKey that)
  {
     _level = that._level;
     _row = that._row;
     _column = that._column;

  }


  public final int getLevel()
  {
    return _level;
  }

  public final int getRow()
  {
    return _row;
  }

  public final int getColumn()
  {
    return _column;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + _column;
    result = prime * result + _level;
    result = prime * result + _row;
    return result;
  }
  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileKey other = (TileKey) obj;
		if (_column != other._column)
			return false;
		if (_level != other._level)
			return false;
		if (_row != other._row)
			return false;
		return true;
	}

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(level=");
    isb.addInt(_level);
    isb.addString(", row=");
    isb.addInt(_row);
    isb.addString(", col=");
    isb.addInt(_column);
    isb.addString(")");
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

  public final String tinyDescription()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addInt(_level);
    isb.addString("-");
    isb.addInt(_row);
    isb.addString("/");
    isb.addInt(_column);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}