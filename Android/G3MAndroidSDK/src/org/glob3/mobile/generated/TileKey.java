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


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getLevel() const
  public final int getLevel()
  {
	return _level;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getRow() const
  public final int getRow()
  {
	return _row;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getColumn() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("(level=").add(_level).add(", row=").add(_row).add(", col=").add(_column).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String tinyDescription() const
  public final String tinyDescription()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add(_level).add("-").add(_row).add("/").add(_column);
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}