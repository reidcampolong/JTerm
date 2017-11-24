package jterm.gui;

import jterm.JTerm;
import jterm.io.InputHandler;
import jterm.io.Keys;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class Terminal extends JFrame implements KeyListener {
    private JPanel contentPane;
    private JTextPane textPane;
    private AttributeSet asWhite;
    private AttributeSet asOffWhite;
    private ProtectedTextComponent ptc;

    public Terminal() {
        setContentPane(contentPane);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        textPane.setBackground(new Color(28, 28, 28));

        StyleContext sc = StyleContext.getDefaultStyleContext();
        //Color 1 - White
        asWhite = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(255, 255, 255));
        asWhite = sc.addAttribute(asWhite, StyleConstants.FontFamily, "Lucida Console");
        asWhite = sc.addAttribute(asWhite, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        sc = StyleContext.getDefaultStyleContext();
        //Color 2 - Off White
        asOffWhite = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(130, 130, 130));
        asOffWhite = sc.addAttribute(asOffWhite, StyleConstants.FontFamily, "Lucida Console");
        asOffWhite = sc.addAttribute(asOffWhite, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        textPane.setEditable(true);
        textPane.addKeyListener(this);
        ptc = new ProtectedTextComponent(textPane);
        println(JTerm.LICENSE, false);
        showPrompt();
        //overrideEnter();
        JTerm.IS_WIN = false;
        JTerm.IS_UNIX = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Consume under certain conditions
        switch (e.getKeyCode()) {
            //These keys need to be handled by InputHandler only
            case KeyEvent.VK_TAB:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                e.consume();
                break;
            //Consume without processing shift and capslock keys
            //These keys are modifiers. We can ignore them
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_CAPS_LOCK:
                e.consume();
                return;
        }
        if ((int) e.getKeyChar() == 65535) {
            //An arrow key was pressed. Switch the key code into the negatives so it wont interfere with any real chars
            new Thread(() -> InputHandler.process(Keys.getKeyByValue(e.getKeyCode() * -1), e.getKeyChar())).start();
        } else
            new Thread(() -> InputHandler.process(Keys.getKeyByValue((int) e.getKeyChar()), e.getKeyChar())).start();
    }

    private void onCancel() {
        dispose();
        System.exit(0);
    }

    public void showPrompt() {
        String prompt = JTerm.PROMPT;
        print(prompt, true);
        print("", true);
        int promptIndex = textPane.getDocument().getLength();
        textPane.setCaretPosition(promptIndex);
        ptc.protectText(0, promptIndex - 1);
    }

    public void clear() {
        ptc.clearProtections();
        textPane.setText("");
    }

    public void clearLine(String line, boolean clearPrompt) {
        if (clearPrompt) ptc.clearProtections();
        String text = textPane.getText().replaceAll("\r", "");
        int ix = text.lastIndexOf("\n") + 1;
        int len = line.length();
        if (clearPrompt) len += 3;
        else ix += 3;
        if (ix >= text.length()) return;
        try {
            textPane.getDocument().remove(ix, len);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void print(String s, boolean white) {
        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(white ? asWhite : asOffWhite, false);
        textPane.replaceSelection(s);
    }

    public void println(String s, boolean white) {
        print(s + "\n", white);
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textPane = new JTextPane();
        scrollPane1.setViewportView(textPane);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
