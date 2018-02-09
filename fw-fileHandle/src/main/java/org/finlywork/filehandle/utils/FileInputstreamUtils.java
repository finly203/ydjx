package org.finlywork.filehandle.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.finlywork.filehandle.entity.OrderTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileInputstreamUtils {

	@Value("${org.finlywork.filehandle.filepath}")
	private String rootPath;
	
	public InputStream getLocalFileInputStream(int start,int end) {
		Vector<InputStream> vector = new Vector<InputStream>();
	    for(int i=start;i<=end;i++) {
	    	try {
				vector.addElement(new FileInputStream(rootPath+"order"+i+".txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
        Enumeration<InputStream> e = vector.elements();  
	    return new SequenceInputStream(e); 
	}
	
	/**
	 * 此方法效率太低，放弃
	 * @param start
	 * @param end
	 * @return
	 */
	public List<OrderTable> findOrderTableFromInputstream(int start,int end){
		InputStream is = this.getLocalFileInputStream(start, end);
		List<OrderTable> list = new ArrayList();
//		FileChannel inChannel=((FileInputStream)is).getChannel();
		try {
			BufferedReader bre = new BufferedReader(new InputStreamReader(is));
			String str = null;
			while ((str = bre.readLine())!= null) {
				String[] s = str.split("\\|");
				OrderTable order = new OrderTable();
				order.setOrderNo(s[0]);
				order.setOrderTime(s[1]);
				order.setClientNo(s[2]);
				order.setClientName(s[3]);
				order.setGoodsNo(s[4]);
				order.setGoodsName(s[5]);
				order.setGoodsNum(Integer.parseInt(s[6]));
				order.setGoodsPrice(Integer.parseInt(s[7].trim()));
				list.add(order);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return list;
	}
}
