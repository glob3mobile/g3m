
#ifndef G3MBuilder_Emscripten_hpp
#define G3MBuilder_Emscripten_hpp


#include <G3M/IG3MBuilder.hpp>


class IThreadUtils;
class IStorage;
class IDownloader;
class G3MWidget_Emscripten;


class G3MBuilder_Emscripten : public IG3MBuilder {
private:
  G3MWidget_Emscripten* _nativeWidget;

  void addGPUProgramSources();
  
protected:
  IThreadUtils* createDefaultThreadUtils();
  IStorage*     createDefaultStorage();
  IDownloader*  createDefaultDownloader();
  
  
public:
  G3MBuilder_Emscripten();
  
  ~G3MBuilder_Emscripten();
  
  G3MWidget_Emscripten* createWidget();

};

#endif
