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
    private int pc;

    private short delay_timer;
    private short sound_timer;

    //16 itmes
    private int[] stack = new int[16];
    private int sp;

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

    public byte[] getGfx() {
        return gfx;
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
            memory[i+0x200] = bytes[i];
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

            case 0x1:
                pc = (opcode & 0x0FFF);
                break;


            case 0x0:
                switch (opcode & 0x00FF){
                    case 0xE0:
                        for(int i = 0; i < gfx.length; i++){
                            gfx[i] = 0;
                        }

                        pc =+ 2;
                        break;

                    case 0xEE:

                        sp--;
                        pc = stack[sp];
                        System.out.println(stack[sp] + "+++++++++++RETURN+++++++");

                        break;

                    default:
                        print("bad 0 code");
                        pc +=2;
                        break;

                }
                break;

            case 0xF:
                switch (opcode & 0x00FF) {
                    case 0x07:

                        V[(opcode & 0x0F00) >> 8] = (byte) delay_timer;
                        pc =+2;
                        break;

                    case 0x0A:
                            print("unimplemented F code");
                        break;

                    case 0x15:
                        delay_timer = (short) (opcode & 0x0F00);
                        pc += 2;
                        break;

                    case 0x18:
                        sound_timer = (short) (opcode & 0x0F00);
                        pc += 2;
                        break;

                    case 0x1E:
                            I =+ V[(opcode & 0x0F00) >> 8];
                        break;

                    case 0x29:

                        int sourceRegister = (opcode & 0x0F00) >> 8;
                        I = V[sourceRegister] * 5;
                        pc += 2;
                        break;

                    case 0x33:

                        memory[I]     = (byte) (V[(opcode & 0x0F00) >> 8] / 100);
                        memory[I + 1] = (byte) ((V[(opcode & 0x0F00) >> 8] / 10) % 10);
                        memory[I + 2] = (byte) ((V[(opcode & 0x0F00) >> 8] % 100) % 10);
                        pc += 2;
                        break;

                    case 0x55:
                            print("unimplemented F code");
                        break;

                    case 0x65:

                        int numRegisters = (opcode & 0x0F00) >> 8;
                        for (int counter = 0; counter <= numRegisters; counter++) {
                            V[counter] = memory[I + counter];
                        }
                        pc += 2;
                        break;

                    default:
                        print("Bad F code");
                        pc += 2;
                        break;

                }
                break;


            case 0xA:
                I =  (opcode & 0x0FFF);
                pc += 2;
                break;

            case 0x2:


                stack[sp] = pc;

                pc = (opcode & 0x0FFF);
                sp++;
                System.out.println(stack[sp] + "=======GOTO======" + pc);
                break;
            case 0x3 :

                if(V[(opcode & 0x0F00) >> 8] == (opcode & 0x00FF)){
                    pc += 4;
                } else {
                    pc =+ 2;
                }
                break;
            case 0x6:
                V[(opcode & 0x0F00) >> 8] = (byte) (opcode & 0x00FF);
                pc += 2;
                break;

            case 0x7:
                short add1 = (short) (opcode & 0x00FF);
                V[(opcode &0x0F00) >> 8] += add1;
                pc += 2;
                break;

            case 0x9:
                if(((opcode & 0x0F00) >> 8) != ((opcode & 0x00F0) >> 4)){
                    pc +=4;
                }else{
                    pc +=2;
                }
                break;

            case 0xD:

                short x = V[(opcode & 0x0F00) >> 8];
                short y = V[(opcode & 0x00F0) >> 4];
                short height = (short) (opcode & 0x000F);
                byte pixel;

                V[0xf] = 0;
                for (int yline = 0; yline < height; yline++)
                {
                    pixel = memory[I + yline];
                    for(int xline = 0; xline < 8; xline++)
                    {
                        if((pixel & (0x80 >> xline)) != 0)
                        {
                            if(gfx[(x + xline + ((y + yline) * 64))] == 1)
                                V[0xf] = 1;
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


                //print("Exiting game!");
                //Gdx.app.exit();
                pc += 2;
                break;

        }

        if(delay_timer > 0){
            delay_timer -= 1;
        }

        if(sound_timer > 0){
            sound_timer -= 1;
        }

    }


    /*
                short x = V[(opcode & 0x0F00) >> 8];
                short y = V[(opcode & 0x00F0) >> 4];
                short height = (short) (opcode & 0x000F);
                byte pixel;

                V[0xf] = 0;
                for (int yline = 0; yline < height; yline++)
                {
                    pixel = memory[I + yline];
                    for(int xline = 0; xline < 8; xline++)
                    {
                        if((pixel & (0x80 >> xline)) != 0)
                        {
                            if(gfx[(x + xline + ((y + yline) * 64))] == 1)
                                V[0xf] = 1;
                            gfx[x + xline + ((y + yline) * 64)] ^= 1;
                        }
                    }
                }
*/






}
