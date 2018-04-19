package Decompressor;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class App 
{
    public static void main( String[] args )
    {
    ReaderData reader = new ReaderData();
    reader.readData("/home/paul/path/shtorm.arh");

    }
}
class Data {
    private int count_ch;
    private int tail;
    private HashMap<Character,Float> tableFrequency = new HashMap<>();
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

class ReaderData{
    private Data data = new Data();
    public Data readData(String path){

        try(DataInputStream dis = new DataInputStream(new FileInputStream(path))){
            char tmp_ch;
            float tmp_f;
            data.setCount_ch(dis.readInt());
            System.out.println(data.getCount_ch());
            data.setTail(dis.readInt());
            System.out.println(data.getTail());

            for(int i = data.getCount_ch();i>0;i--) {
                tmp_ch = dis.readChar();
                tmp_f = dis.readFloat();
                System.out.println("Key = " + tmp_ch + " Value = " + tmp_f);
                data.getTableFrequency().put(tmp_ch, tmp_f);
            }
                int jj = dis.readChar();
            System.out.println(jj);
                int co =0;
                while (dis.available() > 0){
                    jj=dis.readInt();
                    System.out.println(jj);
                    data.getText().add((char)jj);

            }

        }
        catch (FileNotFoundException fnfe){
            System.out.println("File not found");
        }
        catch (IOException ioe){
            System.out.println("Read error");

        }

        return data;



    }

}
