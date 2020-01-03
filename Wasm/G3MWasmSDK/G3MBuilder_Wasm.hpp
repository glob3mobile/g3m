

#include "IG3MBuilder.hpp"

class IThreadUtils;
class IStorage;
class IDownloader;


class G3MBuilder_Wasm : public IG3MBuilder {
private:
  void addGPUProgramSources() const;

protected:
  IThreadUtils* createDefaultThreadUtils();
  IStorage*     createDefaultStorage();
  IDownloader*  createDefaultDownloader();


public:
  G3MBuilder_Wasm();
  
  ~G3MBuilder_Wasm();

  G3MWidget_Wasm* createWidget() const;

};
