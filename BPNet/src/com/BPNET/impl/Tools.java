package com.BPNET.impl;

public class Tools {
   public static double sigmod(double target){
	   return 1/(1+Math.exp((-1)*(target)));
   }
   public static double[] from8ByteToBit(byte _byte,double bits[]){
	   int i = 7;
	   int b = (new Byte(_byte)).intValue();
	   if(b<0)
		   b += 256;
	   for(;i>=0&b!=0;b=b/2,i--)
		  bits[i] = (b%2==0)?0:1;
	   for(;i>=0;i--)
		   bits[i] = 0;
	   return bits;
   }
}
