package Compressor;

import java.io.*;
import java.util.*;


public class App {
    public static void main(String[] args) throws IOException {


        BuildTable bt = new BuildTable();
        HashMap<Character, Float> tableFrequency = bt.createTable("/home/paul/path/shtorm.txt");

        ArrayList<Node> list = new ArrayList<>();
        for (Map.Entry<Character, Float> entry : tableFrequency.entrySet()) {
            list.add(new Node(entry.getKey(), entry.getValue()));
        }

        BuildTree root = new BuildTree();
        Node n = root.createTree(list, list.size());
        MakeCode mk = new MakeCode();
        mk.createCode(n);

        Compressor comp = new Compressor();
        ArrayList<Character> ch_coutput = comp.fullCode(list);
        new HoffWriter().write(ch_coutput, "/home/paul/path/shtorm.arh", tableFrequency);


    }
}


class Node {

    private Character ch;
    private Float frequency;
    private ArrayList<Byte> code = new ArrayList<>();

    Node(Character ch, Float frequency) {

        this.ch = ch;
        this.frequency = frequency;
    }

    Node left = null;
    Node right = null;

    public Character getCh() {
        return ch;
    }

    public Float getFrequency() {
        return frequency;
    }

    public ArrayList<Byte> getCode() {
        return code;
    }

    public void setCode(ArrayList<Byte> code) {
        this.code = code;
    }
}

class BuildTree {
    ArrayList<Node> list;


    public Node createTree(ArrayList<Node> list, int size) {
        Node tmp = new Node(null, list.get(size - 1).getFrequency() + list.get(size - 2).getFrequency());

        tmp.left = list.get(size - 1);

        tmp.right = list.get(size - 2);

        if (size == 2) {
            return tmp;
        }
        list.add(tmp);

        Collections.sort(list, new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                if (o1.getFrequency() < o2.getFrequency()) {
                    return 1;
                } else if (o1.getFrequency() > o2.getFrequency()) {
                    return -1;
                } else
                    return 0;
            }
        });

        return createTree(list, size - 1);
    }
}

class BuildTable {

    public HashMap<Character, Float> createTable(String path) {
        Character ch;
        Float frequency;
        int count = 0;

        HashMap<Character, Float> hm = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(path)) {
            while (fis.available() > 0) {
                ch = (char) fis.read();
                count++;
                if (hm.containsKey(ch)) {
                    hm.put(ch, hm.get(ch) + 1);
                } else {
                    hm.put(ch, (float) 1);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        } catch (IOException ioe) {
            System.out.println("Read error");
        }

        for (Map.Entry<Character, Float> entry : hm.entrySet()) {
            hm.put(entry.getKey(), entry.getValue() / count);
        }
        return hm;
    }
}

class MakeCode {

    public void createCode(Node node) {
        if (node.left != null) {
            node.left.setCode(new ArrayList<Byte>(node.getCode()));
            node.left.getCode().add((byte) 0);
            MakeCode mk = new MakeCode();
            mk.createCode(node.left);
        }

        if (node.right != null) {
            node.right.setCode(new ArrayList<Byte>(node.getCode()));
            node.right.getCode().add((byte) 1);
            MakeCode mk = new MakeCode();
            mk.createCode(node.right);
        }
    }
}

class Compressor {

    public ArrayList<Character> fullCode(ArrayList<Node> nodes) {
        HashMap<Character, ArrayList<Byte>> hm = new HashMap<>();
        ArrayList<Byte> code = new ArrayList<>();
        byte[] arr = new byte[7];
        byte count = 0;
        ArrayList<Character> characters = new ArrayList<>();
        Character ch = 0;

        for (Node node : nodes) {
            if (node.getCh() != null) {
                hm.put(node.getCh(), node.getCode());
            }
        }

        try (FileInputStream fis = new FileInputStream("/home/paul/path/shtorm.txt")) {
            while (fis.available() > 0) {
                ch = (char) fis.read();
                code.addAll(hm.get(ch));
            }
        } catch (IOException e) {
            System.out.println("File not found or read error");
        }
        int tail = code.size() % 7;
        for (int i = tail; i != 0; i--) {
            code.add((byte) 0);
        }
        characters.add((char) tail);

        for (byte tmp : code) {
            if (count != 7) {
                arr[count] = tmp;
                count++;
            } else {
                count = 0;
                char tmp_ch = 0;
                for (byte i = 0; i < 7; i++) {
                    switch (i) {
                        case 0:
                            if (arr[i] == 1) tmp_ch = (char) (tmp_ch + 64);
                            break;
                        case 1:
                            if (arr[i] == 1) tmp_ch = (char) (tmp_ch + 32);
                            break;
                        case 2:
                            if (arr[i] == 1) tmp_ch = (char) (tmp_ch + 16);
                            break;
                        case 3:
                            if (arr[i] == 1) tmp_ch = (char) (tmp_ch + 8);
                            break;
                        case 4:
                            if (arr[i] == 1) tmp_ch = (char) (tmp_ch + 4);
                            break;
                        case 5:
                            if (arr[i] == 1) tmp_ch = (char) (tmp_ch + 2);
                            break;
                        case 6:
                            if (arr[i] == 1) tmp_ch = (char) (tmp_ch + 1);
                            break;
                    }
                }

                characters.add(tmp_ch);
                arr[count] = tmp;
                count++;
            }

        }
        System.out.println(code);
        System.out.println(code.size());
        System.out.println(characters);
        System.out.println(characters.size());
        return characters;
    }
}

class HoffWriter {

    public void write(ArrayList<Character> list, String path, HashMap<Character, Float> table) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("/home/paul/path/shtorm.arh"))) {
            for (Map.Entry<Character, Float> entry : table.entrySet()) {
                System.out.println(entry.getKey());
            }
            out.writeInt(table.size());
            System.out.println(table.size() + " размер таблицы встречаемости");
            out.writeInt(list.get(0));
            System.out.println((int) list.get(0) + " хвост");
            list.remove(0);

            Iterator it = table.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println("Key = " + pair.getKey() + "    Value = " + pair.getValue());
                out.writeChar((Character) pair.getKey());
                out.writeFloat((Float) pair.getValue());
            }

            out.writeChar('\n');
            System.out.println(list.size());
            for (char ch : list) {
                out.writeByte(ch);
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File is missing");

        } catch (IOException ioex) {
            System.out.println("Write error");
        }
    }
}