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

# <pep8-80 compliant>

print("LOADING SceneJS exporter")

bl_info = {
    "name": "SceneJS format",
    "author": "-",
    "blender": (2, 5, 8),
    "location": "File > Import-Export",
    "description": "Export SceneJS format",
    "warning": "",
    "wiki_url": "",
    "tracker_url": "",
    "support": 'COMMUNITY',
    "category": "Import-Export"}

if "bpy" in locals():
    import imp
    if "export_scenejs" in locals():
        imp.reload(export_scenejs)


import bpy
from bpy.props import (BoolProperty,
                       FloatProperty,
                       StringProperty,
                       EnumProperty,
                       )
#                                 ImportHelper,

from bpy_extras.io_utils import (ExportHelper,
                                 path_reference_mode,
                                 axis_conversion,
                                 )

class ExportSceneJS(bpy.types.Operator, ExportHelper):
    """Save a SceneJS File"""

    bl_idname = "export_scene.scenejs"
    bl_label = 'Export SceneJS'
    bl_options = {'PRESET'}

    filename_ext = ".json"
    filter_glob = StringProperty(
            default="*.json",
            options={'HIDDEN'},
            )
    
    group_by = EnumProperty(
        name='Group by',
        description="Select the option to group by",
        items=(('OMG',  'obj/mat/geom', ''),
               ('MG',   'mat/geom',     ''),
        ),
        default='MG',
    )
    
    vertices_range = EnumProperty(
        name='Vertices Range',
        description='Select the vertices range',
        items=(('uint',     'uint',     ''),
               ('int',      'int',      ''),
               ('ushort',   'ushort',   ''),
               ('short',    'short',    ''),
        ),
        default='short',
    )

    # context group
    use_selection = BoolProperty(
            name="Selection Only",
            description="Export selected objects only",
            default=False,
            )
    use_animation = BoolProperty(
            name="Animation",
            description="Write out an OBJ for each frame",
            default=False,
            )

    # extra data group
    use_normals = BoolProperty(
            name="Include Normals",
            description="",
            default=False,
            )
    use_uvs = BoolProperty(
            name="Include UVs",
            description="Write out the active UV coordinates",
            default=True,
            )

    # grouping group
    
    keep_vertex_order = BoolProperty(
            name="Keep Vertex Order",
            description="",
            default=False,
            )

    global_scale = FloatProperty(
            name="Scale",
            description="Scale all data",
            min=0.01, max=1000.0,
            soft_min=0.01,
            soft_max=1000.0,
            default=1.0,
            )

    axis_forward = EnumProperty(
            name="Forward",
            items=(('X', "X Forward", ""),
                   ('Y', "Y Forward", ""),
                   ('Z', "Z Forward", ""),
                   ('-X', "-X Forward", ""),
                   ('-Y', "-Y Forward", ""),
                   ('-Z', "-Z Forward", ""),
                   ),
            default='-Z',
            )

    axis_up = EnumProperty(
            name="Up",
            items=(('X', "X Up", ""),
                   ('Y', "Y Up", ""),
                   ('Z', "Z Up", ""),
                   ('-X', "-X Up", ""),
                   ('-Y', "-Y Up", ""),
                   ('-Z', "-Z Up", ""),
                   ),
            default='Y',
            )

    path_mode = path_reference_mode

    check_extension = True

    def execute(self, context):
        from . import export_scenejs

        from mathutils import Matrix
        keywords = self.as_keywords(ignore=("axis_forward",
                                            "axis_up",
                                            "global_scale",
                                            "check_existing",
                                            "filter_glob",
                                            ))

        global_matrix = Matrix()

        global_matrix[0][0] = \
        global_matrix[1][1] = \
        global_matrix[2][2] = self.global_scale

        global_matrix = (global_matrix *
                         axis_conversion(to_forward=self.axis_forward,
                                         to_up=self.axis_up,
                                         ).to_4x4())
        print("= Global Matrix =" + str(global_matrix))
        keywords["global_matrix"] = global_matrix
        return export_scenejs.save(self, context, **keywords)


def menu_func_export(self, context):
    self.layout.operator(ExportSceneJS.bl_idname, text="SceneJS (.json)")


def register():
    bpy.utils.register_module(__name__)

    bpy.types.INFO_MT_file_export.append(menu_func_export)


def unregister():
    bpy.utils.unregister_module(__name__)

    bpy.types.INFO_MT_file_export.remove(menu_func_export)

if __name__ == "__main__":
    register()
