import java.io.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class Main {

   private static final String CLASS_SOURCE = """
         //
         //  BasicShadersGL2.hpp
         //  G3M
         //
         //

         #ifndef G3M_BasicShadersGL2_h
         #define G3M_BasicShadersGL2_h

         #include "GPUProgramFactory.hpp"

         class BasicShadersGL2: public GPUProgramFactory {

         public:
            BasicShadersGL2() {
         #ifdef C_CODE
               const std::string emptyString = "";
         #endif
         #ifdef JAVA_CODE
               final String emptyString = "";
         #endif

         ADDING_SHADERS  }

         };

         #endif
         """;

   private static final String ADD_PROGRAM_SOURCE = """
         // Shader_Name
               {
                  GPUProgramSources srcShader_Name(
                     "Shader_Name",
                     Shader_Vertex,
                     Shader_Fragment);
                  this->add(srcShader_Name);
               }

         """;

   private static class Shader {
      public String _name;
      public String _vertex;
      public String _fragment;
   }

   private static String readFile(final String fileName) throws IOException {
      try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
         final StringBuilder sb = new StringBuilder();

         String line;
         while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
         }
         return sb.toString();
      }
   }

   private static String processSourceString(final String source) {
      final String[] lines = source.split(System.getProperty("line.separator"));

      String result = "emptyString +\n";

      for (int i = 0; i < lines.length; i++) {
         String line = lines[i];

         //         line = line.trim();
         line = line.replaceAll("(\\r|\\n)", "");
         if (line.length() < 1) {
            continue;
         }

         if (line.startsWith("/")) {
            continue;
         }

         //final boolean firstLine = (result == "");
         result += "            \"" + StringEscapeUtils.escapeJava(line) + "\\n\"";

         if (i < (lines.length - 1)) {
            result += " +\n";
         }

      }

      return result;
   }

   private static List<Shader> getShadersInFolder(final File shadersDir) throws IOException {

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

   private static void process(final File shadersDir, final File filePath) throws IOException {
      System.out.println("Shaders Directory = " + shadersDir);

      final List<Shader> shaders = getShadersInFolder(shadersDir);

      final StringBuilder addingShadersString = new StringBuilder();
      for (final Main.Shader shader : shaders) {
         final String source = getSource(shader);
         addingShadersString.append(source);
      }

      final String outPath = filePath.getAbsolutePath();
      try (final PrintWriter out = new PrintWriter(outPath)) {
         out.print(CLASS_SOURCE.replace("ADDING_SHADERS", addingShadersString));
      }
   }

   private static String getSource(final Main.Shader shader) {
      final String vertexShaderSource   = processSourceString(shader._vertex);
      final String fragmentShaderSource = processSourceString(shader._fragment);

      final String source = ADD_PROGRAM_SOURCE //
            .replace("Shader_Name", shader._name) //
            .replace("Shader_Vertex", vertexShaderSource) //
            .replace("Shader_Fragment", fragmentShaderSource);
      return source;
   }

   public static void main(final String[] args) throws IOException {
      final File pwd    = new File(System.getProperty("user.dir"));
      final File g3mDir = pwd.getParentFile().getParentFile();

      final File shadersDir = new File(g3mDir, "iOS/G3MiOSSDK/Resources/Shaders/");
      final File filePath   = new File(g3mDir, "cpp/G3M/BasicShadersGL2.hpp");

      process(shadersDir, filePath);
   }

}
