#version 330 core

in vec2 passTex;
in vec2 vLocal;

out vec4 outC;

uniform sampler2D tex;
uniform vec4 color;
uniform vec4 lightLevels; // x=top-left, y=top-right, z=bottom-right, w=bottom-left

void main() {
    vec4 t = color*texture(tex, passTex);
    float top = mix(lightLevels.x, lightLevels.y, vLocal.x);
    float bottom = mix(lightLevels.w, lightLevels.z, vLocal.x);
    float light = mix(bottom, top, vLocal.y);
    outC = vec4(t.xyz*light, t.w);
}
