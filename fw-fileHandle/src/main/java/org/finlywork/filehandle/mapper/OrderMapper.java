package org.finlywork.filehandle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.finlywork.filehandle.entity.OrderTable;
import org.finlywork.filehandle.provider.OrderDAOProvider;

public interface OrderMapper {

	@InsertProvider(type = OrderDAOProvider.class, method = "insertAll")  
	public void insertAll(@Param("list") List<OrderTable> orderList);
	
	@SelectProvider(type = OrderDAOProvider.class,method = "findByOrderNo")
	public List<OrderTable> findByOrderNo(@Param("orderNo") String orderNo);
}
