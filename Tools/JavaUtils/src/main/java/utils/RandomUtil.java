package utils;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 在某个范围内生成随机数
 * @author angilin
 *
 */
public class RandomUtil {
	
	
	public static void main(String[] args) {
       /* System.out.println(randomInt(0,Integer.MAX_VALUE));
        
        System.out.println(randomLong);*/
		
		check(10,100);
    }
	
	/**
	 * 生成随机整数int，范围为[min,max)
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max){
		Random random = new Random();
		//nextInt(n)，生成一个随机的int值，该值介于[0,n)的区间
		int result = random.nextInt(max-min) + min;
        return result;
	}
	
	/**
	 * 生成随机长整数long
	 * @return
	 */
	public static long randomLong(){
		Random random = new Random();
		return random.nextLong();
	}
	
	/**
	 * 测试10000次随机后的分布
	 * @param min
	 * @param max
	 */
	private static void check(int min, int max){
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
        for(int i=0;i<10000;i++){
        	int r = randomInt(min,max);
      
        	if(map.containsKey(r)){
        		map.put(r, map.get(r)+1);
        	}
        	else{
        		map.put(r, 1);
        	}
        	
        }
        for(int k : map.keySet()){
        	System.out.println(k+":"+map.get(k));
        }
	}
	

}
