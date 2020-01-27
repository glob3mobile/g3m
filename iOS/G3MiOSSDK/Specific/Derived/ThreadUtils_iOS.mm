//
//  ThreadUtils_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//

#include "ThreadUtils_iOS.hpp"

#include "G3M/GTask.hpp"

#include <mach/mach_host.h>


static int countCores() {
  host_basic_info_data_t hostInfo;
  mach_msg_type_number_t infoCount;

  infoCount = HOST_BASIC_INFO_COUNT;
  host_info( mach_host_self(), HOST_BASIC_INFO, (host_info_t)&hostInfo, &infoCount ) ;

  return hostInfo.max_cpus ;
}


ThreadUtils_iOS::ThreadUtils_iOS() {
  _backgroundQueue = [[NSOperationQueue alloc] init];

  int maxThreads = countCores();
  if (maxThreads > 1) {
    maxThreads--;
  }
  [_backgroundQueue setMaxConcurrentOperationCount: maxThreads];
  [_backgroundQueue setName: @"com.glob3.backgroundqueue"];
}

ThreadUtils_iOS::~ThreadUtils_iOS() {
  [_backgroundQueue waitUntilAllOperationsAreFinished];
}


void ThreadUtils_iOS::justInitialized() {

}

void ThreadUtils_iOS::invokeInRendererThread(GTask* task,
                                             bool autoDelete) const {
  dispatch_async(dispatch_get_main_queue(),
                 ^{
                   task->run(getContext());
                   if (autoDelete) {
                     delete task;
                   }
                 });
}

void ThreadUtils_iOS::invokeInBackground(GTask* task,
                                         bool autoDelete) const {
  [ _backgroundQueue addOperationWithBlock:
   ^{
     task->run(getContext());
     if (autoDelete) {
       delete task;
     }
   }
   ];
}
