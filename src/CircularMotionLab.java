/*
 * Circular Motion Lab — Non-uniform circular motion simulator (Java Swing port).
 * Single file, no dependencies. Requires Java 11+.
 *
 *   javac CircularMotionLab.java
 *   java CircularMotionLab
 *
 * Physics: constant angular acceleration α.
 *   θ(t+dt) = θ + ω dt + ½ α dt²     ω(t+dt) = ω + α dt   (exact for constant α)
 *   v = r|ω|,  a_t = rα (signed along t̂),  a_c = rω² (toward center),  |a| = √(a_t² + a_c²)
 * Convention: counter-clockwise positive; t̂ points toward increasing θ.
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class CircularMotionLab extends JFrame {

    // ---------- Theme ----------
    static final Color BG = new Color(0x0B, 0x10, 0x1C);
    static final Color CARD = new Color(0x12, 0x18, 0x27);
    static final Color BORDER = new Color(255, 255, 255, 28);
    static final Color GRID = new Color(255, 255, 255, 14);
    static final Color FG = new Color(0xE6, 0xEA, 0xF2);
    static final Color MUTED = new Color(0x8A, 0x94, 0xA8);
    static final Color VEL = new Color(0x4F, 0xD8, 0xEB);   // cyan
    static final Color ACC_C = new Color(0xA7, 0x8B, 0xFA); // violet
    static final Color ACC_T = new Color(0xF2, 0xB8, 0x4B); // amber
    static final Color ACC_NET = new Color(0xD0, 0xD5, 0xE0);
    static final Color LIVE = new Color(0x4A, 0xDE, 0x80);
    static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    static final Font MONO_B = new Font(Font.MONOSPACED, Font.BOLD, 12);
    static final Font SANS = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    // ---------- Physics ----------
    static final class SimState {
        final double t, theta, omega;
        SimState(double t, double theta, double omega) { this.t = t; this.theta = theta; this.omega = omega; }
        SimState advance(double alpha, double dt) {
            return new SimState(t + dt, theta + omega * dt + 0.5 * alpha * dt * dt, omega + alpha * dt);
        }
    }

    static final class Sample {
        final double t, theta, omega, alpha, v, a;
        Sample(double t, double theta, double omega, double alpha, double v, double a) {
            this.t = t; this.theta = theta; this.omega = omega; this.alpha = alpha; this.v = v; this.a = a;
        }
        double get(int series) {
            switch (series) { case 0: return theta; case 1: return omega; case 2: return alpha; case 3: return v; default: return a; }
        }
    }

    static double normAngle(double th) { double tp = 2 * Math.PI; return ((th % tp) + tp) % tp; }

    // ---------- Parameters / defaults ----------
    static final double D_R = 3, D_OMEGA = 1, D_ALPHA = 0.3, D_M = 1, D_SPEED = 1;
    static final int MAX_SAMPLES = 400;   // 20 s window at 20 Hz
    static final double SAMPLE_DT = 0.05;
    static final int TRAIL_LEN = 160;
    static final double STEP_DT = 1.0 / 30;
    static final String[] SERIES_LABELS = {"θ(t) rad", "ω(t) rad/s", "α(t) rad/s²", "|v|(t) m/s", "|a|(t) m/s²"};

    double r = D_R, omega0 = D_OMEGA, alpha = D_ALPHA, m = D_M, speed = D_SPEED;
    SimState sim = new SimState(0, 0, D_OMEGA);
    final ArrayDeque<Sample> history = new ArrayDeque<>();
    final ArrayDeque<double[]> trail = new ArrayDeque<>();
    double lastSample = Double.NEGATIVE_INFINITY;
    boolean running = true;
    boolean showVel = true, showAc = true, showAt = true, showNet = false, showTrail = true, showGrid = true, showAngle = true;
    int series = 1;
    long lastNanos = System.nanoTime();

    // ---------- UI ----------
    final SimPanel simPanel = new SimPanel();
    final GraphPanel graphPanel = new GraphPanel();
    final JLabel status = new JLabel("● LIVE");
    final JLabel timeLbl = new JLabel();
    final JButton playBtn = new JButton("Pause");
    final JButton stepBtn = new JButton("Step");
    final JLabel motionLbl = new JLabel();
    final java.util.Map<String, JLabel> readouts = new java.util.LinkedHashMap<>();
    ParamSlider sR, sOmega, sAlpha, sM, sSpeed;
    final List<JCheckBox> toggleBoxes = new ArrayList<>();
    JComboBox<String> seriesBox;

    public CircularMotionLab() {
        super("Circular Motion Lab");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);

        JPanel left = new JPanel(new BorderLayout(0, 12));
        left.setOpaque(false);
        left.add(card(simWithControls()), BorderLayout.CENTER);
        JPanel graphCard = new JPanel(new BorderLayout(0, 6));
        graphCard.setOpaque(false);
        JPanel gh = new JPanel(new BorderLayout());
        gh.setOpaque(false);
        gh.add(label("Live graph · last 20 s", FG, SANS.deriveFont(Font.BOLD)), BorderLayout.WEST);
        seriesBox = new JComboBox<>(SERIES_LABELS);
        seriesBox.setSelectedIndex(series);
        seriesBox.addActionListener(e -> { series = seriesBox.getSelectedIndex(); graphPanel.repaint(); });
        gh.add(seriesBox, BorderLayout.EAST);
        graphCard.add(gh, BorderLayout.NORTH);
        graphPanel.setPreferredSize(new Dimension(600, 190));
        graphCard.add(graphPanel, BorderLayout.CENTER);
        left.add(card(graphCard), BorderLayout.SOUTH);

        JPanel side = new JPanel();
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.add(card(buildPresets()));
        side.add(Box.createVerticalStrut(10));
        side.add(card(buildParams()));
        side.add(Box.createVerticalStrut(10));
        side.add(card(buildReadouts()));
        side.add(Box.createVerticalStrut(10));
        side.add(card(buildToggles()));
        side.add(Box.createVerticalStrut(10));
        side.add(card(buildPhysics()));
        JScrollPane sideScroll = new JScrollPane(side);
        sideScroll.setBorder(null);
        sideScroll.getViewport().setOpaque(false);
        sideScroll.setOpaque(false);
        sideScroll.getVerticalScrollBar().setUnitIncrement(16);
        sideScroll.setPreferredSize(new Dimension(360, 700));
        sideScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel main = new JPanel(new BorderLayout(12, 0));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(12, 12, 12, 12));
        main.add(left, BorderLayout.CENTER);
        main.add(sideScroll, BorderLayout.EAST);
        add(main, BorderLayout.CENTER);

        resetSim();
        // ~60 fps loop; real dt measured with nanoTime and clamped to avoid jumps
        new Timer(16, e -> tick()).start();
        setSize(1300, 860);
        setLocationRelativeTo(null);
    }

    // ---------- Simulation loop ----------
    void tick() {
        long now = System.nanoTime();
        double real = Math.min((now - lastNanos) / 1e9, 0.05);
        lastNanos = now;
        if (running) step(real * speed);
        refresh();
    }

    void step(double dt) {
        int n = Math.max(1, (int) Math.ceil(dt / 0.01));
        for (int i = 0; i < n; i++) { sim = sim.advance(alpha, dt / n); record(false); }
    }

    void record(boolean force) {
        double x = Math.cos(sim.theta), y = Math.sin(sim.theta);
        double[] last = trail.peekLast();
        if (last == null || Math.hypot(x - last[0], y - last[1]) > 0.02) {
            trail.addLast(new double[]{x, y});
            while (trail.size() > TRAIL_LEN) trail.removeFirst();
        }
        if (force || sim.t - lastSample >= SAMPLE_DT) {
            double v = r * Math.abs(sim.omega), at = r * Math.abs(alpha), ac = r * sim.omega * sim.omega;
            history.addLast(new Sample(sim.t, sim.theta, sim.omega, alpha, v, Math.hypot(at, ac)));
            while (history.size() > MAX_SAMPLES) history.removeFirst();
            lastSample = sim.t;
        }
    }

    void resetSim() {
        sim = new SimState(0, 0, omega0);
        history.clear(); trail.clear();
        lastSample = Double.NEGATIVE_INFINITY;
        record(true);
        refresh();
    }

    void setRunning(boolean run) {
        running = run;
        lastNanos = System.nanoTime();
        playBtn.setText(run ? "Pause" : "Resume");
        stepBtn.setEnabled(!run);
        status.setText(run ? "● LIVE" : "● PAUSED");
        status.setForeground(run ? LIVE : MUTED);
    }

    void applyPreset(double w0, double a) {
        omega0 = w0; alpha = a;
        sOmega.setValue(w0); sAlpha.setValue(a);
        resetSim();
        setRunning(true);
    }

    void resetAll() {
        r = D_R; omega0 = D_OMEGA; alpha = D_ALPHA; m = D_M; speed = D_SPEED;
        sR.setValue(r); sOmega.setValue(omega0); sAlpha.setValue(alpha); sM.setValue(m); sSpeed.setValue(speed);
        showVel = showAc = showAt = showTrail = showGrid = showAngle = true; showNet = false;
        for (JCheckBox cb : toggleBoxes) cb.setSelected(!cb.getText().startsWith("Net"));
        series = 1; seriesBox.setSelectedIndex(1);
        resetSim();
        setRunning(true);
    }

    static String fmt(double x, int d) { if (Math.abs(x) < 1e-9) x = 0; return String.format("%." + d + "f", x); }
    static String signed(double x, int d) {
        String s = x > 1e-9 ? "+" : x < -1e-9 ? "−" : "±";
        return s + String.format("%." + d + "f", Math.abs(x));
    }

    void refresh() {
        double v = r * Math.abs(sim.omega), at = r * Math.abs(alpha), ac = r * sim.omega * sim.omega, a = Math.hypot(at, ac);
        double th = normAngle(sim.theta);
        readouts.get("θ").setText(fmt(th, 3) + " rad · " + fmt(Math.toDegrees(th), 1) + "°");
        readouts.get("ω").setText(signed(sim.omega, 3) + " rad/s");
        readouts.get("α").setText(signed(alpha, 3) + " rad/s²");
        readouts.get("|v| = r|ω|").setText(fmt(v, 3) + " m/s");
        readouts.get("|a_t| = r|α|").setText(fmt(at, 3) + " m/s²");
        readouts.get("a_c = rω²").setText(fmt(ac, 3) + " m/s²");
        readouts.get("|a| = √(a_t²+a_c²)").setText(fmt(a, 3) + " m/s²");
        readouts.get("|F| = m|a|").setText(fmt(m * a, 3) + " N");
        readouts.get("T = 2π/|ω|").setText(alpha == 0 && sim.omega != 0 ? fmt(2 * Math.PI / Math.abs(sim.omega), 3) + " s" : "— (ω not constant)");
        readouts.get("t").setText(fmt(sim.t, 2) + " s");
        timeLbl.setText("t = " + fmt(sim.t, 2) + " s");
        String motion = Math.abs(sim.omega) < 1e-3 ? "momentarily at rest"
                : alpha == 0 ? "uniform" : sim.omega * alpha > 0 ? "speeding up" : "slowing down";
        motionLbl.setText(motion);
        simPanel.repaint();
        graphPanel.repaint();
    }

    // ---------- UI builders ----------
    JComponent buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG);
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER), new EmptyBorder(10, 16, 10, 16)));
        JPanel t = new JPanel(new GridLayout(2, 1));
        t.setOpaque(false);
        t.add(label("Circular Motion Lab", FG, SANS.deriveFont(Font.BOLD, 18f)));
        t.add(label("Non-uniform circular motion · constant angular acceleration", MUTED, SANS.deriveFont(11f)));
        h.add(t, BorderLayout.WEST);
        status.setFont(MONO_B);
        status.setForeground(LIVE);
        h.add(status, BorderLayout.EAST);
        return h;
    }

    JComponent simWithControls() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false);
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        legend.setOpaque(false);
        legend.add(legendItem(VEL, "v (tangent)"));
        legend.add(legendItem(ACC_C, "a_c (to centre)"));
        legend.add(legendItem(ACC_T, "a_t (∥ tangent, sign of α)"));
        legend.add(legendItem(ACC_NET, "a (net)"));
        overlay.add(legend, BorderLayout.WEST);
        motionLbl.setFont(MONO); motionLbl.setForeground(MUTED);
        overlay.add(motionLbl, BorderLayout.EAST);
        p.add(overlay, BorderLayout.NORTH);
        simPanel.setPreferredSize(new Dimension(700, 480));
        p.add(simPanel, BorderLayout.CENTER);

        JPanel c = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        c.setOpaque(false);
        playBtn.addActionListener(e -> setRunning(!running));
        stepBtn.setEnabled(false);
        stepBtn.addActionListener(e -> { step(STEP_DT * speed); refresh(); });
        JButton reset = new JButton("Reset motion");
        reset.addActionListener(e -> resetSim());
        JButton defaults = new JButton("Reset to defaults");
        defaults.addActionListener(e -> resetAll());
        c.add(playBtn); c.add(stepBtn); c.add(reset); c.add(defaults);
        timeLbl.setFont(MONO); timeLbl.setForeground(MUTED);
        c.add(Box.createHorizontalStrut(16));
        c.add(timeLbl);
        p.add(c, BorderLayout.SOUTH);
        return p;
    }

    JComponent buildPresets() {
        JPanel p = section("PRESETS");
        JPanel g = new JPanel(new GridLayout(2, 2, 6, 6));
        g.setOpaque(false);
        g.add(button("Uniform", () -> applyPreset(1.5, 0)));
        g.add(button("Speeding up", () -> applyPreset(0.5, 0.4)));
        g.add(button("Slowing down", () -> applyPreset(3, -0.4)));
        g.add(button("Reversing", () -> applyPreset(2, -0.8)));
        p.add(g);
        return p;
    }

    JComponent buildParams() {
        JPanel p = section("PARAMETERS");
        sR = new ParamSlider("Radius r", "m", 0.5, 5, 0.1, 1, false, r, v -> r = v);
        sOmega = new ParamSlider("Angular velocity ω", "rad/s", -5, 5, 0.05, 2, true, omega0,
                v -> { omega0 = v; sim = new SimState(sim.t, sim.theta, v); });
        sAlpha = new ParamSlider("Angular acceleration α", "rad/s²", -2, 2, 0.05, 2, true, alpha, v -> alpha = v);
        sM = new ParamSlider("Mass m", "kg", 0.1, 10, 0.1, 1, false, m, v -> m = v);
        sSpeed = new ParamSlider("Simulation speed", "×", 0.1, 4, 0.1, 1, false, speed, v -> speed = v);
        for (ParamSlider s : new ParamSlider[]{sR, sOmega, sAlpha, sM, sSpeed}) p.add(s);
        p.add(label("ω slider sets current ω (and ω₀ for reset)", MUTED, SANS.deriveFont(10f)));
        return p;
    }

    JComponent buildReadouts() {
        JPanel p = section("LIVE READOUTS");
        p.add(label("cyan = signed (CCW +)   ·   |x| = magnitude", MUTED, SANS.deriveFont(10f)));
        String[] keys = {"θ", "ω", "α", "|v| = r|ω|", "|a_t| = r|α|", "a_c = rω²", "|a| = √(a_t²+a_c²)", "|F| = m|a|", "T = 2π/|ω|", "t"};
        JPanel g = new JPanel(new GridLayout(keys.length, 2, 4, 3));
        g.setOpaque(false);
        for (String k : keys) {
            g.add(label(k, MUTED, MONO));
            boolean isSigned = k.equals("ω") || k.equals("α");
            JLabel v = label("", isSigned ? VEL : FG, MONO);
            v.setHorizontalAlignment(SwingConstants.RIGHT);
            readouts.put(k, v);
            g.add(v);
        }
        p.add(g);
        return p;
    }

    JComponent buildToggles() {
        JPanel p = section("DISPLAY");
        JPanel g = new JPanel(new GridLayout(4, 2, 4, 2));
        g.setOpaque(false);
        g.add(toggle("Velocity v", true, b -> showVel = b));
        g.add(toggle("Centripetal a_c", true, b -> showAc = b));
        g.add(toggle("Tangential a_t", true, b -> showAt = b));
        g.add(toggle("Net a", false, b -> showNet = b));
        g.add(toggle("Trail", true, b -> showTrail = b));
        g.add(toggle("Grid", true, b -> showGrid = b));
        g.add(toggle("Angle θ", true, b -> showAngle = b));
        p.add(g);
        return p;
    }

    JComponent buildPhysics() {
        JPanel p = section("PHYSICS");
        JTextArea t = new JTextArea(
                "v = rω   ·   a_t = rα   ·   a_c = rω²\n" +
                "a = a_t t̂ − a_c r̂ ,  |a| = √(a_t² + a_c²)\n\n" +
                "θ = θ₀ + ω₀t + ½αt²,  ω = ω₀ + αt.\n" +
                "Positive = counter-clockwise; t̂ points toward increasing θ.\n\n" +
                "v is always tangent. a_c always points to the centre. a_t lies along " +
                "the tangent with the sign of α: parallel to v when speeding up, opposite " +
                "when slowing down. When ω crosses zero the particle stops momentarily and reverses.\n\n" +
                "Arrow lengths saturate to stay readable; directions are exact.");
        t.setLineWrap(true); t.setWrapStyleWord(true); t.setEditable(false);
        t.setOpaque(false); t.setForeground(MUTED); t.setFont(SANS.deriveFont(11f));
        t.setColumns(28);
        p.add(t);
        return p;
    }

    // ---------- Small helpers ----------
    static JLabel label(String s, Color c, Font f) { JLabel l = new JLabel(s); l.setForeground(c); l.setFont(f); return l; }

    static JPanel card(JComponent inner) {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(CARD);
        c.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(10, 12, 10, 12)));
        c.add(inner);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    static JPanel section(String title) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel l = label(title, MUTED, SANS.deriveFont(Font.BOLD, 11f));
        l.setBorder(new EmptyBorder(0, 0, 8, 0));
        p.add(l);
        return p;
    }

    static JButton button(String text, Runnable r) { JButton b = new JButton(text); b.addActionListener(e -> r.run()); return b; }

    static JComponent legendItem(Color c, String text) {
        JLabel l = label("━ " + text, c, MONO.deriveFont(11f));
        return l;
    }

    JCheckBox toggle(String text, boolean init, java.util.function.Consumer<Boolean> on) {
        JCheckBox cb = new JCheckBox(text, init);
        cb.setOpaque(false); cb.setForeground(FG); cb.setFont(SANS);
        cb.addItemListener(e -> { on.accept(cb.isSelected()); refresh(); });
        toggleBoxes.add(cb);
        return cb;
    }

    /** Slider with label, live value and unit; signed params shown with explicit sign in cyan. */
    final class ParamSlider extends JPanel {
        final JSlider slider; final JLabel val; final double min, step; final int dec; final boolean sign; final String unit;
        boolean silent = false;
        ParamSlider(String name, String unit, double min, double max, double step, int dec, boolean sign, double init,
                    java.util.function.DoubleConsumer on) {
            super(new BorderLayout(0, 2));
            this.min = min; this.step = step; this.dec = dec; this.sign = sign; this.unit = unit;
            setOpaque(false);
            setBorder(new EmptyBorder(0, 0, 8, 0));
            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(label(name, FG, SANS), BorderLayout.WEST);
            val = label("", sign ? VEL : FG, MONO);
            top.add(val, BorderLayout.EAST);
            add(top, BorderLayout.NORTH);
            slider = new JSlider(0, (int) Math.round((max - min) / step));
            slider.setOpaque(false);
            slider.addChangeListener(e -> {
                double v = value();
                val.setText((sign ? signed(v, dec) : fmt(v, dec)) + " " + unit);
                if (!silent) { on.accept(v); refresh(); }
            });
            add(slider, BorderLayout.CENTER);
            setValue(init);
        }
        double value() { return Math.round((min + slider.getValue() * step) * 1e6) / 1e6; }
        void setValue(double v) {
            silent = true;
            slider.setValue((int) Math.round((v - min) / step));
            val.setText((sign ? signed(value(), dec) : fmt(value(), dec)) + " " + unit);
            silent = false;
        }
    }

    // ---------- Simulator canvas ----------
    final class SimPanel extends JPanel {
        static final double R_MAX = 5;
        SimPanel() { setBackground(CARD); }

        @Override protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            double cx = w / 2.0, cy = h / 2.0;
            double maxPx = Math.max(Math.min(w, h) / 2.0 - 40, 40);
            double scale = maxPx / R_MAX; // px per metre
            double rp = r * scale;

            if (showGrid) {
                g.setColor(GRID);
                for (double x = cx % scale; x < w; x += scale) g.draw(new Line2D.Double(x, 0, x, h));
                for (double y = cy % scale; y < h; y += scale) g.draw(new Line2D.Double(0, y, w, y));
                g.setColor(BORDER);
                g.draw(new Line2D.Double(0, cy, w, cy));
                g.draw(new Line2D.Double(cx, 0, cx, h));
                g.setColor(MUTED); g.setFont(MONO.deriveFont(10f));
                g.drawString("1 m", (float) (cx + scale + 3), (float) (cy - 4));
            }

            // track
            g.setColor(new Color(MUTED.getRed(), MUTED.getGreen(), MUTED.getBlue(), 130));
            g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{4, 6}, 0));
            g.draw(new Ellipse2D.Double(cx - rp, cy - rp, 2 * rp, 2 * rp));
            g.setStroke(new BasicStroke(1f));
            g.setColor(FG);
            g.fill(new Ellipse2D.Double(cx - 3, cy - 3, 6, 6));

            // trail (unit-circle points scaled by current r)
            if (showTrail && trail.size() > 1) {
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                double[] prev = null; int i = 0, n = trail.size();
                for (double[] p : trail) {
                    if (prev != null) {
                        int alphaC = (int) (180.0 * i / n);
                        g.setColor(new Color(VEL.getRed(), VEL.getGreen(), VEL.getBlue(), alphaC));
                        g.draw(new Line2D.Double(cx + prev[0] * rp, cy - prev[1] * rp, cx + p[0] * rp, cy - p[1] * rp));
                    }
                    prev = p; i++;
                }
            }

            double th = sim.theta, om = sim.omega;
            double rx = Math.cos(th), ry = Math.sin(th), tx = -ry, ty = rx;
            double px = cx + rx * rp, py = cy - ry * rp;

            if (showAngle) {
                g.setStroke(new BasicStroke(1f));
                g.setColor(BORDER);
                g.draw(new Line2D.Double(cx, cy, px, py));
                g.setColor(new Color(255, 255, 255, 60));
                g.draw(new Line2D.Double(cx, cy, cx + rp + 14, cy));
                double tn = normAngle(th);
                double ar = Math.min(34, rp * 0.4 + 12);
                g.setColor(FG);
                g.setStroke(new BasicStroke(1.5f));
                g.draw(new Arc2D.Double(cx - ar, cy - ar, 2 * ar, 2 * ar, 0, Math.toDegrees(tn), Arc2D.OPEN));
                g.setFont(MONO);
                g.drawString("θ", (float) (cx + (ar + 8) * Math.cos(tn / 2) - 3), (float) (cy - (ar + 8) * Math.sin(tn / 2) + 4));
            }

            // physics vectors (y up)
            double vx = r * om * tx, vy = r * om * ty;
            double atx = r * alpha * tx, aty = r * alpha * ty;
            double acx = -r * om * om * rx, acy = -r * om * om * ry;
            double L = Math.max(maxPx * 0.55, 50);
            if (showNet) arrow(g, px, py, atx + acx, aty + acy, 4, L, ACC_NET, "a");
            if (showAc) arrow(g, px, py, acx, acy, 4, L, ACC_C, "a_c");
            if (showAt) arrow(g, px, py, atx, aty, 4, L, ACC_T, "a_t");
            if (showVel) arrow(g, px, py, vx, vy, 5, L, VEL, "v");

            // particle with glow
            for (int k = 4; k >= 1; k--) {
                g.setColor(new Color(VEL.getRed(), VEL.getGreen(), VEL.getBlue(), 25));
                double rr = 7 + k * 3;
                g.fill(new Ellipse2D.Double(px - rr, py - rr, 2 * rr, 2 * rr));
            }
            g.setColor(FG);
            g.fill(new Ellipse2D.Double(px - 7, py - 7, 14, 14));
        }

        /** Length = L·tanh(|vec|/ref): readable over wide ranges, direction exact. */
        void arrow(Graphics2D g, double px, double py, double vx, double vy, double ref, double L, Color c, String label) {
            double mag = Math.hypot(vx, vy);
            if (mag < 1e-6) return;
            double len = L * Math.tanh(mag / ref);
            if (len < 3) return;
            double ux = vx / mag, uy = -vy / mag; // screen y down
            double ex = px + ux * len, ey = py + uy * len;
            g.setColor(c);
            g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(new Line2D.Double(px, py, ex, ey));
            double a = Math.atan2(uy, ux), hs = 10;
            Path2D head = new Path2D.Double();
            head.moveTo(ex + Math.cos(a) * 2, ey + Math.sin(a) * 2);
            head.lineTo(ex - hs * Math.cos(a - 0.4), ey - hs * Math.sin(a - 0.4));
            head.lineTo(ex - hs * Math.cos(a + 0.4), ey - hs * Math.sin(a + 0.4));
            head.closePath();
            g.fill(head);
            g.setFont(MONO_B);
            g.drawString(label, (float) (ex + ux * 12 - 8), (float) (ey + uy * 12 + 4));
        }
    }

    // ---------- Graph ----------
    final class GraphPanel extends JPanel {
        GraphPanel() { setBackground(CARD); }

        @Override protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int pl = 52, pr = 10, pt = 10, pb = 22;
            int gw = w - pl - pr, gh = h - pt - pb;
            g.setFont(MONO.deriveFont(10f));
            if (history.size() < 2) {
                g.setColor(MUTED);
                g.drawString("Run the simulation to record data", pl + 8, pt + gh / 2);
                return;
            }
            Sample[] data = history.toArray(new Sample[0]);
            double t0 = data[0].t, t1 = data[data.length - 1].t;
            double lo = Double.POSITIVE_INFINITY, hi = Double.NEGATIVE_INFINITY;
            for (Sample s : data) { double y = s.get(series); lo = Math.min(lo, y); hi = Math.max(hi, y); }
            if (series >= 3) lo = Math.min(lo, 0);
            if (hi - lo < 1e-6) { hi += 1; lo -= 1; }
            double pad = (hi - lo) * 0.08; hi += pad; lo -= pad;
            final double flo = lo, fhi = hi;
            java.util.function.DoubleUnaryOperator X = t -> pl + (t - t0) / Math.max(t1 - t0, 1e-6) * gw;
            java.util.function.DoubleUnaryOperator Y = y -> pt + (1 - (y - flo) / (fhi - flo)) * gh;

            for (int i = 0; i <= 4; i++) {
                double y = lo + (hi - lo) * i / 4, yy = Y.applyAsDouble(y);
                g.setColor(BORDER);
                g.draw(new Line2D.Double(pl, yy, w - pr, yy));
                g.setColor(MUTED);
                g.drawString(fmt(y, Math.abs(hi - lo) < 5 ? 2 : 1), 4, (float) yy + 3);
            }
            if (lo < 0 && hi > 0) {
                g.setColor(new Color(MUTED.getRed(), MUTED.getGreen(), MUTED.getBlue(), 120));
                double y0 = Y.applyAsDouble(0);
                g.draw(new Line2D.Double(pl, y0, w - pr, y0));
            }
            g.setColor(MUTED);
            g.drawString(fmt(t0, 1) + " s", pl, h - 6);
            String end = fmt(t1, 1) + " s";
            g.drawString(end, w - pr - g.getFontMetrics().stringWidth(end), h - 6);

            Color[] colors = {LIVE, VEL, new Color(0xF0, 0x7A, 0x6A), VEL, ACC_C};
            g.setColor(colors[series]);
            g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D path = new Path2D.Double();
            for (int i = 0; i < data.length; i++) {
                double x = X.applyAsDouble(data[i].t), y = Y.applyAsDouble(data[i].get(series));
                if (i == 0) path.moveTo(x, y); else path.lineTo(x, y);
            }
            g.draw(path);
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new CircularMotionLab().setVisible(true));
    }
}
