package com.electrician;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
    服务端实现的需求：
        1.创建服务端，绑定端口
        2.接收客户端的socket连接，交给一个独立的线程来处理
        3.把当前连接的客户端socket存入到一在线socket集合中保存
        4.接收客户端的消息，然后推送给当前所有在线的socket接收
 */
public class ChatServer {

    //静态代码块
    static {
        try {
            //读取xml文件
            SAXReader reader = new SAXReader();
            InputStream in = ChatServer.class.getClassLoader().getResourceAsStream("admins.xml");
            Document document = reader.read(in);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements("user");

            // 加载已注册的用户信息
            HashSet<String> set = new HashSet<>();
            File file = new File("chatserver\\User\\Users.txt");

            if (file.length() != 0){
                BufferedReader br = new BufferedReader(new FileReader("chatserver\\User\\Users.txt"));
                String str;
                while ((str = br.readLine()) != null){
                    set.add(str);
                }
            }

            if(set.size()!=0){
                for (String s : set) {
                    System.out.println(s);
                }
            }else{
                System.out.println("现无注册用户");
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter("chatserver\\User\\Users.txt",true));
            for (Element element : elements) {

                String Admin = element.attributeValue("isAdmin");
                boolean isAdmin = Boolean.parseBoolean(Admin);

                String userName = element.elementText("userName");
                String sex = element.elementText("sex");

                String ageStr = element.elementText("age");
                int age = Integer.parseInt(ageStr);

                String str = userName + "," + sex + "," + age + "," + isAdmin;

                bw.write(str);
                bw.newLine();
            }
            bw.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    // 定义一个静态集合，保存登陆用户的Socket
    public static Map<Socket, User> allSocketOnLine = new HashMap<>();

    public static Map<String,String> blockedName = new HashMap<>();
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(6969);
            System.out.println("服务端已启动!");
            while (true) {
                Socket socket = ss.accept();
                String ip =socket.getRemoteSocketAddress().toString();
                // 为当前登录成功的socket分配一个独立的线程来处理与之通信
                new ChatServerThread(socket).start();
                if (blockedName.containsKey(ip)) {
                    allSocketOnLine.remove(socket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
