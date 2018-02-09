package org.finlywork.filehandle.utils;

import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.finlywork.filehandle.entity.OrderTable;
import org.finlywork.filehandle.service.OrderManager;
import org.finlywork.filehandle.service.impl.OrderForMongoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class AsyncFileInsert {

	@Resource(name="orderForMysqlService")
	private OrderManager orderForMysqlService;
	@Resource(name="orderForMongoDBService")
	private OrderManager orderForMongoDBService;
	/**
	 * 异步方法，并发执行导入操作
	 * @param start
	 * @param end
	 * @return
	 * @throws InterruptedException
	 */
	@Async
    public Future<String> fileAsyncInsertToMysql(int start,int end) throws InterruptedException {
        System.out.println("start:"+start+" end:"+end);
        long begin = System.currentTimeMillis();
        int count = orderForMysqlService.orderInsertAll(start, end);
        System.out.println("start:"+start+" end:"+end +" is finished "+count+" rows insert in "+ (System.currentTimeMillis() - begin)+"ms");
        return new AsyncResult<String>("start:"+start+" end:"+end+" is finished");
    }
	
	
	@Async
    public Future<String> fileAsyncInsertToMongoDB(int start,int end) throws InterruptedException {
		System.out.println("start:"+start+" end:"+end);
        
        long begin = System.currentTimeMillis();
        int count = orderForMongoDBService.orderInsertAll(start,end);
        System.out.println("start:"+start+" end:"+end +" is finished "+count+" rows insert in "+ (System.currentTimeMillis() - begin)+"ms");
        return new AsyncResult<String>("start:"+start+" end:"+end+" is finished");
    }
}
