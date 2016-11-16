package com.only.chart;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;

public class Pie3D extends Chart3DBean {
	
	private static final long serialVersionUID = -7777404623107653308L;
	private double startAngle;
	private double extentAngle;
	private double percent;
	private Number rawData;

	public Pie3D(String name, double startAngle, double extentAngle) {
		this.name = name;
		this.startAngle = startAngle;
		this.extentAngle = extentAngle;
		this.textColor = Color.BLACK;
		this.legendBounds = new Rectangle();
		this.textPosition = new Point();
		this.setArea(new Area());
	}

	public Color getPieColor() {
		return super.getColor();
	}

	public void setPieColor(Color pieColor) {
		super.setColor(pieColor);
	}

	public Number getRawData() {
		return rawData;
	}

	public void setRawData(Number rawData) {
		this.rawData = rawData;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getStartAngle() {
		return startAngle;
	}

	public double getExtentAngle() {
		return extentAngle;
	}
}