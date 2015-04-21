

package com.glob3mobile.tools.mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;


public class G3MeshCollection {

   private final List<G3Mesh> _meshes;


   public G3MeshCollection() {
      _meshes = new ArrayList<>();
   }


   private void validateMeshes() {
      for (final G3Mesh mesh : _meshes) {
         mesh.validate();
      }
   }


   //   private static class ConsolidatedMaterial {
   //      private final G3MeshMaterial _material;
   //      private final int           _index;
   //
   //
   //      private ConsolidatedMaterial(final G3MeshMaterial material,
   //                                   final int index) {
   //         _material = material;
   //         _index = index;
   //      }
   //   }
   //
   //   private List<ConsolidatedMaterial> collectMaterials() {
   //      final List<ConsolidatedMaterial> result = new ArrayList<>();
   //      
   //      
   //      
   //      return result;
   //   }


   public JSONObject toG3MeshJSON() {
      validateMeshes();

      final Map<String, G3MeshMaterial> consolidatedMaterials = consolidateMaterials();

      final JSONObject result = new JSONObject();

      final JSONArray jsonMaterials = new JSONArray();
      for (final G3MeshMaterial material : consolidatedMaterials.values()) {
         jsonMaterials.add(material.toG3MeshJSON());
      }
      result.put("materials", jsonMaterials);

      final JSONArray jsonMeshes = new JSONArray();
      for (final G3Mesh mesh : _meshes) {
         jsonMeshes.add(mesh.toG3MeshJSON());
      }
      result.put("meshes", jsonMeshes);

      return result;
   }


   private Map<String, G3MeshMaterial> consolidateMaterials() {
      final Map<String, G3MeshMaterial> result = new HashMap<>();

      for (final G3Mesh mesh : _meshes) {
         final G3MeshMaterial material = mesh.getMaterial();
         final String materialID = material.getID();
         if (!result.containsKey(materialID)) {
            result.put(materialID, material);
         }
      }

      return result;
   }


   public void add(final G3Mesh mesh) {
      _meshes.add(mesh);
   }


}
