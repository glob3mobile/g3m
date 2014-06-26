//
//  StringBuilder_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 02/06/14.
//  
//NOTE: exact copy of StringBuilder_iOS.hpp
//

#ifndef __G3MWindowsSDK_StringBuilder_win8__
#define __G3MWindowsSDK_StringBuilder_win8__

#include "IStringBuilder.hpp"

#include <sstream>

class StringBuilder_win8 : public IStringBuilder {
private:
	std::ostringstream _oss;

protected:

	IStringBuilder* getNewInstance() const {
		return new StringBuilder_win8();
	}

public:

	StringBuilder_win8() {
		_oss.precision(20);
	}

	IStringBuilder* addBool(bool b) {
		_oss << (b ? "true" : "false");
		return this;
	}

	IStringBuilder* addDouble(double d) {
		_oss << d;
		return this;
	}

	IStringBuilder* addFloat(float f) {
		_oss << f;
		return this;
	}

	IStringBuilder* addInt(int i) {
		_oss << i;
		return this;
	}

	IStringBuilder* addLong(long long l) {
		_oss << l;
		return this;
	}

	IStringBuilder* addString(const std::string& s) {
		_oss << s;
		return this;
	}

	const std::string getString() const {
		return _oss.str();
	}

};

#endif