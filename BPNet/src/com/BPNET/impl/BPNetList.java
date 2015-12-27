package com.BPNET.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BPNetList {
    private BPNetwork[] BPLst;
    
    public BPNetList(int layers,int nodeNumOfLayer[],String filename[]){
    	if(nodeNumOfLayer[nodeNumOfLayer.length-1]!=filename.length){
    		System.out.println("filename number err!");
    		return;
    	}    		
    	BPLst = new BPNetwork[nodeNumOfLayer[nodeNumOfLayer.length-1]];
    	for(int i=0;i<BPLst.length;i++)
    		BPLst[i] = new BPNetwork(layers,nodeNumOfLayer,filename[i]);    	
    }
    
    public BPNetList(String filename[]) throws FileNotFoundException{
    	BPLst = new BPNetwork[filename.length];
    	for(int i=0;i<BPLst.length;i++)
    		BPLst[i] = new BPNetwork(filename[i]);
    }
    
	public void test(double[][] trainData, int[] tSignal){
    	int num = trainData.length;
    	int right=0,sum=0;
    	for(int i=0;i<num;i++){
    		if(this.BPLst[tSignal[i]].checkOne(trainData[i], tSignal[i]))
    			right++;
    		sum++;
    		System.out.println((i+1)+". Correct percent: " + (((double)right)/((double)sum))*100 + " %.");
    	}
    }
    
    public void learn(double[][] trainData, int[] tSignal) throws IOException{
       int num = trainData.length;
 	   long t = System.currentTimeMillis();
       for(int i=0;i<num;i++){
    		this.BPLst[tSignal[i]].learnOne(trainData[i], tSignal[i]);
    		System.out.println((i+1)+" .learn percent:" + (((double)i)/((double)num))*100 +" %.");
    	}
 	   System.out.println("Learning finish!");           //ÑµÁ·½áÊø
 	   System.out.println("Running time: "+(System.currentTimeMillis()-t)+"ms");
       for(int i=0;i<this.BPLst.length;i++)
    		this.BPLst[i].writeWeightToFile();
    }
    
    public BPNetwork[] getBPLst() {
		return BPLst;
	}

	public void setBPLst(BPNetwork[] bPLst) {
		BPLst = bPLst;
	}

}
