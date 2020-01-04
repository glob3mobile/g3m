
#ifndef Downloader_Wasm_hpp
#define Downloader_Wasm_hpp

#include "IDownloader.hpp"


class Downloader_Wasm : public IDownloader {
public:
  
  void onResume(const G3MContext* context);
  
  void onPause(const G3MContext* context);
  
  void onDestroy(const G3MContext* context);

  void initialize(const G3MContext* context,
                  FrameTasksExecutor* frameTasksExecutor);

  const std::string statistics();

};

#endif
