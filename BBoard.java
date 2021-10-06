
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author grant
 */
public class BBoard {
    public static int width;
    public static int height;
    public static ArrayList<Post> posts = new ArrayList<Post>();
    public static ArrayList<Pin> pins = new ArrayList<Pin>();
    static String[] colors;
    static String seperator = "@@";
             
    public static void main(String[] args) throws IOException {
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
        ServerSocket run = new ServerSocket(port);
        try {
            while (true) {
                new server(run.accept()).start();
            }    
        } finally {
            run.close();
        }
    
    }
    
    public static class server extends Thread {
        Socket socket;
        
        public server(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new 
                                InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(),
                                                                        true);
                
                while(true) {
                    String client = input.readLine();
                    System.out.println(client);
                    String[] temp = client.trim().split(seperator);
                    boolean result = false;
                    System.out.println("key = " + temp[0]);
                    switch(temp[0]) {
                        case "UNPIN":
                            result = unpin(Integer.parseInt(temp[1]), 
                                                Integer.parseInt(temp[2]));
                            if (result) {
                                output.println("Pin (" + temp[1] + "," + temp[2] 
                                            + ") was successfully removed.");
                            } else {
                                output.println("Specified pin does not exist.");
                            }
                            
                        case "PIN":
                            result = pin(Integer.parseInt(temp[1]), 
                                                    Integer.parseInt(temp[2]));
                            if (result) {
                                output.println("Pin (" + temp[1] + "," + temp[2] 
                                            + ") was successfully created.");
                            } else {
                                output.println("Specified location does not "
                                        + "     exist or pin already exists.");
                            }
                            
                        case "GET PINS":
                            String message = "";
                            for (Pin pin : pins) {
                                message = message + pin.getPinstr() + "\n";
                            } 
                            output.println(message);
                            
                        case "SHAKE":
                            shake();
                            
                        case "CLEAR":
                            clear();
                        case "POST":
                        	System.out.println("length = " + temp.length);
                            if (temp.length == 7){
                                result = makePost(Integer.parseInt(temp[1]), 
                           Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), 
                           Integer.parseInt(temp[4]), temp[5], temp[6]);
                            } else if (temp.length == 6) {
                                result = makePost(Integer.parseInt(temp[1]), 
                           Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), 
                           Integer.parseInt(temp[4]), "", temp[5]);
                            }
                            
                            if (result) {
                                output.println("Post successfully created.");
                            } else {
                                output.println("Unable to create post. Please "
                                        + "check the validity of your input.");
                            }
                        case "GET":
                            String c, co, r;
                            if (temp.length == 4) {
	                            if ((temp[1].split("=")).length < 2) {
	                                c = "";
	                            } else {
	                                c = temp[1].split("=")[1];
	                            }
	                            if ((temp[2].split("=")).length < 2) {
	                                r = "";
	                            } else {
	                                r = temp[2].split("=")[1];
	                            }
	                            if ((temp[1].split("=")).length < 2) {
	                                co = "";
	                            } else {
	                                co = temp[1].split("=")[1];
	                            }
	                            
	                            String info = get(c,r,co);
	                            
	                            output.println(info);
	                            
                            }
                            
                    }
                }
                
            } catch (IOException e) {
		System.out.println(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
		}
            }
        
        }
}

    public static class Post {
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
    
    public static class Pin {
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
    
    public static boolean checkPost(int x, int y, int w, int h, String color) {
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
    
    public static boolean checkPin(int x, int y, int w, int h, int x1, int y1) {
        return (x <= x1 && x1 <= x + w && y <= y1 && y1 <= y + h);
    }
    
    public static boolean makePost(int x, int y, int w, int h, String content, 
                                                                String color) {
        if (checkPost(x,y,w,h,color)) {
            posts.add(new Post(x,y,w,h,content,color));
        } else {
            return false;
        }
        return true;
    }
    
    public static ArrayList<Post> refers(String s, ArrayList<Post> p) {
        ArrayList<Post> n = new ArrayList<Post>();
        for (Post post : p) {
            if (post.getContent().contains(s)) {
                n.add(post);
            }
        }
        return n;
    }
    
    public static ArrayList<Post> color(String c, ArrayList<Post> p) {
        ArrayList<Post> n = new ArrayList<Post>();
        for (Post post : p) {
            if (post.getColor().equals(c)) {
                n.add(post);
            }
        }
        return n;
    }
    
    public static void shake() {
        ArrayList<Post> temp = new ArrayList<Post>();
        for (Post post : posts) {
            if (post.isPin()) {
                temp.add(post);
            }
        }
        posts = temp;
    }
    
    public static void clear() {
        posts = new ArrayList<Post>();
        pins = new ArrayList<Pin>();
    }
    
    public static boolean unpin(int x, int y) {
        boolean check = true;
        
        for (int i = 0; i < pins.size(); i++) {
            if (pins.get(i).getPinX() == x && pins.get(i).getPinY() == y) {
                pins.remove(i);
                check = false;
            }
        }
        if (check) {
            return false;
        }
        
        for (Post post : posts) {
            if (checkPin(post.getX(), post.getY(), post.getWidth(),
                                                    post.getHeight(), x, y)) {
                if (post.isPin() && post.getnumPin() == 1) {
                    post.setPin(false);
                    post.updatenumPin(-1);
                }
            }
        }
        return true;
    }
    
    public static boolean pin(int x, int y) {
        
        if (!checkPin(0,0, width, height, x, y)) {
            return false;
        }
        Pin temp = new Pin(x,y);
        for (Pin pin : pins) {
            if (pin.getPinX() == x && pin.getPinY() == y) {
                return false;
            }
        }
        pins.add(temp);
        for (Post post : posts) {
            if (checkPin(post.getX(), post.getY(), post.getWidth(), 
                                                    post.getHeight(), x, y)) {
                post.updatenumPin(1);
                if (!post.isPin())
                    post.setPin(true);
            }
        }
        return true;
    }
    
    public static String get(String color, String refers, String contains) {
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
                    if (checkPin(post.getX(), post.getY(), post.getWidth(), 
            post.getHeight(), Integer.parseInt(c[0]), Integer.parseInt(c[1]))) {
                        temp.add(post);
                    }
                }
            } else {
                for (Post post : temp) {
                    if (checkPin(post.getX(), post.getY(), post.getWidth(), 
           post.getHeight(), Integer.parseInt(c[0]), Integer.parseInt(c[1]))) {
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




 
