package com.game.GalacticOdyssey.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.GalacticOdyssey.main.GalacticOdyssey;


public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Galactic Odyssey: The Cosmic Enigma");
        config.setWindowedMode(800, 600); // Tamaño de la ventana
        config.setResizable(false); // Ventana no redimensionable
        new Lwjgl3Application(new GalacticOdyssey(), config);
    }

}
