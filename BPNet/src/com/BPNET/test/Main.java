package com.BPNET.test;

import java.io.IOException;
import java.util.Random;

import com.BPNET.impl.BPNetwork;
import com.BPNET.impl.Tools;
import com.KNNMINIST.impl.DataSet;
import com.KNNMINIST.impl.LabelSet;

public class Main {
     public static void main(String args[]) throws IOException{
    	 BPNetwork b1 = new BPNetwork(3,new int[]{784,20,10});
    	 //System.out.println(Tools.sigmod(0));
         /**
    	 Random random = new Random(19881211);
    	 System.out.println(random.nextDouble());
          **/
 		DataSet t1=new DataSet(),tr= new DataSet();
 		LabelSet l1 = new LabelSet(),ltr = new LabelSet();
 		long dateF = System.currentTimeMillis();
 		
 		t1.setData(System.getProperty("user.dir")+"\\data\\t10k-images.idx3-ubyte");     //获取测试集数据
 		t1.initAfterGetData();
 		tr.setData(System.getProperty("user.dir")+"\\data\\train-images.idx3-ubyte");    //获取训练集数据
 		tr.initAfterGetData();

 		l1.setData(System.getProperty("user.dir")+"\\data\\t10k-labels.idx1-ubyte");     //获取测试集标签
 		l1.initAfterGetData();
 		ltr.setData(System.getProperty("user.dir")+"\\data\\train-labels.idx1-ubyte");   //获取训练集标签
 		ltr.initAfterGetData();
 		
 		double trainData[][] = new double[10000][784];
 		int tSignal[] = new int[10000];
 		for(int i=0;i<10000;i++){
 		  tSignal[i] = ltr.get(8+i);
		  for(int j=16;j<(16+784);j++)
			trainData[i][j-16] = (double)tr.get(j+i*783);
 		}
 		b1.learn(trainData, tSignal);
     }
}
