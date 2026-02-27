import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 *  CALCULADORA CIENTÍFICA - VERSIÓN CON ERRORES PARA PRACTICAR
 *  Encuentra y corrige los 10 errores ocultos en este código.
 *  Los errores están marcados con: // ❌ ERROR #N
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * Compilar : javac CalculadoraErrores.java
 * Ejecutar : java CalculadoraErrores
 */
public class CalculadoraErrores extends JFrame implements ActionListener {

    private JTextField pantalla;
    private JLabel     lblOperacion;

    private double primerNumero  = 0;
    private double segundoNumero = 0;
    private String operacion     = "";
    private boolean nuevaEntrada = True;              // ❌ ERROR #1

    private final Color BG_PRINCIPAL = new Color(18, 18, 24);
    private final Color BG_DISPLAY   = new Color(10, 10, 16);
    private final Color BG_BTN_NUM   = new Color(38, 38, 52);
    private final Color BG_BTN_OP    = new Color(52, 82, 128);
    private final Color BG_BTN_SCI   = new Color(52, 90, 72);
    private final Color BG_BTN_IGUAL = new Color(60, 140, 220);
    private final Color BG_BTN_CLEAR = new Color(160, 50, 60);
    private final Color TEXT_COLOR   = new Color(230, 230, 245);
    private final Color ACCENT       = new Color(100, 180, 255);

    private final Font FONT_DISPLAY = new Font("Monospaced", Font.BOLD, 32);
    private final Font FONT_LABEL   = new Font("SansSerif",  Font.PLAIN, 12);
    private final Font FONT_BTN     = new Font("SansSerif",  Font.BOLD, 16);
    private final Font FONT_BTN_SCI = new Font("SansSerif",  Font.BOLD, 13);

    public CalculadoraErrores() {
        super("Calculadora Científica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG_PRINCIPAL);
        setLayout(new BorderLayout(10, 10));

        construirDisplay();
        construirBotones();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void construirDisplay() {
        JPanel panelDisplay = new JPanel(new BorderLayout(4, 4));
        panelDisplay.setBackground(BG_PRINCIPAL);
        panelDisplay.setBorder(new EmptyBorder(14, 14, 4, 14));

        lblOperacion = new JLabel(" ");
        lblOperacion.setFont(FONT_LABEL);
        lblOperacion.setForeground(ACCENT);
        lblOperacion.setHorizontalAlignment(SwingConstants.RIGHT);

        pantalla = new JTextField("0");
        pantalla.setEditable(false);
        pantalla.setFont(FONT_DISPLAY);
        pantalla.setBackground(BG_DISPLAY);
        pantalla.setForeground(TEXT_COLOR);
        pantalla.setHorizontalAlignment(SwingConstants.RIGHT);
        pantalla.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));

        panelDisplay.add(lblOperacion, BorderLayout.NORTH);
        panelDisplay.add(pantalla,     BorderLayout.CENTER);
        add(panelDisplay, BorderLayout.NORTH);
    }

    private void construirBotones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_PRINCIPAL);
        panel.setBorder(new EmptyBorder(4, 14, 14, 14));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.insets  = new Insets(4, 4, 4, 4);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.ipady   = 10;

        // Fila 0: científicos
        String[][] sci = {
            {"log₁₀","log₁₀"}, {"ln","ln"},
            {"xⁿ","^"},        {"√x","√"}
        };
        for (int i = 0; i < sci.length; i++) {
            gbc.gridx = i; gbc.gridy = 0;
            panel.add(crearBoton(sci[i][0], sci[i][1], BG_BTN_SCI, FONT_BTN_SCI), gbc);
        }

        agregarFila(panel, gbc, 1,
            new String[]{"C","C"}, new String[]{"±","±"},
            new String[]{"⌫","⌫"}, new String[]{"÷","÷"},
            BG_BTN_CLEAR, BG_BTN_CLEAR, BG_BTN_OP, BG_BTN_OP);

        String[][][] filas = {
            {{"7","7"},{"8","8"},{"9","9"},{"×","×"}},
            {{"4","4"},{"5","5"},{"6","6"},{"-","-"}},
            {{"1","1"},{"2","2"},{"3","3"},{"+","+"}}
        };
        for (int f = 0; f < filas.length; f++) {
            for (int c = 0; c < 4; c++) {
                gbc.gridx = c; gbc.gridy = f + 2;
                Color bg = (c == 3) ? BG_BTN_OP : BG_BTN_NUM;
                panel.add(crearBoton(filas[f][c][0], filas[f][c][1], bg, FONT_BTN), gbc);
            }
        }

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(crearBoton("0", "0", BG_BTN_NUM, FONT_BTN), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 2; panel.add(crearBoton(".", ".", BG_BTN_NUM, FONT_BTN), gbc);
        gbc.gridx = 3; panel.add(crearBoton("=", "=", BG_BTN_IGUAL, FONT_BTN), gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void agregarFila(JPanel p, GridBagConstraints g, int fila,
            String[] b0, String[] b1, String[] b2, String[] b3,
            Color c0, Color c1, Color c2, Color c3) {
        String[][] bs = {b0, b1, b2, b3};
        Color[]    cs = {c0, c1, c2, c3};
        for (int i = 0; i < 4; i++) {
            g.gridx = i; g.gridy = fila;
            p.add(crearBoton(bs[i][0], bs[i][1], cs[i], FONT_BTN), g);
        }
    }

    private JButton crearBoton(String texto, String accion, Color bg, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(accion);
        btn.setFont(fuente);
        btn.setBackground(bg);
        btn.setForeground(TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.brighter(), 1),
            new EmptyBorder(6, 6, 6, 6)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            Color original = bg;
            public void mouseEntered(MouseEvent e) { btn.setBackground(original.brighter()); }
            public void mouseExited (MouseEvent e) { btn.setBackground(original); }
        });
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.matches("[0-9]") || cmd.equals(".")) {
            if (nuevaEntrada) {
                pantalla.setText(cmd.equals(".") ? "0." : cmd);
                nuevaEntrada = false;
            } else {
                String actual = pantalla.getText();
                if (cmd.equals(".") && actual.contains(".")) return;
                pantalla.setText(actual.equals("0") && !cmd.equals(".") ? cmd : actual + cmd);
            }
            return;
        }

        switch (cmd) {
            case "C":
                resetear();
                break;

            case "±":
                double val = Double.parseDouble(pantalla.getText());
                pantalla.setText(formatear(-val));
                break;

            case "⌫":
                String txt = pantalla.getText();
                pantalla.setText(txt.length() > 1 ? txt.substring(0, txt.length() - 1) : "0");
                break;

            case "+": case "-": case "×": case "÷": case "^":
                primerNumero = Double.parseDouble(pantalla.getText());
                operacion    = cmd;
                lblOperacion.setText(formatear(primerNumero) + " " + cmd);
                nuevaEntrada = true;
                break;

            case "√":
                double n = Double.parseDouble(pantalla.getText());
                if (n < 0) { mostrarError("√ de número negativo"); return; }
                pantalla.setText(formatear(Math.sqrt(n)));
                lblOperacion.setText("√(" + formatear(n) + ")");
                nuevaEntrada = true;
                break;

            case "log₁₀":
                double nl = Double.parseDouble(pantalla.getText());
                if (nl <= 0) { mostrarError("log de número ≤ 0"); return; }
                pantalla.setText(formatear(Math.log(nl)));             // ❌ ERROR #2
                lblOperacion.setText("log₁₀(" + formatear(nl) + ")");
                nuevaEntrada = true;
                break;

            case "ln":
                double nln = Double.parseDouble(pantalla.getText());
                if (nln <= 0) { mostrarError("ln de número ≤ 0"); return; }
                pantalla.setText(formatear(Math.log(nln)));
                lblOperacion.setText("ln(" + formatear(nln) + ")");
                nuevaEntrada = true;
                break;

            case "=":
                if (operacion.isEmpty()) return;
                segundoNumero = Double.parseDouble(pantalla.getText());
                double resultado = calcular(primerNumero, segundoNumero, operacion);
                lblOperacion.setText(formatear(primerNumero) + " " + operacion
                    + " " + formatear(segundoNumero) + " =");
                pantalla.setText(formatear(resultado));
                operacion    = "";
                nuevaEntrada = false;                                   // ❌ ERROR #3
                break;
        }
    }

    private double calcular(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "×": return a * b;
            case "÷":
                if (b = 0) {                                           // ❌ ERROR #4
                    mostrarError("División por cero");
                    return 0;
                }
                return a / b;
            case "^": return Math.pow(b, a);                           // ❌ ERROR #5
        }
        return 0;
    }

    private String formatear(double num) {
        if (num == (long) num) return String.valueOf((long) num);
        // Redondea a 6 decimales y elimina ceros finales
        return String.format("%.6f", num).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private void resetear() {
        pantalla.setText("0");
        lblOperacion.setText(" ");
        primerNumero  = 0;
        operacion     = "";
        nuevaEntrada  = true;
    }

    private void mostrarError(String msg) {
        pantalla.setText("Error");
        lblOperacion.setText(msg);
        nuevaEntrada = false;                                           // ❌ ERROR #6
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // ❌ ERROR #7: falta SwingUtilities.invokeLater para seguridad de hilos
        new CalculadoraErrores();
    }

    // ❌ ERROR #8: clase interna para manejar el punto decimal — lógica invertida
    private boolean validarPunto(String texto) {
        return texto.contains(".");   // debería retornar TRUE si NO contiene punto (está al revés)
    }

    // ❌ ERROR #9: método nunca llamado — debería usarse en case "±"
    private double cambiarSigno(double numero) {
        return numero + (-numero * 2);  // lógica incorrecta, debería ser: return -numero;
    }

    // ❌ ERROR #10: comparación de Strings con == en lugar de .equals()
    private boolean esOperador(String cmd) {
        return cmd == "+" || cmd == "-" || cmd == "×" || cmd == "÷" || cmd == "^";
    }
}
