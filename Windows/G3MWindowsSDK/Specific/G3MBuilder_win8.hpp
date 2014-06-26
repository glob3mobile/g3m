//
//  G3MBuilder_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//
//

#ifndef __G3MWindowsSDK_G3MBuilder_win8__
#define __G3MWindowsSDK_G3MBuilder_win8__

#include "IG3MBuilder.hpp"

class G3MWidget_win8;


class G3MBuilder_win8 : public IG3MBuilder{
private:
	G3MWidget_win8* _nativeWidget;

protected:
	IThreadUtils* createDefaultThreadUtils();
	Storage*     createDefaultStorage();
	IDownloader*  createDefaultDownloader();

public:
	G3MBuilder_win8();
	G3MWidget_win8* createWidget();
	
};


#endif