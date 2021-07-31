import java.util.Arrays;
public class Mystack {
    int length;
    String stack;
    Object[] objects;
    public Mystack(int i) {
        length = i;
        objects = new Object[length];
    }

    public void push(Object obj){
        for (int j = 0;j<objects.length;j++){
            if (j==objects.length-1&&objects[j]!=null){
                System.out.println("栈满");
                break;
            }else if (objects[j]==null){
                objects[j]=obj;
                stack = Arrays.toString(objects);
                System.out.println(stack);
                break;
            }
        }
    }
    public void pop(){
        for (int j =objects.length-1;j >= 0;j--){
            if (objects[0]==null){
                System.out.println("栈空");
                break;
            }else if (objects[j]!=null){
                objects[j]=null;
                stack = Arrays.toString(objects);
                System.out.println(stack);
                break;
            }
        }
    }

}
