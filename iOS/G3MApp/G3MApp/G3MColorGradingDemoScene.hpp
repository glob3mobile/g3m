//
//  G3MColorGradingDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#ifndef __G3MApp__G3MColorGradingDemoScene__
#define __G3MApp__G3MColorGradingDemoScene__

#include "G3MDemoScene.hpp"

class Matrix44D;
class Angle;
class Color;


class G3MColorGradingDemoScene : public G3MDemoScene {
private:
  size_t _effectGroupIndex;

  static Matrix44D* createEffectMatrix(const std::string& effect);

  static Matrix44D* createCustomMatrix(double temperature,
                                       double saturation,
                                       const Angle& hue,
                                       double contrast,
                                       double brightness,
                                       const Color& tint,
                                       double tintIntensity);

  void selectEffect(const std::string& effect);

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

  void rawSelectGroupOption(size_t groupIndex,
                            const std::string& option,
                            int optionIndex);

public:
  G3MColorGradingDemoScene(G3MDemoModel* model);

  void setCustomGrade(double temperature,
                      double saturation,
                      const Angle& hue,
                      double contrast,
                      double brightness,
                      const Color& tint,
                      double tintIntensity);

  void logCustomGrade(double temperature,
                      double saturation,
                      const Angle& hue,
                      double contrast,
                      double brightness,
                      const Color& tint,
                      double tintIntensity) const;

};

#endif
