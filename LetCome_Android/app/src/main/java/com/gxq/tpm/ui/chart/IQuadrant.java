package com.gxq.tpm.ui.chart;

public interface IQuadrant {
	static final float DEFAULT_PADDING 	= 0f;
	
	void setPaddingTop(float value);

	void setPaddingLeft(float value);

	void setPaddingBottom(float value);

	void setPaddingRight(float value);

	float getQuadrantStartX();

	float getQuadrantStartY();
	
	float getQuadrantWidth();

	float getQuadrantHeight();
	
	float getQuadrantEndX();

	float getQuadrantEndY();
	
	float getQuadrantPaddingStartX();

	float getQuadrantPaddingEndX();

	float getQuadrantPaddingStartY();

	float getQuadrantPaddingEndY();

	float getQuadrantPaddingWidth();

	float getQuadrantPaddingHeight();
}
