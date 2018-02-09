package org.finlywork.filehandle.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestFileUtils {

	@Value("${org.finlywork.filehandle.filepath}")
	private String rootPath;
	
	/**
	 * 查询文件目录下有多少个待导入的文件
	 * @return
	 */
	public int findRootPathSubfile() {
		File f = new File(rootPath);
		if(f.exists()) {
			return f.listFiles().length;
		}else {
			return 0;
		}
	}
	
	public boolean createTestFile() {
		File f = new File(rootPath);
		if(!f.exists()) {
			f.mkdirs();
		}else {
			File[] children = f.listFiles();
            for (int i=0; i<children.length; i++) {
            	children[i].delete();
            }
		}
		try {
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
