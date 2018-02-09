package org.finlywork.filehandle.provider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.finlywork.filehandle.entity.OrderTable;
import org.finlywork.filehandle.utils.OrderTableCreateUtils;

public class OrderDAOProvider {

	public String findByOrderNo(Map map) {
		String orderNo = (String) map.get("orderNo");
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		List<String> tableList = OrderTableCreateUtils.tableNameList;
		int size = tableList.size();
		int i = 0;
		for(String table : tableList) {
			if(table == null) {
				continue;
			}
			i++;
			sb.append("select * from "+table+" where order_no like '%"+orderNo+"%'");
			if(size > i) {
				sb.append(" UNION all ");
			}
		}
		sb.append(") as AA");
		return sb.toString();
	}
	
	public String insertAll(Map map) {
		List<OrderTable> orderList = (List<OrderTable>) map.get("list");
		StringBuilder sb = new StringBuilder();  
        sb.append("INSERT INTO order_table ");  
        sb.append("(order_no, order_time,client_no,client_name,goods_no,goods_name,goods_num,goods_price) ");  
        sb.append("VALUES "); 
//        MessageFormat mf = new MessageFormat("(#'{'list[{0}].orderNo},#'{'list[{0}].orderTime},#'{'list[{0}].clientNo},#'{'list[{0}].clientName},#'{'list[{0}].goodsNo},#'{'list[{0}].goodsName},#'{'list[{0}].goodsNum},#'{'list[{0}].goodsPrice})");  
        System.out.println(orderList.size());
        for (int i = 0; i < orderList.size(); i++) {  
//            sb.append(mf.format(new Object[]{i}));
//            if(i>1000) {
//            	System.out.println(orderList.get(i).getOrderNo());
//            }
//            if (i < orderList.size() - 1) {  
//                sb.append(",");  
//            } 
        	sb.append("('"+orderList.get(i).getOrderNo()+"','"+orderList.get(i).getOrderTime()+"',"
            		+ "'"+orderList.get(i).getClientNo()+"','"+orderList.get(i).getClientName()+"',"
            				+ "'"+orderList.get(i).getGoodsNo()+"','"+orderList.get(i).getGoodsName()+"',"
            						+ orderList.get(i).getGoodsNum()+","+orderList.get(i).getGoodsPrice()+")");
            if (i < orderList.size() - 1) {  
                sb.append(",");  
            }  

        } 
        return sb.toString();  
	}
}
