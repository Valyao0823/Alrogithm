package com.example.Calculation;

import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by hesolutions on 2016-05-24.
 */
public class Main {
    static TCPClient mTcpClient;
    private static float totallux = 300;
    private static float lightlux = 200;
    public static void main(String[] args) {

        // (X - Xc)^2 + (Y - Yc)^2 + 9*(Z - Zc)^2 =9
        // first is Xc, Yc, Zc
        //function is below

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("background thread start");
                mTcpClient = new TCPClient();
                mTcpClient.run();
            }
        };
        Thread thread = new Thread(r);
        thread.start();
        long startTime = System.currentTimeMillis();
        ArrayList<Position> positions = new ArrayList<>();
        Position position1 = new Position(0,0);
        Position position = new Position(2,5);
        Position position2 = new Position(7,3);
        positions.add(position1);
        positions.add(position);
        positions.add(position2);
        double percentage = (totallux - lightlux) / totallux;
        double answer1 = Calculation(2, 2, positions);
        int Answer1 = (int)(answer1 * 100 * percentage);
        double answer2 = Calculation(6, 2, positions);
        int Answer2 = (int)(answer2 * 100 * percentage);
        double answer3 = Calculation(3, 7, positions);
        int Answer3 = (int)(answer3 * 100 * percentage);
        double answer4 = Calculation(8, 6, positions);
        int Answer4 = (int)(answer4 * 100 * percentage);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime +"ms");
        System.out.println(percentage+"% --> light percentage");
        System.out.println(Answer1);
        System.out.println(Answer2);
        System.out.println(Answer3);
        System.out.println(Answer4);

        try {
            thread.sleep(5000);
            System.out.println("inner command starts");
            mTcpClient.sendMessage("AT+TXA=201,<" + Answer1 + ">\n");
            mTcpClient.sendMessage("AT+TXA=211,<" + Answer2 + ">\n");
            mTcpClient.sendMessage("AT+TXA=203,<" + Answer3 + ">\n");
            mTcpClient.sendMessage("AT+TXA=210,<" + Answer4 + ">\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static double Calculation(double x, double y, ArrayList<Position> Pos){
        double answer = 0;
        for (Position position:Pos){
            double xc = position.getX();
            double yc = position.getY();
            double square = 25 - (pow((x - xc),2) + pow((y - yc),2));
            if (square>answer)answer = square;
        }
        if (answer > 0) {
            answer = sqrt(answer / 25);
        }else {answer = 0;}
        return answer;
    }

    private static class Position{
        private double x;
        private double y;
        Position(double x, double y)
        {
            this.x = x;
            this.y = y;
        }
        private void setX(double x){this.x = x;}
        private void setY(double y){this.y = y;}
        private double getX(){return this.x;}
        private double getY(){return this.y;}
    }
}
