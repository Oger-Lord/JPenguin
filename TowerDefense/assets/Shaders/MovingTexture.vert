uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;
uniform float g_Time;
 
const float pi = 3.14159;
 
#ifdef IS_MOVING
    uniform float m_Speed;
    uniform float m_Rotation;
#endif
 
#ifdef HAS_COLORMAP
    attribute vec2 inTexCoord;
    varying vec2 texCoord1;
#endif
 
void main(){
    #ifdef HAS_COLORMAP
        texCoord1 = inTexCoord;
        #ifdef IS_MOVING
            int i;
            float time = (mod(g_Time,i)*m_Speed);
            texCoord1.x += (time*sin((m_Rotation*(pi/180))));
            texCoord1.y += (time*cos((m_Rotation*(pi/180))));
        #endif
    #endif
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}