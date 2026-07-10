#version 330 core

in vec2 passTex;

out vec4 outC;

uniform sampler2D tex;
uniform vec4 color;

uniform float lightLevel;

void main() {
    vec4 t = color*texture(tex, passTex);
    outC = vec4(t.xyz*lightLevel, t.w);
}