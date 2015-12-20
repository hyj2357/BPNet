package com.KNNMINIST.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.BPNET.impl.Tools;
import com.KNNMINIST.Set;

/**
 * 数据集实现类
 * @author Administrator
 *
 */
public class DataSet implements Set{
    private int magicNum,num,raw,column,cursor=1;
    private byte[] data;
    private double[][] bits;
    
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
    	bits = new double[(data.length-16)/784][6272];
	}
	
	/**
	 * 由文件读取数据后初始化文件头属性数据
	 * @throws IOException
	 */
	public void initAfterGetData() throws IOException{
        magicNum = readInt_32();
        num = readInt_32();
        raw = readInt_32();
        column = readInt_32();
 		for(int i=0;i<(data.length-16)/784;i++){
		  for(int j=16;j<(16+784);j++){
			  double l[] = new double[8]; 
			  l = Tools.from8ByteToBit(data[j+i*783], l);
			  for(int k=0;k<8;k++)
			    bits[i][k+(j-16)*8] = l[k];
 		  }
 		}
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
		System.out.print(this.magicNum+" "+this.num+" "+this.raw+" "+this.raw+"\n");
	    for(int i=0;i<data.length;i++){
	    	System.out.print((data[i]&0x0FF)+" ");
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

	public int getRaw() {
		return raw;
	}

	public void setRaw(int raw) {
		this.raw = raw;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
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
