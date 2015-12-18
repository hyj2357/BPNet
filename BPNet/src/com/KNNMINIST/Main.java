package com.KNNMINIST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.KNNMINIST.impl.DataSet;
import com.KNNMINIST.impl.LabelSet;

public class Main {
	public static void main(String args[]) throws IOException{
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
		int k = 5;
		int right = 0,wrong = 0;
		int wrong_count[] = new int[10];
        for(int e=0;e<10000;e++){
			int label[] = new int[10], //记录0~9每个标签的中标次数
					l[] = new int[k], //记录当前队列中k个样本的标签
					d[] = new int[k]; //记录当前队列中k个样本的距离
			for(int init=0;init<k;init++){
				l[init] = -1;
				d[init] = -1;
			}				                                                              //初始化缓存数据集
    		for(int i=0;i<10000;i++){
                int distance = 0;
    			for(int j=16;j<(16+784);j++)
    			    distance += ((t1.get(j+e*784)-tr.get(j+i*784)) * (t1.get(j+e*784)-tr.get(j+i*784)));	              //获取与当前样本距离
    			if( (distance <d[k-1]) || d[k-1]==-1 ){     //如果距离小于队列首部的样本距离 或者 队列未满
    				if(d[k-1]!=-1)     //如果队列已满
    					label[l[k-1]]--;  //队列首位的样本对应标签中标数减 1
    				for(int t=0;t<k;t++)  //
    					if((distance<=d[t]) || (d[t]==-1)){
    						for(int b=k-1;b>t;b--){
    							d[b] = d[b-1];
    							l[b] = l[b-1];
    						}
    						d[t] = distance;
    						l[t] = ltr.get(i+8);
    						label[l[t]]++;
    						break;
    					}
    			}
    		}
			int gLabel = 0;
			for(int j=0,max=label[0];j<10;j++)
				if(max<label[j]){
					max = label[j];
					gLabel = j;
				}
			if(gLabel == l1.get(e+8))
				right++;
			else{
				wrong_count[gLabel]++;
				wrong++;
			}
			
			System.out.println((e+1)+".Correct forecast percent:"+(((double)((double)right/(double)(right+wrong)))*100)+"%");	  //输出正确率  		
        }
        System.out.println("Run time:"+((double)(System.currentTimeMillis() - dateF))/1000+"s");
        /**
        for(int i = 0;i<10;i++)  //输出每个标签判断错误次数
        	System.out.println(i+":"+wrong_count[i]);  
        **/
	}
}
