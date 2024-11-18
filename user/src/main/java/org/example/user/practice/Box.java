package org.example.user.practice;

import java.util.Arrays;

public class Box<T> {
    private T content;

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Box{" +
                "content=" + content +
                '}';
    }

    public static <K> void printArr(K[] ts) {
        if (ts == null || ts.length == 0) {
            return;
        }
        for (K t : ts) {
            System.out.println(t);
        }
    }


    public static <M extends Comparable<M>> M getMax(M[] ts) {
        if (ts == null || ts.length == 0) {
            return null;
        }
        M max = ts[0];
        for (M t : ts) {
            if (t.compareTo(max) > 0) {
                max = t;
            }
        }


//        Comparator<Pair> comparator = Comparator.comparing(Pair::getFirst);
//        ArrayList<Pair> list = new ArrayList<>();
//        Collections.sort(list,comparator);


        return max;
    }

    public static <M> M[] copyArr(M[] ts) {
        M[] ms = Arrays.copyOf(ts, ts.length);
        return ms;
    }
}
