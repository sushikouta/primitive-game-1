import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    int horizontal_ratio = 1;
    int vertical_ratio = 1;

    int screen_width = 0;
    int screen_height = 0;

    int mouse_x = 0;
    int mouse_y = 0;

    int width = 0;
    int height = 0;

    Color backgroundColor = null;

    JFrame screen = new JFrame(){
        {
            setLocationRelativeTo(null);
            add(new JPanel(){
                int pane_x = 0;
                int pane_y = 0;

                {
                    addComponentListener(new ComponentAdapter(){
                        @Override public void componentMoved(ComponentEvent e) {}
                        @Override public void componentResized(ComponentEvent e) {
                            if (getWidth() / horizontal_ratio <= getHeight() / vertical_ratio) {
                                pane_x = 0;
                                pane_y = (getHeight() / vertical_ratio - getWidth() / horizontal_ratio) * vertical_ratio / 2;
                            }else {
                                pane_x = (getWidth() / horizontal_ratio - getHeight() / vertical_ratio) * horizontal_ratio / 2;
                                pane_y = 0;
                            }
                        }
                    });
                    addMouseMotionListener(new MouseMotionListener() {
                        @Override public void mouseDragged(MouseEvent e) {
                            if (e.getX() > pane_x && getWidth() - pane_x >= e.getX() && e.getY() > pane_y && getHeight() - pane_y >= e.getY()) {
                                mouse_x = (int) ((e.getX() - pane_x) * Math.min(screen_width / horizontal_ratio, screen_height / vertical_ratio) / Math.min(getWidth() / horizontal_ratio, getHeight() / vertical_ratio));
                                mouse_y = (int) ((e.getY() - pane_y) * Math.min(screen_width / horizontal_ratio, screen_height / vertical_ratio) / Math.min(getWidth() / horizontal_ratio, getHeight() / vertical_ratio));
                            }
                        }

                        @Override public void mouseMoved(MouseEvent e) {
                            if ((e.getX() > pane_x) && (getWidth() - pane_x >= e.getX()) && (e.getY() > pane_y) && (getHeight() - pane_y >= e.getY())) {
                                mouse_x = (int) ((e.getX() - pane_x) * Math.min(screen_width / horizontal_ratio, screen_height / vertical_ratio) / Math.min(getWidth() / horizontal_ratio, getHeight() / vertical_ratio));
                                mouse_y = (int) ((e.getY() - pane_y) * Math.min(screen_width / horizontal_ratio, screen_height / vertical_ratio) / Math.min(getWidth() / horizontal_ratio, getHeight() / vertical_ratio));
                            }
                        }
                        
                    });
                    setFocusable(true);
                    addKeyListener(new KeyListener() {
                        @Override public void keyTyped(KeyEvent e) {}

                        @Override public void keyPressed(KeyEvent e) {
                            if (!keyboard.contains(e.getKeyCode())) {
                                keyboard.add(e.getKeyCode());
                            }
                            keyPressListener(e.getKeyCode());
                        }

                        @Override
                        public void keyReleased(KeyEvent e) { 
                            if (keyboard.contains(e.getKeyCode())) {
                                keyboard.remove(keyboard.indexOf(e.getKeyCode()));
                            }
                            keyReleaseListener(e.getKeyCode());
                        }
                    });
                }

                @Override public void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    width = getWidth();
                    height = getHeight();

                    if (backgroundColor != null) { 
                        g.setColor(backgroundColor);
                        g.fillRect(0, 0, width, height);
                    }

                    BufferedImage screen = new BufferedImage(screen_width, screen_height, BufferedImage.TYPE_3BYTE_BGR);
                    draw(screen.getGraphics());

                    g.drawImage(screen, pane_x, pane_y, Math.min(getWidth() / horizontal_ratio, getHeight() / vertical_ratio) * horizontal_ratio, Math.min(getWidth() / horizontal_ratio, getHeight() / vertical_ratio) * vertical_ratio, null);
                }
            });
        }
    };

    public void draw(Graphics g) {
        
    }

    public void paint() {
        screen.repaint();
    }

    public void closeAction(int action) {
        screen.setDefaultCloseOperation(action);
    }

    public void show() {
        screen.setVisible(true);
    }

    public void background(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void title(String title) {
        screen.setTitle(title);
    }

    public void resize(int width, int height) {
        screen.getContentPane().setPreferredSize(new Dimension(width, height));
        screen.pack();
    }

    public Screen() {}

    public Screen(int screen_width, int screen_height) {
        this.screen_width = screen_width;
        this.screen_height = screen_height;
        int[] match = match(divisor(screen_width), divisor(screen_height));
        this.horizontal_ratio = screen_width / match[match.length - 1];
        this.vertical_ratio = screen_height / match[match.length - 1];
        resize(screen_width, screen_height);
    }

    public List<Integer> keyboard = new ArrayList<>();

    public boolean isPress(int code) {
        return keyboard.contains(code);
    }
    public void keyPressListener(int code) {}
    public void keyReleaseListener(int code) {}

    private static int[] divisor(int n) {
        int i = 0;

        for (int a = 2;a < n - 1;a++) {
            if (n / (double) a == (int) (n / (double) a)) {
                i++;
            }
        }

        int[] divisor = new int[i];

        i = 0;
        for (int a = 2;a < n - 1;a++) {
            if (n / (double) a == (int) (n / (double) a)) {
                divisor[i] = a;
                i++;
            }
        }

        return divisor;
    }

    private static int[] match(int[] a, int[] b) {
        int[] d = null;
        int[] e = null;

        if (a.length < b.length) {
            d = a;
            e = b;
        } else {
            d = b;
            e = a;
        }

        int n = 0;
        for (int c = 0;c != d.length;c++) {
            if (charAt(e, d[c]) != -1) {
                n++;
            }
        }

        int[] m = new int[n];
        n = 0;
        for (int c = 0;c != d.length;c++) {
            if (charAt(e, d[c]) != -1) {
                m[n] = d[c];
                n++;
            }
        }
        return m;
    }
    private static int charAt(int[] a, int b) {
        for (int c = 0;c != a.length;c++) {
            if (a[c] == b) {
                return a[c];
            }
        }
        return -1;
    }
}