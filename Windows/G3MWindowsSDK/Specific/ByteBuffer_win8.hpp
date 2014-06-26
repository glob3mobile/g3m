//
//  ByteBuffer_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler S N on 26/05/14.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#ifndef __G3MWindowsSDK_ByteBuffer_win8__
#define __G3MWindowsSDK_ByteBuffer_win8__

#include "IByteBuffer.hpp"
#include "ILogger.hpp"

class ByteBuffer_win8 : public IByteBuffer {
private:
	const int            _size;
	unsigned char* const _values;
	int                  _timestamp;

public:
	ByteBuffer_win8(int size) :
		_values(new unsigned char[size]),
		_size(size),
		_timestamp(-1)
	{
		if (_values == NULL) {
			ILogger::instance()->logError("Allocating error.");
		}
	}

	ByteBuffer_win8(unsigned char* values,
		int size) :
		_values(values),
		_size(size),
		_timestamp(-1)
	{

	}


	virtual ~ByteBuffer_win8() {
		delete[] _values;
	}

	int size() const {
		return _size;
	}

	int timestamp() const {
		return _timestamp;
	}

	unsigned char get(int i) const {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Get error.");
		}

		return _values[i];
	}

	void put(int i,
		unsigned char value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		if (_values[i] != value) {
			_values[i] = value;
			_timestamp++;
		}
	}

	void rawPut(int i,
		unsigned char value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		_values[i] = value;
	}

	unsigned char* getPointer() const {
		return _values;
	}

	const std::string description() const;

	const std::string getAsString() const;

};


#endif