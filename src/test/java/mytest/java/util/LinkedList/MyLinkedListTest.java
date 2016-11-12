package mytest.java.util.LinkedList;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

public class MyLinkedListTest {
	Logger mylog = Logger.getLogger(MyLinkedListTest.class);
	
	@Test
	public void testSimple() {
		mylog.debug("dd");
		List<String> list = new LinkedList<>();
		Collections.addAll(list, "a", "b", "c");
		Iterator<String> itr = list.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
}
