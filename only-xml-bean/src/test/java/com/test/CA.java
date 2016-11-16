package com.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.support.DefaultConversionService;

public class CA {

	public static void main(String[] args) {
		DefaultConversionService conversionService = new DefaultConversionService();
		List<String> a1l = new ArrayList<String>();
		a1l.add("123");
		a1l.add("222");
		a1l.add("123");
		a1l.add("222");
		
		List<String> a2l = new ArrayList<String>();
		a2l.add("321");
		a2l.add("123");
		a2l.add("321");
		a2l.add("123");
		
		List<List<String>> aa1l = new ArrayList<List<String>>();
		aa1l.add(a1l);
		aa1l.add(a2l);
		
		List<List<List<String>>> st = new ArrayList<List<List<String>>>();
		st.add(aa1l);
		String[][][] sa = conversionService.convert(st, String[][][].class);
		System.out.println(sa);
	}

}
