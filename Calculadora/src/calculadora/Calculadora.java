package calculadora;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculadora {

    private JFrame frame;
    private JTextField display;
    private StringBuilder currentInput;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculadora::new);
    }

    public Calculadora() {
        frame = new JFrame("Calculadora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        currentInput = new StringBuilder();

        display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setEditable(false);
        frame.add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4)); // aumentei a grade para 5 linhas

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C" // botão de limpar na última linha
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        // Para completar o grid 5x4 (20 posições), adiciona 3 botões vazios
        for (int i = 0; i < 3; i++) {
            panel.add(new JLabel("")); // espaço vazio para layout ficar certinho
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("=")) {
                try {
                    String result = evaluateExpression(currentInput.toString());
                    display.setText(result);
                    currentInput.setLength(0);
                    currentInput.append(result);
                } catch (Exception ex) {
                    display.setText("Erro");
                    currentInput.setLength(0);
                }
            } else if (command.equals("C")) {
                currentInput.setLength(0);
                display.setText("");
            } else {
                currentInput.append(command);
                display.setText(currentInput.toString());
            }
        }
    }

    private String evaluateExpression(String expression) {
        try {
            double result = new Object() {
                int pos = -1;
                char currentChar;

                void nextChar() {
                    currentChar = (++pos < expression.length()) ? expression.charAt(pos) : (char) -1;
                }

                boolean eat(char charToEat) {
                    while (currentChar == ' ') nextChar();
                    if (currentChar == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) throw new RuntimeException("Erro de sintaxe");
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if (eat('+')) x += parseTerm();
                        else if (eat('-')) x -= parseTerm();
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if (eat('*')) x *= parseFactor();
                        else if (eat('/')) x /= parseFactor();
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor();
                    if (eat('-')) return -parseFactor();

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) {
                        x = parseExpression();
                        eat(')');
                    } else if ((currentChar >= '0' && currentChar <= '9') || currentChar == '.') {
                        while ((currentChar >= '0' && currentChar <= '9') || currentChar == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new RuntimeException("Erro de sintaxe");
                    }

                    return x;
                }
            }.parse();

            return String.valueOf(result);
        } catch (Exception e) {
            return "Erro";
        }
    }
}
