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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean operator <(const TileKey& that) const
//C++ TO JAVA CONVERTER TODO TASK: Operators cannot be overloaded in Java:
  boolean operator <(TileKey that)
  {
	if (_level < that._level)
	{
	  return true;
	}
	else if (_level > that._level)
	{
	  return false;
	}

	if (_column < that._column)
	{
	  return true;
	}
	else if (_column > that._column)
	{
	  return false;
	}

	return (_row < that._row);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	std.ostringstream buffer = new std.ostringstream();
	buffer << "(level=";
	buffer << _level;
	buffer << ", row=";
	buffer << _row;
	buffer << ", col=";
	buffer << _column;
	buffer << ")";
	return buffer.str();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String tinyDescription() const
  public final String tinyDescription()
  {
	std.ostringstream buffer = new std.ostringstream();
	buffer << _level;
	buffer << "-";
	buffer << _row;
	buffer << "/";
	buffer << _column;
	return buffer.str();
  }

}