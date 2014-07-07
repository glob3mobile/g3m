//
//  GPUProgramFactory_D3D.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/07/14.
//
//

#ifndef __G3MWindowsSDK_GPUProgramFactory_D3D__
#define __G3MWindowsSDK_GPUProgramFactory_D3D__

#include "IGPUProgramFactory.hpp"

class GPUProgramFactory_D3D: public IGPUProgramFactory {
public:
	GPUProgramFactory_D3D();
	const GPUProgram* get(const std::string& name) const;

};



#endif