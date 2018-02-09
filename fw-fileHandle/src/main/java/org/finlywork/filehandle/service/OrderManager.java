package org.finlywork.filehandle.service;

import java.io.InputStream;
import java.util.List;

import org.finlywork.filehandle.entity.OrderTable;

public interface OrderManager{

	
	/** 文件输入流方式导入,
	 * 	start:文件起始下标
	 *  end:文件结束下标
	 * */
	public int orderInsertAll(int start,int end);
	
	/**
	 * 导入后的查询
	 * @param orderNo 订单编号
	 * @return
	 */
	public List<OrderTable> searchByOrderNo(String orderNo);
	
}

