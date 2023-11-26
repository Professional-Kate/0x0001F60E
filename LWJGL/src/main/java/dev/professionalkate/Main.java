package dev.professionalkate;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    
    private static long window;
    
    public static void run() {
        init();
        loop();
        
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    private static void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        // configure the window
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        
        // create window
        window = glfwCreateWindow(1600, 900, "The Game", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create window...");
        }
        
        // closing the window and stopping the game
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                // will be used in our loop function
                glfwSetWindowShouldClose(window, true);
            }
        });
        
        try (MemoryStack stack = stackPush()) {
            // allocate some memory
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            
            // get the window size and pass it into our allocated memory
            glfwGetWindowSize(window, pWidth, pHeight);
            
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor()); // get the primary monitor resolution
            
            // center the window on screen
            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
        }
        
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // enables v-sync
        
        glfwShowWindow(window);
    }
    
    private static void loop() {
        GL.createCapabilities(); // must be called every frame
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // black clear
        
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clears the framebuffer
            
            glfwSwapBuffers(window);
            
            glfwPollEvents();
        }
    }
    
    public static void main(String[] args) {
        run();
    }
}