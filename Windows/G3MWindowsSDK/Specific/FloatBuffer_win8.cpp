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

//inserts a 1.0f every three values
float* FloatBuffer_win8::getPaddedTo4Pointer(){
	if (_paddedTo4Values == NULL){
		int paddedSize = _size + (_size / 3);
		_paddedTo4Values = new float[paddedSize];

		for (int i = 0; i < paddedSize; i++){
			if (i % 4 == 3){
				_paddedTo4Values[i] = 1.0f;
			}
			else{
				_paddedTo4Values[i] = _values[i - (i/4)];
			}
		}
	}
	return _paddedTo4Values;
}