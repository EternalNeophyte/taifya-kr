package edu.psuti.alexandrov.ui;

import edu.psuti.alexandrov.interpret.RuntimeContext;
import edu.psuti.alexandrov.lex.LexAnalyzer;
import edu.psuti.alexandrov.lex.LexHighlighting;
import edu.psuti.alexandrov.lex.LexUnit;

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
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.psuti.alexandrov.interpret.FormationType.INCORRECT;
import static edu.psuti.alexandrov.lex.LexUnit.STUB;

public class MainFrame extends JFrame implements LexHighlighting {

    private static final int TYPE_RATE = 4;

    private final JTextPane codePane;
    private final JTextArea outputArea;
    private final JLabel outputLabel;
    private final JButton deployButton;

    private final AtomicInteger cursor;

    public MainFrame(String title) {
        super(title);
        codePane = setupCodePane();
        outputArea = setupOutputArea();
        outputLabel = setupOutputLabel();
        deployButton = setupDeployButton();
        cursor = new AtomicInteger();
        setupSelf();
    }

    private JTextPane setupCodePane() {
        JTextPane codePane = new JTextPane();
        codePane.setBounds(0, 0, 1000, 400);
        return codePane;
    }

    private JTextArea setupOutputArea() {
        JTextArea outputArea = new JTextArea(1, 1);
        outputArea.setBounds(0, 480, 1000, 220);
        return outputArea;
    }

    private JLabel setupOutputLabel() {
        JLabel outputLabel = new JLabel("Вывод программы");
        outputLabel.setBounds(800, 410, 200, 40);
        return outputLabel;
    }

    private JButton setupDeployButton() {
        JButton button = new JButton("Запустить");
        button.setBounds(25, 410, 125, 40);
        button.setForeground(SAKURA_SNOW);

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                outputArea.setText("");
                RuntimeContext context = LexAnalyzer.setupRuntimeContext(codePane.getText());
                context.tryRun();
                outputArea.setForeground(FIRE);
                context.errors()
                        .forEach((unit, message) -> {
                                String outputLine = Optional.of(unit)
                                        .filter(u -> !u.equals(STUB))
                                        .map(context::computePosition)
                                        .map(pos -> String.format("Строка %d, cтолбец %d: %s\n", pos.line(), pos.column() , message))
                                        .orElse(message);
                                outputArea.append(outputLine);
                        });
            }

        });
        return button;
    }

    private void setupSelf() {
        setSize(1000, 700);
        setLayout(null);
        setVisible(true);
        setResizable(false);

        add(codePane);
        add(outputArea);
        add(outputLabel);
        add(deployButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        Executors.newSingleThreadScheduledExecutor()
                 .scheduleAtFixedRate(this::highlightAll, 10, 5, TimeUnit.SECONDS);
    }

    private void addColoredText(String text, Color color) {
        StyledDocument doc = codePane.getStyledDocument();
        Style style = codePane.addStyle("", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void highlightAll() {
        int lastCaretPos = codePane.getCaretPosition();
        String content = codePane.getText();
        codePane.setText("");
        cursor.set(0);
        LexAnalyzer.setupRuntimeContext(content)
                .formations()
                .forEach(formation -> {
                    boolean onFire = formation.type().equals(INCORRECT);
                    formation.units()
                            .forEach(unit -> {
                                    int end = unit.result().end();
                                    String text = content.substring(cursor.getAndSet(end), end);
                                    addColoredText(text, onFire ? FIRE : unit.type().highlight());
                            });
                });
        codePane.setCaretPosition(cursor.get() - lastCaretPos > TYPE_RATE
                                    ? lastCaretPos
                                    : cursor.get());
    }

}
