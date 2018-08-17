package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private static final IStringUtils _instance = null;
//#else
  private static IStringUtils _instance;
//#endif

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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String createString(byte data[], int length) const = 0;
  public abstract String createString(byte[] data, int length);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<String> splitLines(const String& String) const = 0;
  public abstract java.util.ArrayList<String> splitLines(String String);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean beginsWith(const String& String, const String& prefix) const = 0;
  public abstract boolean beginsWith(String String, String prefix);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean endsWith(const String& String, const String& suffix) const = 0;
  public abstract boolean endsWith(String String, String suffix);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String toUpperCase(const String& String) const = 0;
  public abstract String toUpperCase(String String);


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int indexOf(const String& String, const String& search) const = 0;
  public abstract int indexOf(String String, String search);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int indexOf(const String& String, const String& search, int fromIndex) const = 0;
  public abstract int indexOf(String String, String search, int fromIndex);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int indexOf(const String& String, const String& search, int fromIndex, int endIndex) const = 0;
  public abstract int indexOf(String String, String search, int fromIndex, int endIndex);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int indexOfFirstNonBlank(const String& String, int fromIndex) const = 0;
  public abstract int indexOfFirstNonBlank(String String, int fromIndex);

  //  virtual int indexOfFirstBlank(const std::string& string,
  //                                int fromIndex) const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int indexOfFirstNonChar(const String& String, const String& chars, int fromIndex) const = 0;
  public abstract int indexOfFirstNonChar(String String, String chars, int fromIndex);

  /*
   Returns a new string that is a substring of this string. The substring begins at the
   specified beginIndex and extends to the character at index endIndex - 1. Thus the length
   of the substring is endIndex-beginIndex.
   */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String substring(const String& String, int beginIndex, int endIndex) const = 0;
  public abstract String substring(String String, int beginIndex, int endIndex);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String substring(const String& String, int beginIndex) const
  public String substring(String String, int beginIndex)
  {
	return substring(String, beginIndex, String.length());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String replaceAll(const String& originalString, const String& searchString, const String& replaceString) const = 0;
  public abstract String replaceAll(String originalString, String searchString, String replaceString);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String left(const String& String, int endIndex) const
  public String left(String String, int endIndex)
  {
	return substring(String, 0, endIndex);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String rtrim(const String& String) const = 0;
  public abstract String rtrim(String String);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String ltrim(const String& String) const = 0;
  public abstract String ltrim(String String);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String trim(const String& String) const
  public String trim(String String)
  {
	return rtrim(ltrim(String));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long parseHexInt(const String& str) const = 0;
  public abstract long parseHexInt(String str);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String toString(int value) const = 0;
  public abstract String toString(int value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String toString(long value) const = 0;
  public abstract String toString(long value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String toString(double value) const = 0;
  public abstract String toString(double value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String toString(float value) const = 0;
  public abstract String toString(float value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double parseDouble(const String& str) const = 0;
  public abstract double parseDouble(String str);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<double> parseDoubles(const String& str, const String& separator) const = 0;
  public abstract java.util.ArrayList<Double> parseDoubles(String str, String separator);

  //Used for data parsing
  public static class StringExtractionResult
  {

	public String _string;
	public int _endingPos;

	public StringExtractionResult(String String, int endingPos)
	{
		_string = String;
		_endingPos = endingPos;
	}
  }

  //Returns the desired string and the position of the last character where endTag was found
  public static StringExtractionResult extractSubStringBetween(String String, String startTag, String endTag, int startPos)
  {

	int pos1 = String.indexOf(startTag, startPos) + startTag.length();
	int pos2 = String.indexOf(endTag, pos1+1);

	if (pos1 == String.npos || pos2 == String.npos || pos1 < startPos || pos2 < startPos)
	{
	  return new StringExtractionResult("", String.npos);
	}

	String str = String.substring(pos1, pos2);

	return new StringExtractionResult(str, pos2 + endTag.length());
  }

}
