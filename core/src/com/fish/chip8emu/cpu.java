package com.fish.chip8emu;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.fish.chip8emu.utils.isBitSet;
import static com.fish.chip8emu.utils.print;
import static com.fish.chip8emu.utils.printOpcode;


/**
 * Created by User on 12/18/2016.
 */
public class cpu {

    boolean debug = true;
    boolean dump= false;

    public Pixmap screenMap = new Pixmap(64,48, Pixmap.Format.Alpha);

    private short opcode;

    private byte[] gfx = new byte[64*32];

    //4046 bytes
   private byte[] memory = new byte[4096];

    // 16 registers, V0 through V15
    private byte[] V = new byte[16];

    private int I;
    private short pc;

    private short delay_timer;
    private short sound_timer;

    //16 itmes
    private short[] stack;
    short sp;

    //16 keys to kep track of
    private byte[] key;


    private short[] fontset = {
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0x90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };

    public void initialize(){
        System.out.println("Initializing chip8 system...");
        pc = 0x200;
        opcode = 0;
        I = 0;
        sp = 0;

        print("Clearing registers...");
        for(byte mByte: V){
            mByte = 0;
        }
        print("All registers cleared!");

        System.out.println("Clearing memory...");
        for(int mByte: memory){
            mByte = 0;
        }
        print("Memory cleared!");

        print("Loading fonts...");


        for (int i = 0; i < fontset.length; i++){
            memory[i] = (byte) fontset[i];
        }


        print("Fonts loaded!");



    }

    public void loadGame(String path) {

        FileHandle gameHandle = Gdx.files.internal(path);
        byte[] bytes = gameHandle.readBytes();

        System.out.println("Reading game...");
        StringBuilder sb = new StringBuilder();

        if(debug) {

            for (int i = 0; i < bytes.length; i = i + 2) {
                System.out.println("0x" + String.format("%02X", bytes[i]) + String.format("%02X", bytes[i + 1]));
            }
        }
        System.out.println("Loading game into memory...");


        for (int i = 0; i < bytes.length; i++){
            memory[i+512] = bytes[i];
        }

        System.out.println("Game successfully loaded into memory!");




    }


    public void emulateCycle(){

        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(memory[pc]);
        bb.put(memory[pc + 1]);
        opcode = bb.getShort(0);

        //opcode = (short) ((memory[pc] << 8) | memory[pc+1]);
        printOpcode(opcode & 0xFFFF);
        switch ((opcode & 0xF000) >> 12){

            case 0xF:
                switch (opcode & 0x00FF) {
                    case 0x07:

                        break;

                    case 0x0A:

                        break;

                    case 0x15:
                        delay_timer = (short) (opcode & 0x0F00);
                        break;

                    case 0x18:

                        break;

                    case 0x1E:

                        break;

                    case 0x29:

                        break;

                    case 0x33:

                        break;

                    case 0x55:

                        break;

                    case 0x65:

                        break;

                    default:
                        print("Bad F code");
                        pc += 2;

                }
                break;


            case 0xA:
                I =  (opcode & 0x0FFF);
                pc += 2;
                break;

            case 0x2:
                

            case 0x3 :
                if(V[opcode & 0x0F00] == (opcode & 0x00FF)){
                    pc += 4;
                }
            case 0x6:
                V[(opcode & 0x0F0) >> 8] = (byte) (opcode & 0x00FF);
                pc += 2;
                break;

            case 0xD:

                short x = V[(opcode & 0x0F00) >> 8];
                short y = V[(opcode & 0x00F0) >> 4];
                short height = (short) (opcode & 0x000F);
                short pixel;

                V[0xF] = 0;
                for (int yline = 0; yline < height; yline++)
                {
                    pixel = memory[I + yline];
                    for(int xline = 0; xline < 8; xline++)
                    {
                        if((pixel & (0x80 >> xline)) != 0)
                        {
                            if(gfx[(x + xline + ((y + yline) * 64))] == 1)
                                V[0xF] = 1;
                            gfx[x + xline + ((y + yline) * 64)] ^= 1;
                        }
                    }
                }


                pc += 2;
                break;



            default:
                System.out.print("UNKNOWN OPCODE: ");
                printOpcode(opcode);

                if(debug && dump) {
                    print("");
                    print("Printing memory dump...");
                    print("");
                    for (int i = 0; i < memory.length; i++) {
                        System.out.println("0x" + String.format("%02X", memory[i]));

                    }
                    print("");
                    print("End of memory dump");
                    print("");
                }


                print("Exiting game!");
                //Gdx.app.exit();

        }
    }






}
