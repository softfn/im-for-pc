package com.oim.common.component;

import java.awt.Component;

import com.only.OnlySplitPane;

public class OurSplitPane extends OnlySplitPane {
	private static final long serialVersionUID = 1L;

	public OurSplitPane() {
		super();
	}

	public OurSplitPane(int newOrientation) {
		super(newOrientation);

	}

	public OurSplitPane(int newOrientation, boolean newContinuousLayout) {
		super(newOrientation, newContinuousLayout);

	}

	public OurSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
		super(newOrientation, newLeftComponent, newRightComponent);

	}

	public OurSplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent, Component newRightComponent) {
		super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);

	}

}
