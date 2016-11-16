/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.net.action;

import javax.xml.crypto.Data;

import com.only.net.data.action.DataBackAction;

/**
 * 
 * @author XiaHui
 */
public interface DataMonitor {

	void write(Data data);

	void write(Data data, DataBackAction dataAction);

}
