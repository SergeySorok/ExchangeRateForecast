package ru.liga;

import java.util.Scanner;

public class MyExeption {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            int s = sc.nextInt();
            System.out.println(s);
        } catch (Exception e) {
            e.getMessage();
            System.out.println(2+3);
        }
    }

}
