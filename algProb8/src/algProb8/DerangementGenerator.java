package algProb8;

import java.io.*;
import java.util.*;

public class DerangementGenerator {
    public void derangements(int[] a) { d(a, new LinkedList<Integer>()); }

    public void d(int[] a, LinkedList<Integer> curr) {
        if (curr.size() == a.length)
            print(curr);
        else {
            for (int i = 0; i < a.length; i++) {
                if (!curr.contains(a[i]) && i != curr.size()) {
                    curr.addLast(a[i]);
                    d(a, curr);
                    curr.removeLast();
                }
            }
        }
    }

    public void print(List<Integer> l) {
        for (int i = 0; i < l.size() - 1; i++) {
            System.out.print(l.get(i) + ", ");
        }
        System.out.println(l.get(l.size() - 1));
    }

    public static void main(String[] args) {
        /*if (args.length < 1) {
            System.err.println("Usage: java DerangementGenerator N");
            System.exit(-1);
        }*/
        int n = 4; //Integer.parseInt(args[0]);

        DerangementGenerator dg = new DerangementGenerator();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i + 1;
        }

        dg.derangements(a);
    }
}