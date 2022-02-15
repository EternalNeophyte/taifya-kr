package edu.psuti.alexandrov.ui;

import edu.psuti.alexandrov.lex.LexAnalyzer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainFrame extends JFrame {

    private final JTextPane codePane;
    private final JTextArea outputArea;
    private final JButton deployButton;

    private final AtomicInteger cursor;

    public MainFrame(String title) {
        super(title);
        codePane = setupCodePane();
        outputArea = setupOutputArea();
        deployButton = setupDeployButton();
        cursor = new AtomicInteger();
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
                cursor.set(0);
                LexAnalyzer.setupRuntimeContext(content)
                        .formations()
                        .forEach(formation -> formation
                                .units()
                                .forEach(unit -> {
                                    int end = unit.result().end();
                                    String highLighted = content.substring(cursor.getAndSet(end), end);
                                    switch (unit.type()) {
                                        case IDENTIFIER -> addColoredText(highLighted, Color.BLUE);
                                        default -> addColoredText(highLighted, Color.LIGHT_GRAY);
                                    }
                                }));
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
