import math
import sys
import re

VERT_PATTERN = re.compile("[-]?\d*\.\d* [-]?\d*\.\d* [-]?\d*\.\d*")
COLOR_CONTROL_POINTS = [
    ([2, 5, 0], [1.0, 0.3, 0.0]),  # red
    ([-4, 1, 2], [0.5, 1.0, 0.0]),  # green
    ([-3, -4, -1], [0.0, 0.2, 1.0]),  # blue
]


def get_color(vert):
    color = [0, 0, 0]
    distances = []
    colors = []
    for control_point, color in COLOR_CONTROL_POINTS:
        distances.append(math.sqrt(
            (vert[0] - control_point[0])**2 +
            (vert[1] - control_point[1])**2 +
            (vert[2] - control_point[2])**2
        ))
        colors.append(color)
    # the color of the vertex will be an interpolation of the colors of the
    # control points
    for i in range(3):
        color[i] = sum([
            colors[j][i] * (distances[j] / sum(distances))
            for j in range(len(distances))
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
