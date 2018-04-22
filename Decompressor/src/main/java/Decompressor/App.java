package Decompressor;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class App {
    public static void main(String[] args) {
        ReaderData reader = new ReaderData();

        ByteTo byteto = new ByteTo();
        byteto.byteToBit(reader.readData("/home/paul/path/shtorm.arh"));

    }
}

class Data {
    private int count_ch;
    private int tail;
    private HashMap<Character, Float> tableFrequency = new HashMap<>();
    private ArrayList<Character> text = new ArrayList<>();

    public int getCount_ch() {
        return count_ch;
    }

    public void setCount_ch(int count_ch) {
        this.count_ch = count_ch;
    }

    public int getTail() {
        return tail;
    }

    public void setTail(int tail) {
        this.tail = tail;
    }

    public HashMap<Character, Float> getTableFrequency() {
        return tableFrequency;
    }

    public void setTableFrequency(HashMap<Character, Float> tableFrequency) {
        this.tableFrequency = tableFrequency;
    }

    public ArrayList<Character> getText() {
        return text;
    }

    public void setText(ArrayList<Character> text) {
        this.text = text;
    }
}

class ReaderData {
    private Data data = new Data();

    public Data readData(String path) {

        try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) {
            char tmp_ch;
            float tmp_f;
            data.setCount_ch(dis.readInt());
            System.out.println(data.getCount_ch());
            data.setTail(dis.readInt());
            System.out.println(data.getTail());

            for (int i = data.getCount_ch(); i > 0; i--) {
                tmp_ch = dis.readChar();
                tmp_f = dis.readFloat();
                System.out.println("Key = " + tmp_ch + " Value = " + tmp_f);
                data.getTableFrequency().put(tmp_ch, tmp_f);
            }
            char jj = dis.readChar();
            System.out.println(jj);
            byte co = 0;
            while (dis.available() > 0) {
                co = dis.readByte();
                //System.out.println(co);
                data.getText().add((char) co);

            }

        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        } catch (IOException ioe) {
            System.out.println("Read error");

        }

        return data;


    }

}

class ByteTo {
    ArrayList<Byte> code = new ArrayList<>();
    byte tmp;
    ArrayList<Character> text;

    public ArrayList<Byte> byteToBit(Data data) {
        System.out.println(data.getText().size());
        char[] arr = new char[8];
        text = new ArrayList<>(data.getText());
        for (char ch_tmp : text) {
            byte b1 = (byte) ch_tmp;
            String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
            arr = s1.toCharArray();
            System.out.println(s1 + " -- " + ch_tmp);
            for (int i = 1; i < 8; i++) {
                if (arr[i] == '1') code.add((byte) 1);
                else code.add((byte) 0);
            }

        }
        System.out.println(data.getText());
        System.out.println("!!!!!!" + code);
        System.out.println(code.size());
        return code;

    }
}
