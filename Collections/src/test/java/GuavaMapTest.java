import com.google.common.collect.*;
import com.sun.glass.ui.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest(classes = Application.class)
public class GuavaMapTest {

    /**
     * Table - 双键Map
     * guava中的Table允许一个value存在两个key。Table中的两个key分别被称为rowKey和columnKey，也就是行和列。
     * （但是个人感觉将它们理解为行和列并不是很准确，看作两列的话可能会更加合适一些）
     * eg: 记录员工每个月工作的天数
     */
    @Test
    public void testTable(){
        Map<String, Map<String, Integer>> map = new HashMap<>();

        // 存放元素
        Map<String, Integer> workMap = new HashMap<>();
        workMap.put("Jan", 20);
        workMap.put("Feb", 28);
        map.put("niuDa", workMap);
        System.out.println("使用map："+map);

        //取出元素
        Integer dayCount = map.get("niuDa").get("Jan");
        System.out.println("使用map："+dayCount);

        // 存放元素
        Table<String, String, Integer> table = HashBasedTable.create();
        table.put("niuDa", "Jan", 20);
        table.put("niuDa", "Feb", 28);

        table.put("Trunks", "Jan", 28);
        table.put("Trunks", "Feb", 16);
        table.put("Trunks", "Mar", 14);
        System.out.println("使用table："+table);

        //取出元素
        Integer niuDaFebCount = table.get("niuDa", "Feb");
        System.out.println("使用table："+niuDaFebCount);

        /**
         * 1、获得key或value的集合
         * key的集合是不包含重复元素的，value集合则包含了所有元素并没有去重
         */
        //rowKey或columnKey的集合
        Set<String> rowKeySet = table.rowKeySet();
        System.out.println("使用table，rowKeySet："+rowKeySet);
        Set<String> columnKeySet = table.columnKeySet();
        System.out.println("使用table，columnKeySet："+columnKeySet);

        //value集合
        Collection<Integer> values = table.values();
        System.out.println("使用table，values："+values);

        /**
         * 2、计算key对应的所有value的和
         * 统计所有rowKey对应的value之和
         */
        for (String key : table.rowKeySet()) {
            Set<Map.Entry<String, Integer>> rows = table.row(key).entrySet();
            int total = 0;
            for (Map.Entry<String, Integer> row : rows) {
                total += row.getValue();
            }
            System.out.println(key + ":" + total);
        }

        /**
         * 3、转换rowKey和columnKey
         * 这一操作也可以理解为行和列的转置，直接调用Tables的静态方法transpose
         * 利用cellSet方法可以得到所有的数据行
         */
        Set<Table.Cell<String, String, Integer>> cells = table.cellSet();
        cells.forEach(cell -> System.out.println(cell.getRowKey()+","+cell.getColumnKey()+":"+cell.getValue()));
        System.out.println("-----------------------");
        Table<String, String, Integer> transposeTable = Tables.transpose(table);
        Set<Table.Cell<String, String, Integer>> transposeCells = transposeTable.cellSet();
        transposeCells.forEach(transposeCell -> System.out.println(transposeCell.getRowKey()+","+transposeCell.getColumnKey()+":"+transposeCell.getValue()));

        /**
         * 4、转为嵌套的Map
         */
        Map<String, Integer> niuDa = table.row("niuDa");
        System.out.println("使用table，row"+niuDa);
        Map<String, Integer> feb = table.column("Feb");
        System.out.println("使用table，column"+feb);
        Map<String, Map<String, Integer>> rowMap = table.rowMap();
        System.out.println("使用table，rowMap"+rowMap);
        Map<String, Map<String, Integer>> columnMap = table.columnMap();
        System.out.println("使用table，columnMap"+columnMap);
    }

    /**
     * BiMap - 双向Map
     * guava中的BiMap提供了一种key和value双向关联的数据结构
     */
    @Test
    public void testBiMap(){
        Map<String, String> oldMap = new HashMap<>();
        oldMap.put("Hydra", "Programmer");
        oldMap.put("Tony","IronMan");
        oldMap.put("Thanos","Titan");
        List<String> keys = new ArrayList<>();
        for (String key : oldMap.keySet()) {
            if(oldMap.get(key).equals("IronMan"))
                keys.add(key);
        }
        System.out.println("oldMap:"+keys);

        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("Hydra", "Programmer");
        biMap.put("Tony","IronMan");
        biMap.put("Thanos","Titan");
        // 使用key获取value
        System.out.println("使用key获取value:"+biMap.get("Tony"));

        BiMap<String, String> inverse = biMap.inverse();
        //使用value获取key
        System.out.println("使用value获取key:"+inverse.get("Titan"));

        /**
         * 1、反转后操作的影响
         * 用inverse方法反转了原来BiMap的键值映射，但是这个反转后的BiMap并不是一个新的对象，它实现了一种视图的关联，所以对反转后的BiMap执行的所有操作会作用于原先的BiMap上
         * 对反转后的BiMap中的内容进行了修改后，再看一下原先BiMap中的内容,原先值为IronMan时对应的键是Tony，虽然没有直接修改，但是现在键变成了Stark
         */
        inverse.put("IronMan","Stark");
        System.out.println("biMap:"+biMap);
        System.out.println("inverse:"+inverse);

        /**
         * 2、value不可重复
         * 2.1 BiMap的底层继承了Map，我们知道在Map中key是不允许重复的，而双向的BiMap中key和value可以认为处于等价地位，因此在这个基础上加了限制，value也是不允许重复的,会抛出一个IllegalArgumentException: value already present: niuDa
         */
        biMap.put("Hydra", "niuDa");
        System.out.println("biMap:"+biMap);
        // biMap.put("ceShi", "niuDa");
        // System.out.println("biMap:"+biMap);

        // 2.2 你非想把新的key映射到已有的value上，那么也可以使用forcePut方法强制替换掉原有的key
        biMap.forcePut("ceShi", "niuDa");
        System.out.println("biMap:"+biMap);

        /**
         * 由于BiMap的value是不允许重复的，因此它的values方法返回的是没有重复的Set，而不是普通Collection
         */
        Collection<String> values = oldMap.values();
        System.out.println("oldMap.values():"+ values);

        Set<String> valueSet = biMap.values();
        System.out.println("biMap.values():"+ valueSet);
    }

    /**
     * Multimap - 多值Map
     * guava中的Multimap提供了将一个键映射到多个值的形式，使用起来无需定义复杂的内层集合，可以像使用普通的Map一样使用它
     */
    @Test
    public void testMultimap(){
        Map<String, List<Integer>> oldMap = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        oldMap.put("day",list);
        System.out.println("oldMap:"+oldMap);

        Multimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.put("day", 1);
        multimap.put("day", 2);
        multimap.put("day", 8);
        multimap.put("month", 3);
        System.out.println("multimap:"+multimap);

        /**
         * 1、获取值的集合
         * 创建的普通Multimap的get(key)方法将返回一个Collection类型的集合
         */
        Collection<Integer> day = multimap.get("day");
        System.out.println("day返回Collection:"+day);

        /**
         * 创建时指定为ArrayListMultimap类型，那么get方法将返回一个List
         */
        ArrayListMultimap<String, Integer> multimap1 = ArrayListMultimap.create();
        multimap1.put("day", 1);
        multimap1.put("day", 2);
        multimap1.put("day", 8);
        List<Integer> dayList = multimap1.get("day");
        System.out.println("ArrayListMultimap.get()返回list:"+ dayList);
        List<Integer> year = multimap1.get("year");
        System.out.println("ArrayListMultimap.get()返回year:"+ year);


    }

}
