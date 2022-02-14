package edu.psuti.alexandrov.ui;

import edu.psuti.alexandrov.lex.LexAnalyzer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private final JTextPane codePane;
    private final JTextArea outputArea;
    private final JButton deployButton;

    public MainFrame(String title) {
        super(title);
        codePane = setupCodePane();
        outputArea = setupOutputArea();
        deployButton = setupDeployButton();
        setupSelf();
    }

    public JTextPane setupCodePane() {
        JTextPane codePane = new JTextPane();
        codePane.setBounds(0, 0, 990, 400);
        return codePane;
    }

    private JTextArea setupOutputArea() {
        JTextArea outputArea = new JTextArea(1, 1);
        outputArea.setBounds(25, 460, 940, 225);
        return outputArea;
    }

    private JButton setupDeployButton() {
        JButton button = new JButton("Запустить программу");
        button.setBounds(25, 410, 200, 40);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String content = codePane.getText();
                codePane.setText("");
                LexAnalyzer.setupRuntimeContext(content)
                        .formations()
                        .forEach(formation -> formation
                                .units()
                                .forEach(unit -> {
                                    String text = unit.result().group();
                                    switch (unit.type()) {
                                        case IDENTIFIER -> addColoredText(text, Color.BLUE);
                                        default -> addColoredText(text, Color.LIGHT_GRAY);
                                    }
                                }));
                //codeArea.append("Clicked ");
            }
        });
        return button;
    }

    private void setupSelf() {
        setSize(1000, 700);
        setLayout(null);
        setVisible(true);
        add(codePane);
        add(outputArea);
        add(deployButton);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
    }

    public void addColoredText(String text, Color color) {
        StyledDocument doc = codePane.getStyledDocument();
        Style style = codePane.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
