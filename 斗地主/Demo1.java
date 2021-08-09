package cn.electrician.链表.斗地主;

import java.util.ArrayList;
import java.util.Collections;

/*
    目标：完成斗地主游戏的发牌功能

    需求：
        按照斗地主的规则，完成洗牌发牌的动作。
        具体规则：使用54张牌打乱顺序,三个玩家参与游戏，三人交替摸牌，每人17张牌，最后三张留作底牌。
        牌号：{"2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3"};
        花色：{"♠", "♥", "♣", "♦"};

    步骤分析：


 */
public class Demo1 {
    private static ArrayList<Card> cardList = new ArrayList<>();

    static {

        //初始化纸牌
        cardList.add(new Card("大王", "",1));
        cardList.add(new Card("小王", "",2));

        String[] numbers = {"2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3"}; //牌号
        String[] colors = {"♠", "♥", "♣", "♦"};

        int id = 3;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < colors.length; j++) {
                cardList.add(  new Card(colors[j], numbers[i],id)   );
                id++;
            }
        }

    }

    public static void main(String[] args) {
        System.out.println("洗牌前：" + cardList);

        //洗牌
        Collections.shuffle(cardList);
        System.out.println("洗牌后：" + cardList);

        //使用3个集合代替三个玩家
        ArrayList<Card> player1 = new ArrayList<>();
        ArrayList<Card> player2 = new ArrayList<>();
        ArrayList<Card> player3 = new ArrayList<>();

        //发牌：将集合中的前51张纸牌平均分给3个玩家
        for (int i = 0; i < cardList.size() - 3; i++) {
            Card card = cardList.get(i);
            /*
              player1  0  3  6  9  i%3==0
              player2  1  4  7  10 i%3==1
              player3  2  5  8  11 i%3==2
             */

            if (i % 3 == 0) {
                player1.add(card);
            } else if (i % 3 == 1) {
                player2.add(card);
            } else {
                player3.add(card);
            }
        }

        //创建一个底牌集合
        ArrayList<Card> diPai = new ArrayList<>();
        Collections.addAll(diPai, cardList.get(51), cardList.get(52), cardList.get(53));

        //排序
        Collections.sort(player1);
        Collections.sort(player2);
        Collections.sort(player3);

        //看牌
        System.out.println("玩家1：" + player1);
        System.out.println("玩家2：" + player2);
        System.out.println("玩家3：" + player3);
        System.out.println("底牌：" + diPai);

    }
}
