package com.smart.framework;

import android.view.View;

public interface SmartActivityHandler {

	/**
	 * @return <b>R.layout.layout_id</b> <br>
	 * 
	 *This method will set the layout of the activity from R class. <br>
	 *<code><pre>
	 *public int setLayoutId() { 
	 *	return R.layout.main; 
	 *}</pre></code>
	 */
	public int setLayoutId();

	/**
	 * @return <b>new View()</b> <br>
	 * 
	 *This method will set the custom view for the activity. <br>
	 */
	public View setLayoutView();

	/**
	 * This method will set layout of activity header. 
	 * @return <b>R.layout.layout_id</b>
	 */
	public int setHeaderLayoutId();

	/**
	 * @return <b>new View()</b> <br>
	 * 
	 *This method will set the custom header view for the activity. <br>
	 */
//	public View setHeaderLayoutView();

	/**
	 * This method will set layout of activity footer. 
	 * @return <b>R.layout.layout_id</b>
	 */
	public int setFooterLayoutId();

	/**
	 * @return <b>new View()</b> <br>
	 * 
	 *This method will set the custom footer view for the activity. <br>
	 */
//	public View setFooterLayoutView();

	/**
	 * This is the first method which is being called from <b>OnCreate()</b> method.<br>
	 * For keeping the activity code organized, the whole activity development process is divided into three different parts.<br>
	 * <li><b>initComponents</b><br>
	 * This method will include all the "findViewByIds" and initial code for each view which is to be displayed on activity.</li>
	 * <li><b>prepareViews</b><br>
	 * This method will include all the view preparation code like, setting some views visible or invisible, setting text to header etc.</li>
	 * <li><b>setActionListeners</b><br>
	 * This method will include all actionListeners to be set to the views used in activity.<br>
	 * <b>Note </b>: All the action listeners' should only be implemented on class level.</li>  
	 */
	public void initComponents();

	/**
	 * This is the second method which is being called from <b>OnCreate()</b> method.<br>
	 * For keeping the activity code organized, the whole activity development process is divided into three different parts.<br>
	 * <li>initComponents<br>
	 * This method will include all the "findViewByIds" and initial code for each view which is to be displayed on activity.</li>
	 * <li>prepareViews<br>
	 * This method will include all the view preparation code like, setting some views visible or invisible, setting text to header etc.</li>
	 * <li>setActionListeners<br>
	 * This method will include all actionListeners to be set to the views used in activity.<br>
	 * <b>Note </b>: All the action listeners' should only be implemented on class level.</li>  
	 */
	public void prepareViews();

	/**
	 * This is the third and last method which is being called from <b>OnCreate()</b> method.<br>
	 * For keeping the activity code organized, the whole activity development process is divided into three different parts.<br>
	 * <li>initComponents<br>
	 * This method will include all the "findViewByIds" and initial code for each view which is to be displayed on activity.</li>
	 * <li>prepareViews<br>
	 * This method will include all the view preparation code like, setting some views visible or invisible, setting text to header etc.</li>
	 * <li>setActionListeners<br>
	 * This method will include all actionListeners to be set to the views used in activity.<br>
	 * <b>Note </b>: All the action listeners' should only be implemented on class level.</li>  
	 */
	public void setActionListeners();
	
	public void loadHeaderComponents();
}
