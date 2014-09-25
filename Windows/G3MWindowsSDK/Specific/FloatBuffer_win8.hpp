//
//  FloatBuffer_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 26/05/14.
//
//

#ifndef __G3MWindowsSDK__FloatBuffer_win8__
#define __G3MWindowsSDK__FloatBuffer_win8__

#include "IFloatBuffer.hpp"
#include "ILogger.hpp"


class FloatBuffer_win8 : public IFloatBuffer {
private:
	const int _size;
	float*    _values;
	int       _timestamp;

	//Same values with added padding (to turn vec3 to vec4)
	float* _paddedTo4Values;


	//ID
	const long long _id;
	static long long _nextID;

public:
	FloatBuffer_win8(int size) :
		_size(size),
		_timestamp(0),
		_values(new float[size]),
		//_vertexBuffer(-1),
		//_vertexBufferTimeStamp(-1),
		//_vertexBufferCreated(false),
		_id(_nextID++)
	{
		if (_values == NULL) {
			ILogger::instance()->logError("Allocating error.");
		}

		//_newCounter++;
		//showStatistics();

	}

	long long getID() const{
		return _id;
	}

	FloatBuffer_win8(float f0,
		float f1,
		float f2,
		float f3,
		float f4,
		float f5,
		float f6,
		float f7,
		float f8,
		float f9,
		float f10,
		float f11,
		float f12,
		float f13,
		float f14,
		float f15) :
		_size(16),
		_timestamp(0),
		/*_vertexBuffer(-1),
		_vertexBufferTimeStamp(-1),
		_vertexBufferCreated(false),*/
		_id(_nextID)
	{
		_values = new float[16];
		_values[0] = f0;
		_values[1] = f1;
		_values[2] = f2;
		_values[3] = f3;
		_values[4] = f4;
		_values[5] = f5;
		_values[6] = f6;
		_values[7] = f7;
		_values[8] = f8;
		_values[9] = f9;
		_values[10] = f10;
		_values[11] = f11;
		_values[12] = f12;
		_values[13] = f13;
		_values[14] = f14;
		_values[15] = f15;

		//_newCounter++;
		//showStatistics();
	}

	virtual ~FloatBuffer_win8(){
		delete[] _values;
		delete[] _paddedTo4Values;
	}

	int size() const {
		return _size;
	}

	int getPaddedSize() const{
		return _size + _size / 3;
	}

	int timestamp() const {
		return _timestamp;
	}

	float get(int i) const {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Get error.");
		}

		return _values[i];
	}

	void put(int i,
		float value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		if (_values[i] != value) {
			_values[i] = value;
			_timestamp++;
		}
		if (_paddedTo4Values != NULL){
			int paddedIndex = i + (i / 3);
			if (_paddedTo4Values[paddedIndex] != value){
				_paddedTo4Values[paddedIndex] = value;
			}
		}
	}

	void rawPut(int i,
		float value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		_values[i] = value;
		if (_paddedTo4Values != NULL){
			_paddedTo4Values[i + (i / 3)] = value;
		}
	}

	void rawAdd(int i, float value) {
		if (i < 0 || i > _size) {
			ILogger::instance()->logError("Buffer Put error.");
		}

		_values[i] = _values[i] + value;
		if (_paddedTo4Values == NULL){
			int paddedIndex = i + (i / 3);
			_paddedTo4Values[paddedIndex] = _paddedTo4Values[paddedIndex]+value;
		}
	}

	float* getPointer() const {
		return _values;
	}

	float* getPaddedTo4Pointer();


	const std::string description() const;

};

#endif