package org.finlywork.filehandle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.finlywork.filehandle.entity.OrderTable;
import org.finlywork.filehandle.service.OrderManager;
import org.finlywork.filehandle.service.impl.OrderForMysqlService;
import org.finlywork.filehandle.utils.AsyncFileInsert;
import org.finlywork.filehandle.utils.FileInputstreamUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileHandleApplicationTests {

	Logger logger = Logger.getLogger(FileHandleApplicationTests.class);
	
	@Resource(name="orderForMysqlService")
	private OrderForMysqlService orderForMysqlService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="orderForMongoDBService")
	private OrderManager orderForMongoDBService;
	@Autowired
	private FileInputstreamUtils fileInputstreamUtils;
	@Autowired
	private AsyncFileInsert asyncFileInsert;
	
	@Test
	public void test() {
		
	}
	
	/**
	 * 通过批量插入方式,mysql
	 */
//	@Test
	public void testInsertMysql() {
		List<OrderTable> orderList = new ArrayList();
		long starttime = System.currentTimeMillis();
		for(int i=0;i<300000;i++) {
			OrderTable order = new OrderTable();
			order.setClientName("客户"+i);
			order.setClientNo("clientNo:"+i);
			order.setGoodsName("商品名"+i);
			order.setGoodsNo("goodsNo:"+i);
			order.setGoodsNum(i+3);
			order.setGoodsPrice(i+1);
			order.setOrderNo("orderNo:"+i);
			order.setOrderTime("2018-02-02 15:32:00");
			orderList.add(order);
		}
		long endtime = System.currentTimeMillis();
		logger.info("数据初始化："+(endtime-starttime)+"ms");
		orderForMysqlService.orderInsertAllBysql(orderList);
		long dataInsertTime = System.currentTimeMillis();
		logger.info("数据入库："+(dataInsertTime-endtime)+"ms");
	}
	
	
	/**
	 * 输出测试文件，100个文件，每个文件5万条数据，一共500万条数据
	 * @throws IOException 
	 */
//	@Test
	public void outputTestFile() throws IOException {
		String rootPath = "E:\\filetest\\";
		for(int i = 1 ;i <= 100;i++) {
			FileOutputStream fos = new FileOutputStream(rootPath + "order"+i+".txt",true);
			PrintStream p = new PrintStream(fos);
			for(int j =1;j<=50000;j++) {
				int flag = ((i-1)*50000)+j;
				String str = "orderNo:"+flag+"|"+"2018-02-02 15:32:00"+"|"+"clientNo:"+flag+"|"+"客户:"+flag+"|"
				+"goodsNo:"+flag+"|" + "商品名"+flag+"|"+flag+"|"+flag+"\t\n";
				p.print(str);
			}
			p.close();
			fos.flush();
		}
         
	}
	
//	@Test
	public void fileAsyncInsertToMysql() throws InterruptedException {
		long begin = System.currentTimeMillis();
		int j = 10;//设置分几次导入,每次是一个线程
		int fileNum = 100;//文件个数
		Future<String>[] future = new Future[j];
		for(int i = 0 ; i<j;i++) {
			int start = (fileNum/j)*i+1;
			int end = 0;
			if(i == (j-1)) {
				end = fileNum;
			}else {
				end = (fileNum/j)*(i+1);
			}
			future[i] = this.asyncFileInsert.fileAsyncInsertToMysql(start, end);
		}
		//判断每个线程是否完成导入
        while(true) {
        	boolean isAllDone = true;
        	for(Future<String> f : future) {
        		if(!f.isDone()) {
        			isAllDone = false;
        		}
        	}
        	if(isAllDone) {
        		break;
        	}
            Thread.sleep(200);
        }

        System.out.println("end 耗时: " + (System.currentTimeMillis() - begin));
		
	}
	
//
//	@Test
//	public void fileAsyncInsertToMongoDB() throws InterruptedException {
//		long begin = System.currentTimeMillis();
//		int j = 10;//设置分几次导入,每次是一个线程
//		int fileNum = 100;//文件个数
//		Future<String>[] future = new Future[j];
//		for(int i = 0 ; i<j;i++) {
//			int start = (fileNum/j)*i+1;
//			int end = 0;
//			if(i == (j-1)) {
//				end = fileNum;
//			}else {
//				end = (fileNum/j)*(i+1);
//			}
//			future[i] = this.asyncFileInsert.fileAsyncInsertToMongoDB(start, end);
//		}
//		//判断每个线程是否完成导入
//        while(true) {
//        	boolean isAllDone = true;
//        	for(Future<String> f : future) {
//        		if(!f.isDone()) {
//        			isAllDone = false;
//        		}
//        	}
//        	if(isAllDone) {
//        		break;
//        	}
//            Thread.sleep(200);
//        }
//
//        System.out.println("end 耗时: " + (System.currentTimeMillis() - begin));
//		
//	}
}
