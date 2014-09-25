//
//  ShortBuffer_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 26/05/14.
//
//

#ifndef __G3MWindowsSDK__ShortBuffer_win8__
#define __G3MWindowsSDK__ShortBuffer_win8__

#include "IShortBuffer.hpp"
#include "ILogger.hpp"

class ShortBuffer_win8 : public IShortBuffer {
private:
	int _size;
	short*    _values;
	int       _timestamp;

	//ID
	const long long _id;
	static long long _nextID;

public:
	ShortBuffer_win8(int size) :
		_size(size),
		_timestamp(0),
		_id(_nextID++)
	{
		_values = new short[size];
		if (_values == NULL) {
			ILogger::instance()->logError("Allocating error.");
		}
	}

	long long getID() const{
		return _id;
	}

	virtual ~ShortBuffer_win8(){
		delete[] _values;
	}


	int size() const {
		return _size;
	}

	int timestamp() const {
		return _timestamp;
	}

	short get(int i) const {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		return _values[i];
	}

	void put(int i, short value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		if (_values[i] != value) {
			_values[i] = value;
			_timestamp++;
		}
	}

	void rawPut(int i, short value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}
		_values[i] = value;
	}

	short* getPointer() const {
		return _values;
	}

	const std::string description() const;


};

#endif