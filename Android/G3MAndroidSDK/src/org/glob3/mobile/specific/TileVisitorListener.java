

package org.glob3.mobile.specific;

public interface TileVisitorListener {

   public void onTileDownloaded();


   public void onStartedProccess();


   public void onFinishedProcess();


   public void onPetition(String url);
}
