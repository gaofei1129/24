package com.example.eli.a24dian.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Eli on 2016/10/27.
 */

public class Card {
    //static List<Node> result = new ArrayList<Node>();

    public static Set<String> traverse(int[] a) {
        List<Node> result = new ArrayList<Node>();
        Set<String> calculateSet = new HashSet<String>();
        if (a.length != 4) {
            System.out.println("不得多于或少于4个数");
            return null;
        }
        Double[] dArrays = new Double[4];
        dArrays[0] = a[0] + 0D;
        dArrays[1] = a[1] + 0D;
        dArrays[2] = a[2] + 0D;
        dArrays[3] = a[3] + 0D;
        List<Node> headerList = null;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == i) {
                    continue;
                }
                for (int k = 0; k < 4; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    headerList = createHeaderList(dArrays[i], dArrays[j]);
                    List<Node> chooseThreeNum = null;
                    for (Node t : headerList) {
                        chooseThreeNum = chooseThreeOrFourNum(t, dArrays[k]);
                        List<Node> chooseFourNum = null;
                        for (Node temp : chooseThreeNum) {
                            chooseFourNum = chooseThreeOrFourNum(temp, dArrays[6 - (i + j + k)]);
                            pickNode(chooseFourNum,result);
                        }
                    }
                }
            }
        }
        getAllCalculate(calculateSet,result);
        if (calculateSet.size() > 0) {
            display(calculateSet);
            return calculateSet;
        } else {
            return null;
        }
    }
    private static void display(Set<String> calculateSet) {
        System.out.println("共有解:" + calculateSet.size() + "个,具体如下:");
        for (String s : calculateSet) {
            System.out.println(s + " = 24");
        }
    }

    private static void getAllCalculate(Set<String> calculateSet,List<Node> result) {
        if (result.size() < 1) {
            System.out.println("无解");
            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
        }
        Node second = null;
        Node first = null;
        for (Node t : result) {
            second = t.getPre_node();
            first = second.getPre_node();
            if (second.get_a().doubleValue() == first.getValue().doubleValue()) {
                second.setA_cul_str(first.getCul_str());
            } else {
                second.setB_cul_str(first.getCul_str());
            }
            second.doCalculate();

            if (t.get_a().doubleValue() == second.getValue().doubleValue()) {
                t.setA_cul_str(second.getCul_str());
            } else {
                t.setB_cul_str(second.getCul_str());
            }
            t.doCalculate();
            calculateSet.add(filterCalculate(t.getCul_str()));

        }
    }

    private static String filterCalculate(String str) {
        str = str.substring(1, str.length() - 1);
        str = str.replace(".0", "");
        str = str.replace("*", "×");
        str = str.replace("/", "÷");
        return str;
    }

    private static void pickNode(List<Node> list,List<Node> result) {
        for (Node t : list) {
            if (judge24(t.getValue())) {
                result.add(t);
            }
        }
    }

    private static boolean judge24(Double d) {
        if (Math.abs(d.doubleValue() - 24) <= 0.000001) {
            return true;
        }
        return false;

    }

    private static List<Node> chooseThreeOrFourNum(Node t, Double b) {
        List<Node> result = new ArrayList<Node>();
        Double a = t.getValue();
        Node n1 = new Node(a, b, '+');
        Node n2 = new Node(a, b, '-');
        Node n3 = new Node(a, b, '-', true);
        Node n4 = new Node(a, b, '*');
        Node n5 = new Node(a, b, '/');
        Node n6 = new Node(a, b, '/', true);
        n1.doCalculate();
        n2.doCalculate();
        n3.doCalculate();
        n4.doCalculate();
        n5.doCalculate();
        n6.doCalculate();
        insertNode(n1, result);
        insertNode(n2, result);
        insertNode(n3, result);
        insertNode(n4, result);
        insertNode(n5, result);
        insertNode(n6, result);
        n1.setPre_node(t);
        n2.setPre_node(t);
        n3.setPre_node(t);
        n4.setPre_node(t);
        n5.setPre_node(t);
        n6.setPre_node(t);
        return result;
    }

    private static List<Node> createHeaderList(Double a, Double b) {
        List<Node> result = new ArrayList<Node>();
        Node n1 = new Node(a, b, '+');
        Node n2 = new Node(a, b, '-');
        Node n3 = new Node(a, b, '-', true);
        Node n4 = new Node(a, b, '*');
        Node n5 = new Node(a, b, '/');
        Node n6 = new Node(a, b, '/', true);
        n1.doCalculate();
        n2.doCalculate();
        n3.doCalculate();
        n4.doCalculate();
        n5.doCalculate();
        n6.doCalculate();
        insertNode(n1, result);
        insertNode(n2, result);
        insertNode(n3, result);
        insertNode(n4, result);
        insertNode(n5, result);
        insertNode(n6, result);
        return result;
    }


    private static void insertNode(Node p, List<Node> list) {
        if (p.getValue() != null) {
            list.add(p);
        }
    }

}

class Node {
    Double _a;
    Double _b;
    char opr;
    Double value;
    boolean is_rev_order;
    String a_cul_str;//_a的算式
    String b_cul_str;//_b的算式
    String cul_str;//自身的算式
    Node pre_node;
    Node next_branch1;
    Node next_branch2;
    Node next_branch3;
    Node next_branch4;
    Node next_branch5;
    Node next_branch6;

    public Node(Double a, Double b, char opr) {
        this._a = a;
        this._b = b;
        this.opr = opr;
        this.a_cul_str = Double.toString(_a);
        this.b_cul_str = Double.toString(_b);
    }

    public Node(Double a, Double b, char opr, boolean is_rev_order) {
        this._a = a;
        this._b = b;
        this.opr = opr;
        this.is_rev_order = is_rev_order;
        this.a_cul_str = Double.toString(_a);
        this.b_cul_str = Double.toString(_b);
    }

    public Double getValue() {
        return value;
    }

    public void doCalculate() {
        switch (opr) {
            case '+':
                cul_str = "(" + a_cul_str + "+" + b_cul_str + ")";
                value = _a + _b;
                break;
            case '-':
                if (is_rev_order) {
                    cul_str = "(" + b_cul_str + "-" + a_cul_str + ")";
                    value = _b - _a;
                } else {
                    cul_str = "(" + a_cul_str + "-" + b_cul_str + ")";
                    value = _a - _b;
                }
                break;
            case '*':
                cul_str = "(" + a_cul_str + "*" + b_cul_str + ")";
                value = _a * _b;
                break;
            case '/':
                if (is_rev_order) {
                    if (_a != 0) {
                        cul_str = "(" + b_cul_str + "/" + a_cul_str + ")";
                        value = _b / _a;
                    }

                } else {
                    if (_b != 0) {
                        cul_str = "(" + a_cul_str + "/" + b_cul_str + ")";
                        value = _a / _b;
                    }
                }
                break;
        }
    }

    public Double get_a() {
        return _a;
    }

    public void set_a(Double _a) {
        this._a = _a;
    }

    public Double get_b() {
        return _b;
    }

    public void set_b(Double _b) {
        this._b = _b;
    }

    public String getA_cul_str() {
        return a_cul_str;
    }

    public void setA_cul_str(String a_cul_str) {
        this.a_cul_str = a_cul_str;
    }

    public String getB_cul_str() {
        return b_cul_str;
    }

    public void setB_cul_str(String b_cul_str) {
        this.b_cul_str = b_cul_str;
    }

    public String getCul_str() {
        return cul_str;
    }

    public void setCul_str(String cul_str) {
        this.cul_str = cul_str;
    }

    public Node getPre_node() {
        return pre_node;
    }

    public void setPre_node(Node pre_node) {
        this.pre_node = pre_node;
    }

    public Node getNext_branch1() {
        return next_branch1;
    }

    public void setNext_branch1(Node next_branch1) {
        this.next_branch1 = next_branch1;
    }

    public Node getNext_branch2() {
        return next_branch2;
    }

    public void setNext_branch2(Node next_branch2) {
        this.next_branch2 = next_branch2;
    }

    public Node getNext_branch3() {
        return next_branch3;
    }

    public void setNext_branch3(Node next_branch3) {
        this.next_branch3 = next_branch3;
    }

    public Node getNext_branch4() {
        return next_branch4;
    }

    public void setNext_branch4(Node next_branch4) {
        this.next_branch4 = next_branch4;
    }

    public Node getNext_branch5() {
        return next_branch5;
    }

    public void setNext_branch5(Node next_branch5) {
        this.next_branch5 = next_branch5;
    }

    public Node getNext_branch6() {
        return next_branch6;
    }

    public void setNext_branch6(Node next_branch6) {
        this.next_branch6 = next_branch6;
    }


}
