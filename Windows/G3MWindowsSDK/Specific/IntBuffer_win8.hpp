//
//  IntBuffer_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 26/05/14.
//
//

#ifndef __G3MWindowsSDK__IntBuffer_win8__
#define __G3MWindowsSDK__IntBuffer_win8__


#include "IIntBuffer.hpp"
#include "ILogger.hpp"

class IntBuffer_win8 : public IIntBuffer {
private:
	const int _size;
	int*      _values;
	int       _timestamp;

	//ID
	const long long _id;
	static long long _nextID;

public:
	IntBuffer_win8(int size) :
		_size(size),
		_timestamp(0),
		_id(_nextID++)
	{
		_values = new int[size];

		if (_values == NULL) {
			ILogger::instance()->logError("Allocating error.");
		}
	}

	long long getID() const{
		return _id;
	}

	virtual ~IntBuffer_win8() {
		delete[] _values;
	}

	int size() const {
		return _size;
	}

	int timestamp() const {
		return _timestamp;
	}

	int get(int i) const {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Get error.");
		}

		return _values[i];
	}

	void put(int i, int value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		if (_values[i] != value) {
			_values[i] = value;
			_timestamp++;
		}
	}

	void rawPut(int i, int value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		_values[i] = value;
	}

	int* getPointer() const {
		return _values;
	}

	const std::string description() const;

};

#endif