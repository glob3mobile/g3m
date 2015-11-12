

package org.glob3.mobile.specific;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import org.glob3.mobile.generated.IStringUtils;


public final class StringUtils_Android
   extends
      IStringUtils {

   @Override
   public String createString(final byte[] data,
                              final int length) {
      try {
         return new String(data, "UTF8");
      }
      catch (final UnsupportedEncodingException e) {
         return null;
      }
   }


   @Override
   public ArrayList<String> splitLines(final String string) {
      final String lines[] = string.split("\\r?\\n");
      final ArrayList<String> l = new ArrayList<String>();
      for (final java.lang.String line : lines) {
         l.add(line);
      }

      return l;
   }


   @Override
   public boolean beginsWith(final String string,
                             final String prefix) {
      return string.startsWith(prefix);
   }


   @Override
   public int indexOf(final String string,
                      final String search) {
      return string.indexOf(search);
   }


   @Override
   public String substring(final String string,
                           final int beginIndex,
                           final int endIndex) {
      return string.substring(beginIndex, endIndex);
   }


   @Override
   public String rtrim(final String string) {
      int index = string.length() - 1;
      while ((index > 0) && (string.charAt(index) == ' ')) {
         index--;
      }
      return string.substring(0, index + 1);
   }


   @Override
   public String ltrim(final String string) {
      int index = 0;
      final int stringLength = string.length();
      while ((index < stringLength) && (string.charAt(index) == ' ')) {
         index++;
      }
      return string.substring(index, stringLength);
   }


   @Override
   public boolean endsWith(final String string,
                           final String suffix) {
      return string.endsWith(suffix);
   }


   @Override
   public String toUpperCase(final String string) {
      return string.toUpperCase(Locale.ENGLISH);
   }


   @Override
   public long parseHexInt(final String str) {
      return Long.parseLong(str, 16);
   }


   @Override
   public int indexOf(final String string,
                      final String search,
                      final int fromIndex) {
      return string.indexOf(search, fromIndex);
   }


   @Override
   public int indexOf(final String string,
                      final String search,
                      final int fromIndex,
                      final int endIndex) {
      final int pos = string.indexOf(search, fromIndex);
      return ((pos < 0) || (pos > endIndex)) ? -1 : pos;
   }


   @Override
   public int indexOfFirstNonBlank(final String string,
                                   final int fromIndex) {
      final int stringLen = string.length();
      for (int i = fromIndex; i < stringLen; i++) {
         if (!Character.isWhitespace(string.charAt(i))) {
            return i;
         }
      }
      return -1;
   }


   @Override
   public int indexOfFirstNonChar(final String string,
                                  final String chars,
                                  final int fromIndex) {
      final int stringLen = string.length();
      for (int i = fromIndex; i < stringLen; i++) {
         if (chars.indexOf(string.charAt(i)) > 0) {
            return i;
         }
      }
      return -1;
   }


   @Override
   public String toString(final int value) {
      return Integer.toString(value);
   }


   @Override
   public String toString(final double value) {
      return Double.toString(value);
   }


   @Override
   public String toString(final long value) {
      return Long.toString(value);
   }


   @Override
   public double parseDouble(final String str) {
      return Double.parseDouble(str);
   }


   @Override
   public String toString(final float value) {
      return Float.toString(value);
   }


   @Override
   public String replaceAll(final String originalString,
                            final String searchString,
                            final String replaceString) {
      return originalString.replace(searchString, replaceString);
   }

}
