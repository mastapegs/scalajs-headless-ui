package com.example.webgl

import com.example.headless.components._
import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.{WebGLProgram, WebGLRenderingContext => GL}

import scala.scalajs.js

/** Shared WebGL renderer that creates a canvas element with lifecycle-managed animation loop.
  *
  * This utility bridges headless [[Visualization]] state to GPU rendering. It lives outside the headless layer (it
  * produces DOM elements) and outside theme packages (shader rendering is theme-agnostic). Theme views call
  * [[createCanvas]] and wrap the result in their own styled layout.
  */
object WebGLRenderer {

  private case class ColorColors(c1: (Float, Float, Float), c2: (Float, Float, Float), c3: (Float, Float, Float))

  private def colorColorsFor(cs: ColorScheme): ColorColors = cs match {
    case ColorScheme.Ocean =>
      ColorColors((0.0f, 0.09f, 0.18f), (0.0f, 0.46f, 0.74f), (0.3f, 0.85f, 0.92f))
    case ColorScheme.Sunset =>
      ColorColors((0.15f, 0.05f, 0.18f), (0.85f, 0.25f, 0.15f), (1.0f, 0.75f, 0.2f))
    case ColorScheme.Neon =>
      ColorColors((0.05f, 0.0f, 0.1f), (0.95f, 0.1f, 0.8f), (0.1f, 1.0f, 0.6f))
    case ColorScheme.Monochrome =>
      ColorColors((0.05f, 0.05f, 0.05f), (0.5f, 0.5f, 0.5f), (0.95f, 0.95f, 0.95f))
  }

  private def compileProgram(gl: GL, fragmentSource: String): WebGLProgram = {
    val vs = gl.createShader(GL.VERTEX_SHADER)
    gl.shaderSource(vs, Shaders.vertexShader)
    gl.compileShader(vs)

    if (!gl.getShaderParameter(vs, GL.COMPILE_STATUS).asInstanceOf[Boolean]) {
      dom.console.error("Vertex shader error:", gl.getShaderInfoLog(vs))
      return null.asInstanceOf[WebGLProgram]
    }

    val fs = gl.createShader(GL.FRAGMENT_SHADER)
    gl.shaderSource(fs, fragmentSource)
    gl.compileShader(fs)

    if (!gl.getShaderParameter(fs, GL.COMPILE_STATUS).asInstanceOf[Boolean]) {
      dom.console.error("Fragment shader error:", gl.getShaderInfoLog(fs))
      return null.asInstanceOf[WebGLProgram]
    }

    val program = gl.createProgram()
    gl.attachShader(program, vs)
    gl.attachShader(program, fs)
    gl.linkProgram(program)

    if (!gl.getProgramParameter(program, GL.LINK_STATUS).asInstanceOf[Boolean]) {
      dom.console.error("Program link error:", gl.getProgramInfoLog(program))
      return null.asInstanceOf[WebGLProgram]
    }

    gl.deleteShader(vs)
    gl.deleteShader(fs)

    program
  }

  /** Creates a canvas element that renders generative art based on the given [[Visualization]] state.
    *
    * The canvas starts its WebGL render loop on mount and stops on unmount. It subscribes to the visualization's
    * reactive signals to update shader uniforms in real-time.
    */
  def createCanvas(viz: Visualization): HtmlElement = {
    var animFrameId: Int             = 0
    var currentProgram: WebGLProgram = null.asInstanceOf[WebGLProgram]
    var glCtx: GL                    = null.asInstanceOf[GL]

    // Mutable state read by the render loop, updated by signal subscriptions
    var currentSpeed: Double       = 1.0
    var currentComplexity: Int     = 5
    var currentMouseX: Double      = 0.5
    var currentMouseY: Double      = 0.5
    var currentColors: ColorColors = colorColorsFor(ColorScheme.Ocean)
    var mouseEnabled: Boolean      = false

    def render(gl: GL, program: WebGLProgram, time: Double): Unit = {
      if (program == null) return

      gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)
      gl.clearColor(0, 0, 0, 1)
      gl.clear(GL.COLOR_BUFFER_BIT)

      gl.useProgram(program)

      gl.uniform1f(gl.getUniformLocation(program, "u_time"), time.toFloat)
      gl.uniform2f(
        gl.getUniformLocation(program, "u_resolution"),
        gl.canvas.width.toFloat,
        gl.canvas.height.toFloat
      )
      gl.uniform1f(gl.getUniformLocation(program, "u_speed"), currentSpeed.toFloat)
      gl.uniform1f(gl.getUniformLocation(program, "u_complexity"), currentComplexity.toFloat)
      gl.uniform2f(gl.getUniformLocation(program, "u_mouse"), currentMouseX.toFloat, currentMouseY.toFloat)

      val cc = currentColors
      gl.uniform3f(gl.getUniformLocation(program, "u_color1"), cc.c1._1, cc.c1._2, cc.c1._3)
      gl.uniform3f(gl.getUniformLocation(program, "u_color2"), cc.c2._1, cc.c2._2, cc.c2._3)
      gl.uniform3f(gl.getUniformLocation(program, "u_color3"), cc.c3._1, cc.c3._2, cc.c3._3)

      val posLoc = gl.getAttribLocation(program, "a_position")
      gl.enableVertexAttribArray(posLoc)
      gl.vertexAttribPointer(posLoc, 2, GL.FLOAT, false, 0, 0)
      gl.drawArrays(GL.TRIANGLES, 0, 6)
    }

    canvasTag(
      width     := "800",
      height    := "600",
      styleAttr := "width: 100%; height: 100%; display: block;",
      onMountCallback { ctx =>
        val canvas = ctx.thisNode.ref.asInstanceOf[dom.HTMLCanvasElement]

        // Size canvas to actual pixel dimensions
        val dpr = dom.window.devicePixelRatio
        val w   = canvas.clientWidth
        val h   = canvas.clientHeight
        canvas.width = (w * dpr).toInt
        canvas.height = (h * dpr).toInt

        glCtx = canvas.getContext("webgl").asInstanceOf[GL]
        if (glCtx == null) {
          dom.console.error("WebGL not supported")
        } else {

          // Set up full-screen quad geometry
          val buffer = glCtx.createBuffer()
          glCtx.bindBuffer(GL.ARRAY_BUFFER, buffer)
          val vertices = js.Array[Float](-1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, 1f)
          glCtx.bufferData(GL.ARRAY_BUFFER, new js.typedarray.Float32Array(vertices), GL.STATIC_DRAW)

          // Compile initial program
          currentProgram = compileProgram(glCtx, Shaders.fragmentShaderFor(Pattern.Waves))

          // Subscribe to signals
          viz.speed.foreach(s => currentSpeed = s)(ctx.owner)
          viz.complexity.foreach(c => currentComplexity = c)(ctx.owner)
          viz.colorScheme.foreach(cs => currentColors = colorColorsFor(cs))(ctx.owner)
          viz.mouseInfluence.foreach(m => mouseEnabled = m)(ctx.owner)
          viz.pattern.foreach { p =>
            val newProg = compileProgram(glCtx, Shaders.fragmentShaderFor(p))
            if (newProg != null) currentProgram = newProg
          }(ctx.owner)

          // Mouse tracking
          canvas.addEventListener(
            "mousemove",
            (e: dom.MouseEvent) =>
              if (mouseEnabled) {
                val rect = canvas.getBoundingClientRect()
                currentMouseX = (e.clientX - rect.left) / rect.width
                currentMouseY = 1.0 - (e.clientY - rect.top) / rect.height
              }
          )

          canvas.addEventListener(
            "mouseleave",
            { (_: dom.MouseEvent) =>
              currentMouseX = 0.5
              currentMouseY = 0.5
            }
          )

          // Animation loop
          val startTime = dom.window.performance.now()

          def loop(timestamp: Double): Unit = {
            val time = (timestamp - startTime) / 1000.0
            render(glCtx, currentProgram, time)
            animFrameId = dom.window.requestAnimationFrame(loop _)
          }

          animFrameId = dom.window.requestAnimationFrame(loop _)
        } // end else (WebGL supported)
      },
      onUnmountCallback { _ =>
        dom.window.cancelAnimationFrame(animFrameId)
      }
    )
  }
}
