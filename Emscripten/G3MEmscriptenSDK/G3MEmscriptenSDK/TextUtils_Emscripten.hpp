
#ifndef TextUtils_Emscripten_hpp
#define TextUtils_Emscripten_hpp

#include "ITextUtils.hpp"

class TextUtils_Emscripten : public ITextUtils {
public:

  void createLabelImage(const std::string& label,
                        float fontSize,
                        const Color* color,
                        const Color* shadowColor,
                        IImageListener* listener,
                        bool autodelete);

  void labelImage(const IImage* image,
                  const std::string& label,
                  const LabelPosition labelPosition,
                  int separation,
                  float fontSize,
                  const Color* color,
                  const Color* shadowColor,
                  IImageListener* listener,
                  bool autodelete);

};

#endif
