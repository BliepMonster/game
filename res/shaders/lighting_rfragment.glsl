#version 330 core

in vec2 passTex;

out vec4 outColor;

uniform vec4 color;

uniform float lightLevel;

void main() {
    outColor = vec4(color.xyz*lightLevel, color.w);
}