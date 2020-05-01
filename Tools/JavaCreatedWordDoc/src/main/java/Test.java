import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception{
        List<String> list = new ArrayList<String>();
        list.add("2335 泽");
        list.add("3455 白");
        String  a= "";
        for (String li:list){
            a += li +" vs ";
        }
        System.out.println("a1=========="+a);
        a=a.substring(0,a.lastIndexOf(" vs "));
        System.out.println("a2========"+a);
    }
}
