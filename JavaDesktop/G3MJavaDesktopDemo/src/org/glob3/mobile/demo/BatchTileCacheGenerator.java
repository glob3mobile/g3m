

package org.glob3.mobile.demo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.Factory_JavaDesktop;
import org.glob3.mobile.specific.Logger_JavaDesktop;
import org.glob3.mobile.specific.MathUtils_JavaDesktop;
import org.glob3.mobile.specific.StringBuilder_JavaDesktop;


public class BatchTileCacheGenerator {
  public static void main(final String[] args) {
    IStringBuilder.setInstance(new StringBuilder_JavaDesktop());
    IMathUtils.setInstance(new MathUtils_JavaDesktop());
    IFactory.setInstance(new Factory_JavaDesktop());
    ILogger.setInstance(new Logger_JavaDesktop(LogLevel.ErrorLevel));

    final WMSLayer osm = new WMSLayer( //
        "osm_auto:all", //
        new URL("http://129.206.228.72/cached/osm", false), //
        WMSServerVersion.WMS_1_1_0, //
        Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
        "image/jpeg", //
        "EPSG:4326", //
        "", //
        false, //
        null, //
        TimeInterval.fromDays(30));


    final TileRendererBuilder tlBuilder = new TileRendererBuilder();
    tlBuilder.setLayerSet(layerSet);
    final TileRenderer tileRenderer = tlBuilder.create();


    try {
      Class.forName("org.sqlite.JDBC");
      // Observen que test.db es la base de datos y se crea en el directorio
      // de trabajo
      final Connection conn = DriverManager.getConnection("jdbc:sqlite:g3m.cache");
      final Statement stat = conn.createStatement();
      stat.executeUpdate("DROP TABLE IF EXISTS buffer;");
      stat.executeUpdate("DROP TABLE IF EXISTS image;");

      stat.executeUpdate("CREATE TABLE IF NOT EXISTS buffer2 (name TEXT, contents TEXT, expiration TEXT);");
      stat.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS buffer_name ON buffer2(name);");

      stat.executeUpdate("CREATE TABLE IF NOT EXISTS image2 (name TEXT, contents TEXT, expiration TEXT);");
      stat.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS image_name ON image2(name);");


    }
    catch (final ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (final SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

}
