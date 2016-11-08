package com.example.eli.a24dian.model;
import java.util.*;
/**
 * Created by Eli on 2016/11/7.
 */


public class Stack<T> {
    private ArrayList<T> stack;
    /**
     *   功能：初始化堆栈。
     * */
    public Stack(){
        this.stack = new ArrayList<T>();
    }

    /**
     *   功能：查看栈顶元素。
     * */
    public T peek(){
        return this.stack.size()>0?this.stack.get(this.stack.size()-1):null;
    }

    /**
     *   功能：将元素压栈。
     * */
    public boolean push(T e){
        boolean mark = false;
        if(e != null){
            this.stack.add(e);
            mark = true;
        }
        return mark;
    }

    /**
     *   功能：将栈顶元素弹栈。
     * */
    public T pop(){
        T e = null;
        if(this.stack.size()>0){
            e = this.stack.remove(this.stack.size()-1);
        }
        return e;
    }

    /**
     *   功能：判断栈是否为空。
     * */
    public boolean isEmpty(){
        return this.stack.size() == 0 ;
    }

    /**
     *   功能：返回栈内元素的个数。
     * */
    public int size(){
        return this.stack.size();
    }
    public String toString(){
        StringBuffer sub = new StringBuffer();
        sub.append("{");
        for(int i=0;i<this.stack.size();i++){
            sub.append(this.stack.get(i));
            sub.append(",");
        }
        sub.append("}");
        return sub.toString();
    }
}