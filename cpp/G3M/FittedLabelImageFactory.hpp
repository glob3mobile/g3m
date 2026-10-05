//
//  FittedLabelImageFactory.hpp
//  G3M
//

#ifndef __G3M__FittedLabelImageFactory__
#define __G3M__FittedLabelImageFactory__

#include "AbstractImageFactory.hpp"

#include <string>
#include <vector>

#include "LabelStyle.hpp"

class ICanvas;
class GFont;


/**
 A label that fits a maximum width: when the text is too wide it is split in
 two lines at the blank that leaves them most even; when still too wide the
 font shrinks, one point at a time, down to minFontSizeFactor times its size.
 The maximum width is given in pixels or as a reference text measured with the
 style's font ("Washington, D.C.").
 */
class FittedLabelImageFactory : public AbstractImageFactory {
private:
  const std::string _text;
  const LabelStyle* _style;
  const std::string _maxWidthText;
  const float       _maxWidth;
  const float       _minFontSizeFactor;
  const int         _lineSeparation;

  float maxWidth(ICanvas* canvas) const;
  float minFontSize() const;

  static float widthOf(ICanvas* canvas,
                       const std::string& text);

  static const std::vector<std::string> twoLines(ICanvas* canvas,
                                                 const std::string& text);

  static const std::vector<int> blanksPositions(const std::string& text);

  IImageFactory* createLines(const std::vector<std::string>& lines,
                             const LabelStyle& style) const;

  IImageFactory* createFitted(ICanvas* canvas) const;

protected:
  ~FittedLabelImageFactory();

public:

  /** minFontSizeFactor in [0.1, 0.99] allows shrinking the font; any other value keeps its size */
  FittedLabelImageFactory(const std::string& text,
                          const LabelStyle&  style,
                          const std::string& maxWidthText,
                          const float        minFontSizeFactor,
                          const int          lineSeparation);

  FittedLabelImageFactory(const std::string& text,
                          const LabelStyle&  style,
                          const float        maxWidth,
                          const float        minFontSizeFactor,
                          const int          lineSeparation);

  bool isMutable() const {
    return false;
  }

  void create(const G3MContext* context,
              IImageFactoryListener* listener,
              bool deleteListener);

};

#endif
