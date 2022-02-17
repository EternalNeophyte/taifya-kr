package edu.psuti.alexandrov.ui;

import edu.psuti.alexandrov.interpret.RuntimeContext;
import edu.psuti.alexandrov.lex.LexAnalyzer;
import edu.psuti.alexandrov.lex.LexHighlighting;

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

public class UIFrame extends JFrame implements LexHighlighting {

    private static final int TYPE_RATE = 2;

    private final JTextPane codePane;
    private final JTextArea outputArea;
    private final JCheckBox devModeCheckBox;

    private final AtomicInteger cursor;

    public UIFrame(String title) {
        super(title);
        codePane = setupCodePane();
        outputArea = setupOutputArea();
        devModeCheckBox = setupDevModeCheckBox();
        cursor = new AtomicInteger();
        setupAllRemaining();
    }

    private JTextPane setupCodePane() {
        JTextPane codePane = new JTextPane();
        codePane.setBounds(0, 0, 1000, 400);
        return codePane;
    }

    private JTextArea setupOutputArea() {
        JTextArea outputArea = new JTextArea(1, 1);
        outputArea.setBounds(0, 485, 1000, 220);
        outputArea.setEditable(false);
        return outputArea;
    }

    private JLabel setupOutputLabel() {
        JLabel outputLabel = new JLabel("Вывод программы");
        outputLabel.setBounds(25, 460, 200, 20);
        return outputLabel;
    }

    private JButton setupDeployButton() {
        JButton button = new JButton("Запустить");
        button.setBounds(25, 410, 125, 40);
        button.setForeground(SAKURA_SNOW);

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                RuntimeContext context = LexAnalyzer.setupRuntimeContext(codePane.getText());
                outputArea.setText("");
                if(devModeCheckBox.isSelected()) {
                    context.formations().forEach(f -> outputArea.append(f.toString()));
                }
                if(context.runWithoutErrors()) {
                    outputArea.setForeground(SAKURA_SNOW);
                    context.textHandlers().forEach(handler -> handler.accept(outputArea));
                }
                else {
                    outputArea.setForeground(FIRE);
                    context.errors()
                            .forEach((unit, message) -> {
                                String outputLine = Optional.ofNullable(unit)
                                        .map(context::computePosition)
                                        .map(pos -> String.format("Строка %d, cтолбец %d: %s\n",
                                                                    pos.line(), pos.column() , message))
                                        .orElse(message);
                                outputArea.append(outputLine);
                            });
                }

            }

        });
        return button;
    }

    private JCheckBox setupDevModeCheckBox() {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBounds(175, 420, 20, 20);
        return checkBox;
    }

    private JLabel setupDevModeLabel() {
        JLabel devModeLabel = new JLabel("Показывать внутреннее представление программы");
        devModeLabel.setBounds(200, 420, 400, 20);
        return devModeLabel;
    }

    private void setupAllRemaining() {
        setSize(1000, 700);
        setLayout(null);
        setVisible(true);
        setResizable(false);

        add(codePane);
        add(outputArea);
        add(devModeCheckBox);
        add(setupDevModeLabel());
        add(setupOutputLabel());
        add(setupDeployButton());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        Executors.newSingleThreadScheduledExecutor()
                 .scheduleAtFixedRate(this::highlightAll, 10, 5, TimeUnit.SECONDS);

    }

    private void addColoredCode(String fragment, Color color) {
        StyledDocument doc = codePane.getStyledDocument();
        Style style = codePane.addStyle("", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), fragment, style);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void highlightAll() {
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
                                    addColoredCode(text, onFire ? FIRE : unit.type().highlight());
                            });
                });
        codePane.setCaretPosition(codePane.getText().length());
    }

}
