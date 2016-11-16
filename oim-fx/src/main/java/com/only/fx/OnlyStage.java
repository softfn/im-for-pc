/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.fx;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Only
 */
public class OnlyStage extends Stage implements SuperStage{

    public OnlyStage() {
        this(StageStyle.TRANSPARENT);
    }

    public OnlyStage(StageStyle style) {
        super(style);
    }

}
