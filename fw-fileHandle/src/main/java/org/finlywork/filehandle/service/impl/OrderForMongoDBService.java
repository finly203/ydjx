package org.finlywork.filehandle.service.impl;

import java.util.List;

import org.finlywork.filehandle.entity.OrderTable;
import org.finlywork.filehandle.service.OrderManager;
import org.finlywork.filehandle.utils.FileInputstreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service("orderForMongoDBService")
public class OrderForMongoDBService implements OrderManager{

	@Autowired
    private MongoTemplate mongoTemplate;
	@Autowired
	private FileInputstreamUtils fileInputstreamUtils;
	

	@Override
	public int orderInsertAll(int start,int end) {
		List<OrderTable> list = fileInputstreamUtils.findOrderTableFromInputstream(start,end);
		mongoTemplate.insertAll(list);
		return list.size();
	}


	@Override
	public List<OrderTable> searchByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return null;
	}

}

