//
//  ByteBuffer_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler S N on 26/05/14.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "ByteBuffer_win8.hpp"

#include "IStringBuilder.hpp"


const std::string ByteBuffer_win8::description() const {
	IStringBuilder* isb = IStringBuilder::newStringBuilder();
	isb->addString("(ByteBuffer_win: size=");
	isb->addInt(_size);

	isb->addString(")");
	const std::string s = isb->getString();
	delete isb;
	return s;
}

const std::string ByteBuffer_win8::getAsString() const {
	return std::string(_values, _values + _size);
}