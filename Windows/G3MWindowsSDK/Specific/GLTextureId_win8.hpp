//
//  GLTextureId_win8.h
//

#ifndef __G3MWindowsSDK__GLTextureId_win8__
#define __G3MWindowsSDK__GLTextureId_win8__

#include "IGLTextureId.hpp"

class GLTextureID_win8 : public IGLTextureId{
private:
	unsigned int _id;
public:
	GLTextureID_win8(unsigned int id) :
		_id(id){}

	unsigned int getID(){
		return _id;
	}

	bool isEquals(const IGLTextureId* that) const{
		return (_id == ((GLTextureID_win8*)that)->_id);
	}

	const std::string description() const{
		std::string errMsg("TODO: Implementation");
		throw std::exception(errMsg.c_str());
	}

#ifdef C_CODE
	virtual ~GLTextureID_win8() { }
#endif
#ifdef JAVA_CODE
	void dispose();
#endif
};


#endif