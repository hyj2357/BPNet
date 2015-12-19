package com.BPNET.test;

import java.io.IOException;
import java.util.Random;

import com.BPNET.impl.BPNetList;
import com.BPNET.impl.BPNetwork;
import com.BPNET.impl.Tools;
import com.KNNMINIST.impl.DataSet;
import com.KNNMINIST.impl.LabelSet;

public class Main {
     public static void main(String args[]) throws IOException{
    	 
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
 		 

 		String b[] = new String[]{"0.txt","1.txt","2.txt","3.txt","4.txt","5.txt","6.txt","7.txt","8.txt","9.txt"};
 /**       BPNetList b1 = new BPNetList(3,new int[]{784,20,10},b);

 		double data[][] = new double[10000][784];
 		int label[] = new int[10000];
 		for(int i=0;i<10000;i++){
 		  label[i] = ltr.get(8+i);
		  for(int j=16;j<(16+784);j++)
			  data[i][j-16] = (double)tr.get(j+i*783);
 		}
 		
 		b1.learn(data, label);
**/
 		

      	BPNetList b1 = new BPNetList(b);
/**
 		double trainData[][] = new double[10000][784];
 		int tSignal[] = new int[10000];
 		for(int i=0;i<10000;i++){
 		  tSignal[i] = l1.get(8+i);
		  for(int j=16;j<(16+784);j++)
			trainData[i][j-16] = (double)t1.get(j+i*783);
 		}
 		**/
 		double trainData[][] = new double[60000][784];
 		int tSignal[] = new int[60000];
 		for(int i=0;i<60000;i++){
 			tSignal[i] = ltr.get(8+i);
		  for(int j=16;j<(16+784);j++)
			  trainData[i][j-16] = (double)tr.get(j+i*783);
 		}
 		int right = 0,sum=0;
 		for(int i=0;i<60000;i++){
 			for(int j=0;j<b1.getBPLst().length;j++){
 				boolean k = false;
 		 		if(k = b1.getBPLst()[j].checkOne(trainData[i], tSignal[i])){
 		 			System.out.print(j+" "+k+" "+tSignal[i]+" ");
 		 			if(j==tSignal[i])
 		 				right++;
 		 			break;
 		 		}
 			}
 			sum++;
    		System.out.println((i+1)+". Correct percent: " + (((double)right)/((double)sum))*100 + " %.");
 		}
 		//b1.test(trainData, tSignal); 	

     }
}
