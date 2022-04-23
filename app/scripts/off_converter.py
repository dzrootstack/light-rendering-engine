import math
import sys
import re

VERT_PATTERN = re.compile("[-]?\d*\.\d* [-]?\d*\.\d* [-]?\d*\.\d*")
COLOR_CONTROL_POINTS = [
    ([-1764, -1560, -168], [0.823, 0.062, 0.878]),
    ([1079, 1560, 639], [0.011, 0.925, 0.988]),
]


def get_color(vert):
    color = [0, 0, 0]
    sq_distances = []
    colors = []
    for control_point, control_color in COLOR_CONTROL_POINTS:
        sq_distances.append(
            (vert[0] - control_point[0])**2 +
            (vert[1] - control_point[1])**2 +
            (vert[2] - control_point[2])**2
        )
        colors.append(control_color)
    # the color of the vertex will be an interpolation of the colors of the
    # control points
    for i in range(3):
        color[i] = sum([
            colors[j][i] * (sq_distances[j] / sum(sq_distances))
            for j in range(len(sq_distances))
        ])
    return color


def convert(input_filename, output_filename):
    # read input file
    with open(input_filename, "r") as input_file:
        # write first 2 lines to output file
        with open(output_filename, "w") as output_file:
            lines = input_file.readlines()

            print("Read {} lines from {}".format(len(lines), input_filename))
            print(f"Converting {input_filename} to {output_filename}")

            output_file.write(lines[0])
            output_file.write(lines[1])

            # read vertices and primitives
            vertices = []
            primitives = []
            for line in lines[2:]:
                data = re.split('\s+', line.removesuffix("\n"))
                if len(data) == 3:
                    vertices.append(
                        [float(data[0]), float(data[1]), float(data[2])])
                else:
                    try:
                        prim = [int(val) for val in data[:-3]]
                        primitives.append(prim)
                    except ValueError:
                        pass

            # find model bounding box
            min_x = min(vert[0] for vert in vertices)
            max_x = max(vert[0] for vert in vertices)
            min_y = min(vert[1] for vert in vertices)
            max_y = max(vert[1] for vert in vertices)
            min_z = min(vert[2] for vert in vertices)
            max_z = max(vert[2] for vert in vertices)
            print(f"Model bounding box: {min_x}, {min_y}, {min_z} -> "
                  f"{max_x}, {max_y}, {max_z}")

            # append colors and write to output file
            for vert in vertices:
                color = get_color(vert)
                color = [round(c, 2) for c in color]
                output_file.write(
                    f"{vert[0]} {vert[1]} {vert[2]}  {color[0]}   {color[1]}   {color[2]}\n")

            # write primitives to output file
            for prim in primitives:
                output_file.write(
                    f"{prim[0]} {prim[1]} {prim[2]} {prim[3]}\n")

            print("Wrote {} lines to {}".format(len(lines), output_filename))


# get filename from arguments
if len(sys.argv) < 3:
    print("Usage: off_converter.py <input_filename> <output_filename>")
    sys.exit(1)
input_filename = sys.argv[1]
output_filename = sys.argv[2]


convert(input_filename, output_filename)
