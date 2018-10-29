package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.model.Products;
import com.zunder.smart.MyApplication;

public class ProductFactory {
	public static List<Products> list = new ArrayList<Products>();

	static {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getProducts();
		}
	}

	public static List<Products> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getProducts();
		}
		return list;
	}

	public static List<Products> getProducts(int deviceTypekey) {
		List<Products> resultList = new ArrayList<Products>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getProducts();
		}

		for (Products products : list) {
			if (products.getDeviceTypekey() == deviceTypekey) {
				resultList.add(products);
			}
		}
		return resultList;
	}
    public static List<String> getProductNames(int deviceTypekey) {
        List<String> resultList = new ArrayList<String>();
        if (list.size() == 0) {
            list = MyApplication.getInstance().getWidgetDataBase()
                    .getProducts();
        }

        for (Products products : list) {
            if (products.getDeviceTypekey() == deviceTypekey) {
                resultList.add(products.getProductsName());
            }
        }
        return resultList;
    }

	public static Products getProduct(int productsKey) {
		Products product = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getProducts();
		}

		for (Products products : list) {
			if (products.getProductsKey() == productsKey) {
				product = products;
				break;
			}
		}
		return product;
	}

	public static String getProductName(String productCode) {
		String result = "";
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getProducts();
		}

		for (Products products : list) {
			if (products.getProductsCode().equals(productCode)) {
				result = products.getProductsName();
				break;
			}
		}
		return result;
	}

	public static String getProductNameByKey(int productsKey) {
		String result = "";
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getProducts();
		}

		for (Products products : list) {
			if (products.getProductsKey()==productsKey) {
				result = products.getProductsName();
				break;
			}
		}
		return result;
	}

	public static Products getProduct(String productCode) {
		Products resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getProducts();
		}

		for (Products products : list) {
			if (products.getProductsCode().equals(productCode)) {
				resultList = products;
				break;
			}
		}
		return resultList;
	}

	public static void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getProducts();
	}

	public static List<Products> getMenu() {
		List<Products> resultList = new ArrayList<Products>();

		Products products = new Products();
		products.setId(-1);
		products.setProductsName("全部产品");
		resultList.add(products);
		return resultList;
	}
}
