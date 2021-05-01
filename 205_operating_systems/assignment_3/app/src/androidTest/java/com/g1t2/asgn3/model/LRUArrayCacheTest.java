package com.g1t2.asgn3.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LRUArrayCacheTest {

    LRUCache<String, ListItem> cache;
    List<Object> pq;
    Map<String, Object> map;
    Method removeLRU;

    @Before
    public void setUp() throws Exception {
        cache = new LRUArrayCache<String, ListItem>(1000);
        Class c = cache.getClass();
        Field pqField = c.getDeclaredField("pq");
        pqField.setAccessible(true);
        pq = (List<Object>) pqField.get(cache);
        Field mapField = c.getDeclaredField("map");
        pqField.setAccessible(true);
        map = (Map<String, Object>) mapField.get(cache);
        removeLRU = c.getDeclaredMethod("removeLeastRecentlyUsed", null);
        removeLRU.setAccessible(true);
    }

    @Test
    public void testPut() {
        cache.put("a", new ListItem("first", null));
        cache.put("b", new ListItem("second", null));
        cache.put("c", new ListItem("third", null));
        cache.put("d", new ListItem("fourth", null));
        cache.put("e", new ListItem("fifth", null));
        assertEquals(5, pq.size());
        assertEquals("PQItem{pqLabel=0, mapKey=a}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=2, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(3).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(4).toString());
        assertEquals(5, map.keySet().size());
        assertEquals("MapItem{value=first, pqOffset=0}", map.get("a").toString());
        assertEquals("MapItem{value=second, pqOffset=1}", map.get("b").toString());
        assertEquals("MapItem{value=third, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=3}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=4}", map.get("e").toString());

        cache.put("a", new ListItem("sixth", null));
        assertEquals(5, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=2, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=5, mapKey=a}", pq.get(3).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(4).toString());
        assertEquals(5, map.keySet().size());
        assertEquals("MapItem{value=sixth, pqOffset=3}", map.get("a").toString());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=third, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=1}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=4}", map.get("e").toString());

        cache.put("c", new ListItem("seventh", null));
        assertEquals(5, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=6, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=5, mapKey=a}", pq.get(3).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(4).toString());
        assertEquals(5, map.keySet().size());
        assertEquals("MapItem{value=sixth, pqOffset=3}", map.get("a").toString());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=seventh, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=1}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=4}", map.get("e").toString());

        cache.put("d", new ListItem("eighth", null));
        assertEquals(5, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=6, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=5, mapKey=a}", pq.get(3).toString());
        assertEquals("PQItem{pqLabel=7, mapKey=d}", pq.get(4).toString());
        assertEquals(5, map.keySet().size());
        assertEquals("MapItem{value=sixth, pqOffset=3}", map.get("a").toString());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=seventh, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=eighth, pqOffset=4}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=1}", map.get("e").toString());
    }

    @Test
    public void testGet() {
        ListItem first = new ListItem("first", null);
        ListItem third = new ListItem("third", null);
        ListItem fourth = new ListItem("fourth", null);
        cache.put("a", first);
        cache.put("b", new ListItem("second", null));
        cache.put("c", third);
        cache.put("d", fourth);
        cache.put("e", new ListItem("fifth", null));

        ListItem retrieved = cache.get("a");

        assertEquals(first, retrieved);
        assertEquals(5, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=2, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=5, mapKey=a}", pq.get(3).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(4).toString());
        assertEquals(5, map.keySet().size());
        assertEquals("MapItem{value=first, pqOffset=3}", map.get("a").toString());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=third, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=1}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=4}", map.get("e").toString());

        retrieved = cache.get("c");
        assertEquals(third, retrieved);
        assertEquals(5, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=6, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=5, mapKey=a}", pq.get(3).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(4).toString());
        assertEquals(5, map.keySet().size());
        assertEquals("MapItem{value=first, pqOffset=3}", map.get("a").toString());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=third, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=1}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=4}", map.get("e").toString());

        retrieved = cache.get("d");
        assertEquals(fourth, retrieved);
        assertEquals(5, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=6, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=5, mapKey=a}", pq.get(3).toString());
        assertEquals("PQItem{pqLabel=7, mapKey=d}", pq.get(4).toString());
        assertEquals(5, map.keySet().size());
        assertEquals("MapItem{value=first, pqOffset=3}", map.get("a").toString());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=third, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=4}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=1}", map.get("e").toString());
    }

    @Test
    public void testRemove() {
        ListItem first = new ListItem("first", null);
        ListItem third = new ListItem("third", null);
        ListItem fourth = new ListItem("fourth", null);
        cache.put("a", first);
        cache.put("b", new ListItem("second", null));
        cache.put("c", third);
        cache.put("d", fourth);
        cache.put("e", new ListItem("fifth", null));

        ListItem removed = cache.remove("a");

        assertEquals(first, removed);
        assertEquals(4, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=2, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(3).toString());
        assertEquals(4, map.keySet().size());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=third, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=1}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=3}", map.get("e").toString());

        removed = cache.remove("c");
        assertEquals(third, removed);
        assertEquals(3, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(2).toString());
        assertEquals(3, map.keySet().size());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=fourth, pqOffset=1}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=2}", map.get("e").toString());

        removed = cache.remove("d");
        assertEquals(fourth, removed);
        assertEquals(2, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(1).toString());
        assertEquals(2, map.keySet().size());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=fifth, pqOffset=1}", map.get("e").toString());
    }

    @Test
    public void testRemoveLRU() throws Exception {
        cache.put("a", new ListItem("first", null));
        cache.put("b", new ListItem("second", null));
        cache.put("c", new ListItem("third", null));
        cache.put("d", new ListItem("fourth", null));
        cache.put("e", new ListItem("fifth", null));

        removeLRU.invoke(cache);
        assertEquals(4, pq.size());
        assertEquals("PQItem{pqLabel=1, mapKey=b}", pq.get(0).toString());
        assertEquals("PQItem{pqLabel=3, mapKey=d}", pq.get(1).toString());
        assertEquals("PQItem{pqLabel=2, mapKey=c}", pq.get(2).toString());
        assertEquals("PQItem{pqLabel=4, mapKey=e}", pq.get(3).toString());
        assertEquals(4, map.keySet().size());
        assertEquals("MapItem{value=second, pqOffset=0}", map.get("b").toString());
        assertEquals("MapItem{value=third, pqOffset=2}", map.get("c").toString());
        assertEquals("MapItem{value=fourth, pqOffset=1}", map.get("d").toString());
        assertEquals("MapItem{value=fifth, pqOffset=3}", map.get("e").toString());
    }
}