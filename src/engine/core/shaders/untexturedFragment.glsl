#version 330 core

in vec2 passTex;

out vec4 outColor;

uniform vec4 color;

void main() {
    outColor = color;
}