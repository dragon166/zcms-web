package org.zr.web.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * jsp处理相关工具方法类
 * <br>创建日期：2017年5月15日
 * <br><b>Copyright 2017 Gwideal All rights reserved</b>
 * @author 朱瑞
 * @since 1.0
 * @version 1.0
 */
public class JspUtils {
	
	/**
	 * 获取jsp渲染后的字符串，以ByteArrayOutputStream流的形式返回
	 * @since 1.0 
	 * @param request
	 * @param response
	 * @param path jsp文件路径,以"/"开头
	 * @return 
	 * <br><b>作者： @author 朱瑞</b>
	 * <br>创建时间：2017年5月15日 下午1:35:07
	 */
	public static ByteArrayOutputStream getJspRenderByteArrayOutputStream(HttpServletRequest request, HttpServletResponse response, String path ){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintWriter printWriter = new PrintWriter(baos);
		HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
			@Override
			public PrintWriter getWriter() throws IOException {
				return printWriter;
			}
		};
		
		try {
			request.getRequestDispatcher(path).forward(request, responseWrapper);
			return baos;
		} catch (ServletException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			printWriter.close();
		}
	}
	
	/**
	 * 获取jsp渲染后的字符串，以FileOutputStream流的形式返回
	 * @since 1.0 
	 * @param request 
	 * @param response 
	 * @param src jsp文件路径,以"/"开头
	 * @param des 目标文件
	 * @return
	 * <br><b>作者： @author 朱瑞</b>
	 * <br>创建时间：2017年5月15日 下午1:42:07
	 */
	public static FileOutputStream getJspRenderFileOutputStream(HttpServletRequest request, HttpServletResponse response, String src, File des){
		ByteArrayOutputStream baos =  getJspRenderByteArrayOutputStream(request, response, src);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		try {
			if (!des.exists()){
				boolean ok = des.createNewFile();
				if (!ok){
					throw new RuntimeException(des + "文件创建失败");
				}
			}
			FileOutputStream fos = new FileOutputStream(des);
			IOUtils.copy(bais, fos);
			return fos;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取jsp渲染后的字符串
	 * @since 1.0 
	 * @param request
	 * @param response
	 * @param path jsp文件路径,以"/"开头
	 * @return
	 * <br><b>作者： @author 朱瑞</b>
	 * <br>创建时间：2017年5月15日 下午1:43:22
	 */
	public static String getJspRenderText(HttpServletRequest request, HttpServletResponse response, String path ){
		 return getJspRenderByteArrayOutputStream(request, response, path).toString(Charset.forName("utf-8"));
	}
	
	/**
	 * 获取jsp渲染后的字符串，以FileOutputStream流的形式返回
	 * @since 1.0 
	 * @param request
	 * @param response
	 * @param src jsp文件路径,以"/"开头
	 * @param des 目标文件路径,不存在就创建
	 * @return
	 * <br><b>作者： @author 朱瑞</b>
	 * <br>创建时间：2017年5月15日 下午1:43:53
	 */
	public static FileOutputStream getJspRenderFileOutputStream(HttpServletRequest request, HttpServletResponse response, String src, String des){
		return getJspRenderFileOutputStream(request, response, src, new File(des));
	}
	
	/**
	 * 获取jsp渲染后的字符串，保存到目标文件
	 * @since 1.0 
	 * @param request
	 * @param response
	 * @param src jsp文件路径,以"/"开头
	 * @param des 目标文件路径,不存在就创建
	 * <br><b>作者： @author 朱瑞</b>
	 * <br>创建时间：2017年5月15日 下午1:44:44
	 */
	public static void saveJspRenderResultToFile(HttpServletRequest request, HttpServletResponse response, String src, String des){
		FileOutputStream fos = getJspRenderFileOutputStream(request, response, src, des);
		try {
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
