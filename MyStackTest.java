/*
1.一维数组
2.可储存任何类型
3.push方法压栈
4.pop方法弹栈
5.栈满、栈空都要有提示信息
 */



public class MyStackTest {
    public static void main(String[] args) {
        Mystack mystack = new Mystack(5);
        mystack.push(4545);
        mystack.pop();
        mystack.pop();
        mystack.push(4545);
        mystack.push(4545);
        mystack.push(4545);
        mystack.push(4545);
        mystack.push(4545);
        mystack.push(4545);
        mystack.pop();
        mystack.pop();
        mystack.pop();
        mystack.pop();
        mystack.pop();
        mystack.pop();
        mystack.pop();

    }


}

