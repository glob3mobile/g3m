//
//  TileKey.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#ifndef __G3MiOSSDK__TileKey__
#define __G3MiOSSDK__TileKey__

#include <string>


class TileKey {
private:
  const int    _level;
  const int    _row;
  const int    _column;
  
  
public:
  TileKey(int level,
          int row,
          int column):
  _level(level),
  _row(row),
  _column(column)
  {
    
  }
  
  TileKey(const TileKey& that):
  _level(that._level),
  _row(that._row),
  _column(that._column)
  {
    
  }
  
  
  int getLevel() const {
    return _level;
  }
  
  int getRow() const {
    return _row;
  }
  
  int getColumn() const {
    return _column;
  }
  
#ifdef C_CODE
  bool operator<(const TileKey& that) const {
    if (_level < that._level) {
      return true;
    }
    else if (_level > that._level) {
      return false;
    }

    if (_column < that._column) {
      return true;
    }
    else if (_column > that._column) {
      return false;
    }

    return (_row < that._row);
  }
#endif
  
#ifdef JAVA_CODE
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
#endif
  
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  const std::string tinyDescription() const;

};

#endif
