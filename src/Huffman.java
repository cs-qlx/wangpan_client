
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Huffman
{
    public static void unhfmFile(String zipFile,String dstFile)
    {
        InputStream inputStream=null;
        ObjectInputStream objectInputStream=null;
        OutputStream outputStream=null;
        try
        {
            inputStream=new FileInputStream(zipFile);
            objectInputStream=new ObjectInputStream(inputStream);
            byte [] array= (byte [])objectInputStream.readObject();
            Map<Byte,String> map=(Map<Byte,String>)objectInputStream.readObject();
            byte[] decode = decode(map, array);
            outputStream=new FileOutputStream(dstFile);
            outputStream.write(decode);
        } catch (Exception e)
        {
            System.out.println(e);
        }finally
        {
            try {
                outputStream.close();
                objectInputStream.close();
                inputStream.close();

            } catch (Exception e2) {
                System.out.println(e2);
            }

        }


    }

    public static void hfmFile(String srcFile,String dstFile)
    {
        FileInputStream inputStream=null;
        OutputStream outputStream=null;
        ObjectOutputStream objectOutputStream=null;

        try
        {
            inputStream=new FileInputStream(srcFile);
            byte [] b=new byte[inputStream.available()];
            inputStream.read(b);
            byte[] huffmanZip = huffmanZip(b);
            outputStream=new FileOutputStream(dstFile);
            objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(huffmanZip);
            objectOutputStream.writeObject(map);
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "文件过大无法压缩!", "警告", JOptionPane.WARNING_MESSAGE);
        }
        finally
        {
            if(inputStream!=null)
            {
                try
                {
                    objectOutputStream.close();
                    outputStream.close();
                    inputStream.close();//释放资源

                } catch (Exception e2)
                {
                    JOptionPane.showMessageDialog(null, "文件过大无法压缩!", "警告", JOptionPane.WARNING_MESSAGE);
                }

            }
        }
    }

    private static byte[] decode(Map<Byte, String> map,byte [] array)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<array.length;i++)
        {
            boolean flag=(i==array.length-1);
            stringBuilder.append(byteToBitString(!flag, array[i]));
        }

        Map<String, Byte> map2=new HashMap<String, Byte>();//反向编码表
        Set<Byte> keySet = map.keySet();
        for(Byte b:keySet)
        {
            String value=map.get(b);
            map2.put(value, b);
        }
        List<Byte> list=new ArrayList<Byte>();
        for (int i = 0; i < stringBuilder.length();)
        {
            int count=1;
            boolean flag=true;
            Byte byte1=null;
            while (i < stringBuilder.length() && flag)
            {
                String substring = stringBuilder.substring(i, i+count);
                byte1 = map2.get(substring);
                if(byte1==null)
                {
                    count++;
                }
                else
                {
                    flag=false;
                }

            }
            list.add(byte1);
            i+=count;
        }
        byte[] by = new byte[list.size()];
        for(int i = 0;i < list.size(); i++){
            by[i] = list.get(i);
        }

        return by;
    }

    private static String byteToBitString(boolean flag, byte b)
    {
        int temp=b;
        if(flag)
        {
            temp|=256;
        }

        String binaryString = Integer.toBinaryString(temp);
        if(flag)
        {
            return binaryString.substring(binaryString.length()-8);
        }
        else
        {
            return  "0" + binaryString;
        }

    }

    private static byte[] huffmanZip(byte [] array)
    {
        List<Node> nodes = getNodes(array);
        Node createHuffManTree = createHuffManTree(nodes);
        Map<Byte, String> m=getCodes(createHuffManTree);
        byte[] zip = zip(array, m);
        return zip;
    }

    //
    private static byte[] zip(byte [] array,Map<Byte,String> map)
    {
        StringBuilder sBuilder=new StringBuilder();
        for(byte item:array)
        {
            String value=map.get(item);
            sBuilder.append(value);
        }
        //System.out.println(sBuilder);
        int len;
        if(sBuilder.toString().length()%8==0)//如果可以整除
        {
            len=sBuilder.toString().length()/8;
        }
        else //如果不能整除
        {
            len=sBuilder.toString().length()/8+1;
        }

        byte [] by=new byte[len];
        int index=0;
        for(int i=0;i<sBuilder.length();i+=8)
        {
            String string;
            if((i+8)>sBuilder.length())
            {
                string=sBuilder.substring(i);
            }
            else
            {
                string=sBuilder.substring(i, i+8);
            }

            by[index]=(byte)Integer.parseInt(string,2);
            index++;
        }

        return by;

    }


    //重载
    private static Map<Byte, String> getCodes(Node root)
    {
        if(root==null)
        {
            return null;
        }
        getCodes(root.leftNode,"0",sBuilder);
        getCodes(root.rightNode,"1",sBuilder);
        return map;
    }



    //获取哈夫曼编码
    static Map<Byte, String> map=new HashMap<>();
    static StringBuilder sBuilder=new StringBuilder();
    public static void getCodes(Node node,String code,StringBuilder stringBuilder)
    {
        StringBuilder stringBuilder2=new StringBuilder(stringBuilder);
        stringBuilder2.append(code);
        if(node!=null)
        {
            if(node.data==null)//非叶子结点
            {
                //向左递归
                getCodes(node.leftNode,"0",stringBuilder2);
                //向右递归
                getCodes(node.rightNode,"1",stringBuilder2);
            }
            else //如果是叶子结点
            {
                map.put(node.data,stringBuilder2.toString());
            }
        }
    }



    public static List<Node> getNodes(byte [] array)
    {
        List<Node> list=new ArrayList<Node>();
        Map<Byte, Integer> map=new HashMap<Byte, Integer>();
        for(Byte data:array)
        {
            Integer count=map.get(data);//通过键获取值
            if(count==null)//说明此时map集合中还没有改字符
            {
                map.put(data, 1);
            }
            else
            {
                map.put(data,count+1);
            }
        }
        //遍历map集合
        Set<Byte> set=map.keySet();
        for(Byte key:set)
        {
            int value=map.get(key);
            Node node=new Node(key, value);
            list.add(node);
        }
        return list;
    }

    private static Node createHuffManTree(List<Node> list)
    {
        while(list.size()>1)
        {
            Collections.sort(list);//先对集合进行排序
            Node leftNode=list.get(0);
            Node rightNode=list.get(1);

            Node parentNode=new Node(null, leftNode.weight+rightNode.weight);
            parentNode.leftNode=leftNode;
            parentNode.rightNode=rightNode;

            list.remove(leftNode);
            list.remove(rightNode);

            list.add(parentNode);
        }
        return list.get(0);

    }

}

class Node implements Comparable<Node>
{
    Byte data;//字符
    int weight;//字符出现的次数
    Node leftNode;
    Node rightNode;

    public Node(Byte data,int weight)//构造器
    {
        this.data=data;
        this.weight=weight;
    }

    @Override
    public int compareTo(Node o)
    {
        return this.weight-o.weight;
    }

    @Override
    public String toString()
    {
        return "Node [data=" + data + ", weight=" + weight + "]";
    }

    //前序遍历
    public void preOrder()
    {
        System.out.println(this);
        if(this.leftNode!=null)
        {
            this.leftNode.preOrder();
        }
        if(this.rightNode!=null)
        {
            this.rightNode.preOrder();
        }
    }


}