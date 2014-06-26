//
//  FloatBuffer_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 26/05/14.
//
//

#include "FloatBuffer_win8.hpp"

#include <sstream>

long long FloatBuffer_win8::_nextID = 0;

const std::string FloatBuffer_win8::description() const {
	std::ostringstream oss;

	oss << "FloatBuffer_win(";
	oss << "size=";
	oss << _size;
	oss << ", timestamp=";
	oss << _timestamp;
	oss << ", values=(";
	for (int i = 0; i < _size; i++) {
		if (i > 0) {
			oss << ",";
		}
		oss << _values[i];
	}
	oss << ")";

	oss << ")";

	return oss.str();
}