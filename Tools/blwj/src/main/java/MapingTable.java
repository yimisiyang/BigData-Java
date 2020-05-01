
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileWriter;
import java.util.Map;


public class MapingTable {
    public static Set<String> set = new HashSet<>();
    /*
    筛选出所有不重名的TXT文件！！！
     */
    public static void getAllFiles(File file){
        File writename = new File("F:/读到的文件名.txt");
//        System.out.println(file.getAbsolutePath());//打印绝对路径
        if (file.isDirectory()){
            //如果file是文件夹，就把file文件夹里面的所有文件都放到file[]数组中；
            File files[] = file.listFiles();
            //把files数组中的文件（文件和文件夹）遍历出
            for(File f: files){
                if(f.isFile()){
                    String f_name = f.getName();
//                    System.out.println(f_name);
                    String f_nameSuffix = f_name.substring(f_name.lastIndexOf("."),f_name.length());
//                    System.out.println(f_nameSuffix);
                    if(f_nameSuffix.equals(".txt")){
                        Boolean b = set.add(f_name);
//                        System.out.println(b);
                        if(b){
                            try{
                                BufferedWriter out = new BufferedWriter(new FileWriter(writename,true));
                                out.write(f_name + "; ");
//                                out.newLine();
                                out.flush();
                                out.close();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            System.out.println(f_name);
                        }
                        }
                }else{
                    //否则回到方法再次执行，递归下去
                    getAllFiles(f);
                }
            }
        }
    }

    /*
    将相同TXT文件中的字段名提取出来，并写入TXT文档。
     */
    public static void getAllNumericFiled(File file){
        File writeline = new File("F:/读到的字段名.txt");
//        System.out.println(file.getAbsolutePath());
        if(file.isDirectory()){
            //如果file是文件夹，就把file文件夹里面的所有文件都放到files数组中
            File files[] = file.listFiles();
            //遍历出files数组中的所有文件和文件夹。
            for(File f:files){
                if(f.isFile()){
                    String f_name = f.getName();
                    String f_nameSuffix = f_name.substring(f_name.lastIndexOf("."),f_name.length());
                    if(f_nameSuffix.equals(".txt")){

                        //读当前TXT文件
                        try {
                            InputStreamReader reader = new InputStreamReader(
                                    new FileInputStream(f));//建立一个输入流对象reader
                            BufferedReader br = new BufferedReader(reader);
                            String line = br.readLine();
                            String filepath = f.getAbsolutePath();
                            BufferedWriter out = new BufferedWriter(new FileWriter(writeline,true));
                            out.write( line + filepath);
                            out.newLine();
                            out.flush();
                            out.close();
 //                           System.out.println(line + "  路径： "+ filepath);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    getAllNumericFiled(f);
                }
            }

        }
    }
    public static void main(String[] args){
        File fp = new File("F:/训练-2017-8-31");
        Boolean Flag = new Boolean("False");
        if(Flag.equals("True")){
            System.out.println("getAllFiles正在运行");
            getAllFiles(fp);
        }else{
            System.out.println("getAllNumericFiled正在运行");
            getAllNumericFiled(fp);
        }
    }
}
