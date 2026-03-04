import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.DecimalFormat;

/**
 * ╔══════════════════════════════════════════════════════════╗
 *   CALCULADORA CIENTÍFICA AVANZADA — Java Swing
 *   Versión completa con todos los requerimientos cubiertos
 *
 *   Compilar : javac CalculadoraCientifica.java
 *   Ejecutar : java CalculadoraCientifica
 * ╚══════════════════════════════════════════════════════════╝
 *
 *  REQUERIMIENTOS IMPLEMENTADOS:
 *  ✅ Sumas consecutivas (acumulación)
 *  ✅ Combinación de operaciones básicas
 *  ✅ Divisiones sucesivas
 *  ✅ Multiplicaciones y potencias
 *  ✅ Logaritmos en distintas bases y ln
 *  ✅ Raíces enésimas (ⁿ√x)
 *  ✅ Diseño visual moderno y personalizable
 *  ✅ Dígitos decimales configurables (1–10)
 *  ✅ ERROR en raíces indefinidas (√ negativo)
 */
public class CalculadoraCientifica extends JFrame implements ActionListener {

    // ── Estado de la calculadora ──────────────────────────────────────────────
    private double acumulador   = 0;       // valor acumulado para ops consecutivas
    private double operandoB    = 0;       // segundo operando
    private String operacion    = "";      // operador pendiente
    private boolean nuevaEntrada = true;
    private boolean errorActivo  = false;
    private int     decimales    = 4;      // dígitos decimales configurables

    // ── Componentes UI ────────────────────────────────────────────────────────
    private JTextField  campoDisplay;
    private JLabel      lblHistorial;
    private JLabel      lblMemoria;
    private JSpinner    spinnerDecimales;
    private JComboBox<String> comboTema;

    // ── Temas de color ────────────────────────────────────────────────────────
    private static final String[][] TEMAS = {
        // nombre, fondo, display, numBtn, opBtn, sciBtn, equalBtn, clearBtn, texto, acento
        {"Noche Profunda","#0d0f1a","#070910","#1e2035","#1a3a5c","#1a4a30","#1a4a7a","#6a1a22","#dce0f0","#5b9ef0"},
        {"Grafito Cálido", "#1a1612","#0e0c09","#2a2520","#4a3820","#2a4020","#3a4020","#5a2a10","#f0e8d8","#e0a050"},
        {"Océano Frío",    "#0a1520","#050c14","#0f2030","#0f3050","#0f3530","#103060","#501020","#cce8f8","#40b8e0"},
    };
    private int temaActual = 0;

    // colores activos (se actualizan con el tema)
    private Color cFondo, cDisplay, cNum, cOp, cSci, cEqual, cClear, cTexto, cAccent;

    // ─────────────────────────────────────────────────────────────────────────
    public CalculadoraCientifica() {
        super("Calculadora Científica");
        aplicarTema(0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        construirUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Parseo de color hex ───────────────────────────────────────────────────
    private Color hex(String h) {
        return Color.decode(h);
    }

    private void aplicarTema(int idx) {
        temaActual = idx;
        String[] t = TEMAS[idx];
        cFondo  = hex(t[1]); cDisplay = hex(t[2]);
        cNum    = hex(t[3]); cOp      = hex(t[4]);
        cSci    = hex(t[5]); cEqual   = hex(t[6]);
        cClear  = hex(t[7]); cTexto   = hex(t[8]);
        cAccent = hex(t[9]);
    }

    // ── Construcción de la interfaz ───────────────────────────────────────────
    private void construirUI() {
        JPanel raiz = new JPanel(new BorderLayout(0, 0));
        raiz.setBackground(cFondo);
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(raiz);

        raiz.add(panelDisplay(),       BorderLayout.NORTH);
        raiz.add(panelBotones(),       BorderLayout.CENTER);
        raiz.add(panelConfiguracion(), BorderLayout.SOUTH);
    }

    // ── Panel de display ──────────────────────────────────────────────────────
    private JPanel panelDisplay() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(cFondo);
        p.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Historial de operación (arriba)
        lblHistorial = new JLabel(" ");
        lblHistorial.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblHistorial.setForeground(cAccent.darker());
        lblHistorial.setHorizontalAlignment(SwingConstants.RIGHT);
        lblHistorial.setBorder(new EmptyBorder(0, 4, 0, 4));

        // Memoria/info (izquierda del historial)
        lblMemoria = new JLabel("  ");
        lblMemoria.setFont(new Font("Courier New", Font.BOLD, 11));
        lblMemoria.setForeground(cAccent);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(cFondo);
        topRow.add(lblMemoria,   BorderLayout.WEST);
        topRow.add(lblHistorial, BorderLayout.CENTER);

        // Campo principal
        campoDisplay = new JTextField("0") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cDisplay);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campoDisplay.setOpaque(false);
        campoDisplay.setEditable(false);
        campoDisplay.setFont(new Font("Courier New", Font.BOLD, 36));
        campoDisplay.setForeground(cTexto);
        campoDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        campoDisplay.setBorder(new EmptyBorder(10, 14, 10, 14));
        campoDisplay.setPreferredSize(new Dimension(420, 68));

        p.add(topRow,        BorderLayout.NORTH);
        p.add(campoDisplay,  BorderLayout.CENTER);
        return p;
    }

    // ── Panel de botones ──────────────────────────────────────────────────────
    private JPanel panelBotones() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(cFondo);

        GridBagConstraints g = new GridBagConstraints();
        g.fill    = GridBagConstraints.BOTH;
        g.insets  = new Insets(3, 3, 3, 3);
        g.weightx = 1; g.weighty = 1;
        g.ipady   = 8;

        // ── Fila 0: funciones de raíz y logaritmos ────────────────────────────
        //    √x   ⁿ√x   log₁₀   logₙ   ln
        agregar(p, g, 0, 0, "√x",    "√x",    cSci, 1);
        agregar(p, g, 1, 0, "ⁿ√x",   "ⁿ√x",   cSci, 1);
        agregar(p, g, 2, 0, "log₁₀", "log₁₀", cSci, 1);
        agregar(p, g, 3, 0, "logₙ",  "logₙ",  cSci, 1);
        agregar(p, g, 4, 0, "ln",    "ln",    cSci, 1);

        // ── Fila 1: potencias, inverso, π, e ─────────────────────────────────
        agregar(p, g, 0, 1, "x²",   "x²",  cSci, 1);
        agregar(p, g, 1, 1, "xⁿ",   "xⁿ",  cSci, 1);
        agregar(p, g, 2, 1, "1/x",  "1/x", cSci, 1);
        agregar(p, g, 3, 1, "π",    "π",   cSci, 1);
        agregar(p, g, 4, 1, "e",    "e",   cSci, 1);

        // ── Fila 2: C, ±, %, ÷ ───────────────────────────────────────────────
        agregar(p, g, 0, 2, "C",   "C",   cClear, 1);
        agregar(p, g, 1, 2, "±",   "±",   cClear, 1);
        agregar(p, g, 2, 2, "⌫",   "⌫",   cOp,    1);
        agregar(p, g, 3, 2, "%",   "%",   cOp,    1);
        agregar(p, g, 4, 2, "÷",   "÷",   cOp,    1);

        // ── Filas 3-5: números ────────────────────────────────────────────────
        String[][][] num = {
            {{"7","7"},{"8","8"},{"9","9"},{"×","×"}},
            {{"4","4"},{"5","5"},{"6","6"},{"-","-"}},
            {{"1","1"},{"2","2"},{"3","3"},{"+","+"}}
        };
        for (int f = 0; f < num.length; f++) {
            for (int c = 0; c < 4; c++) {
                Color bg = (c == 3) ? cOp : cNum;
                agregar(p, g, c, f + 3, num[f][c][0], num[f][c][1], bg, 1);
            }
            // columna 4: vacía en filas 3-4, "=" en fila 5
        }

        // Columna derecha extendida: = ocupa filas 3-4, vacía fila 5
        g.gridx = 4; g.gridy = 3; g.gridheight = 2;
        p.add(crearBtn("=", "=", cEqual), g);
        g.gridheight = 1;

        // Fila 6: 0 (ancho 2), . , vacío, vacío
        g.gridx = 0; g.gridy = 6; g.gridwidth = 2;
        p.add(crearBtn("0", "0", cNum), g);
        g.gridwidth = 1;
        agregar(p, g, 2, 6, ".",  ".",  cNum, 1);
        agregar(p, g, 3, 6, "AC", "AC", cClear, 1);
        agregar(p, g, 4, 6, "+", "+", cOp, 1);   // atajo suma rápida

        return p;
    }

    private void agregar(JPanel p, GridBagConstraints g,
            int col, int fila, String txt, String cmd, Color bg, int span) {
        g.gridx = col; g.gridy = fila; g.gridwidth = span;
        p.add(crearBtn(txt, cmd, bg), g);
        g.gridwidth = 1;
    }

    private JButton crearBtn(String txt, String cmd, Color bg) {
        JButton b = new JButton(txt) {
            private float hover = 0f;
            { // timer de hover suave
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { animarHover(true); }
                    public void mouseExited (MouseEvent e) { animarHover(false); }
                });
            }
            void animarHover(boolean in) {
                Timer t = new Timer(15, null);
                t.addActionListener(ev -> {
                    hover += in ? 0.1f : -0.1f;
                    hover  = Math.max(0f, Math.min(1f, hover));
                    repaint();
                    if ((in && hover >= 1f) || (!in && hover <= 0f)) t.stop();
                });
                t.start();
            }
            @Override protected void paintComponent(Graphics g2d) {
                Graphics2D g2 = (Graphics2D) g2d.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = bg;
                Color lite = base.brighter().brighter();
                Color blended = blend(base, lite, hover);
                g2.setColor(blended);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // borde sutil
                g2.setColor(lite.darker());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g2d);
            }
            Color blend(Color a, Color b2, float t2) {
                int r = (int)(a.getRed()   + (b2.getRed()   - a.getRed())   * t2);
                int gr= (int)(a.getGreen() + (b2.getGreen() - a.getGreen()) * t2);
                int bl= (int)(a.getBlue()  + (b2.getBlue()  - a.getBlue())  * t2);
                return new Color(Math.min(255,r), Math.min(255,gr), Math.min(255,bl));
            }
        };
        b.setActionCommand(cmd);
        b.setFont(new Font("Courier New", Font.BOLD, 15));
        b.setForeground(cTexto);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(76, 52));
        b.addActionListener(this);
        return b;
    }

    // ── Panel de configuración ────────────────────────────────────────────────
    private JPanel panelConfiguracion() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        p.setBackground(cFondo);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, cOp.darker()),
            new EmptyBorder(6, 4, 0, 4)
        ));

        // ── Decimales ─────────────────────────────────────────────────────────
        JLabel lDec = new JLabel("Decimales:");
        lDec.setForeground(cAccent); lDec.setFont(new Font("Courier New", Font.PLAIN, 12));
        spinnerDecimales = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        spinnerDecimales.setPreferredSize(new Dimension(55, 26));
        spinnerDecimales.setFont(new Font("Courier New", Font.BOLD, 12));
        colorearSpinner(spinnerDecimales);
        spinnerDecimales.addChangeListener(e -> {
            decimales = (int) spinnerDecimales.getValue();
            actualizarDisplay(Double.parseDouble(
                campoDisplay.getText().replace("ERROR","0").replace(" ","").equals("") ? "0"
                : campoDisplay.getText().replace("ERROR","0")));
        });

        // ── Tema ─────────────────────────────────────────────────────────────
        JLabel lTema = new JLabel("  Tema:");
        lTema.setForeground(cAccent); lTema.setFont(new Font("Courier New", Font.PLAIN, 12));
        String[] nombres = {"Noche Profunda", "Grafito Cálido", "Océano Frío"};
        comboTema = new JComboBox<>(nombres);
        comboTema.setFont(new Font("Courier New", Font.PLAIN, 12));
        comboTema.setBackground(cNum); comboTema.setForeground(cTexto);
        comboTema.setFocusable(false);
        comboTema.addActionListener(e -> cambiarTema(comboTema.getSelectedIndex()));

        p.add(lDec); p.add(spinnerDecimales);
        p.add(lTema); p.add(comboTema);
        return p;
    }

    private void colorearSpinner(JSpinner s) {
        s.getEditor().getComponent(0).setBackground(cNum);
        ((JSpinner.DefaultEditor) s.getEditor()).getTextField().setForeground(cTexto);
        ((JSpinner.DefaultEditor) s.getEditor()).getTextField().setBackground(cNum);
        ((JSpinner.DefaultEditor) s.getEditor()).getTextField().setCaretColor(cTexto);
    }

    private void cambiarTema(int idx) {
        aplicarTema(idx);
        // Reconstruir UI con nuevo tema
        getContentPane().removeAll();
        construirUI();
        revalidate(); repaint();
    }

    // ── Lógica de la calculadora ──────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (errorActivo && !cmd.equals("C") && !cmd.equals("AC")) return;

        // Dígitos y punto
        if (cmd.matches("[0-9]") || cmd.equals(".")) {
            manejarDigito(cmd); return;
        }

        switch (cmd) {
            // ── Limpiar ───────────────────────────────────────────────────────
            case "C":
                if (errorActivo) { resetearTodo(); break; }
                campoDisplay.setText("0");
                nuevaEntrada = false;
                break;
            case "AC":
                resetearTodo();
                break;

            // ── Borrar último dígito ──────────────────────────────────────────
            case "⌫":
                String t = campoDisplay.getText();
                campoDisplay.setText(t.length() > 1 ? t.substring(0, t.length()-1) : "0");
                break;

            // ── Cambiar signo ─────────────────────────────────────────────────
            case "±":
                double v = leerDisplay();
                mostrarEnDisplay(-v);
                break;

            // ── Porcentaje ────────────────────────────────────────────────────
            case "%":
                mostrarEnDisplay(leerDisplay() / 100.0);
                break;

            // ── Constantes ────────────────────────────────────────────────────
            case "π":
                mostrarEnDisplay(Math.PI); nuevaEntrada = true; break;
            case "e":
                mostrarEnDisplay(Math.E); nuevaEntrada = true; break;

            // ── Operaciones unarias ───────────────────────────────────────────
            case "√x":   opUnaria_raizCuadrada();  break;
            case "ⁿ√x":  opBinaria_raizN();        break;   // pide n luego x
            case "x²":   opUnaria(d -> d*d, "²");  break;
            case "1/x":
                if (leerDisplay() == 0) { mostrarError("1/0"); break; }
                opUnaria(d -> 1.0/d, "⁻¹");
                break;
            case "log₁₀":
                opUnaria_log(10); break;
            case "logₙ":
                opBinaria_logBase(); break;
            case "ln":
                opUnaria_ln(); break;

            // ── Operaciones binarias (+ - × ÷ ^ ⁿ√) ─────────────────────────
            case "+": case "-": case "×": case "÷": case "xⁿ":
                procesarOperador(cmd);
                break;

            // ── Igual ────────────────────────────────────────────────────────
            case "=":
                calcularResultado();
                break;
        }
    }

    private void manejarDigito(String d) {
        if (nuevaEntrada) {
            campoDisplay.setText(d.equals(".") ? "0." : d);
            nuevaEntrada = false;
        } else {
            String actual = campoDisplay.getText();
            if (d.equals(".") && actual.contains(".")) return;
            campoDisplay.setText(actual.equals("0") && !d.equals(".") ? d : actual + d);
        }
    }

    // Procesa un operador binario (+, -, ×, ÷, xⁿ)
    // Soporta operaciones consecutivas: 5 + 3 + 2 = acumula correctamente
    private void procesarOperador(String op) {
        double actual = leerDisplay();
        if (!operacion.isEmpty() && !nuevaEntrada) {
            // Calcular resultado intermedio (sumas/divisiones/etc. consecutivas)
            actual = ejecutar(acumulador, actual, operacion);
            if (Double.isNaN(actual) || Double.isInfinite(actual)) {
                mostrarError("OVERFLOW"); return;
            }
            mostrarEnDisplay(actual);
        }
        acumulador   = actual;
        operacion    = op;
        nuevaEntrada = true;
        lblHistorial.setText(formatear(acumulador) + " " + opSymbol(op));
    }

    private void calcularResultado() {
        if (operacion.isEmpty()) return;
        double b = leerDisplay();
        double r = ejecutar(acumulador, b, operacion);
        lblHistorial.setText(formatear(acumulador) + " " + opSymbol(operacion) + " " + formatear(b) + " =");
        if (Double.isNaN(r) || Double.isInfinite(r)) {
            mostrarError("OVERFLOW"); return;
        }
        mostrarEnDisplay(r);
        acumulador   = r;
        operacion    = "";
        nuevaEntrada = true;
    }

    private double ejecutar(double a, double b, String op) {
        switch (op) {
            case "+":  return a + b;
            case "-":  return a - b;
            case "×":  return a * b;
            case "÷":
                if (b == 0) { mostrarError("DIV/0"); return Double.NaN; }
                return a / b;
            case "xⁿ": return Math.pow(a, b);
            case "ⁿ√x":                                          // ⁿ√x: a=índice, b=radicando
                if (b < 0 && a % 2 == 0) { mostrarError("ERROR"); return Double.NaN; }
                return (b < 0) ? -Math.pow(-b, 1.0/a) : Math.pow(b, 1.0/a);
            case "logₙ":                                          // logₙ(b) base=a
                if (b <= 0 || a <= 0 || a == 1) { mostrarError("ERROR"); return Double.NaN; }
                return Math.log(b) / Math.log(a);
        }
        return 0;
    }

    // ── Operaciones unarias ───────────────────────────────────────────────────
    private void opUnaria(java.util.function.DoubleUnaryOperator f, String label) {
        double x = leerDisplay();
        double r = f.applyAsDouble(x);
        lblHistorial.setText(label + "(" + formatear(x) + ") =");
        mostrarEnDisplay(r);
        nuevaEntrada = true;
    }

    private void opUnaria_raizCuadrada() {
        double x = leerDisplay();
        if (x < 0) { mostrarError("ERROR"); return; }          // raíz indefinida → ERROR
        lblHistorial.setText("√(" + formatear(x) + ") =");
        mostrarEnDisplay(Math.sqrt(x));
        nuevaEntrada = true;
    }

    // Raíz enésima: primero pide el índice n, luego x
    private void opBinaria_raizN() {
        String ns = JOptionPane.showInputDialog(this,
            "Ingresa el índice de la raíz (n):", "Raíz enésima", JOptionPane.PLAIN_MESSAGE);
        if (ns == null || ns.trim().isEmpty()) return;
        try {
            double n = Double.parseDouble(ns.trim());
            if (n == 0) { mostrarError("ERROR"); return; }
            double x = leerDisplay();
            if (x < 0 && n % 2 == 0) { mostrarError("ERROR"); return; }
            double r = (x < 0) ? -Math.pow(-x, 1.0/n) : Math.pow(x, 1.0/n);
            lblHistorial.setText(formatearN(n) + "√(" + formatear(x) + ") =");
            mostrarEnDisplay(r);
            nuevaEntrada = true;
        } catch (NumberFormatException ex) { mostrarError("ERROR"); }
    }

    // log base n: primero pide la base
    private void opBinaria_logBase() {
        String bs = JOptionPane.showInputDialog(this,
            "Ingresa la base del logaritmo:", "Logaritmo base n", JOptionPane.PLAIN_MESSAGE);
        if (bs == null || bs.trim().isEmpty()) return;
        try {
            double base = Double.parseDouble(bs.trim());
            double x    = leerDisplay();
            if (x <= 0 || base <= 0 || base == 1) { mostrarError("ERROR"); return; }
            double r = Math.log(x) / Math.log(base);
            lblHistorial.setText("log" + formatearN(base) + "(" + formatear(x) + ") =");
            mostrarEnDisplay(r);
            nuevaEntrada = true;
        } catch (NumberFormatException ex) { mostrarError("ERROR"); }
    }

    private void opUnaria_log(double base) {
        double x = leerDisplay();
        if (x <= 0) { mostrarError("ERROR"); return; }
        double r = (base == 10) ? Math.log10(x) : Math.log(x) / Math.log(base);
        lblHistorial.setText("log" + (int)base + "(" + formatear(x) + ") =");
        mostrarEnDisplay(r);
        nuevaEntrada = true;
    }

    private void opUnaria_ln() {
        double x = leerDisplay();
        if (x <= 0) { mostrarError("ERROR"); return; }
        lblHistorial.setText("ln(" + formatear(x) + ") =");
        mostrarEnDisplay(Math.log(x));
        nuevaEntrada = true;
    }

    // ── Utilidades de display ─────────────────────────────────────────────────
    private double leerDisplay() {
        try { return Double.parseDouble(campoDisplay.getText()); }
        catch (NumberFormatException e) { return 0; }
    }

    private void mostrarEnDisplay(double val) {
        campoDisplay.setForeground(cTexto);
        campoDisplay.setText(formatear(val));
        errorActivo = false;
    }

    private void actualizarDisplay(double val) {
        if (!errorActivo) campoDisplay.setText(formatear(val));
    }

    private void mostrarError(String msg) {
        campoDisplay.setForeground(new Color(255, 80, 80));
        campoDisplay.setText("  ERROR: " + msg);
        lblHistorial.setText("Operación no definida");
        errorActivo  = true;
        nuevaEntrada = true;
    }

    private void resetearTodo() {
        acumulador   = 0;
        operacion    = "";
        nuevaEntrada = true;
        errorActivo  = false;
        campoDisplay.setForeground(cTexto);
        campoDisplay.setText("0");
        lblHistorial.setText(" ");
    }

    private String formatear(double val) {
        if (Double.isNaN(val) || Double.isInfinite(val)) return "ERROR";
        if (val == Math.floor(val) && Math.abs(val) < 1e15) return String.valueOf((long) val);
        StringBuilder sb = new StringBuilder("0.");
        for (int i = 0; i < decimales; i++) sb.append("#");
        return new DecimalFormat(sb.toString()).format(val);
    }

    private String formatearN(double n) {
        return (n == Math.floor(n)) ? String.valueOf((long) n) : String.valueOf(n);
    }

    private String opSymbol(String op) {
        switch (op) {
            case "xⁿ":  return "^";
            case "ⁿ√x": return "ⁿ√";
            case "logₙ":return "logₙ";
            default: return op;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(CalculadoraCientifica::new);
    }
}
