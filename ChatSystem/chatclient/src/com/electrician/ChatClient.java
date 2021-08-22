package com.electrician;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
    实现客户端的开发
    客户端有两个功能:
        1.客户端可以收到服务端发送的各种消息,并打印
        2.客户端发送各种消息给服务端

    客户端和服务端消息通信规则约定(简称通信码):
         100: 表示登陆
         101: 表示注册

         200: 表示单聊
         201: 表示群聊
         202: 表示随机聊

         300: 客户端获取已上线的所有用户
         301: 客户端获取上线用户根据用户名升序排序
         302: 客户端获取指定性别的上线用户

         401: 客户端获取聊天记录

         举例:客户端给服务端发送100,表示客户端要进行登录操作
 */
public class ChatClient {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输服务端IP：");
            String ip = sc.nextLine();

            // 1.创建于服务端的Socket,并保存到成员变量
            Socket socket = new Socket(ip, 6969);
            System.out.println("连接服务器成功!");

            // 2.分配一个线程为客户端socket服务接收服务端发来的消息
            new ChatClientReaderThread(socket).start();

            // 获取Socket的输出流转成打印流,方便一次打印一行数据
            PrintStream ps = new PrintStream(socket.getOutputStream());

            // 用户可以循环操作
            while (true) {
                // 3.打印操作信息
                System.out.println("1.登陆");
                System.out.println("2.注册");
                System.out.println("3.单聊");
                System.out.println("4.群聊");
                System.out.println("5.随机聊");
                System.out.println("6.获取上线用户根据用户名升序排序");
                System.out.println("7.获取指定性别的上线用户");
                System.out.println("8.获取已上线的所有用户");
                System.out.println("9.查看聊天记录");
                System.out.println("10.拉黑");
                System.out.println("11.退出");
                System.out.println("请输入你的选择：");

                // 4.使用Scanner进行键盘输入
                String operation = sc.nextLine();
                // 判断消息类型
                switch (operation) {
                    case "1": // 1.表示登陆
                        login(ps);
                        break;
                    case "2": // 2.表示注册
                        register(ps);
                        break;
                    case "3": // 3.表示单聊
                        sendOne(ps);
                        break;
                    case "4": // 4.表示群聊
                        sendAll(ps);
                        break;
                    case "5": // 5.表示随机聊
                        randomSend(ps);
                        break;
                    case "6": // 6.获取上线用户根据用户名升序排序
                        orderUsersByUserName(ps);
                        break;
                    case "7": // 7.获取指定性别的上线用户
                        getUserBySex(ps);
                        break;
                    case "8": // 8.获取已上线的所有用户
                        getAllUsers(ps);
                        break;
                    case "9": // 9.获取聊天记录
                        getChatNotes(ps); // 获取聊天记录
                        break;
                    case "10":// 10.拉黑
                        block(ps);
                        break;
                    case "11":// 11.退出
                        break;
                    default:
                        System.out.println("没有这样的操作!");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 拉黑
    private static void block(PrintStream ps){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入禁言名称：");
        String name = sc.next();
        ps.println("209");
        ps.println(name);
    }

    // 获取聊天记录
    private static void getChatNotes(PrintStream ps){
        System.out.println("将获取聊天记录");
        ps.println(401);
    }

    // 随机聊
    private static void randomSend(PrintStream ps) {
        System.out.println("进入随机聊");
        System.out.println("请输出要随机发送的话");
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        ps.println(202);
        ps.println(str);
    }

    // 获取已上线的所有用户
    private static void getAllUsers(PrintStream ps) {
        System.out.println("获取已上线的所有用户");
        ps.println(300);
    }

    // 获取指定性别的上线用户
    private static void getUserBySex(PrintStream ps) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要显示的性别：");
        String sex = sc.nextLine();
        switch (sex){
            case "男":
                ps.println("302");
                ps.println(sex);
                break;
            case "女":
                ps.println("302");
                ps.println(sex);
                break;
            default:
                System.out.println("性别输入错误！");
                break;
        }
    }

    // 获取上线用户根据用户名升序排序
    private static void orderUsersByUserName(PrintStream ps) {
        //System.out.println("客户端[获取上线用户根据用户名升序排序]功能暂未实现");
        System.out.println("上线用户根据用户名升序排序：");
        ps.println(301);
    }

    // 注册
    private static void register(PrintStream ps) {
        System.out.println("进入注册");
        try {
            Scanner sc = new Scanner(System.in);
            //用户名
            System.out.println("输入姓名：");
            String registerName = sc.nextLine();
            //校验用户名
            //注册的用户名必须符合：首字母为a-z或A-Z，其它部分可以为字母、数字或者下划线；长度在10个字符以内
            Pattern userRegular = Pattern.compile("^[a-zA-Z]\\w{3,10}$");
            Matcher matcher = userRegular.matcher(registerName);
            if (!matcher.find()) {
                System.out.println("请输入正确的用户名！");
                return;
            }
            //性别
            System.out.println("输入性别：");
            String registerSex = sc.next();
            if (!registerSex.equals("男") && !registerSex.equals("女")) {
                System.out.println("------------------请输入正确的性别！---------------");
                return;
            }
            //年龄
            System.out.println("输入年龄");
            int age = sc.nextInt();
            ps.println("101");
            //注册格式
            ps.println(registerName + "," + registerSex + "," + age);
            System.out.println("发送了数据！");
        } catch (Exception e) {
            System.out.println("请输入正确的年龄！");
        }
    }

    // 单聊
    private static void sendOne(PrintStream ps) {
        System.out.println("进入单聊");
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入对方姓名：");
        String destName = sc.nextLine();

        System.out.println("请输入消息：");
        String msg = sc.nextLine();

        // 200: 表示单聊
        ps.println("200");
        ps.println(destName);
        ps.println(msg);

        /* 客户端发给服务器的数据如下
        200
        单聊姓名
        单聊消息内容
         */
    }

    // 群聊
    private static void sendAll(PrintStream ps) {
        System.out.println("进入群聊");
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入群发信息：");
        String msg = sc.nextLine();

        // 201: 表示群聊
        ps.println("201");
        ps.println(msg);

        /* 客户端发给服务器的数据如下
        201
        群聊消息内容
         */
    }

    // 登录
    public static void login(PrintStream ps) {
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入登录名称：");
        String name = sc.nextLine();

        // 100: 表示登陆
        ps.println("100");
        ps.println(name);
        /* 客户端发给服务器的数据如下
        100
        姓名,性别,年龄
         */
    }
}