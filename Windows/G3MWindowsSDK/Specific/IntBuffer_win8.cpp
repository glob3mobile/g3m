//
//  IntBuffer_win.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 26/05/14.
//
//

#include "IntBuffer_win8.hpp"
#include <sstream>

long long IntBuffer_win8::_nextID = 0;

const std::string IntBuffer_win8::description() const {
	std::ostringstream oss;

	oss << "IntBuffer_win(";
	oss << "size=";
	oss << _size;
	oss << ", timestamp=";
	oss << _timestamp;
	oss << ", values=";
	oss << _values;
	oss << ")";

	return oss.str();
}