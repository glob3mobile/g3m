//
//  FittedLabelImageFactory.cpp
//  G3M
//

#include "FittedLabelImageFactory.hpp"

#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IStringUtils.hpp"
#include "IMathUtils.hpp"
#include "IImageFactoryListener.hpp"
#include "LabelImageFactory.hpp"
#include "ColumnLayoutImageFactory.hpp"
#include "ImageBackground.hpp"


class FittedLabelImageFactory_Listener : public IImageFactoryListener {
private:
  IImageFactory*         _fitted;
  IImageFactoryListener* _listener;
  const bool             _deleteListener;

public:
  FittedLabelImageFactory_Listener(IImageFactory*         fitted,
                                   IImageFactoryListener* listener,
                                   bool                   deleteListener) :
  _fitted(fitted),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  void imageCreated(const IImage*      image,
                    const std::string& imageName) {
    _listener->imageCreated(image, imageName);
  }

  void onError(const std::string& error) {
    _listener->onError(error);
  }

  ~FittedLabelImageFactory_Listener() {
    if (_deleteListener) {
      delete _listener;
    }
    delete _fitted;
  }

};


FittedLabelImageFactory::FittedLabelImageFactory(const std::string& text,
                                                 const LabelStyle&  style,
                                                 const std::string& maxWidthText,
                                                 const float        minFontSizeFactor,
                                                 const int          lineSeparation) :
_text(text),
_style(new LabelStyle(style)),
_maxWidthText(maxWidthText),
_maxWidth(0),
_minFontSizeFactor(minFontSizeFactor),
_lineSeparation(lineSeparation)
{
}

FittedLabelImageFactory::FittedLabelImageFactory(const std::string& text,
                                                 const LabelStyle&  style,
                                                 const float        maxWidth,
                                                 const float        minFontSizeFactor,
                                                 const int          lineSeparation) :
_text(text),
_style(new LabelStyle(style)),
_maxWidthText(""),
_maxWidth(maxWidth),
_minFontSizeFactor(minFontSizeFactor),
_lineSeparation(lineSeparation)
{
}

FittedLabelImageFactory::~FittedLabelImageFactory() {
  delete _style;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

float FittedLabelImageFactory::widthOf(ICanvas* canvas,
                                       const std::string& text) {
  return canvas->textExtent(text)._x;
}

float FittedLabelImageFactory::maxWidth(ICanvas* canvas) const {
  if (_maxWidthText.empty()) {
    return _maxWidth;
  }
  canvas->setFont(_style->getFont());
  return widthOf(canvas, _maxWidthText);
}

float FittedLabelImageFactory::minFontSize() const {
  const float fontSize = _style->getFont().getSize();
  if ((_minFontSizeFactor < 0.1) || (_minFontSizeFactor > 0.99)) {
    return fontSize;
  }
  return IMathUtils::instance()->floor(fontSize * _minFontSizeFactor);
}

const std::vector<int> FittedLabelImageFactory::blanksPositions(const std::string& text) {
  const IStringUtils* su = IStringUtils::instance();

  std::vector<int> result;
  int position = su->indexOf(text, " ");
  while (position >= 0) {
    result.push_back(position);
    position = su->indexOf(text, " ", position + 1);
  }
  return result;
}

const std::vector<std::string> FittedLabelImageFactory::twoLines(ICanvas* canvas,
                                                                 const std::string& text) {
  const IStringUtils* su = IStringUtils::instance();
  const IMathUtils*   mu = IMathUtils::instance();

  const std::string normalized = su->replaceAll(su->trim(text), "  ", " ");

  std::vector<std::string> lines;

  const std::vector<int> blanks = blanksPositions(normalized);
  if (blanks.empty()) {
    lines.push_back(normalized);
    return lines;
  }

  std::string bestLeft  = "";
  std::string bestRight = "";
  float       bestDiff  = 0;
  for (size_t i = 0; i < blanks.size(); i++) {
    const int blank = blanks.at(i);

    const std::string left  = su->trim( su->left(normalized, blank) );
    const std::string right = su->trim( su->substring(normalized, blank + 1, normalized.size()) );

    const float diff = mu->abs( widthOf(canvas, left) - widthOf(canvas, right) );
    if ((i == 0) || (diff < bestDiff)) {
      bestDiff  = diff;
      bestLeft  = left;
      bestRight = right;
    }
  }

  lines.push_back(bestLeft);
  lines.push_back(bestRight);
  return lines;
}

IImageFactory* FittedLabelImageFactory::createLines(const std::vector<std::string>& lines,
                                                    const LabelStyle& style) const {
  if (lines.size() == 1) {
    return new LabelImageFactory(lines.at(0), style);
  }

  const LabelStyle lineStyle = style.copyWithoutBackground();
  return new ColumnLayoutImageFactory(new LabelImageFactory(lines.at(0), lineStyle),
                                      new LabelImageFactory(lines.at(1), lineStyle),
                                      style.copyBackground(),
                                      _lineSeparation);
}

IImageFactory* FittedLabelImageFactory::createFitted(ICanvas* canvas) const {
  const float maxWidth    = this->maxWidth(canvas);
  const float minFontSize = this->minFontSize();

  float fontSize = _style->getFont().getSize();
  while (true) {
    const GFont font = _style->getFont().copyWithSize(fontSize);
    canvas->setFont(font);

    std::vector<std::string> lines;
    float width = widthOf(canvas, _text);
    if (width <= maxWidth) {
      lines.push_back(_text);
    }
    else {
      lines = twoLines(canvas, _text);
      width = 0;
      for (size_t i = 0; i < lines.size(); i++) {
        width = IMathUtils::instance()->max(width, widthOf(canvas, lines.at(i)));
      }
    }

    const bool fits           = (width <= maxWidth);
    const bool canShrink      = ((fontSize - 1) >= minFontSize);
    if (fits || !canShrink) {
      return createLines(lines, _style->copyWithFont(font));
    }
    fontSize--;
  }
}

void FittedLabelImageFactory::create(const G3MContext* context,
                                     IImageFactoryListener* listener,
                                     bool deleteListener) {
  ICanvas* measuringCanvas = context->getFactory()->createCanvas(true);
  IImageFactory* fitted = createFitted(measuringCanvas);
  delete measuringCanvas;

  fitted->create(context,
                 new FittedLabelImageFactory_Listener(fitted, listener, deleteListener),
                 true);
}
