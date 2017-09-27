

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Browser_Android {

   private Activity      _activity;
   private final WebView _webView;


   public Browser_Android(final WebView webView) {
      _webView = webView;
   }


   public Browser_Android(final Activity activity) {
      _activity = activity;
      _webView = new WebView(_activity);
      _activity.setContentView(_webView);
   }


   public Browser_Android(final Activity activity,
                          final WebView webView) {
      _activity = activity;
      _webView = webView;
   }


   public void openInBrowser(final String targetUrl) {

      final URL url = new URL(targetUrl, false);
      openInBrowser(url);
   }


   @SuppressLint("SetJavaScriptEnabled")
   public void openInBrowser(final URL targetUrl) {

      _webView.getSettings().setAllowFileAccess(true);
      _webView.getSettings().setJavaScriptEnabled(true);
      _webView.getSettings().setAppCacheEnabled(true);
      _webView.setClickable(true);
      _webView.getSettings().setLightTouchEnabled(true);

      _webView.setWebViewClient(new WebViewClient());

      _webView.setWebChromeClient(new WebChromeClient() {
         @Override
         public boolean onJsAlert(final WebView view,
                                  final String url,
                                  final String message,
                                  final JsResult result) {
            return super.onJsAlert(view, url, message, result);
         }


         @Override
         public void onProgressChanged(final WebView view,
                                       final int progress) {
            if (_activity != null) {
               if (progress == 100) {
                  _activity.setProgressBarIndeterminateVisibility(false);
               }
            }
         }
      });

      Log.v("Opening url: ", targetUrl._path);
      _webView.loadUrl(targetUrl._path);
   }

}
