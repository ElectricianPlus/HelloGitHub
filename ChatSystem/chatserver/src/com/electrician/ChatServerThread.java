package com.electrician;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChatServerThread extends Thread {
    private Socket socket;

    public ChatServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 1.从socket中去获取当前客户端的输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                // 读取客户端发送过来的消息
                String flag = br.readLine();
                // 对客户端的消息进行处理
                switch (flag) { // 100: 表示登陆
                    case "100":
                        doLogin(br);
                        break;
                    case "200": // 200: 表示单聊
                        doSingleChat(br);
                        break;
                    case "201": // 201: 表示群聊
                        doAllChat(br);
                        break;
                    case "101": // 101: 表示注册
                        doRegistered(br);
                        break;
                    case "202": // 202: 表示随机聊
                        doRandomChat(br);
                        break;
                    case "300": // 300：获取所有在线用户
                        getAllScoke();
                        break;
                    case "301": // 301: 用户升序排序
                        doSort(br);
                        break;
                    case "401": // 401: 显示聊天记录
                        doShowNotes();
                        break;
                    case "302": // 302: 根据性别显示在线用户
                        getUserBySex(br);
                        break;
                    case "209": // 209: 拉黑指定用户
                        block(br);
                        break;
                }
            }
        } catch (Exception e) {
            try {
                System.out.println("当前有人下线了！" + e.getMessage());
                // 从在线socket集合中移除本socket
                ChatServer.allSocketOnLine.remove(socket);
                // 发送在线用户给所有人
                String values = ChatServer.allSocketOnLine.values().toString();
                sendMsgToAllClient("有用户下线了，当前在线用户： " + values);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // 聊天记录
    private void doShowNotes() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("chatserver\\User\\chatNotes.txt"));
        String str;
        System.out.println("聊天记录如下");
        PrintStream ps = new PrintStream(socket.getOutputStream());
        while ((str = br.readLine()) != null){
            //System.out.println(str);
            ps.println(str);
        }
    }

    // 群聊
    private void doAllChat(BufferedReader br) throws Exception {
        // 群发消息
        User user = ChatServer.allSocketOnLine.get(socket);
        boolean isAdmin = user.isIsAdmin();
        if(!isAdmin){
            String str = "您不是管理员，不能群发消息";
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println(str);
        }else{
            String msg = br.readLine();
            // 群发消息给所有人
            sendMsgToAllClient(ChatServer.allSocketOnLine.get(socket).getUserName() + "对所有人说： " + msg);
        }
    }

    // 单聊
    private void doSingleChat(BufferedReader br) throws Exception {
        // 私发消息
        // 获取对谁私发！
        String destName = br.readLine();
        // 获取私发消息
        String privateMsg = br.readLine();
        sendMsgToOneClient(destName, ChatServer.allSocketOnLine.get(socket).getUserName() + "对你说： " + privateMsg);
    }

    // 登陆
    private void doLogin(BufferedReader br) throws Exception {
        // 登录消息: 用户登录发送过来的数据格式为:用户名,性别,年龄
        String userName = br.readLine();

        if(userExist(userName)){
            BufferedReader br2 = new BufferedReader(new FileReader("chatserver\\User\\Users.txt"));
            String s ;
            while ((s = br2.readLine()) != null){
                String[] split = s.split(",");
                if(userName.equals(split[0]))
                    break;
            }
            String[] split = s.split(",");
            String sex = split[1];
            int age =Integer.parseInt(split[2]);
            boolean isAdmin =Boolean.parseBoolean(split[3]);
            User user = null;
            user = new User(userName, sex, age,isAdmin);
            ChatServer.allSocketOnLine.put(socket, user);
            // 发送在线用户给所有人
            String values = userName + "用户登录了, 当前在线：" + ChatServer.allSocketOnLine.values().toString();
            sendMsgToAllClient(values);
        }else{
            String values =userName + "用户不存在，请先去注册";
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println(values);
        }
    }

    // 注册
    private void doRegistered(BufferedReader br) throws IOException, ClassNotFoundException {
        //获取客户端发送的数据
        String userInfo = br.readLine();
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        //以,分割数据
        String[] userInfos = userInfo.split(",");
        String name = userInfos[0];
        if(name.equals("admin")){
            userInfo+=",true";
        }else{
            userInfo+=",false";
        }
        BufferedReader ois = new BufferedReader(new FileReader("chatserver\\User\\users.txt"));
        String line;
        while ((line=ois.readLine())!=null){
            System.out.println(line);
            String[] user = line.split(",");
            if(name.equals(user[0])){
                System.out.println("用户已存在!");
                pw.println("用户已存在!");
                pw.flush();
                return;
            }
        }
        BufferedWriter ops = new BufferedWriter(new FileWriter("chatserver\\User\\users.txt",true));
        ops.write(userInfo);
        ops.newLine();
//      System.out.println("恭喜你，注册成功！！");
        pw.println("恭喜你，注册成功！！");
        pw.flush();
        ops.flush();
    }

    // 随机聊
    private void doRandomChat(BufferedReader br) throws IOException {
//        Random r = new Random();
//        Object[] objects = ChatServer.allSocketOnLine.keySet().toArray();
//        Socket socket =(Socket) objects[r.nextInt(ChatServer.allSocketOnLine.size())];
//        String printMsg = br.readLine();
//        User user = ChatServer.allSocketOnLine.get(socket);
//        PrintStream ps = new PrintStream(socket.getOutputStream());
//        String userName = user.getUserName();
//        System.out.println("将随机发送给" + userName);
//        ps.println(printMsg);
//        ps.flush();

        //1.创建list集合存储socket
        ArrayList<Socket> list = new ArrayList<>();
        //2.获取socket发送的消息及其用户姓名
        String msg = br.readLine();
        String userName = ChatServer.allSocketOnLine.get(socket).getUserName();
        sendMsgToRandomClient(list,"你收到消息：随机聊选中了你," + userName + " 对你说:" + msg,userName);

    }

    /**
     * 把当前客户端发来的消息推送给一个随机在线的socket
     * @param list
     * @param randomMsg
     * @param userName
     * @throws IOException
     */
    private void sendMsgToRandomClient(ArrayList<Socket> list,String randomMsg,String userName) throws IOException {
        //1.先获取allSocketOnLine集合中键值对集合entries和当前发送消息的socket的用户姓名
        Set<Map.Entry<Socket, User>> entries = ChatServer.allSocketOnLine.entrySet();
        //2.遍历entries集合获取其他所有在线的socket并存储到list中
        for (Map.Entry<Socket, User> entry : entries) {
            Socket socket = entry.getKey();
            String randomUserName = entry.getValue().getUserName();
            if(!userName.equals(randomUserName)) {
                list.add(socket);
            }
        }
        //3.从list中随机获取1个socket
        Random r = new Random();
        int index = r.nextInt(list.size()) ;
        Socket randomSocket = list.get(index);
        //4.给随机选定的socket发送消息
        PrintStream ps = new PrintStream(randomSocket.getOutputStream());
        ps.println(randomMsg);
    }

    //拉黑
    private void block(BufferedReader br) throws Exception {
        PrintStream pp = new PrintStream(socket.getOutputStream());
        if(!ChatServer.allSocketOnLine.get(socket).isIsAdmin()){
            pp.println("你不是管理员，无此权限");
            return;
        }
        String name = br.readLine();
        String clientName;//拿来省打字的
        String ip = socket.getRemoteSocketAddress().toString();
        //创建键值对
        Set<Map.Entry<Socket, User>> entries = ChatServer.allSocketOnLine.entrySet();
        //遍历键值对集合
        for (Map.Entry<Socket, User> entry : entries) {
            //取客户名字
            clientName = entry.getValue().getUserName();
            //判断所有的客户名字和输入的是否有相等，即：找到要封禁的这个人
            if (name.equals(clientName)){
                //删掉他这个键
                ChatServer.allSocketOnLine.remove(entry.getKey());
                sendMsgToAllClient(name+"此用户已被封禁");
                //用来存封禁用户的Map是以ip为键，用户名为值，把这个封禁的用户添加进Map
                ChatServer.blockedName.put(ip,name);
                sendMsgToOneClient(clientName,"你已被封禁");
            }else {
                pp.println(clientName+"无此用户");
            }
        }
    }

    /**
     *
     * @param destName 单聊的用户
     * @param privateMsg 单聊的消息
     * @throws Exception 异常
     */
    private void sendMsgToOneClient(String destName, String privateMsg) throws Exception {
        Set<Socket> keySet = ChatServer.allSocketOnLine.keySet();
        for (Socket sk : keySet) {
            User user = ChatServer.allSocketOnLine.get(sk);
            if (user.getUserName().equals(destName)) {
                PrintStream ps = new PrintStream(sk.getOutputStream());
                ps.println(privateMsg);
                break;
            }
        }
    }

    /**
     * 把当前客户端发来的消息推送给全部在线的socket
     *
     * @param msg
     */
    private void sendMsgToAllClient(String msg) throws Exception {
        Set<Socket> keySet = ChatServer.allSocketOnLine.keySet();
        for (Socket sk : keySet) {
            PrintStream ps = new PrintStream(sk.getOutputStream());
            ps.println(msg);
        }
    }

    /**
     * 判断用户是否存在
     * @param userInfo
     * @return
     * @throws IOException
     */
    private boolean userExist(String userInfo) throws IOException {
        BufferedReader br2 = new BufferedReader(new FileReader("chatserver\\User\\Users.txt"));
        String str = null;
        String userName = userInfo;
        while ((str = br2.readLine()) != null){
            String[] split = str.split(",");
            if(userName.equals(split[0])){
                br2.close();
                return true;
            }
        }
        br2.close();
        return false;
    }


    // 对在线用户进行排序
    private  void doSort(BufferedReader br) throws Exception {
        PrintWriter pw=new PrintWriter(socket.getOutputStream());
        TreeSet<User> userTreeSet=new TreeSet<>(Comparator.comparing(User::getUserName));
        Set<Map.Entry<Socket, User>> entries = ChatServer.allSocketOnLine.entrySet();
        for (Map.Entry<Socket, User> entry : entries) {
            userTreeSet.add(entry.getValue());
        }
        for (User user : userTreeSet) {
            System.out.println(user);
        }
        pw.println("升序排序后："+userTreeSet);
        pw.flush();
    }


    /*private void getUserBySex(BufferedReader br) throws IOException {
        PrintStream pp = new PrintStream(socket.getOutputStream());
        String inputSex = br.readLine();
        Set<Map.Entry<Socket, User>> entries = ChatServer.allSocketOnLine.entrySet();
        for (Map.Entry<Socket, User> entry : entries) {
            if (inputSex.equals(entry.getValue().getSex())) {
                pp.println(inputSex + "性用户" + entry.getValue() + "已上线");
            } else {
                pp.println("无符合标准的用户");
            }
    }*/

    // 查询不同性别的在线用户
    private void getUserBySex(BufferedReader br) throws IOException {
        String sex = br.readLine();

        getUserBySex(sex);
    }
    private void getUserBySex(String sex) throws IOException {
        ArrayList<User> malelist = new ArrayList<>();
        ArrayList<User> femalelist = new ArrayList<>();
        int sum = 0;

        Set<Map.Entry<Socket, User>> entrySet = ChatServer.allSocketOnLine.entrySet();
        for (Map.Entry<Socket, User> entry : entrySet) {
            User user = entry.getValue();
            if(sex.equals("男")){
                if (user.getSex().equals(sex)){
                    malelist.add(user);
                    sum++;
                }
            }
            if (sex.equals("女")){
                if (user.getSex().equals(sex)){
                    femalelist.add(user);
                    sum++;
                }
            }
        }
        Set<Socket> keySet = ChatServer.allSocketOnLine.keySet();
        for (Socket sk : keySet) {
            PrintStream ps = new PrintStream(sk.getOutputStream());
            if(sex.equals("男")){
                ps.println("当前男性在线人数:"+sum+"     已上线的男性用户: " + malelist);
            }else if(sex.equals("女")){
                ps.println("当前女性在线人数:"+sum+"     已上线的女性用户: " + femalelist);
            }
        }
    }
    /**
     * 获取所有登录用户
     */
    public void getAllScoke() throws IOException {
        PrintWriter pw=new PrintWriter(socket.getOutputStream());
        Set<Map.Entry<Socket, User>> entries = ChatServer.allSocketOnLine.entrySet();
        for (Map.Entry<Socket, User> entry : entries) {
            System.out.println(entry.getValue());
            pw.println(entry.getValue());
            pw.flush();
        }
        pw.flush();
    }
}
