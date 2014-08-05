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
//#include <ppltasks.h>
#include "NativeGL_win8.hpp"


//class NativeGL_win8;
//struct ID3D11VertexShader;


class GPUProgramFactory_D3D: public IGPUProgramFactory {
private:
	NativeGL_win8* _ngl;
	bool _loadingComplete = false;
	
	


	char* loadShaderFileFromDisk(std::string filename);
	


public:
	GPUProgramFactory_D3D() :
		_ngl(NULL){};

	IGPUProgram* get(GL* gl, const std::string& name);



};



#endif