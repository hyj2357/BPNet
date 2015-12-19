package com.BPNET.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;

import com.BPNET.Net;

public class BPNetwork implements Net{
	/**
	 * 输入输出.
	 */
   private double input[],output[]; 
   /**
    * 每层的输出,例如 X[0][1],表示第0层的第1个神经元的输出.并且 X[k][i] = Xi(k) = f(Ui(k)) .Xi.
    */
   private double X[][];  
   /**
    * 每两层之间的各个神经元连接的权值 ,例如 weight[0][1][2],表示第0层第1个神经元到第1层第2个神经元之间连接输入输出的权值 .Wij.
    */
   private double weight[][][];
   /**
    * 每次调整权值改变量.ΔWij.
    */
   private double deltaWeight[][][]; 
   /**
    * 该神经网络层数.
    */
   private int layers;  
   /**
    * 每一层的神经元数.
    */
   private int nodeNumOfLayer[];
   /**
    * 教师信号.Yi.
    */
   private double Y[];    
   /**
    * 每一层每个神经元的d值,用于反向传播.di(k).
    */
   private double d[][];  
   /**
    * 输出文件名
    */
   private String filename;   
   /**
    * 学习步数,η. 
    */
   private static final double STEP = 0.2; 
   /**
    * 误差范围,e.
    */
   private static final double E = 0.005;     
   private static String FPREFIX = System.getProperty("user.dir")+File.separator+"data"+File.separator+"weight"+File.separator;
   private static final String DEFAULT_FILENAME = "weight.txt";
   
   /**
    * 由BP神经网络文件读取该文件中BP神经网络层数,各层神经元之间权重等相关信息
    * @param filename   文件名,需包含后缀,文件需放在 app/data/weight 下,否则需要修改文件前缀.<br/>如果为null则默认为默认文件名.
    * @throws FileNotFoundException
    */
   public BPNetwork(String filename) throws FileNotFoundException{
	      if(filename==null)
	    	  filename = DEFAULT_FILENAME;
	      File fl = new File(FPREFIX+filename);
	      if(!fl.exists()){
	    	  System.out.println("weight file <"+filename+"> isn't exists.");
	          System.exit(0);
	      }
	      Scanner sc = new Scanner(fl);
	      layers = sc.nextInt();
	      nodeNumOfLayer = new int[layers];
		  this.weight = new double[this.layers-1][1][1];

	      for(int i=0;i<layers;i++)
	    	  nodeNumOfLayer[i] = sc.nextInt();   
		  for(int i=0;i<nodeNumOfLayer.length-1;i++){
			  int left = this.nodeNumOfLayer[i];
			  int right= this.nodeNumOfLayer[i+1];
			  this.weight[i] = new double[left][right];
			  //deltaWeight[i] = new double[left][right];
		  }
		  for(int k=0;k<layers-1;k++){
			  for(int i=0;i<nodeNumOfLayer[k];i++)
				for(int j=0;j<nodeNumOfLayer[k+1];j++)
				  weight[k][i][j] = sc.nextDouble();
		  }
		   this.X = new double[this.layers][1];
	       for(int i=0;i<this.nodeNumOfLayer.length;i++)
	    	   this.X[i] = new double[this.nodeNumOfLayer[i]];  
	       this.input = new double[this.nodeNumOfLayer[0]];
	       this.Y = new double[this.nodeNumOfLayer[this.nodeNumOfLayer.length-1]];
	       this.output = new double[this.nodeNumOfLayer[this.nodeNumOfLayer.length-1]];
   }
   
   /**
    * 自定义生成神经网络
    * @param layers  神经网络层数
    * @param nodeNumOfLayer  每层的神经元数
    */
   public BPNetwork(int layers,int nodeNumOfLayer[],String filename){
	   if(layers<2){
		   System.out.println("At least 2 layers.");
		   return;
	   }
	   this.layers = layers;
	   this.nodeNumOfLayer = nodeNumOfLayer;
       this.filename = filename;
	   
	   this.weight = new double[this.layers-1][1][1];
	   this.deltaWeight = new double[this.layers-1][1][1];
	   this.d = new double[this.layers][1];
	   for(int i=0;i<nodeNumOfLayer.length-1;i++){
		   int left = this.nodeNumOfLayer[i];
		   int right= this.nodeNumOfLayer[i+1];
		   this.weight[i] = new double[left][right];
		   this.deltaWeight[i] = new double[left][right];
	   }
	   for(int i=0;i<nodeNumOfLayer.length;i++){
	       this.d[i] = new double[nodeNumOfLayer[i]];
	   }
	   this.initWeight();                         //init weight matrix
	   this.X = new double[this.layers][1];
       for(int i=0;i<this.nodeNumOfLayer.length;i++)
    	   this.X[i] = new double[this.nodeNumOfLayer[i]];  
       this.input = new double[this.nodeNumOfLayer[0]];
       this.Y = new double[this.nodeNumOfLayer[this.nodeNumOfLayer.length-1]];
       this.output = new double[this.nodeNumOfLayer[this.nodeNumOfLayer.length-1]];
   }
   
   /**
    * 测试数据
    * @param data
    * @param label
    */
   public void checkTestSet(double data[][],int label[]){
	   if(data.length!=label.length){
		   System.out.println("测试集数据个数与对应标签个数不符合.");
		   return;
	   }
	   int right=0,wrong=0,sum=0;
	   for(int i=0;i<data.length;i++){
		   for(int j=0;j<data[i].length;j++)   //注入输入信号input
			    input[j] = data[i][j];
		   this.forward_propagating();      //正向传播获取output
       	   for(int j=0;j<output.length;j++){
    		 if(j==label[i])
    			Y[j] = 1;
    		 else
    			Y[j] = 0;
    	   }
       	   if(this.scopeJudge()){
       		   sum++;
       		   right++;
       	   }else{
       		   sum++;
       		   wrong++;
       	   }
       	   System.out.println("Right rate: " + ((((double)right)/((double)sum))*100) + "%");
	   }
   }
   
   public boolean checkOne(double data[],int label){
	   for(int j=0;j<data.length;j++)   //注入输入信号input
		    input[j] = data[j];
	   forward_propagating();      //正向传播获取output
  	   for(int j=0;j<output.length;j++){
		 if(j==label)
			Y[j] = 1;
		 else
			Y[j] = 0;
	   }
  	   if(scopeJudge())
  		   return true;
  	   else
	       return false;
   }
   
   /**
    * 由训练集进行学习
    * @param trainData 训练数据
    * @param tSignal   教师信号
 * @throws IOException 
    */
   public void learn(double trainData[][],int tSignal[]) throws IOException{
	   if(trainData.length!=tSignal.length){
		   System.out.println("训练集数据个数与对应标签(教师信号)个数不符合");
		   return;
	   }
	   int sum = trainData.length;
	   for(int i=0;i<sum;i++){
		   learnOne(trainData[i], tSignal[i]);
		   System.out.println("Learn rate: " + ((((double)(i+1))/(double)(sum))*100) + "%.");
	   }
	   System.out.println("Learning finish!");           //训练结束
       writeWeightToFile();                              //将训练结果写入到文件当中
   }
   
   /**
    * 初始化权重矩阵
    */
   private void initWeight(){
	   Random random = new Random(20000101);
	   for(int i=0;i<this.weight.length;i++)
		   for(int j=0;j<this.weight[i].length;j++)
			   for(int k=0;k<this.weight[i][j].length;k++){
				   double o = random.nextDouble();
				   this.weight[i][j][k] = o>0.5?o:-o;
			   }
   }
   
   /**
    * 进行单个数据学习.
    * @param data
    */
   public void learnOne(double data[],int tSignal){
	  for(int i=0;i<data.length;i++)   //注入输入信号input
		  input[i] = data[i];
	  int sum = 0;
	  for(int i=0;i<10000;i++,sum++){          //单个数据学习次数最多为1000次
        this.forward_propagating();    //正向传播
        if(i==0){                      //如果是第一次学习,则注入教师信号.
        	for(int j=0;j<output.length;j++){
        		if(j==tSignal)
        			Y[j] = 1;
        		else
        			Y[j] = 0;
        	}
        }        
        if(this.scopeJudge())          //与教师信号比对
        	break;
        this.backard_propagating();    //如果出现结果不符,进行反向传播.
	  }
	  System.out.println("lsum: "+ sum+" .");
	  //System.out.print("weight 0 0 0: "+ this.weight[1][9][3]+" .");	  
   }
   
   /**
    * 进行结果范围判定
    * @return  output 是否在  Y 误差范围内
    */
   private boolean scopeJudge(){
	     boolean e = true;
	     for(int i=0;i<output.length;i++){
            if(output[i]==Y[i])
            	e = e&true;
            else{
            	if(output[i]<Y[i])
            		e=e&((output[i]+E)>Y[i]?true:false);
            	else if(output[i]>Y[i])
            		e=e&((output[i]-E)<Y[i]?true:false);
            }
	     }
	     return e;
   }
   
   /**
    * 正向传播过程
    * 获取output
    */
   private void forward_propagating(){
	   for(int i=0;i<input.length;i++)
		   X[0][i] = Tools.sigmod(input[i]);            //计算第0层神经元的输出.X[0][i] = Xi(0) = f(Ui(0)).f(Ui(0)) = sigmod(Ui(0)).
	   for(int k=1;k<X.length;k++){
		   for(int j=0;j<X[k].length;j++){
			   double U = 0;
			   for(int i=0;i<X[k-1].length;i++)
				   U += (X[k-1][i]*weight[k-1][i][j]);  //Ui(k)=(j)Σ(Wij*Xj(k-1)).
			   X[k][j] = Tools.sigmod(U);               //X[k][i] = Xi(k) = f(Ui(k)).
		   }
	   }
	   for(int i=0;i<output.length;i++)
		   output[i] = X[X.length-1][i];
   }
   
   /**
    * 反向传播
    * 并修改每层权值
    */
   private void backard_propagating(){
	   //求解公式
	   //ΔWij = -η*di(m)*Xj(m-1).( k=m AND di(m)=(Xi(m)-Yi)*Xi(m)*(1-Xi(m)) )
	   //ΔWij = -η*di(k)*Xj(k-1).( k<m AND di(k)=(Xi(k)*(1-Xi(k))*(l)Σ(Wli*dl(k+1)) ) )
	   //初始化第m层(m为输出层)d与权值
	   int m = X.length-1;
	   for(int i=0;i<d[m].length;i++)
		   d[m][i] = (X[m][i]-Y[i])*X[m][i]*(1-X[m][i]); //di(m)=(Xi(m)-Yi)*Xi(m)*(1-Xi(m))
	   for(int j=0;j<X[m-1].length;j++){
		   for(int i=0;i<X[m].length;i++){
			   weight[m-1][j][i] += (-1)*STEP*d[m][i]*X[m-1][j];   // ΔWij = -η*di(m)*Xj(m-1)  (k=m)
		   }
	   }
	   for(int k=layers-2;k>0;k--){
		   for(int i=0;i<X[k].length;i++){
			   double sum_wd = 0;
			   for(int l=0;l<X[k+1].length;l++)
				   sum_wd += (weight[k][i][l]*d[k+1][l]);    //(l)Σ(Wli*dl(k+1))
			   d[k][i] =  X[k][i]*(1-X[k][i])*sum_wd;        //di(k)=(Xi(k)*(1-Xi(k))*(l)Σ(Wli*dl(k+1)) )
		   }
		   for(int j=0;j<X[k-1].length;j++)
			   for(int i=0;i<X[k].length;i++)
				   weight[k-1][j][i] += (-1)*STEP*d[k][i]*X[k-1][j];	// ΔWij = -η*di(k)*Xj(k-1)   		   
	   }
   }
   
   /**
    * 将该网络写入到文件当中
    * @throws IOException
    */
   public void writeWeightToFile() throws IOException{
   	   File f = new File(FPREFIX + ((filename==null)?DEFAULT_FILENAME:filename) );    		
   	   if(!f.exists())
   			f.createNewFile();
   	   String writeContent = "";
   	   writeContent += (layers+"\r\n");
   	   for(int i=0;i<layers;i++)
   		   writeContent += (nodeNumOfLayer[i]+"\r\n");
	   for(int k=0;k<layers-1;k++){
		   for(int i=0;i<nodeNumOfLayer[k];i++)
		      for(int j=0;j<nodeNumOfLayer[k+1];j++){
				  writeContent += weight[k][i][j];
				  writeContent += "\r\n";
		      }
	   }
       OutputStream fotps = new FileOutputStream(f);
       fotps.write(writeContent.getBytes());
       System.out.println("Net has been write to file <"+f.getPath()+" "+f.getName()+">.");
       fotps.close();
   }
}
