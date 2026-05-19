#version 330 core

in vec2 passTex;

out vec4 outC;

uniform sampler2D tex;
uniform vec4 color;

void main() {
    outC = color*texture(tex, passTex);
}