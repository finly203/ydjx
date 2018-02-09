package org.finlywork.filehandle.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.finlywork.filehandle.entity.OrderTable;
import org.finlywork.filehandle.service.OrderManager;
import org.finlywork.filehandle.service.impl.OrderForMysqlService;
import org.finlywork.filehandle.utils.AsyncFileInsert;
import org.finlywork.filehandle.utils.TestFileUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
	
	@Resource(name="orderForMysqlService")
	private OrderForMysqlService orderForMysqlService;
	@Autowired
	private AsyncFileInsert asyncFileInsert;
	@Autowired
	private TestFileUtils testFileUtils;
//	/**
//	 * 文件导入接口，文件存放在与应用同一服务器
//	 * @param start 导入文件开始下标
//	 * @param end 导入文件结束下标
//	 * @return
//	 */
//	@RequestMapping("/filehandle/insertFileRange")
//	public String insertFileRange(String start,String end) {
//		int s = 0;
//		int e = 0;
//		try {
//			s = Integer.parseInt(start);
//			e = Integer.parseInt(end);
//		}catch(NumberFormatException e1) {
//			return "start or end parameter is null or is not a number!";
//		}
//		
//		long beginTime=System.currentTimeMillis();
//		int count = orderManager.orderInsertAll(s,e);
//		long endTime=System.currentTimeMillis();
//		String str = "importing "+count+" rows data into mysql and cost "+(endTime-beginTime)+" ms!";  
//		return str;
//	}
//	
	/**
	 * 测试文件生成
	 * @return
	 */
	@RequestMapping("/filehandle/createTestFile")
	public String crateTestFile() {
		long beginTime=System.currentTimeMillis();
		boolean flag = testFileUtils.createTestFile();
		long endTime=System.currentTimeMillis();
		if(flag) {
			return "create test file success and cost "+(endTime-beginTime)+" ms!";
		}else {
			return "create test file fail!";
		}
		
	}
	
	/**
	 * 文件导入接口
	 * @param thread 分几个线程并发导入
	 * @return
	 */
	@RequestMapping("/filehandle/insertAsync")
	public String insertAsync(String thread) {
		//先把缓存中的表删除
		orderForMysqlService.dropTable();
		int j = 0;//设置分几次导入
		int fileNum = testFileUtils.findRootPathSubfile();//查询有多少个文件要导入
		try {
			j = Integer.parseInt(thread);
		}catch(NumberFormatException e1) {
			return "thread parameter is null or is not a number!";
		}
		
		long begin = System.currentTimeMillis();
		Future<String>[] future = new Future[j];
		//这里主要计算分批导入的文件下标
		for(int i = 0 ; i<j;i++) {
			int start = (fileNum/j)*i+1;
			int end = 0;
			if(i == (j-1)) {
				end = fileNum;
			}else {
				end = (fileNum/j)*(i+1);
			}
			try {
				future[i] = this.asyncFileInsert.fileAsyncInsertToMysql(start, end);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
            try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

		String str = "end 耗时: " + (System.currentTimeMillis() - begin);  
		return str;
	}
	
	@RequestMapping("/filehandle/searchByOrderNo")
	public List<OrderTable> searchByOrderNo(String orderNo){
		if(StringUtils.isEmpty(orderNo)) {
			return new ArrayList();
		}else {
			return orderForMysqlService.searchByOrderNo(orderNo);
		}
	}
}