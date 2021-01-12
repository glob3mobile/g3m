

package org.glob3.mobile.client;


import org.glob3.mobile.generated.*;
import org.glob3.mobile.specific.*;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.ui.*;


public class G3MWebGLDemo
                          implements
                             EntryPoint {

   private static final String _g3mWidgetHolderId = "g3mWidgetHolder";
   private G3MWidget_WebGL     _widget            = null;


   private static IDownloader createDownloaderSansProxy(final boolean verboseErrors) {
      return new Downloader_WebGL( //
            8, // maxConcurrentOperationCount
            10, // delayMillis
            null, // proxy,
            verboseErrors);
   }


   @Override
   public void onModuleLoad() {
      /*
       * ======================================================================
       * WARNING / CUIDADO / ACHTUNG
       * ---------------------------------------------------------------------- Don't
       * touch this project for quick & dirty tests. If you do it, your commit rights
       * will be revoked and you'll spend your after-life burning at the hell.
       * ======================================================================
       */

      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final boolean verboseDownloaderErrors = true;
      builder.setDownloader(createDownloaderSansProxy(verboseDownloaderErrors));

      _widget = builder.createWidget();

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
      g3mWidgetHolder.add(_widget);
   }


}
