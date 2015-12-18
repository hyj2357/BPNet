package com.KNNMINIST;

import java.io.InputStream;

/**
 * 数据集接口
 * @author Administrator
 *
 */
public interface Set {
	/**
	 * 根据文件路径获取数据集
	 * @param filePath
	 */
    public void setData(String filePath);
    /**
     * 打印数据集数据
     */
    public void printData();
}
