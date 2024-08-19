package com.example.ogani.utils;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.lib.payos.type.ItemData;

public class ItemDataReflectionUtils {
    public static ItemData createItemDataFromMap(Map<String, Object> dataMap) throws Exception {
        // Lấy ra constructor có tham số của lớp ItemData
        Constructor<ItemData> constructor = ItemData.class.getDeclaredConstructor(String.class, int.class, int.class);

        // Kích hoạt constructor để tạo đối tượng ItemData
        ItemData itemData = constructor.newInstance(
            (String) dataMap.get("name"),
            (int) dataMap.get("quantity"),
            (int) dataMap.get("price")
        );

        return itemData;
    }
}
