# ##### BEGIN GPL LICENSE BLOCK #####
#
#  This program is free software; you can redistribute it and/or
#  modify it under the terms of the GNU General Public License
#  as published by the Free Software Foundation; either version 2
#  of the License, or (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software Foundation,
#  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
#
# ##### END GPL LICENSE BLOCK #####

# This plugin was based on io_scene_obj plugin

# <pep8 compliant>

import os
import time

import bpy
import math
import mathutils
import bpy_extras.io_utils
import re

def create_object(name):
    object = {}
    
    object["name"] = name
    object["materials"] = {}
    
    object["vertices"] = []
    object["normals"]  = []
    object["uv"]       = []
    object["indices"]  = []
        
    object["indicesDict"] = {}

    return object  


def create_material(key, blenderMaterial):
    #AT_WORK
    material = {}
    
    material["name"]    = key[0]
    material["texture"] = key[1]
    
    material["blenderMaterial"] = blenderMaterial
    
    material["vertices"] = []
    material["normals"]  = []
    material["uv"]       = []
    material["indices"]  = []
        
    material["indicesDict"] = {}
    
    material["objects"] = []
    
    return material


def name_compat(name):
    if name is None:
        return 'None'
    else:
        return name.replace(' ', '_')
    

def test_nurbs_compat(ob):
    if ob.type != 'CURVE':
        return False

    for nu in ob.data.splines:
        if nu.point_count_v == 1 and nu.type != 'BEZIER':  # not a surface and not bezier
            return True

    return False


# Write file grouping data by Material-Object-Geometry
def write_file_grouped_by_MOG(fw,
                              materialsDict):
    firstMaterial=True
    for key, material in materialsDict.items():
        
        # start material
        if firstMaterial:
            firstMaterial=False
            fw('{"type":"material"')
        else:
            fw(',\n{"type":"material"')
            
        if (material["name"]):
            fw(',"sid":"%s"' % material["name"])
            
        if material["blenderMaterial"]:
            matB = material["blenderMaterial"]
            fw(',"baseColor":{"r":%.6g,"g":%.6g,"b":%.6g}'     % (matB.diffuse_intensity  * matB.diffuse_color)[:])   # Diffuse
            fw(',"specularColor":{"r":%.6g,"g":%.6g,"b":%.6g}' % (matB.specular_intensity * matB.specular_color)[:])  # Specular
        else:
            fw(',"baseColor":{"r":0.8,"g":0.8,"b":0.8}')
            fw(',"specularColor":{"r":0.8,"g":0.8,"b":0.8}')

        fw(',"nodes":[\n')

        if (material["texture"]):
            uri=re.sub(r'\.[0-9]{3}', r'', material["texture"])
            print ( '*** Found texture "%s"' % uri )
            fw('  {"type":"texture","layers":[{"uri":"%s"}],"nodes":[\n' % uri)
        
        firstOb=True
        for object in material["objects"]:
            
            # start object
            if firstOb:
                firstOb=False
                fw('    {"type":"node","id":"%s","nodes":[\n' % object["name"])
            else:
                fw(',\n    {"type":"node","id":"%s","nodes":[\n' % object["name"])
            
            # start geometry
            fw('      {"type":"geometry","primitive":"triangles","id":"%s_%s"\n' % (material["name"], object["name"]) )
            
            # start positions (vertices)
            fw('      ,"positions":[\n')
            firstVertex=True
            for vertex in object["vertices"]:
                if firstVertex:
                    firstVertex=False
                    fw('        %.10g,%.10g,%.10g' %  vertex)
                else:
                    fw(',\n        %.10g,%.10g,%.10g' %  vertex)
            # end positions (vertices)
            fw('\n       ]\n')

            if (object["normals"]):
                # start normals
                fw('      ,"normals":[\n')
                firstNormal=True
                for normal in object["normals"]:
                    if firstNormal:
                        firstNormal=False
                        fw('        %.10g,%.10g,%.10g\n' %  normal)
                    else:
                        fw('        ,%.10g,%.10g,%.10g\n' %  normal)
                # end normals
                fw('       ]\n')

            if (object["uv"]):
                # start uv
                fw('      ,"uv":[\n')
                firstUV=True
                for uv in object["uv"]:
                    x,y = uv
                    if firstUV:
                        firstUV=False
                        fw('        %.10g,%.10g' %  uv)
                    else:
                        fw(',\n        %.10g,%.10g' %  uv)
                # end uv
                fw('\n       ]\n')

            # start indices
            fw('      ,"indices":[')
            firstIndex=True
            for index in object["indices"]:
                if firstIndex:
                    firstIndex=False
                    fw('%g' %  (index - 1))
                else:
                    fw(',%g' %  (index - 1))
            # end indices
            fw(']\n')
            
            #end geometry
            fw('      }\n')
            
            # end object
            fw('    ]}\n')
        
        if (material["texture"]):
            fw('  ]}\n')     
        
        #end material
        fw(']}\n')
# end def write_file_grouped_by_MOG
    

# Write file grouping data by Object-Material-Geometry
def write_file_grouped_by_OMG(fw,
                              objectsList):
    firstOb=True        
    for object in objectsList:
        # start object
        if firstOb:
            firstOb=False
            fw('  {"type":"node","id":"%s","nodes":[\n' % object["name"])
        else:
            fw('  ,{"type":"node","id":"%s","nodes":[\n' % object["name"])
        
        firstMaterial=True
        for matKey, material in object["materials"].items():
            print ( "  " + str(matKey) + " => "+ str(material) )
            
            # start material
            if firstMaterial:
                firstMaterial=False
                fw('    {"type":"material"')
            else:
                fw('    ,{"type":"material"')
            if (material["name"]):
                fw(',"sid":"%s"' % material["name"])
                
            if material["blenderMaterial"]:
                matB = material["blenderMaterial"]
                fw(',"baseColor":{"r":%.6g,"g":%.6g,"b":%.6g}'     % (matB.diffuse_intensity  * matB.diffuse_color)[:])   # Diffuse
                fw(',"specularColor":{"r":%.6g,"g":%.6g,"b":%.6g}' % (matB.specular_intensity * matB.specular_color)[:])  # Specular
            else:
                fw(',"baseColor":{"r":0.8,"g":0.8,"b":0.8}')
                fw(',"specularColor":{"r":0.8,"g":0.8,"b":0.8}')

            fw(',"nodes":[\n')

            if (material["texture"]):
                uri=re.sub(r'\.[0-9]{3}', r'', material["texture"])
                print ( '*** Found texture "%s"' % uri )
                fw('      {"type":"texture","layers":[{"uri":"%s"}],"nodes":[' % uri)
            
            # start geometry
            fw('      {"type":"geometry","primitive":"triangles","id":"%s_%s"\n' % (object["name"], material["name"]) )
            fw('      ,"positions":[\n')

            firstVertex=True
            for vertex in material["vertices"]:
                if firstVertex:
                    firstVertex=False
                    fw('        %.10g,%.10g,%.10g\n' %  vertex)
                else:
                    fw('        ,%.10g,%.10g,%.10g\n' %  vertex)
            fw('      ]\n')

            if (material["normals"]):
                fw('      ,"normals":[\n')
                firstNormal=True
                for normal in material["normals"]:
                    if firstNormal:
                        firstNormal=False
                        fw('        %.10g,%.10g,%.10g\n' %  normal)
                    else:
                        fw('        ,%.10g,%.10g,%.10g\n' %  normal)
                fw('      ]\n')

            if (material["uv"]):
                fw('      ,"uv":[\n')
                firstUV=True
                for uv in material["uv"]:
                    if firstUV:
                        firstUV=False
                        fw('        %.10g,%.10g\n' %  uv)
                    else:
                        fw('        ,%.10g,%.10g\n' %  uv)
                fw(']\n')

            fw('      ,"indices":[')
            firstIndex=True
            for index in material["indices"]:
                if not firstIndex:
                    fw(',%g' %  (index - 1))
                else:
                    fw('%g' %  (index - 1))
                    firstIndex=False                        
            fw(']\n')
            
            #end geometry
            fw('      }\n') 

            if (material["texture"]):
                fw('      ]}\n')                    
            
            #end material
            fw('    ]}\n')
            
        # end object
        fw('  ]}\n')
# end def write_file_grouped_by_OMG


# Write file grouping data by Material-Geometry
def write_file_grouped_by_MG(fw,
                             materialsDict):
    firstMaterial=True
    for key, material in materialsDict.items():
        
        # start material
        if firstMaterial:
            firstMaterial=False
            fw('{"type":"material"')
        else:
            fw(',\n{"type":"material"')
            
        if (material["name"]):
            fw(',"sid":"%s"' % material["name"])
            
        if material["blenderMaterial"]:
            matB = material["blenderMaterial"]
            fw(',"baseColor":{"r":%.6g,"g":%.6g,"b":%.6g}'     % (matB.diffuse_intensity  * matB.diffuse_color)[:])   # Diffuse
            fw(',"specularColor":{"r":%.6g,"g":%.6g,"b":%.6g}' % (matB.specular_intensity * matB.specular_color)[:])  # Specular
        else:
            fw(',"baseColor":{"r":0.8,"g":0.8,"b":0.8}')
            fw(',"specularColor":{"r":0.8,"g":0.8,"b":0.8}')

        fw(',"nodes":[\n')

        if (material["texture"]):
            uri=re.sub(r'\.[0-9]{3}', r'', material["texture"])
            print ( '*** Found texture "%s"' % uri )
            fw('  {"type":"texture","layers":[{"uri":"%s"}],"nodes":[\n' % uri)
        
            
        # start geometry
        fw('      {"type":"geometry","primitive":"triangles","id":"%s"\n' % material["name"] )
        
        # start positions (vertices)
        fw('      ,"positions":[\n')
        firstVertex=True
        for vertex in material["vertices"]:
            if firstVertex:
                firstVertex=False
                fw('        %.10g,%.10g,%.10g' %  vertex)
            else:
                fw(',\n        %.10g,%.10g,%.10g' %  vertex)
        # end positions (vertices)
        fw('\n       ]\n')

        if (material["normals"]):
            # start normals
            fw('      ,"normals":[\n')
            firstNormal=True
            for normal in material["normals"]:
                if firstNormal:
                    firstNormal=False
                    fw('        %.10g,%.10g,%.10g\n' %  normal)
                else:
                    fw('        ,%.10g,%.10g,%.10g\n' %  normal)
            # end normals
            fw('       ]\n')

        if (material["uv"]):
            # start uv
            fw('      ,"uv":[\n')
            firstUV=True
            for uv in material["uv"]:
                x,y = uv
                if firstUV:
                    firstUV=False
                    fw('        %.10g,%.10g' %  uv)
                    ##fw('        %.10g,%.10g' %  (math.fabs(x), math.fabs(y)))
                else:
                    ##fw(',\n        %.10g,%.10g' %  (math.fabs(x), math.fabs(y)))
                    fw(',\n        %.10g,%.10g' %  uv)
            # end uv
            fw('\n       ]\n')

        # start indices
        fw('      ,"indices":[')
        firstIndex=True
        for index in material["indices"]:
            if firstIndex:
                firstIndex=False
                fw('%g' %  (index - 1))
            else:
                fw(',%g' %  (index - 1))
        # end indices
        fw(']\n')
        
        #end geometry
        fw('      }\n')
        
        if (material["texture"]):
            fw('  ]}\n')     
        
        #end material
        fw(']}\n')
# end def write_file_grouped_by_MG


def write_file(filepath,
               objects,
               scene,
               EXPORT_GROUPBY='MG',
               EXPORT_NORMALS=False,
               EXPORT_UV=True,
               EXPORT_KEEP_VERT_ORDER=False,
               EXPORT_GLOBAL_MATRIX=None,
               EXPORT_PATH_MODE='AUTO',
               ):
    objectsList = []
    materialsDict = {}

    if EXPORT_GLOBAL_MATRIX is None:
        EXPORT_GLOBAL_MATRIX = mathutils.Matrix()

    def veckey3d(v):
        return round(v.x, 6), round(v.y, 6), round(v.z, 6)

    def veckey2d(v):
        return round(v[0], 6), round(v[1], 6)

    def findVertexGroupName(face, vWeightMap):
        """
        Searches the vertexDict to see what groups is assigned to a given face.
        We use a frequency system in order to sort out the name because a given vetex can
        belong to two or more groups at the same time. To find the right name for the face
        we list all the possible vertex group names with their frequency and then sort by
        frequency in descend order. The top element is the one shared by the highest number
        of vertices is the face's group
        """
        weightDict = {}
        for vert_index in face.vertices:
            vWeights = vWeightMap[vert_index]
            for vGroupName, weight in vWeights:
                weightDict[vGroupName] = weightDict.get(vGroupName, 0.0) + weight

        if weightDict:
            return max((weight, vGroupName) for vGroupName, weight in weightDict.items())[1]
        else:
            return '(null)'

    print('SceneJS Export path: %r' % filepath)

    time1 = time.time()

    # Initialize totals, these are updated each object
    face_vert_index = 1

    for ob_main in objects:
        
        # ignore dupli children
        if ob_main.parent and ob_main.parent.dupli_type in {'VERTS', 'FACES'}:
            # XXX
            print(ob_main.name, 'is a dupli child - ignoring')
            continue

        obs = []
        if ob_main.dupli_type != 'NONE':
            # XXX
            print('creating dupli_list on', ob_main.name)
            ob_main.dupli_list_create(scene)

            obs = [(dob.object, dob.matrix) for dob in ob_main.dupli_list]

            # XXX debug print
            print(ob_main.name, 'has', len(obs), 'dupli children')
        else:
            obs = [(ob_main, ob_main.matrix_world)]

        for ob, ob_mat in obs:

            if test_nurbs_compat(ob):
                print("Exporting of NURBS not supported, skipping")
                continue

            try:
                me = ob.to_mesh(scene, True, 'PREVIEW')
            except RuntimeError:
                me = None

            if me is None:
                continue

            me.transform(EXPORT_GLOBAL_MATRIX * ob_mat)

            if EXPORT_UV:
                faceuv = len(me.uv_textures) > 0
                if faceuv:
                    uv_layer = me.tessface_uv_textures.active.data[:]
            else:
                faceuv = False

            me_verts = me.vertices[:]

            # Make our own list so it can be sorted to reduce context switching
            face_index_pairs = [(face, index) for index, face in enumerate(me.tessfaces)]

            #if not (len(face_index_pairs) + len(edges) + len(me.vertices)):  # Make sure there is something to write
            if not (len(face_index_pairs) + len(me.vertices)):  # Make sure there is something to write
                # clean up
                bpy.data.meshes.remove(me)
                continue  # dont bother with this mesh.

            if EXPORT_NORMALS and face_index_pairs:
                me.calc_normals()

            materials = me.materials[:]
            material_names = [m.name if m else None for m in materials]

            # avoid bad index errors
            if not materials:
                materials = [None]
                material_names = [""]

            # Sort by Material, then images
            # so we dont over context switch in the obj file.
            if EXPORT_KEEP_VERT_ORDER:
                pass
            elif faceuv:
                face_index_pairs.sort(key=lambda a: (a[0].material_index, hash(uv_layer[a[1]].image), a[0].use_smooth))
            elif len(materials) > 1:
                face_index_pairs.sort(key=lambda a: (a[0].material_index, a[0].use_smooth))
            else:
                # no materials
                face_index_pairs.sort(key=lambda a: a[0].use_smooth)

            name1 = ob.name
            name2 = ob.data.name
            if name1 == name2:
                obnamestring = name_compat(name1)
            else:
                obnamestring = '%s_%s' % (name_compat(name1), name_compat(name2))

            # UV
            uv_textures = None
            if faceuv:
                # in case removing some of these dont get defined.
                uv = uvkey = f_index = uv_index = None

                uv_textures = {}
                uv_layer = me.tessface_uv_textures.active.data
                for f, f_index in face_index_pairs:
                    for uv_index, uv in enumerate(uv_layer[f_index].uv):
                        uv_textures[ (f_index, uv_index) ] = veckey2d(uv)

                del uv, uvkey, f_index, uv_index
            
            if not faceuv:
                f_image = None

            # XXX
            verticesList = []
            normalsList = []
            uvList = []
            indicesList = []
            indicesDict = {}

            #AT_WORK
            object = create_object(obnamestring)
            
            objectsList.append(object)

            for f, f_index in face_index_pairs:
                f_mat = min(f.material_index, len(materials) - 1)
                
                if faceuv:
                    tface = uv_layer[f_index]
                    f_image = tface.image

                # MAKE KEY
                if faceuv and f_image:  # Object is always true.
                    key = material_names[f_mat], f_image.name
                else:
                    key = material_names[f_mat], None  # No image, use None instead.

                #print ( "  f.material_index=" + str(f.material_index) + ", f_mat=" + str(f_mat) + ", key=" + str(key) )

                #AT_WORK
                if ( key in object["materials"] ):
                    currentMaterial = object["materials"][key]
                else:
                    currentMaterial = create_material(key, materials[f_mat])
                    object["materials"][key] = currentMaterial
                
                # store material in materials dictionary if not exists
                if not (key in materialsDict):
                    materialsDict[key] = currentMaterial
                    print ( "stored '" + str(key) + "' => "+ str( currentMaterial) )

                f_v_orig = [(vi, me_verts[v_idx]) for vi, v_idx in enumerate(f.vertices)]

                if len(f_v_orig) == 3:
                    f_v_iter = (f_v_orig, )
                else:
                    f_v_iter = (f_v_orig[0], f_v_orig[1], f_v_orig[2]), (f_v_orig[0], f_v_orig[2], f_v_orig[3])

                # support for triangulation
                for f_v in f_v_iter:
                    for vi, v in f_v:
                        vertex = v.co[:]

                        if EXPORT_NORMALS:
                            if f.use_smooth:
                                normal = veckey3d(v.normal)
                            else:  # No smoothing, face normals
                                normal = veckey3d(f.normal)
                        else:
                            normal = None

                        if (uv_textures):
                            uvX = uv_textures[ (f_index, vi) ][0]
                            uvY = uv_textures[ (f_index, vi) ][1]
                            if math.isnan(uvX):
                                uvX = 0
                            if math.isnan(uvY):
                                uvY = 0
                            uv = (uvX,uvY)
                        else:
                            uv = None

                        vertexData = (vertex, normal, uv)
                        #AT_WORK
                        if (EXPORT_GROUPBY == 'MG'):
                            storedMtl = materialsDict.get(key)
                            if (vertexData in storedMtl["indicesDict"]):
                                objectIndex = storedMtl["indicesDict"][ vertexData ]
                            else:
                                objectIndex = len( storedMtl["indicesDict"] ) + 1
                                storedMtl["indicesDict"][ vertexData ] = objectIndex
                                storedMtl["vertices"].append( vertex )
                                if ( normal ):
                                    storedMtl["normals"].append( normal )
                                if ( uv ):
                                    storedMtl["uv"].append( uv )
                            storedMtl["indices"].append( objectIndex )
#                            print("STOREDMTL=" + str(storedMtl))
                            
                        elif (EXPORT_GROUPBY == 'MOG'):
                            if (vertexData in object["indicesDict"]):
                                objectIndex = object["indicesDict"][ vertexData ]
                            else:
                                objectIndex = len( object["indicesDict"] ) + 1
                                object["indicesDict"][ vertexData ] = objectIndex
                                object["vertices"].append( vertex )
                                if ( normal ):
                                    object["normals"].append( normal )
                                if ( uv ):
                                    object["uv"].append( uv )
                            object["indices"].append( objectIndex )
#                            print ( "OBJECT:  " + str(object) )

                        else:
                            if (vertexData in currentMaterial["indicesDict"]):
                                materialIndex = currentMaterial["indicesDict"][ vertexData ]
                            else:
                                materialIndex = len( currentMaterial["indicesDict"] ) + 1
                                currentMaterial["indicesDict"][ vertexData ] = materialIndex
                                currentMaterial["vertices"].append( vertex )
                                if ( normal ):
                                    currentMaterial["normals"].append( normal )
                                if ( uv ):
                                    currentMaterial["uv"].append( uv )
                            currentMaterial["indices"].append( materialIndex )
                        
                        if ( vertexData in indicesDict ):
                            index = indicesDict[ vertexData ]
                            #print ("** Recycling vertex data **")
                        else:
                            index = len( indicesDict ) + 1
                            indicesDict[ vertexData ] = index
                            verticesList.append( vertex )
                            if ( normal ):
                                normalsList.append( normal )
                            if ( uv ):
                                uvList.append( uv )
                        indicesList.append( index )

            #AT_WORK
            print("object=" + str(object["name"]))
            
            # store object in material
            materialsDict.get(key)["objects"].append(object)
            
            # clean up
            bpy.data.meshes.remove(me)

        if ob_main.dupli_type != 'NONE':
            ob_main.dupli_list_clear()
    
      
    # Write to the file
    file = open(filepath, "w", encoding="utf8", newline="\n")
    fw = file.write

    # Write Header    
    fw('{"type":"node","id":"root","nodes":[\n')
    
    if (EXPORT_GROUPBY == 'MG'):
        write_file_grouped_by_MG(fw, materialsDict)
    elif (EXPORT_GROUPBY == 'MOG'):
        write_file_grouped_by_MOG(fw, materialsDict)
    else:
        write_file_grouped_by_OMG(fw, objectsList)
            
    # end root node
    fw(']}\n')
    file.close()

    print("SceneJS Export time: %.2f" % (time.time() - time1))


def _write(context,
           filepath,
           EXPORT_GROUPBY,
           EXPORT_NORMALS,  # not yet
           EXPORT_UV,  # ok
           EXPORT_KEEP_VERT_ORDER,
           EXPORT_SEL_ONLY,  # ok
           EXPORT_ANIMATION,
           EXPORT_GLOBAL_MATRIX,
           EXPORT_PATH_MODE,
           ):  # Not used

    base_name, ext = os.path.splitext(filepath)
    context_name = [base_name, '', '', ext]  # Base name, scene name, frame number, extension

    scene = context.scene

    # Exit edit mode before exporting, so current object states are exported properly.
    if bpy.ops.object.mode_set.poll():
        bpy.ops.object.mode_set(mode='OBJECT')

    orig_frame = scene.frame_current

    # Export an animation?
    if EXPORT_ANIMATION:
        scene_frames = range(scene.frame_start, scene.frame_end + 1)  # Up to and including the end frame.
    else:
        scene_frames = [orig_frame]  # Dont export an animation.

    # Loop through all frames in the scene and export.
    for frame in scene_frames:
        if EXPORT_ANIMATION:  # Add frame to the filepath.
            context_name[2] = '_%.6d' % frame

        scene.frame_set(frame, 0.0)
        if EXPORT_SEL_ONLY:
            objects = context.selected_objects
        else:
            objects = scene.objects

        full_path = ''.join(context_name)

        # erm... bit of a problem here, this can overwrite files when exporting frames. not too bad.
        # EXPORT THE FILE.
        write_file(full_path,
                   objects,
                   scene,
                   EXPORT_GROUPBY,
                   EXPORT_NORMALS,
                   EXPORT_UV,
                   EXPORT_KEEP_VERT_ORDER,
                   EXPORT_GLOBAL_MATRIX,
                   EXPORT_PATH_MODE,
                   )

    scene.frame_set(orig_frame, 0.0)

    # Restore old active scene.
#   orig_scene.makeCurrent()
#   Window.WaitCursor(0)


"""
Currently the exporter lacks these features:
* multiple scene export (only active scene is written)
* particles
"""


def save(operator,
         context,
         filepath="",
         group_by="MG",
         use_normals=False,
         use_uvs=True,
         keep_vertex_order=False,
         use_selection=True,
         use_animation=False,
         global_matrix=None,
         path_mode='AUTO'
         ):

    _write(context,
           filepath,
           EXPORT_GROUPBY=group_by,
           EXPORT_NORMALS=use_normals,
           EXPORT_UV=use_uvs,
           EXPORT_KEEP_VERT_ORDER=keep_vertex_order,
           EXPORT_SEL_ONLY=use_selection,
           EXPORT_ANIMATION=use_animation,
           EXPORT_GLOBAL_MATRIX=global_matrix,
           EXPORT_PATH_MODE=path_mode,
           )

    return {'FINISHED'}
