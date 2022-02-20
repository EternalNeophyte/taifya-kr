package edu.psuti.alexandrov.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface ReleaseKeyListener extends KeyListener {

    @Override
    default void keyTyped(KeyEvent e) { }

    @Override
    default void keyPressed(KeyEvent e) { }

}
