package edu.psuti.alexandrov;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import edu.psuti.alexandrov.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Runner {

    public static void main(String... args) {
        FlatAtomOneDarkContrastIJTheme.setup();
        UIManager.getLookAndFeelDefaults().put("TextPane.font", new Font("Segoe UI", Font.BOLD, 18));
        new MainFrame("Курсовая по ТАиФЯ");

        //LexAnalyzer.toRuntimeContext().run();
    }
}
