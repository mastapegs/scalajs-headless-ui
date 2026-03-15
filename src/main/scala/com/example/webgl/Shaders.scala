package com.example.webgl

import com.example.headless.components.Pattern

/** GLSL shader source strings for WebGL generative art visualizations. */
object Shaders {

  val vertexShader: String =
    """attribute vec2 a_position;
      |void main() {
      |  gl_Position = vec4(a_position, 0.0, 1.0);
      |}""".stripMargin

  private val uniformPreamble: String =
    """precision mediump float;
      |uniform float u_time;
      |uniform vec2 u_resolution;
      |uniform float u_speed;
      |uniform float u_complexity;
      |uniform vec2 u_mouse;
      |uniform vec3 u_color1;
      |uniform vec3 u_color2;
      |uniform vec3 u_color3;
      |""".stripMargin

  private val wavesFragment: String = uniformPreamble +
    """void main() {
      |  vec2 uv = gl_FragCoord.xy / u_resolution;
      |  float t = u_time * u_speed * 0.5;
      |  vec2 m = u_mouse;
      |
      |  float wave = 0.0;
      |  for (float i = 1.0; i <= 10.0; i += 1.0) {
      |    if (i > u_complexity) break;
      |    float freq = i * 1.5 + m.x * 2.0;
      |    float amp = 1.0 / i;
      |    wave += sin(uv.x * freq + t * (0.8 + i * 0.3) + uv.y * i * 0.5) * amp;
      |    wave += cos(uv.y * freq * 0.7 + t * (0.6 + i * 0.2) + m.y * 3.14159) * amp * 0.5;
      |  }
      |  wave = wave * 0.5 + 0.5;
      |
      |  vec3 col = mix(u_color1, u_color2, wave);
      |  col = mix(col, u_color3, sin(wave * 3.14159) * 0.5 + 0.5);
      |  gl_FragColor = vec4(col, 1.0);
      |}""".stripMargin

  private val spiralFragment: String = uniformPreamble +
    """void main() {
      |  vec2 uv = (gl_FragCoord.xy - 0.5 * u_resolution) / min(u_resolution.x, u_resolution.y);
      |  float t = u_time * u_speed * 0.4;
      |  vec2 m = u_mouse - 0.5;
      |
      |  float angle = atan(uv.y + m.y * 0.3, uv.x + m.x * 0.3);
      |  float dist = length(uv);
      |
      |  float spiral = 0.0;
      |  for (float i = 1.0; i <= 10.0; i += 1.0) {
      |    if (i > u_complexity) break;
      |    float twist = i * 3.0;
      |    spiral += sin(angle * twist + dist * 10.0 * i - t * (2.0 + i * 0.5)) / i;
      |  }
      |  spiral = spiral * 0.5 + 0.5;
      |
      |  float ring = sin(dist * 20.0 - t * 3.0) * 0.5 + 0.5;
      |  vec3 col = mix(u_color1, u_color2, spiral);
      |  col = mix(col, u_color3, ring * 0.4);
      |  col *= 1.0 - dist * 0.5;
      |  gl_FragColor = vec4(col, 1.0);
      |}""".stripMargin

  private val particlesFragment: String = uniformPreamble +
    """float hash(vec2 p) {
      |  return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
      |}
      |
      |void main() {
      |  vec2 uv = gl_FragCoord.xy / u_resolution;
      |  float t = u_time * u_speed * 0.3;
      |  vec2 m = u_mouse;
      |
      |  float gridSize = 3.0 + u_complexity * 2.0;
      |  vec2 grid = uv * gridSize;
      |  vec2 cell = floor(grid);
      |  vec2 local = fract(grid);
      |
      |  float brightness = 0.0;
      |  for (float dx = -1.0; dx <= 1.0; dx += 1.0) {
      |    for (float dy = -1.0; dy <= 1.0; dy += 1.0) {
      |      vec2 neighbor = cell + vec2(dx, dy);
      |      float h = hash(neighbor);
      |      vec2 particlePos = vec2(hash(neighbor + 0.1), hash(neighbor + 0.2));
      |      particlePos += vec2(sin(t + h * 6.28) * 0.3, cos(t * 0.7 + h * 6.28) * 0.3);
      |      particlePos += (m - 0.5) * 0.2;
      |
      |      vec2 diff = local - particlePos - vec2(dx, dy);
      |      float dist = length(diff);
      |      float size = 0.03 + h * 0.04;
      |      brightness += smoothstep(size, size * 0.2, dist) * (0.5 + h * 0.5);
      |    }
      |  }
      |
      |  vec3 col = u_color1 * 0.1;
      |  col = mix(col, u_color2, brightness * 0.7);
      |  col = mix(col, u_color3, brightness * brightness * 0.5);
      |  gl_FragColor = vec4(col, 1.0);
      |}""".stripMargin

  private val fractalFragment: String = uniformPreamble +
    """void main() {
      |  vec2 uv = (gl_FragCoord.xy - 0.5 * u_resolution) / min(u_resolution.x, u_resolution.y);
      |  uv *= 2.5;
      |  float t = u_time * u_speed * 0.2;
      |  vec2 m = u_mouse - 0.5;
      |
      |  vec2 c = vec2(
      |    0.36 * cos(t * 0.7 + m.x * 2.0) - 0.1,
      |    0.36 * sin(t * 0.5 + m.y * 2.0) + 0.1
      |  );
      |
      |  vec2 z = uv;
      |  float iterations = 0.0;
      |  float maxIter = 5.0 + u_complexity * 15.0;
      |
      |  for (float i = 0.0; i < 200.0; i += 1.0) {
      |    if (i >= maxIter) break;
      |    z = vec2(z.x * z.x - z.y * z.y, 2.0 * z.x * z.y) + c;
      |    if (dot(z, z) > 4.0) break;
      |    iterations = i;
      |  }
      |
      |  float f = iterations / maxIter;
      |  f = sqrt(f);
      |
      |  vec3 col = mix(u_color1, u_color2, f);
      |  col = mix(col, u_color3, sin(f * 6.28 + t) * 0.5 + 0.5);
      |  if (dot(z, z) <= 4.0) col = u_color1 * 0.15;
      |  gl_FragColor = vec4(col, 1.0);
      |}""".stripMargin

  def fragmentShaderFor(pattern: Pattern): String = pattern match {
    case Pattern.Waves     => wavesFragment
    case Pattern.Spiral    => spiralFragment
    case Pattern.Particles => particlesFragment
    case Pattern.Fractal   => fractalFragment
  }
}
