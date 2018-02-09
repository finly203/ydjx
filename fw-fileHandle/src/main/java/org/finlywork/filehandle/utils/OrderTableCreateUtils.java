package org.finlywork.filehandle.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态创建order表脚本
 * @author finly
 *
 */
public class OrderTableCreateUtils {

	//缓存表名用于查询
	public static List<String> tableNameList= new ArrayList();
	
	/**
	 * 根据文件范围创建表
	 * @param start 文件下标
	 * @param end 文件下标
	 * @return
	 */
	
	public static String createOrderTableSql(String tableName) {
		StringBuffer sb = new StringBuffer("");  
        sb.append("CREATE TABLE "+tableName+" (");  
        sb.append(" order_no varchar(255) NOT NULL,");          
        sb.append(" order_time datetime,");       
        sb.append(" client_name varchar(255) ,");
        sb.append(" client_no varchar(255) ,");
        sb.append(" goods_no varchar(255) ,");
        sb.append(" goods_name varchar(255) ,");
        sb.append(" goods_num int ,");
        sb.append(" goods_price int ");
        sb.append(") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
        return sb.toString();
	}
}
