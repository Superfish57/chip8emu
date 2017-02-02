package com.fish.chip8emu;

/**
 * Created by User on 12/19/2016.
 */
public class utils {


    public static void print(String text) {

        System.out.println(text);
    }

    public static void printOpcode(int opcode) {
        print(String.format("%02X", opcode & 0xFFFF));
        //System.out.println(opcode);
    }

}