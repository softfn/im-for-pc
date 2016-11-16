package com.oim.core.common.app.view;

import java.util.List;

import com.oim.core.bean.Group;
import com.oim.core.bean.GroupCategory;

/**
 * 描述： 添加好友或者加入群显示窗口
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午10:42:19
 * @version 0.0.1
 */
public interface AddGroupView extends View{


	public boolean isShowing() ;
	
	public void showWaiting(boolean show) ;

	public void set(Group group, List<GroupCategory> groupCategoryList);
}
