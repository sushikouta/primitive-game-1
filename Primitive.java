import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;

import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Primitive {
    public static void main(String[] args) {
        new Primitive();
    }
    // system
    public Primitive() {
        setup_handler();
    }
    public void setup_handler() {
        timer_setup();
        window_setup();
    }
    public void every() {
        System.out.print("fps : " + fps + " x : " + main_chara.position_x + " y : " + main_chara.position_y  + "\r\r");
        move();
    }
    // window system
    public Screen main_window = null;
    public void window_setup() {
        main_window = new Screen(600, 400) {
            {
                title("Primitive");
                closeAction(javax.swing.JFrame.EXIT_ON_CLOSE);
                show();
            }
            @Override public void draw(Graphics ui) {
                
            }
            @Override public void keyPressListener(int code) {
                key_bind_list[code] = true;
            }
            @Override public void keyReleaseListener(int code) {
                key_bind_list[code] = false;
            }
        };
    }
    // key bind
    public static final int number_of_key = 107;
    public boolean[] key_bind_list = new boolean[number_of_key];
    public int[] press_key_count_list = new int[number_of_key];
    public boolean is_press_key(int code) {
        return key_bind_list[code];
    }
    public int get_press_key_count(int code) {
        return press_key_count_list[code];
    }
    public void press_key_count() {
        for (int loop = 0;loop != number_of_key;loop++) {
            if (is_press_key(loop)) { press_key_count_list[loop]++; }
            else { press_key_count_list[loop] = 0; }
        }
    }
    // timer
    public Thread main_timer = null;
    public boolean timer_running = false;
    public int target_fps = 20;
    public int fps = 0;
    public void timer_setup() {
        timer_running = true;
        main_timer = new Thread(new Runnable() {
            @Override public void run() {
                int fps_count = 0;
                long fps_start = System.currentTimeMillis();
                while (timer_running) {
                    if (fps_start + 1000 < System.currentTimeMillis()) {
                        fps = fps_count;
                        fps_count = 0;
                        fps_start = System.currentTimeMillis();
                    }
                    long start_time = System.currentTimeMillis();
                    every();
                    if ((start_time - System.currentTimeMillis()) < 1000 / target_fps) {
                        try {
                            Thread.sleep(1000 / target_fps - (start_time - System.currentTimeMillis()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    fps_count++;
                }
            }
        });
        main_timer.start();
    }
    // game system
    public Player main_chara = new Player() {
        {
            move_speed = 10;
        }
    };
    // game player
    public class Player {
        double number_of_view = 10;
        double move_speed = 0;
        double position_x = 0;
        double position_y = 0;
    }
    //game field
    public int chunk_block_size = 16;
    public class field {
        public Map<Point, chunk> field_map = new HashMap<>();
        public BufferedImage camera(int camera_x, int camera_y, int number_of_view) {
            BufferedImage camera_image = new BufferedImage(number_of_view * block_size, number_of_view * block_size, BufferedImage.TYPE_3BYTE_BGR);
            Graphics camera_graphic = camera_image.getGraphics();
            for (int draw_x = 0;draw_x != number_of_view;draw_x++) {
                for (int draw_y = 0;draw_y != number_of_view;draw_y++) {
                    
                }
            }
            return camera_image;
        }
        public class chunk {
            public block[][] blocks = null;
            public void blocks_setup() {
                blocks = new block[chunk_block_size][];
                for (int loop = 0;loop != chunk_block_size;loop++) {
                    blocks[loop] = new block[chunk_block_size];
                    for (int loop2 = 0;loop2 != chunk_block_size;loop2++) {
                        blocks[loop][loop2] = new block();
                    }
                }
            }
            public BufferedImage get_chunk_image() {
                BufferedImage chunk_image = new BufferedImage(chunk_block_size * block_size, chunk_block_size * block_size, BufferedImage.TYPE_3BYTE_BGR);
                Graphics canvas = chunk_image.getGraphics();
                for (int get_x = 0;get_x != chunk_block_size;get_x++) {
                    for (int get_y = 0;get_y != chunk_block_size;get_y++) {
                        canvas.drawImage(blocks[get_x][get_y].get_texture(), get_x * block_size, get_y * block_size, null);
                    }
                }
                return chunk_image;
            }
        }
    }
    // game block
    public int block_size = 16;
    public class block {
        public BufferedImage get_texture() {
            return new BufferedImage(block_size, block_size, BufferedImage.TYPE_3BYTE_BGR);
        }
    }
    public class no_data_block extends block {
        @Override public BufferedImage get_texture() {
            try {
                return new BufferedImage(block_size, block_size, BufferedImage.TYPE_3BYTE_BGR) { {
                    Graphics texture = getGraphics();
                    texture.drawImage(ImageIO.read(new File("")), 0, 0, block_size, block_size, null);
                } };
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new BufferedImage(block_size, block_size, BufferedImage.TYPE_3BYTE_BGR) { {
                Graphics texture = getGraphics();
                texture.setColor(Color.GREEN);
                texture.drawString("no data", 0, 5);
            } };
        }
    }
    // move 
    public int move_up_key = KeyEvent.VK_UP;
    public int move_down_key = KeyEvent.VK_DOWN;
    public int move_left_key = KeyEvent.VK_LEFT;
    public int move_right_key = KeyEvent.VK_RIGHT;
    public void move() {
        int vector_x = Boolean.compare(is_press_key(move_left_key), false) - Boolean.compare(is_press_key(move_right_key), false);
        int vector_y = Boolean.compare(is_press_key(move_up_key), false) - Boolean.compare(is_press_key(move_down_key), false);
        double angle = Math.atan2(vector_x, vector_y);
        main_chara.position_x += Math.sin(angle) * main_chara.move_speed * Math.abs(vector_x);
        main_chara.position_y += Math.cos(angle) * main_chara.move_speed * Math.abs(vector_y);
    }
}