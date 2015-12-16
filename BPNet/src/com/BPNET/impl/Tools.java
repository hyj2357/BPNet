package com.BPNET.impl;

public class Tools {
   public static double sigmod(double target){
	   return 1/(1+Math.exp((-1)*(target)));
   }
}
