//
//  StringBuilder_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//

#ifndef G3MiOSSDK_StringBuilder_iOS
#define G3MiOSSDK_StringBuilder_iOS

#include "G3M/IStringBuilder.hpp"

#include <sstream>
#include <string>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>

class StringBuilder_iOS: public IStringBuilder {
private:
  std::ostringstream _oss;
  int                _floatPrecision;

  // As StringBuilder_WebGL writes numbers: GWT's NumberFormat with pattern "0" and
  // floatPrecision maximum fraction digits (com.google.gwt.i18n.client.NumberFormat 2.13.1).
  static std::string format(double d, int maxDecimals) {
    if (std::isnan(d)) {
      return "nan";
    }
    const bool negative = std::signbit(d);
    const std::string sign = negative ? "-" : "";
    const double absolute = negative ? -d : d;
    if (std::isinf(absolute)) {
      return sign + "\xE2\x88\x9E"; // U+221E, NumberFormat's infinity
    }

    // NumberFormat.toScaledString: JavaScript's toPrecision(20), value = digits * 10^scale
    std::string digits;
    int scale;
    toPrecision20(absolute, digits, scale);

    // NumberFormat.format(double): pre-rounds .1499999... at three digits past the decimals
    const int preRound = (int) digits.size() + scale + maxDecimals + 3;
    if ((preRound > 0) && (preRound < (int) digits.size()) && (digits[preRound] == '9')) {
      propagateCarry(digits, preRound - 1);
      scale += (int) digits.size() - preRound;
      digits.erase(preRound);
    }

    // NumberFormat.processLeadingZeros, with one minimum integer digit
    int decimalPosition = (int) digits.size() + scale;
    if (decimalPosition > (int) digits.size()) {
      digits.append(decimalPosition - digits.size(), '0');
    }
    if (decimalPosition < 1) {
      digits.insert(0, 1 - decimalPosition, '0');
      decimalPosition = 1;
    }
    else if (decimalPosition > 1) {
      int strip = 0;
      while ((strip < decimalPosition - 1) && (digits[strip] == '0')) {
        strip++;
      }
      digits.erase(0, strip);
      decimalPosition -= strip;
    }

    // NumberFormat.roundValue: half up
    const int roundPosition = decimalPosition + maxDecimals;
    if (((int) digits.size() > roundPosition) && (digits[roundPosition] >= '5')) {
      if (propagateCarry(digits, roundPosition - 1)) {
        decimalPosition++;
      }
    }

    // NumberFormat.adjustFractionDigits, with no minimum fraction digits
    int keep = decimalPosition + maxDecimals;
    if (keep > (int) digits.size()) {
      keep = (int) digits.size();
    }
    while ((keep > decimalPosition) && (digits[keep - 1] == '0')) {
      keep--;
    }
    digits.erase(keep);

    if (decimalPosition < (int) digits.size()) {
      digits.insert(decimalPosition, 1, '.');
    }
    return sign + digits;
  }

  // Correctly rounded to 20 significant digits, ties up, as JavaScript's toPrecision(20).
  static void toPrecision20(double d, std::string& digits, int& scale) {
    if (d == 0) {
      digits.assign(20, '0');
      scale = -19;
      return;
    }
    // 40 significant digits decide the rounding unless they read as an exact tie, which
    // "%.39e" may have rounded; then the exact expansion (at most 767 digits) decides
    char scientific[1024];
    snprintf(scientific, sizeof(scientific), "%.39e", d);
    std::string mantissa = mantissaDigits(scientific);
    if (mantissa.compare(20, 20, "50000000000000000000") == 0) {
      snprintf(scientific, sizeof(scientific), "%.766e", d);
      mantissa = mantissaDigits(scientific);
    }
    const int exponent = atoi(strchr(scientific, 'e') + 1);

    digits = mantissa.substr(0, 20);
    scale = exponent - 19;
    if (mantissa[20] >= '5') {
      if (propagateCarry(digits, 19)) {
        digits.erase(20);
        scale++;
      }
    }
  }

  static std::string mantissaDigits(const char* scientific) {
    std::string result;
    for (const char* c = scientific; *c != 'e'; c++) {
      if (*c != '.') {
        result += *c;
      }
    }
    return result;
  }

  // NumberFormat.propagateCarry: adds one at index i; returns true when it adds a leading 1
  static bool propagateCarry(std::string& digits, int i) {
    while (i >= 0) {
      if (digits[i] != '9') {
        digits[i]++;
        return false;
      }
      digits[i] = '0';
      i--;
    }
    digits.insert(0, 1, '1');
    return true;
  }

protected:

  IStringBuilder* clone(const int floatPrecision) const {
    return new StringBuilder_iOS(floatPrecision);
  }

public:

  StringBuilder_iOS(const int floatPrecision) :
  _floatPrecision(floatPrecision)
  {
  }

  IStringBuilder* addBool(bool b) {
    _oss << (b ? "true" : "false");
    return this;
  }

  IStringBuilder* addDouble(double d) {
    _oss << format(d, _floatPrecision);
    return this;
  }

  IStringBuilder* addFloat(float f) {
    _oss << format(f, _floatPrecision);
    return this;
  }

  IStringBuilder* addInt(int i) {
    _oss << i;
    return this;
  }

  IStringBuilder* addLong(long long l) {
    _oss << l;
    return this;
  }

  IStringBuilder* addString(const std::string& s) {
    _oss << s;
    return this;
  }

  const std::string getString() const {
    return _oss.str();
  }

  IStringBuilder* clear(const int floatPrecision) {
    _oss.str(std::string());
    _floatPrecision = floatPrecision;
    return this;
  }

  bool contentEqualsTo(const std::string& that) const {
    return _oss.str() == that;
  }

};

#endif
