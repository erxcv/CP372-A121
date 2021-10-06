
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author grant
 */
public class BBoard {
    public static int width;
    public static int height;
    public ArrayList<Post> posts = new ArrayList<Post>();
    public ArrayList<Pin> pins = new ArrayList<Pin>();
    String[] colors;
             
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        height = Integer.parseInt(args[2]);
	width = Integer.parseInt(args[1]);
        
        if (args.length > 3) {
            String[] colors = new String[args.length - 3];
            int i = 3;

            while (i > args.length) {
                colors[i] = args[i];
                i++;
            }
        }
        
    
    }

    public class Post {
        int x, y, w, h, numPin;
        String color, content;
        boolean isPin;
        
        public Post(int x, int y, int w, int h, String content, String color) {
            this.x = x;
            this. y = y;
            this.color = color;
            this.w = w;
            this.content = content;
            this.h = h;
            this.isPin = false;
            this.numPin = 0;
        }
        
        public String getContent() {
            return this.content;
        }
        
        public String getColor() {
            return this.color;
        }
        
        public void setPin(boolean b) {
            this.isPin = b;
        }
        
        public boolean isPin() {
            return this.isPin;
        }
        
        public void updatenumPin(int n) {
            this.numPin = this.numPin + n;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        public int getnumPin() {
            return this.numPin;
        }
        
        public int getWidth() {
            return this.w;
        }
        
        public int getHeight() {
            return this.h;
        }
        
    }
    
    public class Pin {
        int x, y;
        
        public Pin(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public String getPinstr() {
            return "(" + String.valueOf(this.x) + "," + String.valueOf(this.y) 
                                                                         + ")";
        }
        
        public int getPinX() {
            return this.x;
        }
        
        public int getPinY() {
            return this.y;
        }
    }
    
    public boolean checkPost(int x, int y, int w, int h, String color) {
        boolean check = false;

        if (colors!= null && colors.length > 0) {
            for (String color1 : colors) {
                if (color.equals(color1)) {
                    check = true;
                }
            }
        }
        return (x + w < width && y + h < height && check);
    }
    
    public boolean checkPin(int x, int y, int w, int h, int x1, int y1) {
        return (x <= x1 && x1 <= x + w && y <= y1 && y1 <= y + h);
    }
    
    public boolean makePost(int x, int y, int w, int h, String content, String color) {
        if (checkPost(x,y,w,h,color)) {
            posts.add(new Post(x,y,w,h,content,color));
        } else {
            return false;
        }
        return true;
    }
    
    public ArrayList<Post> refers(String s, ArrayList<Post> p) {
        ArrayList<Post> n = new ArrayList<Post>();
        for (Post post : p) {
            if (post.getContent().contains(s)) {
                n.add(post);
            }
        }
        return n;
    }
    
    public ArrayList<Post> color(String c, ArrayList<Post> p) {
        ArrayList<Post> n = new ArrayList<Post>();
        for (Post post : p) {
            if (post.getColor().equals(c)) {
                n.add(post);
            }
        }
        return n;
    }
    
    public void shake() {
        ArrayList<Post> temp = new ArrayList<Post>();
        for (Post post : posts) {
            if (post.isPin()) {
                temp.add(post);
            }
        }
        posts = temp;
    }
    
    public void clear() {
        posts = new ArrayList<Post>();
        pins = new ArrayList<Pin>();
    }
    
    public void unpin(int x, int y) {
        for (int i = 0; i < pins.size(); i++) {
            if (pins.get(i).getPinX() == x && pins.get(i).getPinY() == y) {
                pins.remove(i);
            }
        }
        for (Post post : posts) {
            if (checkPin(post.getX(), post.getY(), post.getWidth(), post.getHeight(), x, y)) {
                if (post.isPin() && post.getnumPin() == 1) {
                    post.setPin(false);
                    post.updatenumPin(-1);
                }
            }
        }
    }
    
    public void pin(int x, int y) {
        pins.add(new Pin(x,y));
        for (Post post : posts) {
            if (checkPin(post.getX(), post.getY(), post.getWidth(), post.getHeight(), x, y)) {
                post.updatenumPin(1);
                if (!post.isPin())
                    post.setPin(true);
            }
        }
    }
    
    public String get(String color, String refers, String contains) {
        String out = "";
        ArrayList<Post> temp = new ArrayList<Post>();
        boolean check = false;
        
        if (!color.equals("")) {
            temp = color(color, posts);
            check = true;
        }
        if (!refers.equals("")) {
            if (!check) {
                temp = refers(refers, posts);
                check = true;
            } else {
                temp = refers(refers, temp);
            }
        }
        if (!contains.equals("")) {
            String[] c = contains.split(" ", 2);
            if (!check) {
                for (Post post : posts) {
                    if (checkPin(post.getX(), post.getY(), post.getWidth(), post.getHeight(), Integer.parseInt(c[0]), Integer.parseInt(c[1]))) {
                        temp.add(post);
                    }
                }
            } else {
                for (Post post : temp) {
                    if (checkPin(post.getX(), post.getY(), post.getWidth(), post.getHeight(), Integer.parseInt(c[0]), Integer.parseInt(c[1]))) {
                        temp.add(post);
                    }
                }
            }
        }
        
        for (Post post : temp) {
            out = out + post.getContent() + "\n\n";
        }
        
        return out;
    }
}




 