package org.glob3.mobile.generated; 
//
//  IStringUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

//
//  IStringUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//




public abstract class IStringUtils
{
  private static IStringUtils _instance;

  public static void setInstance(IStringUtils instance)
  {
    if (_instance != null)
    {
      ILogger.instance().logWarning("IStringUtils instance already set!");
      if (_instance != null)
         _instance.dispose();
    }
    _instance = instance;
  }

  public static IStringUtils instance()
  {
    return _instance;
  }

  public void dispose()
  {
  }

  public abstract String createString(byte[] data, int length);

  public abstract java.util.ArrayList<String> splitLines(String String);

  public abstract boolean beginsWith(String String, String prefix);

  public abstract boolean endsWith(String String, String suffix);

  public abstract String toUpperCase(String String);


  public abstract int indexOf(String String, String search);

  public abstract int indexOf(String String, String search, int fromIndex);

  public abstract int indexOf(String String, String search, int fromIndex, int endIndex);

  public abstract int indexOfFirstNonBlank(String String, int fromIndex);

  //  virtual int indexOfFirstBlank(const std::string& string,
  //                                int fromIndex) const = 0;

  public abstract int indexOfFirstNonChar(String String, String chars, int fromIndex);

  /*
   Returns a new string that is a substring of this string. The substring begins at the
   specified beginIndex and extends to the character at index endIndex - 1. Thus the length
   of the substring is endIndex-beginIndex.
   */
  public abstract String substring(String String, int beginIndex, int endIndex);

  public String substring(String String, int beginIndex)
  {
    return substring(String, beginIndex, String.length());
  }

  public abstract String replaceAll(String originalString, String searchString, String replaceString);

  public String left(String String, int endIndex)
  {
    return substring(String, 0, endIndex);
  }

  public abstract String rtrim(String String);

  public abstract String ltrim(String String);

  public String trim(String String)
  {
    return rtrim(ltrim(String));
  }

  public abstract long parseHexInt(String str);

  public abstract String toString(int value);

  public abstract String toString(long value);

  public abstract String toString(double value);

  public abstract String toString(float value);

  public abstract double parseDouble(String str);

}