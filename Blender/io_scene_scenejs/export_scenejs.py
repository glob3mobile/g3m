# <pep8 compliant>

import os
import time

import bpy
import mathutils
import bpy_extras.io_utils

def create_object(name):
    object = {}
    object["name"] = name
    object["materials"] = {}
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
    
    return material

def name_compat(name):
    if name is None:
        return 'None'
    else:
        return name.replace(' ', '_')

def write_mtl(scene, filepath, path_mode, copy_set, mtl_dict):
    from mathutils import Color

    # world = scene.world
    # if world:
    #     world_amb = world.ambient_color
    # else:
    #     world_amb = Color((0.0, 0.0, 0.0))

    source_dir = os.path.dirname(bpy.data.filepath)
    dest_dir = os.path.dirname(filepath)

    file = open(filepath, "w", encoding="utf8", newline="\n")
    fw = file.write

    fw('# Blender MTL File: %r\n' % (os.path.basename(bpy.data.filepath) or "None"))
    fw('# Material Count: %i\n' % len(mtl_dict))

    mtl_dict_values = list(mtl_dict.values())
    mtl_dict_values.sort(key=lambda m: m[0])

    # Write material/image combinations we have used.
    # Using mtl_dict.values() directly gives un-predictable order.
    for mtl_mat_name, mat, face_img in mtl_dict_values:

        # Get the Blender data for the material and the image.
        # Having an image named None will make a bug, dont do it :)

        fw('newmtl %s\n' % mtl_mat_name)  # Define a new material: matname_imgname

        if mat:
            # convert from blenders spec to 0 - 1000 range.
            if mat.specular_shader == 'WARDISO':
                tspec = (0.4 - mat.specular_slope) / 0.0004
            else:
                tspec = (mat.specular_hardness - 1) * 1.9607843137254901
            fw('Ns %.6f\n' % tspec)
            del tspec

            fw('Ka %.6f %.6f %.6f\n' % (mat.ambient * world_amb)[:])  # Ambient, uses mirror color,
            fw('Kd %.6f %.6f %.6f\n' % (mat.diffuse_intensity * mat.diffuse_color)[:])  # Diffuse
            fw('Ks %.6f %.6f %.6f\n' % (mat.specular_intensity * mat.specular_color)[:])  # Specular
            if hasattr(mat, "ior"):
                fw('Ni %.6f\n' % mat.ior)  # Refraction index
            else:
                fw('Ni %.6f\n' % 1.0)
            fw('d %.6f\n' % mat.alpha)  # Alpha (obj uses 'd' for dissolve)

            # 0 to disable lighting, 1 for ambient & diffuse only (specular color set to black), 2 for full lighting.
            if mat.use_shadeless:
                fw('illum 0\n')  # ignore lighting
            elif mat.specular_intensity == 0:
                fw('illum 1\n')  # no specular.
            else:
                fw('illum 2\n')  # light normaly

        else:
            #write a dummy material here?
            fw('Ns 0\n')
            fw('Ka %.6f %.6f %.6f\n' % world_amb[:])  # Ambient, uses mirror color,
            fw('Kd 0.8 0.8 0.8\n')
            fw('Ks 0.8 0.8 0.8\n')
            fw('d 1\n')  # No alpha
            fw('illum 2\n')  # light normaly

        # Write images!
        if face_img:  # We have an image on the face!
            # write relative image path
            rel = bpy_extras.io_utils.path_reference(face_img.filepath, source_dir, dest_dir, path_mode, "", copy_set, face_img.library)
            fw('map_Kd %s\n' % rel)  # Diffuse mapping image

        if mat:  # No face image. if we havea material search for MTex image.
            image_map = {}
            # backwards so topmost are highest priority
            for mtex in reversed(mat.texture_slots):
                if mtex and mtex.texture.type == 'IMAGE':
                    image = mtex.texture.image
                    if image:
                        # texface overrides others
                        if mtex.use_map_color_diffuse and face_img is None:
                            image_map["map_Kd"] = image
                        if mtex.use_map_ambient:
                            image_map["map_Ka"] = image
                        if mtex.use_map_specular:
                            image_map["map_Ks"] = image
                        if mtex.use_map_alpha:
                            image_map["map_d"] = image
                        if mtex.use_map_translucency:
                            image_map["map_Tr"] = image
                        if mtex.use_map_normal:
                            image_map["map_Bump"] = image
                        if mtex.use_map_hardness:
                            image_map["map_Ns"] = image

            for key, image in image_map.items():
                filepath = bpy_extras.io_utils.path_reference(image.filepath, source_dir, dest_dir, path_mode, "", copy_set, image.library)
                fw('%s %s\n' % (key, repr(filepath)[1:-1]))

        fw('\n\n')

    file.close()


def test_nurbs_compat(ob):
    if ob.type != 'CURVE':
        return False

    for nu in ob.data.splines:
        if nu.point_count_v == 1 and nu.type != 'BEZIER':  # not a surface and not bezier
            return True

    return False


def write_file(filepath, objects, scene,
               EXPORT_NORMALS=False,
               EXPORT_UV=True,
               EXPORT_MTL=True,
               EXPORT_KEEP_VERT_ORDER=False,
               EXPORT_GLOBAL_MATRIX=None,
               EXPORT_PATH_MODE='AUTO',
               ):
    """
    Basic write function. The context and options must be already set
    This can be accessed externaly
    eg.
    write( 'c:\\test\\foobar.obj', Blender.Object.GetSelected() ) # Using default options.
    """

    if EXPORT_GLOBAL_MATRIX is None:
        EXPORT_GLOBAL_MATRIX = mathutils.Matrix()

    def veckey3d(v):
        return round(v.x, 6), round(v.y, 6), round(v.z, 6)
    #return v.x, v.y, v.z

    def veckey2d(v):
        return round(v[0], 6), round(v[1], 6)
    #return v[0], v[1]

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

    file = open(filepath, "w", encoding="utf8", newline="\n")
    fw = file.write

    # Write Header
    #fw('# Blender v%s SceneJS File: %r\n' % (bpy.app.version_string, os.path.basename(bpy.data.filepath)))
    #fw('# www.blender.org\n')
    fw('{"type":"node","id":"root","nodes":[\n')

    # Tell the obj file what material file to use.
    if EXPORT_MTL:
        mtlfilepath = os.path.splitext(filepath)[0] + ".mtl"
    #    fw('mtllib %s\n' % repr(os.path.basename(mtlfilepath))[1:-1])  # filepath can contain non utf8 chars, use repr

    # Initialize totals, these are updated each object

    face_vert_index = 1

    #globalNormals = {}

    # A Dict of Materials
    # (material.name, image.name):matname_imagename # matname_imagename has gaps removed.
    mtl_dict = {}
    # Used to reduce the usage of matname_texname materials, which can become annoying in case of
    # repeated exports/imports, yet keeping unique mat names per keys!
    # mtl_name: (material.name, image.name)
    mtl_rev_dict = {}

    copy_set = set()

    # Get all meshes
    #contextMat = 0, 0  # Can never be this, so we will label a new material the first chance we get.

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
            # faces = [ f for f in me.tessfaces ]

            #edges = []

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

            # Set the default mat to no material and no image.
            contextMat = 0, 0  # Can never be this, so we will label a new material the first chance we get.

            name1 = ob.name
            name2 = ob.data.name
            if name1 == name2:
                obnamestring = name_compat(name1)
            else:
                obnamestring = '%s_%s' % (name_compat(name1), name_compat(name2))

            #fw('{"type":"geometry","primitive":"triangles","id":"%s"\n' % obnamestring)

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

                # CHECK FOR CONTEXT SWITCH
                if key == contextMat:
                    pass  # Context already switched, dont do anything
                else:
                    #print('- Switching material from "' + str(contextMat) + '" to "' + str(key) + '"')

                    if key[0] is None and key[1] is None:
                        # Write a null material, since we know the context has changed.
                        if EXPORT_MTL:
                            #fw("usemtl (null)\n")  # mat, image
                            pass

                    else:
                        mat_data = mtl_dict.get(key)
                        if not mat_data:
                            # First add to global dict so we can export to mtl
                            # Then write mtl

                            # Make a new names from the mat and image name,
                            # converting any spaces to underscores with name_compat.

                            # If none image dont bother adding it to the name
                            # Try to avoid as much as possible adding texname (or other things)
                            # to the mtl name (see [#32102])...
                            mtl_name = "%s" % name_compat(key[0])
                            if mtl_rev_dict.get(mtl_name, None) not in {key, None}:
                                if key[1] is None:
                                    tmp_ext = "_NONE"
                                else:
                                    tmp_ext = "_%s" % name_compat(key[1])
                                i = 0
                                while mtl_rev_dict.get(mtl_name + tmp_ext, None) not in {key, None}:
                                    i += 1
                                    tmp_ext = "_%3d" % i
                                mtl_name += tmp_ext
                            mat_data = mtl_dict[key] = mtl_name, materials[f_mat], f_image
                            mtl_rev_dict[mtl_name] = key

                        #print ( "    mat_data=" + str(mat_data)  )

                        if EXPORT_MTL:
                            #fw("usemtl %s\n" % mat_data[0])  # can be mat_image or (null)
                            pass

                contextMat = key

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
                            uv = uv_textures[ (f_index, vi) ]
                        else:
                            uv = None

                        vertexData = (vertex, normal, uv)
                        #AT_WORK
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
            fw('{"type":"node","id":"%s","nodes":[\n' % object["name"])
            for matKey, material in object["materials"].items():
                print ( "  " + str(matKey) + " => "+ str(material) )
                
                fw('  {"type":"material"')
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
                    print ( '*** Found texture "%s"' % material["texture"] )
                    fw('    {"type":"texture","layers":[{"uri":"%s"}],"nodes":[' % material["texture"])
                
                fw('    {"type":"geometry","primitive":"triangles","id":"%s_%s"\n' % (object["name"], material["name"]) )
                fw('    ,"positions":[\n')
                for vertex in material["vertices"]:
                    fw('      %.10g,%.10g,%.10g,\n' %  vertex)
                fw('    ]\n')

                if (material["normals"]):
                    fw('    ,"normals":[\n')
                    for normal in material["normals"]:
                        fw('      %.10g,%.10g,%.10g,\n' %  normal)
                    fw('    ]\n')

                if (material["uv"]):
                    fw('    ,"uv":[\n')
                    for uv in material["uv"]:
                        fw('      %.10g,%.10g,\n' %  uv)
                    fw('    ]\n')

                fw('    ,"indices":[')
                for index in material["indices"]:
                    fw('%g,' %  (index - 1))
                fw(']\n')

                fw('    },\n') # end geometry

                if (material["texture"]):
                    fw('    ]}')
                
                fw('  ]},\n') # end material
            print()
            fw(']},\n')
            
            #fw('{"type":"geometry","primitive":"triangles","id":"%s"\n' % obnamestring)
            #
            #fw(',"positions":[\n')
            #for vertex in verticesList:
            #    fw('%.10g,%.10g,%.10g,\n' %  vertex)
            #fw(']\n')
            #
            #if (normalsList):
            #    fw(',"normals":[\n')
            #    for normal in normalsList:
            #        fw('%.10g,%.10g,%.10g,\n' %  normal)
            #    fw(']\n')
            #
            #if (uvList):
            #    fw(',"uv":[\n')
            #    for uv in uvList:
            #        fw('%.10g,%.10g,\n' %  uv)
            #    fw(']\n')
            #
            #fw(',"indices":[')
            #for index in indicesList:
            #    fw('%g,' %  (index - 1))
            #fw(']\n')
            #
            #fw('},\n')

            # clean up
            bpy.data.meshes.remove(me)

        if ob_main.dupli_type != 'NONE':
            ob_main.dupli_list_clear()

    fw(']}\n')

    file.close()

    # Now we have all our materials, save them
    if EXPORT_MTL:
        #write_mtl(scene, mtlfilepath, EXPORT_PATH_MODE, copy_set, mtl_dict)
        pass

    # copy all collected files.
    bpy_extras.io_utils.path_reference_copy(copy_set)

    print("SceneJS Export time: %.2f" % (time.time() - time1))


def _write(context, filepath,
              EXPORT_NORMALS,  # not yet
              EXPORT_UV,  # ok
              EXPORT_MTL,
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
        write_file(full_path, objects, scene,
                   EXPORT_NORMALS,
                   EXPORT_UV,
                   EXPORT_MTL,
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


def save(operator, context, filepath="",
         use_normals=False,
         use_uvs=True,
         use_materials=True,
         keep_vertex_order=False,
         use_selection=True,
         use_animation=False,
         global_matrix=None,
         path_mode='AUTO'
         ):

    _write(context, filepath,
           EXPORT_NORMALS=use_normals,
           EXPORT_UV=use_uvs,
           EXPORT_MTL=use_materials,
           EXPORT_KEEP_VERT_ORDER=keep_vertex_order,
           EXPORT_SEL_ONLY=use_selection,
           EXPORT_ANIMATION=use_animation,
           EXPORT_GLOBAL_MATRIX=global_matrix,
           EXPORT_PATH_MODE=path_mode,
           )

    return {'FINISHED'}
