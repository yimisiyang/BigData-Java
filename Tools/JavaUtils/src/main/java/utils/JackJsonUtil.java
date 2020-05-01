package utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * json转换类
 * @author angilin
 *
 */
public class JackJsonUtil {
	

    public static void main(String[] args){
    	//数字字段的null不会被转成0 ，在JSONArray工具下会被转成0
    	String s = "{\"id\":null,\"name\":\"aaa\",\"amount\":121.01}";
    	try {
    		Cat cat = strToObj(s, Cat.class);
			System.out.println(cat.getId());
			System.out.println(cat.getAmount());
			
    		
			//abc为Cat中不存在的字段，转成Cat对象会出现错误
    		s = "{\"id\":null,\"name\":\"aaa\",\"amount\":121.01,\"abc\":{\"abcd\":111}}";
    		//strToObj(s, Cat.class);    会报错Unrecognized field "abc"
    		Map map = strToObj(s, Map.class);
			System.out.println(map.get("id"));
			System.out.println(map.get("name"));
			System.out.println(map.get("amount"));
			System.out.println(((Map)map.get("abc")).get("abcd"));
			
			
			A a = new A();
	    	a.setId(1L);
	    	List<Cat> catList = new ArrayList<Cat>();
	    	Cat cat1 = new Cat();
	    	cat1.setAmount(new BigDecimal("11.1"));
	    	Cat cat2 = new Cat();
	    	cat2.setAmount(new BigDecimal("12.1"));
	    	catList.add(cat1);
	    	catList.add(cat2);
	    	a.setCatList(catList);
	    	
	    	try {
				System.out.println(JackJsonUtil.objToStr(a));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	    	try {
				A a2 = JackJsonUtil.strToObj(JackJsonUtil.objToStr(a), A.class);
				System.out.println(a2.getCatList().get(1).getAmount());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static class Cat{
    	private Long id;
    	private String name;
    	private BigDecimal amount;
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    }
    
    public static class A{
    	Long id;
    	List<Cat> catList;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public List<Cat> getCatList() {
			return catList;
		}
		public void setCatList(List<Cat> catList) {
			this.catList = catList;
		}
    }
	
	/**
	 * 类转json字符串
	 * @param obj
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
    public static String objToStr(Object obj) throws JsonGenerationException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.writeValueAsString(obj);
    }
    
    /**
     * json字符串转为指定类对象
     * 如果json中含有类T中没有的字段，会导致org.codehaus.jackson.map.exc.UnrecognizedPropertyException错误
     * 转换成Map则不会有这个问题
     * @param json
     * @param cls
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T strToObj(String json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.readValue(json, cls);
    }
    
    /**
     * json字符串转为指定类对象列表
     * @param json
     * @param cls
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> List<T> strToList(String json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	JsonParser parser = mapper.getJsonFactory().createJsonParser(json);
    	JsonNode nodes = parser.readValueAsTree();
    	List<T> list = new ArrayList<T>(nodes.size());
    	for(JsonNode node : nodes){
    		list.add(mapper.readValue(node, cls));
    	}
    	return list;
    }
    
    
}
