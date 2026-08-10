#version 330
layout(location=0) in vec2 position;
layout(location=1) in vec2 texCoords;

out vec2 passTex;
out vec2 vLocal;

uniform mat4 transform;

uniform vec4 texCoordsMultiplier;

void main() {
    gl_Position = transform * vec4(position,0.,1.);
    passTex.x = texCoords.x*texCoordsMultiplier.z+texCoordsMultiplier.x;
    passTex.y = texCoords.y*texCoordsMultiplier.w+texCoordsMultiplier.y;
    vLocal = position;
}
