package com.only.common;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class StackBlurFilter implements BufferedImageOp {
	
	private final int radius;
	private final int iterations;

	public StackBlurFilter(int radius, int iterations) {
		this.radius = Math.max(1, radius);
		this.iterations = Math.max(1, iterations);
	}

	public int getEffectiveRadius() {
		return iterations * radius;
	}

	public int getRadius() {
		return radius;
	}

	public int getIterations() {
		return iterations;
	}

	public Rectangle2D getBounds2D(BufferedImage src) {
		return new Rectangle(0, 0, src.getWidth(), src.getHeight());
	}

	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		return (Point2D) srcPt.clone();
	}

	public RenderingHints getRenderingHints() {
		return null;
	}

	public BufferedImage filter(BufferedImage src, BufferedImage dst) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] srcPixels = new int[width * height];
		int[] dstPixels = new int[width * height];
		dst = dst == null ? createCompatibleDestImage(src, null) : dst;
		getPixels(src, 0, 0, width, height, srcPixels);

		for (int i = 0; i < iterations; i++) {
			blur(srcPixels, dstPixels, width, height, radius);
			blur(dstPixels, srcPixels, height, width, radius);
		}

		setPixels(dst, 0, 0, width, height, srcPixels);
		return dst;
	}

	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstColorModel) {
		dstColorModel = dstColorModel == null ? src.getColorModel() : dstColorModel;
		return new BufferedImage(dstColorModel, dstColorModel.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstColorModel.isAlphaPremultiplied(), null);
	}

	private static void blur(int[] srcPixels, int[] dstPixels, int width, int height, int radius) {
		final int windowSize = radius * 2 + 1;
		final int radiusPlusOne = radius + 1;
		int[] sumLookupTable = new int[256 * windowSize];
		int[] indexLookupTable = new int[radiusPlusOne];
		int sumAlpha;
		int sumRed;
		int sumGreen;
		int sumBlue;
		int srcIndex = 0;
		int dstIndex;
		int pixel;

		for (int i = 0; i < sumLookupTable.length; i++) {
			sumLookupTable[i] = i / windowSize;
		}

		if (radius < width) {
			for (int i = 0; i < indexLookupTable.length; i++) {
				indexLookupTable[i] = i;
			}
		} else {
			for (int i = 0; i < width; i++) {
				indexLookupTable[i] = i;
			}

			for (int i = width; i < indexLookupTable.length; i++) {
				indexLookupTable[i] = width - 1;
			}
		}

		for (int y = 0; y < height; y++) {
			sumAlpha = sumRed = sumGreen = sumBlue = 0;
			dstIndex = y;
			pixel = srcPixels[srcIndex];
			sumAlpha += radiusPlusOne * ((pixel >> 24) & 0xFF);
			sumRed += radiusPlusOne * ((pixel >> 16) & 0xFF);
			sumGreen += radiusPlusOne * ((pixel >> 8) & 0xFF);
			sumBlue += radiusPlusOne * (pixel & 0xFF);

			for (int i = 1; i <= radius; i++) {
				pixel = srcPixels[srcIndex + indexLookupTable[i]];
				sumAlpha += (pixel >> 24) & 0xFF;
				sumRed += (pixel >> 16) & 0xFF;
				sumGreen += (pixel >> 8) & 0xFF;
				sumBlue += pixel & 0xFF;
			}

			for (int x = 0; x < width; x++) {
				dstPixels[dstIndex] = sumLookupTable[sumAlpha] << 24 | sumLookupTable[sumRed] << 16 | sumLookupTable[sumGreen] << 8 | sumLookupTable[sumBlue];
				dstIndex += height;
				int nextPixelIndex = x + radiusPlusOne;
				int previousPixelIndex = Math.max(0, x - radius);

				if (nextPixelIndex >= width) {
					nextPixelIndex = width - 1;
				}

				int nextPixel = srcPixels[srcIndex + nextPixelIndex];
				int previousPixel = srcPixels[srcIndex + previousPixelIndex];
				sumAlpha += (nextPixel >> 24) & 0xFF;
				sumAlpha -= (previousPixel >> 24) & 0xFF;
				sumRed += (nextPixel >> 16) & 0xFF;
				sumRed -= (previousPixel >> 16) & 0xFF;
				sumGreen += (nextPixel >> 8) & 0xFF;
				sumGreen -= (previousPixel >> 8) & 0xFF;
				sumBlue += nextPixel & 0xFF;
				sumBlue -= previousPixel & 0xFF;
			}

			srcIndex += width;
		}
	}

	private static int[] getPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
		if (w == 0 || h == 0) {
			return new int[0];
		}

		if (pixels == null) {
			pixels = new int[w * h];
		} else if (pixels.length < w * h) {
			throw new IllegalArgumentException("pixels array must have a length" + " >= w*h");
		}

		int imageType = img.getType();

		if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
			Raster raster = img.getRaster();
			return (int[]) raster.getDataElements(x, y, w, h, pixels);
		}

		return img.getRGB(x, y, w, h, pixels, 0, w);
	}

	private static void setPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
		if (pixels == null || w == 0 || h == 0) {
			return;
		} else if (pixels.length < w * h) {
			throw new IllegalArgumentException("pixels array must have a length" + " >= w*h");
		}

		int imageType = img.getType();

		if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
			WritableRaster raster = img.getRaster();
			raster.setDataElements(x, y, w, h, pixels);
		} else {
			img.setRGB(x, y, w, h, pixels, 0, w);
		}
	}
}