

package com.glob3mobile.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Logger {
   private Logger() {
   }

   private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS: ");


   private static String timestamp() {
      synchronized (format) {
         return format.format(Calendar.getInstance().getTime());
      }
   }


   public static void log(final Object msg) {
      System.out.println(timestamp() + msg);
   }

}
