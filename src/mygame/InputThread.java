/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.Scanner;

/**
 *
 * @author misat11
 */
public class InputThread extends Thread {

    private Main main;

    public InputThread(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String msg = sc.nextLine();
            String[] args = msg.split(" ");
            if (main.commands.containsKey(args[0])) {
                main.commands.get(args[0]).callFromServer(msg);
            } else {
                System.out.println("Command /" + args[0] + " not found. Type \"/help\" for help.");
            }
        }
    }
}
