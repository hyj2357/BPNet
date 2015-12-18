package com.BPNET.impl;

import java.util.Random;

import com.BPNET.Net;

public class BPNetwork implements Net{
   private double input[],output[];
   private double X[][];
   private double weight[][][];
   private double deltaWeight[][][];
   private int layers;
   private int nodeNumOfLayer[];
   private static final double STEP = 0.2;    //    
   
public BPNetwork(int layers,int nodeNumOfLayer[]){
	   if(layers<2){
		   System.out.println("At least 2 layers.");
		   return;
	   }
	   this.layers = layers;
	   this.nodeNumOfLayer = nodeNumOfLayer;

	   this.weight = new double[this.layers-1][1][1];
	   this.deltaWeight = new double[this.layers-1][1][1];	   
	   for(int i=0;i<nodeNumOfLayer.length-1;i++){
		   int left = this.nodeNumOfLayer[i];
		   int right= this.nodeNumOfLayer[i+1];
		   this.weight[i] = new double[left][right];
		   this.deltaWeight[i] = new double[left][right];
	   }
	   this.initWeight();                         //init weight matrix
	   this.X = new double[this.layers][1];
       for(int i=0;i<this.nodeNumOfLayer.length;i++)
    	   this.X[i] = new double[this.nodeNumOfLayer[i]];
       this.input = new double[this.nodeNumOfLayer[0]];
       this.output = new double[this.nodeNumOfLayer[this.nodeNumOfLayer.length-1]];
   }
   
   public void learn(double trainData[][],double tSignal[][]){
	   
   }
   private void initWeight(){
	   Random random = new Random(19881211);
	   for(int i=0;i<this.weight.length;i++)
		   for(int j=0;j<this.weight[i].length;j++)
			   for(int k=0;k<this.weight[i][j].length;k++){
				   double o = random.nextDouble();
				   this.weight[i][j][k] = o>0.5?o:-o;
			   }
   }
   private void learnOne(double data[]){
      this.forward_propagating();
      this.backard_propagating();
   }
   /**
    * 正向传播过程
    * 获取output
    */
   private void forward_propagating(){
	   
   }
   /**
    * 反向传播
    * 并修改每层权值
    */
   private void backard_propagating(){
	   
   }
public double[] getInput() {
	return input;
}

public void setInput(double[] input) {
	this.input = input;
}

public double[] getOutput() {
	return output;
}

public void setOutput(double[] output) {
	this.output = output;
}

public double[][] getX() {
	return X;
}

public void setX(double[][] x) {
	X = x;
}

public double[][][] getWeight() {
	return weight;
}

public void setWeight(double[][][] weight) {
	this.weight = weight;
}

public int getLayers() {
	return layers;
}

public void setLayers(int layers) {
	this.layers = layers;
}

public int[] getNodeNumOfLayer() {
	return nodeNumOfLayer;
}

public void setNodeNumOfLayer(int[] nodeNumOfLayer) {
	this.nodeNumOfLayer = nodeNumOfLayer;
}

public double[][][] getDeltaWeight() {
	return deltaWeight;
}

public void setDeltaWeight(double[][][] deltaWeight) {
	this.deltaWeight = deltaWeight;
}
}
