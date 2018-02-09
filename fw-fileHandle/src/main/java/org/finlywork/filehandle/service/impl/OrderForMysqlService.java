package org.finlywork.filehandle.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.finlywork.filehandle.entity.OrderTable;
import org.finlywork.filehandle.mapper.OrderMapper;
import org.finlywork.filehandle.service.OrderManager;
import org.finlywork.filehandle.utils.FileInputstreamUtils;
import org.finlywork.filehandle.utils.OrderTableCreateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service("orderForMysqlService")
public class OrderForMysqlService implements OrderManager{

	private static final Logger logger = Logger.getLogger(OrderForMysqlService.class);
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
    private JdbcTemplate jdbcTemplate;
	@Autowired
	private FileInputstreamUtils fileInputstreamUtils;
	

	/**
	 * 采用批量插入方法效率太慢
	 */
	public void orderInsertAllBysql(List<OrderTable> list) {
		orderMapper.insertAll(list);
	}


	@Override
	public int orderInsertAll(int start,int end) {
	    Connection conn = null;
	    int result = 0; 
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			String tableName = "order_table_" + start + "_"+end;
			//先创建表
			conn.prepareStatement(OrderTableCreateUtils.createOrderTableSql(tableName)).execute();
			OrderTableCreateUtils.tableNameList.add(tableName);//缓存下来，用于查询
			//获取start到end文件的输入流
			InputStream dataStream = fileInputstreamUtils.getLocalFileInputStream(start, end);
			String loadDataSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE "+tableName+" fields terminated by '|' lines terminated by '\\t\\n'";
			PreparedStatement statement = conn.prepareStatement(loadDataSql);  
		    //通过mysql的PreparedStatement.setLocalInfileInputStream快速导入数据
			if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {  

		        com.mysql.jdbc.PreparedStatement mysqlStatement = statement  
		                .unwrap(com.mysql.jdbc.PreparedStatement.class);  

		        mysqlStatement.setLocalInfileInputStream(dataStream);  
		        result = mysqlStatement.executeUpdate();  
		    }  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    
	    return result;
	}


	public void dropTable() {
		Connection conn = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			for(String tableName : OrderTableCreateUtils.tableNameList) {
				conn.prepareStatement("DROP table "+tableName).execute();
			}
			OrderTableCreateUtils.tableNameList.clear();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<OrderTable> searchByOrderNo(String orderNo) {
		return orderMapper.findByOrderNo(orderNo);
	}

}

