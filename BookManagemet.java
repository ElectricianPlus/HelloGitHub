package cn.electrician.链表;
import java.util.*;
public class BookManagemet {
    static  HashMap<String, ArrayList<Book>> hashMap = new HashMap<>();
    public static void main(String[] args) {
        ArrayList<Book> mz = new ArrayList<>();
        mz.add(new Book("西游记",20));
        mz.add(new Book("红楼梦",10));
        hashMap.put("名著",mz);
        ArrayList<Book> it = new ArrayList<>();
        it.add(new Book("java",21));
        it.add(new Book("javascrip",19));
        hashMap.put("it书籍",it);
        while (true) {
            System.out.println("--------欢迎来到图书管理系统--------");
            System.out.println("1.查看所有书籍");
            System.out.println("2.添加图书");
            System.out.println("3.删除图书");
            System.out.println("4.修改图书");
            System.out.println("5.退出系统");
            System.out.println("请输入你的选择：");

            Scanner sc = new Scanner(System.in);
            int number = sc.nextInt();
            switch (number){
                case 1:
                    System.out.println("查看所有书籍");
                    BookManagemet.showAllBooks();
                    break;
                case 2:
                    System.out.println("添加图书");
                    BookManagemet.addBooks();

                    break;
                case 3:
                    System.out.println("删除图书");
                    BookManagemet.deleteBooks();
                    break;
                case 4:
                    System.out.println("修改图书");
                    BookManagemet.editBooks();
                    break;
                case 5:
                    System.out.println("谢谢使用");
                    return; //结束main方法
            }
        }
    }
    public static void showAllBooks(){
        System.out.println("类型\t\t书名\t\t\t价格");
        Set<Map.Entry<String, ArrayList<Book>>> entries = hashMap.entrySet();
        for (Map.Entry<String, ArrayList<Book>> entry : entries) {
            String type = entry.getKey();
            System.out.println(type);

            ArrayList<Book> booklist = entry.getValue();
            for (Book book : booklist) {
                System.out.println("\t\t\t"+book.getName()+"\t\t\t"+book.getPrice());
            }
        }

    }
    public static void addBooks(){
        /*
        提示输入
        键盘输入图书类型
        get出这个图书类型的集合
        判断是否存在，不存在则new一个集合
        提示输入图书名字
        判断是否存在这本书，存在则提示已存在，return结束方法
        不存在则提示输入价格
        new出一个新的书对象，把名字和价格通过构造方法传进去
        把书add到集合中
        把集合put到树中
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入图书类型");
        String type = scanner.next();
        ArrayList<Book> bookArrayList = hashMap.get(type);
        if(bookArrayList==null){
            bookArrayList=new ArrayList<Book>();
        }
        System.out.println("输入书名");
        String bookname = scanner.next();
        for (Book book : bookArrayList) {
            if (bookname.equals(book.getName())){
                System.out.println("此书已存在");
                return;
            }
        }
        System.out.println("输入价格");
        double price = scanner.nextDouble();
        Book book = new Book(bookname,price);
        bookArrayList.add(book);
        hashMap.put(type,bookArrayList);
    }
    public static void deleteBooks(){
        /*
        提示输入书类
        搜索有有无，无，return
        有，提示输入书名
        搜索有无，有，删除
        无return
         */
        System.out.println("输入要删除的书类");
        Scanner scanner = new Scanner(System.in);
        String type = scanner.next();
        ArrayList<Book> bookArrayList = hashMap.get(type);
        if (hashMap.containsKey(type)) {
            System.out.println("无此类书");
            System.out.print("");//上面复制了一块代码，加这句去掉重复代码提示
            return;
        }
        System.out.println("输入书名");
        String name = scanner.next();
        for (int i = 0;i<bookArrayList.size();i++){
            Book book = bookArrayList.get(i);
            if (book.getName().equals(name)) {
                bookArrayList.remove(i);
                return;
            }
        }
        System.out.println("无此书");
        return;
    }
    public static void editBooks(){
        System.out.println("输入要编辑的书类");
        Scanner scanner = new Scanner(System.in);
        String type = scanner.next();
        ArrayList<Book> bookArrayList = hashMap.get(type);
        if (hashMap.containsKey(type)) {
            System.out.println("无此类书");
            return;
        }
        System.out.println("输入要编辑的书名");
        String name = scanner.next();
        for (int i = 0;i<bookArrayList.size();i++){
            Book book = bookArrayList.get(i);
            if (book.getName().equals(name)) {
                book.setName(name);
                System.out.println("输入新的价格");
                book.setPrice(scanner.nextDouble());
                return;
            }
        }
        System.out.println("无此书");
        return;
    }
}
