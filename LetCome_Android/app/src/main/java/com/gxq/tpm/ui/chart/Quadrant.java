package com.gxq.tpm.ui.chart;

public abstract class Quadrant implements IQuadrant {

	protected GridChart inChart;

	public Quadrant(GridChart inChart) {
		this.inChart = inChart;

	}

	protected float paddingTop = DEFAULT_PADDING;
	protected float paddingLeft = DEFAULT_PADDING;
	protected float paddingBottom = DEFAULT_PADDING;
	protected float paddingRight = DEFAULT_PADDING;

	/**
	 * @return the paddingTop
	 */
	public float getPaddingTop() {
		return paddingTop;
	}

	/**
	 * @param paddingTop
	 *            the paddingTop to set
	 */
	public void setPaddingTop(float quadrantPaddingTop) {
		this.paddingTop = quadrantPaddingTop;
	}

	/**
	 * @return the paddingLeft
	 */
	public float getPaddingLeft() {
		return paddingLeft;
	}

	/**
	 * @param paddingLeft
	 *            the paddingLeft to set
	 */
	public void setPaddingLeft(float quadrantPaddingLeft) {
		this.paddingLeft = quadrantPaddingLeft;
	}

	/**
	 * @return the paddingBottom
	 */
	public float getPaddingBottom() {
		return paddingBottom;
	}

	/**
	 * @param paddingBottom
	 *            the paddingBottom to set
	 */
	public void setPaddingBottom(float quadrantPaddingBottom) {
		this.paddingBottom = quadrantPaddingBottom;
	}

	/**
	 * @return the paddingRight
	 */
	public float getPaddingRight() {
		return paddingRight;
	}

	/**
	 * @param paddingRight
	 *            the paddingRight to set
	 */
	public void setPaddingRight(float quadrantPaddingRight) {
		this.paddingRight = quadrantPaddingRight;
	}

	/**
	 * @param padding
	 *            the paddingTop paddingBottom
	 *            paddingLeft paddingRight to set
	 *
	 */
	public void setQuadrantPadding(float padding) {
		this.paddingTop = padding;
		this.paddingLeft = padding;
		this.paddingBottom = padding;
		this.paddingRight = padding;
	}

	/**
	 * @param topnbottom
	 *            the paddingTop paddingBottom to set
	 * @param leftnright
	 *            the paddingLeft paddingRight to set
	 *
	 */
	public void setQuadrantPadding(float topnbottom, float leftnright) {
		this.paddingTop = topnbottom;
		this.paddingLeft = leftnright;
		this.paddingBottom = topnbottom;
		this.paddingRight = leftnright;
	}

	/**
	 * @param top
	 *            the paddingTop to set
	 * @param right
	 *            the paddingLeft to set
	 * @param bottom
	 *            the paddingBottom to set
	 * @param left
	 *            the paddingRight to set
	 *
	 */
	public void setQuadrantPadding(float top, float right, float bottom,
			float left) {
		this.paddingTop = top;
		this.paddingLeft = right;
		this.paddingBottom = bottom;
		this.paddingRight = left;
	}
	
	public float getQuadrantEndX(){
		return getQuadrantStartX() + getQuadrantWidth();
	}

	public float getQuadrantEndY(){
		return getQuadrantStartY() + getQuadrantHeight();
	}

	public float getQuadrantPaddingStartX() {
		return getQuadrantStartX() + paddingLeft;
	}

	public float getQuadrantPaddingEndX() {
		return getQuadrantEndX() - paddingRight;
	}

	public float getQuadrantPaddingStartY() {
		return getQuadrantStartY() + paddingTop;
	}

	public float getQuadrantPaddingEndY() {
		return getQuadrantEndY() - paddingBottom;
	}

	public float getQuadrantPaddingWidth() {
		return getQuadrantWidth() - paddingLeft
				- paddingRight;
	}

	public float getQuadrantPaddingHeight() {
		return getQuadrantHeight() - paddingTop
				- paddingBottom;
	}

}
