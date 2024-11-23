package org.example.user.controller;

public class Pair<T> {


    private transient T first;
    private T second;

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public Pair() {
    }

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

//    public static <K> Pair<K> create(K custom.env, K b) {
//        return new Pair<>(custom.env,b);
//    }


}
