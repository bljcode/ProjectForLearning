package testIdea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TestGenericity<K,V> {
	public void testlist(List ls){
		System.out.println(ls.get(0));
		List<?> l3 = ls;
		
	}
	public List<?> getlist(){
		
		return new ArrayList<String>();
	}
	public Hashtable<K,V> h = new Hashtable<K,V>();
	
	public void put(K k, V v){
		h.put(k, v);
	}
	
	public V getV(K k){
		return h.get(k);
	}
	
	public static void main(String[] args){
		/*TestGenericity<String,String> t = new TestGenericity<String,String>();
		t.put("fan", "lei");
		System.out.println(t.getV("fan"));
		List<String> list = new ArrayList<String>();  
        list.add("123");  
        list.add("456");          
        t.testlist(list);*/
        Map<String,String> testmap = new HashMap<String,String>();
        testmap.put(null, "1");
        System.out.println(testmap.containsKey(null));
	}

}
