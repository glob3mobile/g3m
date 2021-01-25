import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;


public class Main {

   private static final String _classSource = "//\n" + //
         "//  BasicShadersGL2.hpp\n" + //
         "//  G3M\n" + //
         "//\n" + //
         "//\n" + //
         "\n" + //
         "#ifndef G3M_BasicShadersGL2_h\n" + //
         "#define G3M_BasicShadersGL2_h\n" + //
         "\n" + //
         "#include \"GPUProgramFactory.hpp\"\n" + //
         "\n" + //
         "class BasicShadersGL2: public GPUProgramFactory {\n" + //
         "\n" + //
         "public:\n" + //
         "  BasicShadersGL2() {\n" + //
         "#ifdef C_CODE\n" + //
         "    const std::string emptyString = \"\";\n" + //
         "#endif\n" + //
         "#ifdef JAVA_CODE\n" + //
         "    final String emptyString = \"\";\n" + //
         "#endif\n" + //
         "\n" + //
         "ADDING_SHADERS" + "  }\n" + //
         "\n" + //
         "};\n" + //
         "#endif\n";

   //   private static final String ADD_PROGRAM_SOURCE = "    GPUProgramSources srcShader_Name(\"Shader_Name\",\n Shader_Vertex,\n Shader_Fragment);\n" + //
   //                                                    "    this->add(srcShader_Name);\n\n";

   private static final String ADD_PROGRAM_SOURCE = "    {\n" + //
         "      GPUProgramSources srcShader_Name(\"Shader_Name\",\n Shader_Vertex,\n Shader_Fragment);\n" + //
         "      this->add(srcShader_Name);\n" + //
         "    }\n" + //
         "\n";


   private static class Shader {
      public String _name;
      public String _vertex;
      public String _fragment;
   }


   static String readFile(final String fileName) throws IOException {
      try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
         final StringBuilder sb   = new StringBuilder();
         String              line = br.readLine();

         while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
         }
         return sb.toString();
      }
      //      catch (final IOException e) {
      //         return null;
      //      }
   }


   static String processSourceString(final String source) {

      final String[] lines = source.split(System.getProperty("line.separator"));

      String result = "emptyString +\n";

      for (int i = 0; i < lines.length; i++) {
         String line = lines[i];

         line = line.trim();
         line = line.replaceAll("(\\r|\\n)", "");
         if (line.length() < 1) {
            continue;
         }

         if (line.startsWith("/")) {
            continue;
         }

         //final boolean firstLine = (result == "");
         result += "\"" + StringEscapeUtils.escapeJava(line) + "\\n\"";

         if (i < (lines.length - 1)) {
            result += " +\n";
         }

      }

      return result;

      // return StringEscapeUtils.escapeJava(source);
   }


   static String processNameString(final String source) {
      return StringEscapeUtils.escapeJava(source);
   }


   static ArrayList<Shader> getShadersInFolder(final File shadersDir) throws IOException {

      final File folder = shadersDir;

      final ArrayList<Shader> shaders = new ArrayList<>();

      for (final File fileEntry : folder.listFiles()) {
         if (fileEntry.isDirectory()) {
            getShadersInFolder(fileEntry);
         }
         else {

            String name = fileEntry.getName();

            if (name.endsWith(".vsh")) {

               final File vertexFile = fileEntry;
               name = (String) name.subSequence(0, name.length() - 4);
               final File fragmentFile = new File(folder, name + ".fsh");

               if (fragmentFile.exists()) {
                  System.out.println("Processing \"" + name + "\" shader.");

                  final Shader shader = new Shader();
                  shader._name     = name;
                  shader._vertex   = readFile(vertexFile.getAbsolutePath());
                  shader._fragment = readFile(fragmentFile.getAbsolutePath());

                  shaders.add(shader);
               }
               else {
                  System.out.println("No fragment shader for " + name);
               }

            }
         }
      }

      return shaders;

   }


   private static void process(final File shadersDir,
                               final File filePath) {
      System.out.println("Shaders Directory = " + shadersDir);

      try {
         final ArrayList<Shader> shaders = getShadersInFolder(shadersDir);

         String addingShadersString = "";
         for (final Main.Shader shader : shaders) {
            String source = ADD_PROGRAM_SOURCE.replaceAll("Shader_Name", shader._name);
            source = source.replace("Shader_Vertex", processSourceString(shader._vertex));
            source = source.replace("Shader_Fragment", processSourceString(shader._fragment));

            //System.out.println(source);

            addingShadersString += source;

         }

         final String outPath = filePath.getAbsolutePath();
         try (final PrintWriter out = new PrintWriter(outPath)) {
            out.print(_classSource.replace("ADDING_SHADERS", addingShadersString));
         }


      }
      catch (final Exception e) {
         e.printStackTrace();
         return;
      }


   }


   public static void main(final String[] args) {
      //      String pwd     = "";

      final File pwd    = new File(System.getProperty("user.dir"));
      final File g3mDir = pwd.getParentFile().getParentFile();


      final File shadersDir = new File(g3mDir, "iOS/G3MiOSSDK/Resources/Shaders/");
      final File filePath   = new File(g3mDir, "cpp/G3M/BasicShadersGL2.hpp");


      process(shadersDir, filePath);


   }

}
