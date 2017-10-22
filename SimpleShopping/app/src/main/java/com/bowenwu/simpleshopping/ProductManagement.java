package com.bowenwu.simpleshopping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bowenwu on 2017/10/22.
 * This is a Singleton class
 */

public class ProductManagement {
    private static ProductManagement instance;
    // all data
    public static List<Map<String, Object>> data = new ArrayList<>();
    // state : whether this product is deleted. True for delete, false for the other
    private static boolean[] whetherDelete;
    // state : whether is in shopping_car
    private static boolean[] whetherBuy;

    // 在alertDialog 确定取消按钮中无法获得之前保存的产品名称
    // 所以在删除前先存下待确认删除的产品明
    // 看起来这张做也许会有问题 也不优雅 先这样处理吧
    private static String productNameReadyToDelete;
    private static String productNameReadyToBuy;
    /**
     * Data Set
     */
    private static String[] firstLetter = new String[] {"E", "A", "D", "K", "W", "M", "F", "M", "L", "B"};
    private static String[] goodsName   = new String[] {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
            "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers",
            "Lindt", "Borggreve"};
    private static String[] price       = new String[] {"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00",
            "¥ 14.90", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
    private static int[] image_rid  = new int[] {R.mipmap.enchatedforest, R.mipmap.arla, R.mipmap.devondale, R.mipmap.kindle, R.mipmap.waitrose,
            R.mipmap.mcvitie, R.mipmap.ferrero, R.mipmap.maltesers, R.mipmap.lindt, R.mipmap.borggreve};
    private String[] good_type   = new String[] {"作者 ", "产地 ", "产地 ", "版本 ", "重量 ", "产地 ", "重量 ", "重量 ", "重量 ", "重量 "};
    private String[] good_info   = new String[] {"Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g", "118g", "249g", "640g"};
    // data num
    int totalDataNum = 10;
    private ProductManagement() {
        for (int i = 0; i < totalDataNum; ++i) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("first_letter", firstLetter[i]);
            temp.put("product_name", goodsName[i]);
            temp.put("product_price", price[i]);
            temp.put("image_rid", image_rid[i]);
            temp.put("product_type", good_type[i]);
            temp.put("product_info", good_info[i]);
            data.add(temp);
        }

        whetherBuy = new boolean[totalDataNum];
        whetherDelete = new boolean[totalDataNum];
        for (int i = 0; i < totalDataNum; ++i) {
            whetherDelete[i] = false;
            whetherBuy[i]    = false;
        }
    }

    public static synchronized ProductManagement getInstance() {
        if (null == instance) {
            instance = new ProductManagement();
        }
        return instance;
    }

    public List<Map<String, Object>> getMainActivityData() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < totalDataNum; ++i) {
            if (false == whetherDelete[i]) {
                result.add(data.get(i));
            }
        }
        return result;
    }

    public List<Map<String, Object>> getShoppingCarData() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < totalDataNum; ++i) {
            if (false == whetherDelete[i] && true == whetherBuy[i]) {
                result.add(data.get(i));
            }
        }
        return result;
    }

    public void tryToDeleteProductInShoppingCar(String name) {
        productNameReadyToDelete = name;
    }
    public void confirmDeleteProductInShoppingCar() {
        for (int i = 0; i < totalDataNum; ++i) {
            if (productNameReadyToDelete.equals(goodsName[i])) {
                whetherBuy[i] = false;
                return;
            }
        }
    }

    public void tryToDeleteProductInMain(String name) {
        productNameReadyToDelete = name;
    }
    public void confirmDeleteProductInMain() {
        for (int i = 0; i < totalDataNum; ++i) {
            if (productNameReadyToDelete.equals(goodsName[i])) {
                whetherDelete[i] = true;
                return;
            }
        }
    }
    public void tryToBuyProduct(String productName) {
        productNameReadyToBuy = productName;
    }
    public void confirmBuyProduct() {
        for (int i = 0; i < totalDataNum; ++i) {
            if (productNameReadyToBuy.equals(goodsName[i])) {
                whetherBuy[i] = true;
                return;
            }
        }
    }
}
