//
//  IGPUProgramFactory.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/07/14.
//
//

#ifndef __G3MiOSSDK_IGPUProgramFactory__
#define __G3MiOSSDK_IGPUProgramFactory__

#include <string>

class GPUProgram;



class IGPUProgramFactory{

public:
	virtual void initialize() const = 0;
	virtual const GPUProgram* get(const std::string& name) const = 0;

	virtual ~IGPUProgramFactory(){};

};
#endif
