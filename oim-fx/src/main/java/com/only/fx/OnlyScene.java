/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.fx;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;

/**
 *
 * @author XiaHui
 */
public class OnlyScene extends Scene {

    public OnlyScene(Parent root) {
        super(root);
        initOnlyScene();
    }

    public OnlyScene(Parent root, double width, double height) {
        super(root, width, height);
        initOnlyScene();
    }

    public OnlyScene(@NamedArg("root") Parent root, @NamedArg(value = "fill", defaultValue = "WHITE") Paint fill) {
        super(root, fill);
        initOnlyScene();
    }

    public OnlyScene(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height,
            @NamedArg(value = "fill", defaultValue = "WHITE") Paint fill) {
        super(root, width, height);
        initOnlyScene();
    }

    public OnlyScene(Parent root, double width, double height, @NamedArg("depthBuffer") boolean depthBuffer) {
        super(root, width, height, depthBuffer);
        initOnlyScene();
    }

    public OnlyScene(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);
        initOnlyScene();
    }

    private void initOnlyScene() {
        this.getStylesheets().add(this.getClass().getResource("/com/only/common/css/only.css").toString());
        Parent root = this.getRoot();

        BorderPane borderPane = new BorderPane();
        this.setRoot(borderPane);
        borderPane.setBackground(Background.EMPTY);
        borderPane.setCenter(root);
    }
}
