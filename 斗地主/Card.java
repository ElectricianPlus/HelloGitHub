package cn.electrician.链表.斗地主;

//纸牌类
public class Card implements Comparable<Card>{
    private String color;//花色
    private String number; //牌号
    private int id; //编号，作用是对纸牌进行排序

    //根据编号实现升序排序
    @Override
    public int compareTo(Card o) {
        return this.id - o.id;
    }

    public Card() {
    }

    public Card(String color, String number, int id) {
        this.color = color;
        this.number = number;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * 获取
     * @return number
     */
    public String getNumber() {
        return number;
    }

    /**
     * 设置
     * @param number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    public String toString() {
        return number+color; //2♠
    }


}