package edu.psuti.alexandrov;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import edu.psuti.alexandrov.ui.UIFrame;


public class Runner {

    public static void main(String... args) {
        //https://habr.com/ru/post/596925/
        FlatAtomOneDarkContrastIJTheme.setup();
        new UIFrame("Курсовая по ТАиФЯ");
    }
}
