

package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.glob3.mobile.generated.BSONGenerator;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.specific.ByteBuffer_Desktop;
import org.glob3.mobile.specific.Factory_Desktop;
import org.glob3.mobile.specific.JSONParser_Desktop;
import org.glob3.mobile.specific.Logger_Desktop;
import org.glob3.mobile.specific.MathUtils_Desktop;
import org.glob3.mobile.specific.StringBuilder_Desktop;


public class BatchJsonParser {
  public static void main(final String[] args) {

    System.out.println("Batch JSON Parser Desktop 0.1");
    System.out.println("-----------------------------\n");

    if (args.length != 2) {
      System.out.println("No se han especificado los argumentos correctamente");
      System.exit(1);
    }

    // Inicializando
    final File fJson = new File(args[0]);
    final File fBson = new File(args[1]);

    IStringBuilder.setInstance(new StringBuilder_Desktop());
    IMathUtils.setInstance(new MathUtils_Desktop());
    IFactory.setInstance(new Factory_Desktop());
    ILogger.setInstance(new Logger_Desktop(LogLevel.ErrorLevel));


    if (fJson.exists() && fJson.isFile()
        && fJson.getName().toLowerCase().endsWith(".json")) {
      final JSONBaseObject jbase = readJsonFile(fJson);
      if (jbase != null) {
        if (fBson.exists() || fBson.mkdirs()) {
          writeBsonFile(jbase, fBson);
        }
        else {
          System.out.println("El fichero de salida no existe o no se ha podido crear");
          System.exit(1);
        }
      }
      else {
        System.out.println("No se ha podido parsear correctamente el fichero json");
        System.exit(1);
      }
    }
    else {
      System.out.println("El fichero de entrada json no se ha especificado correctamente");
      System.exit(1);
    }
  }


  /**
   * @param fBson
   */
  private static void writeBsonFile(final JSONBaseObject jbase,
                                    final File fBson) {
    if (fBson.exists() && (jbase != null)) {
      try {
        final FileOutputStream fout = new FileOutputStream(fBson);

        final ByteBuffer_Desktop bb = (ByteBuffer_Desktop) BSONGenerator.generate(jbase);
        fout.write(bb.getBuffer().array());
        fout.flush();
        fout.close();
      }
      catch (final FileNotFoundException e) {
        System.out.println("File not found" + e);
      }
      catch (final IOException ioe) {
        System.out.println("Exception while reading the file " + ioe);
      }
    }
  }


  /**
   * @param fJson
   */
  private static JSONBaseObject readJsonFile(final File fJson) {
    JSONBaseObject jbase = null;
    if (fJson.exists()) {
      try {
        // create FileInputStream object
        final FileInputStream finJson = new FileInputStream(fJson);
        /*
         * Create byte array large enough to hold the content of the file.
         * Use File.length to determine size of the file in bytes.
         */
        final byte fileContent[] = new byte[(int) fJson.length()];

        /*
         * To read content of the file in byte array, use
         * int read(byte[] byteArray) method of java FileInputStream class.
         */
        finJson.read(fileContent);
        finJson.close();

        final JSONParser_Desktop jp = new JSONParser_Desktop();
        jbase = jp.parse(new ByteBuffer_Desktop(fileContent));
        System.out.println(jbase.description());
      }
      catch (final FileNotFoundException e) {
        System.out.println("File not found" + e);
      }
      catch (final IOException ioe) {
        System.out.println("Exception while reading the file " + ioe);
      }
    }

    return jbase;
  }


  // private static void checkJsonParser() {
  // final Gson gson = new Gson();
  // final Collection<Serializable> collection = new
  // ArrayList<Serializable>();
  // // collection.add("hello");
  // // collection.add(5);
  // // collection.add(new Double(5.234234));
  // // collection.add(new Long((long) 999999999 * (long) 10));
  // // collection.add(new Event("GREETINGS", 4, "guest"));
  //
  // collection.add(false);
  // collection.add(0.0);
  // // final String json = new String(
  // //
  // "{\"f\":false, \"d0\":0.0, \"i0\":0, \"d100\":100.0, \"d1\":1.0, \"t\":true, \"s\":\"hello world\", \"i100\":100, \"i1\":1}");
  // String json = gson.toJson(collection);
  //
  // json = new String("{\"array\":[{\"f\":false}, {\"d00\":0.0}]}");
  //
  // System.out.println("Using Gson.toJson() on a raw collection: " + json);
  //
  //
  // final JSONParser_Desktop jp = new JSONParser_Desktop();
  //
  // final JSONBaseObject jbase = jp.parse(json);
  //
  // System.out.println(jbase.description());
  //
  // final ByteBuffer_Desktop bb = (ByteBuffer_Desktop)
  // BSONGenerator.generate(jbase);
  //
  // final JSONBaseObject jbaseParsed = BSONParser.parse(bb);
  //
  // System.out.println(jbaseParsed.description());
  // }
}
