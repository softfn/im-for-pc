package com.only.query.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * @Author: XiaHui
 * @Date: 2015年12月30日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月30日
 */
public class FreeMarkerUtilTest {

	
	// templatePath模板文件存放路径
	// templateName 模板文件名称
	// filename 生成的文件名称
	public static void analysisTemplate(String templatePath, String templateName, String fileName, Map<?, ?> root) {
		try {
			Version version = new Version("2.3.23");
			
			Configuration config = new Configuration(new Version("2.3.23"));
			// 设置要解析的模板所在的目录，并加载模板文件
			config.setDirectoryForTemplateLoading(new File(templatePath));
			// 设置包装器，并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper(version));

			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			// 否则会出现乱码
			Template template = config.getTemplate(templateName, "UTF-8");
			// 合并数据模型与模板
			FileOutputStream fos = new FileOutputStream(fileName);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	public static void main(String a[]){
		 Map<String,Object> root =new HashMap<String,Object>();
		 root.put("name", "xiahui");
		 root.put("sex", "");
		 String temp="姓名:<#if name??>${name}</#if>/n性别:<#if sex?? && sex!='' >${name}</#if>";
		try {
			
			StringTemplateLoader st=new StringTemplateLoader();
			st.putTemplate("kkk",temp);
			
			Version version = new Version("2.3.23");
			
			Configuration config = new Configuration(version);
			config.setTemplateLoader(st);
			config.setObjectWrapper(new DefaultObjectWrapper(version));

			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			// 否则会出现乱码
			Template template = config.getTemplate("kkk", "UTF-8");
			// 合并数据模型与模板
			StringWriter writer = new StringWriter();   
			template.process(root, writer);
			writer.flush();
			writer.close();
			
			System.out.println(writer.toString());       
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
}
