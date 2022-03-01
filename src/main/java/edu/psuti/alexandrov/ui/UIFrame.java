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
import java.awt.event.*;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.psuti.alexandrov.interpret.FormationType.INCORRECT;

public class UIFrame extends JFrame implements LexHighlighting {

    private static final int TYPE_RATE = 2;

    private final JTextPane codePane;
    private final JTextPane outputPane;
    private final JCheckBox devModeCheckBox;

    private final AtomicInteger cursor;
    private final AtomicBoolean inputReady;

    public UIFrame(String title) {
        super(title);
        codePane = setupCodePane();
        outputPane = setupOutputPane();
        devModeCheckBox = setupDevModeCheckBox();
        cursor = new AtomicInteger();
        inputReady = new AtomicBoolean(false);
        setupAllRemaining();
    }

    public JTextPane codePane() {
        return codePane;
    }

    public JTextPane outputPane() {
        return outputPane;
    }

    public boolean inputReady() {
        return inputReady.get();
    }


    private JScrollPane wrapIntoScrollable(JTextPane source) {
        JScrollPane pane = new JScrollPane(source);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setViewportBorder(null);
        pane.setLocation(source.getLocation());
        Dimension dim = source.getSize();
        dim.width -= 15;
        pane.setSize(dim);
        return pane;
    }

    private JTextPane setupCodePane() {
        JTextPane codePane = new JTextPane();
        codePane.setFont(new Font("Segoe UI", Font.BOLD, 18));
        codePane.setBounds(0, 0, 1000, 400);
        return codePane;
    }

    private JTextPane setupOutputPane() {
        JTextPane outputPane = new JTextPane();
        outputPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        outputPane.setBounds(0, 485, 1000, 175);
        ReleaseKeyListener listener = e -> {
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                inputReady.set(true);
            }
        };
        outputPane.addKeyListener(listener);
        return outputPane;
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
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                inputReady.set(false);
                RuntimeContext context = LexAnalyzer.setupRuntimeContext(codePane.getText());
                outputPane.setText("");
                if(devModeCheckBox.isSelected()) {
                    writeColoredText(outputPane, "Структура программы", SKY_BLUE);
                    context.formations().forEach(fm -> writeColoredText(outputPane, fm.toString(), SAKURA_SNOW));
                    runAndThen(context, () -> {
                        writeColoredText(outputPane, "\n\nПОЛИЗ найденных арифметических выражений\n", SKY_BLUE);
                        context.forEachRpn(rpn -> writeColoredText(outputPane, rpn + "\n", ARCTIC_GRASS));
                    });
                }
                else {
                    runAndThen(context, () -> { });
                }
                context.uiHandlers().forEach(handler -> handler.accept(UIFrame.this));
            }

        });
        return button;
    }

    private void runAndThen(RuntimeContext context, Runnable action) {
        if(context.runWithNoErrors()) {
            action.run();
        }
        else {
            context.errors().forEach((unit, message) -> {
                String outputLine = Optional.ofNullable(unit)
                        .map(context::computePosition)
                        .map(pos -> String.format("\nСтрока %d, cтолбец %d: %s",
                                pos.line(), pos.column() , message))
                        .orElse(message);
                writeColoredText(outputPane, outputLine, FIRE);
            });
        }
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

        add(wrapIntoScrollable(codePane));
        add(wrapIntoScrollable(outputPane));
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


    public void writeColoredText(JTextPane pane, String fragment, Color color) {
        StyledDocument doc = pane.getStyledDocument();
        Style style = pane.addStyle("", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), fragment, style);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void highlightAll() {
        int caretPos = codePane.getCaretPosition();
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
                                    writeColoredText(codePane, text, onFire ? FIRE : unit.type().highlight());
                            });
                });
        codePane.setCaretPosition(cursor.get() - TYPE_RATE > caretPos ? cursor.get() : caretPos);
    }

}
