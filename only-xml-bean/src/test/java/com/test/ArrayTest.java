package com.test;

import java.lang.reflect.Array;

public class ArrayTest {

	public static void main(String[] args) {
		int[][] i = { { 1, 2, 3, 4 }, { 2, 2, 1 }, { 3, 2, 1, 23 } };
		// System.out.println(i);
		test(i);

	}

	public static void test(Object o) {
		if (o.getClass().isArray()) {
			int b = Array.getLength(o);
			for (int i = 0; i < b; i++) {
				Object v = Array.get(o, i);
				if (v.getClass().isArray()) {
					test(v);
				} else {
					System.out.println(v);
				}
			}
		}
	}
}
