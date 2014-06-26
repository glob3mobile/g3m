//
//  ThreadUtils_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//
//

#ifndef __G3MWindowsSDK_ThreadUtils_win8__
#define __G3MWindowsSDK_ThreadUtils_win8__

#include "IThreadUtils.hpp"

class ThreadUtils_win8 : public IThreadUtils{
public:
	void onResume(const G3MContext* context);

	void onPause(const G3MContext* context);

	void onDestroy(const G3MContext* context);

	void initialize(const G3MContext* context);

	void invokeInRendererThread(GTask* task, bool autoDelete) const;

	void invokeInBackground(GTask* task, bool autoDelete) const;

};


#endif