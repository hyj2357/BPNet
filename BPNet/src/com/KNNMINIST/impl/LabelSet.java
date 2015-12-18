package com.KNNMINIST.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.KNNMINIST.Set;

/**
 * 标签集实现类
 * @author Administrator
 *
 */
public class LabelSet implements Set{
    private int magicNum,num,cursor=1;
    private byte[] data;
    
	@Override
	public void setData(String filePath) {
		File file = new File(filePath);
		FileInputStream fis  = null;
    	try {
    	  fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}   	
    	try {
			data = new byte[fis.available()];
			fis.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 由文件读取数据后初始化文件头属性数据
	 * @throws IOException
	 */
	public void initAfterGetData() throws IOException{
        magicNum = readInt_32();
        num = readInt_32();
	}
	
	/**
	 * 读取一个32位的int类型数据
	 * @return
	 * @throws IOException
	 */
	private int readInt_32() throws IOException{
		int num = 0;
		for(int c=0;cursor<data.length;cursor++,c++){
			//System.out.print((b[i-1]&0x0FF)+" ");
			num += data[cursor-1]&0x0FF;
			if(c!=3)
				num*=256;
			else{
				cursor++;
				break;
			}
		}
		return num;
	}

	@Override
	public void printData() {
		System.out.print(this.magicNum+" "+this.num+" \n");
	    for(int i=1;i<data.length;i++){
	    	System.out.print(data[i-1]+" ");
	    	   if(i%28==0)
	    	    System.out.println();
	    }		
	}

	/**
	 * 由下标获取byte数组中的8位int类型数据
	 * @param i
	 * @return
	 */
	public int get(int i){
		return data[i]&0x0FF;
	}
	public int getMagicNum() {
		return magicNum;
	}

	public void setMagicNum(int magicNum) {
		this.magicNum = magicNum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
    
}
