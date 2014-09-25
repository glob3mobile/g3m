//
//  GLUniformID_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler S N on 19/07/14.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef __G3MWindowsSDK_GLUniformID_win8__
#define __G3MWindowsSDK_GLUniformID_win8__
#include "IGLUniformID.hpp"

class GLUniformID_win8 : public IGLUniformID{
private:
	int _id;
public:
	GLUniformID_win8(int id) :_id(id) {}

	int getID() { return _id; }

	bool isValid() const {
		return _id > -1;
	}

};


#endif