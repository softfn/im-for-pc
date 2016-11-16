package com.only.fx;

import java.util.concurrent.CopyOnWriteArraySet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Vertical box with 3 small buttons for window close, minimize and maximize.
 */
public class TitlePane extends HBox {

    Button minButton = new Button();
    Button maxButton = new Button();
    Button closeButton = new Button();
    Move move;
    CopyOnWriteArraySet<EventHandler<ActionEvent>> closeActionSet = new CopyOnWriteArraySet<EventHandler<ActionEvent>>();
    CopyOnWriteArraySet<EventHandler<ActionEvent>> iconifiedActionSet = new CopyOnWriteArraySet<EventHandler<ActionEvent>>();

    boolean isAllowMaximized = true;
    Stage stage;

    public TitlePane(Move move, final Stage stage, int style) {
        // super(4);
        this.stage = stage;
        this.setAlignment(Pos.TOP_RIGHT);
        this.move = move;
        this.setBackground(Background.EMPTY);
        this.setPickOnBounds(false);//面板不参与计算边界，透明区域无鼠标事件
        closeButton.setPrefWidth(30);
        closeButton.setPrefHeight(27);
        closeButton.setId("WindowClose");
        closeButton.setOnAction((ActionEvent actionEvent) -> {
            if (!closeActionSet.isEmpty()) {
                for (EventHandler<ActionEvent> e : closeActionSet) {
                    e.handle(actionEvent);
                }
            } else {
                stage.close();
            }

            // stage.buildEventDispatchChain(tail)
            //stage.setOnCloseRequest(value);
//            stage.setEventDispatcher(new EventDispatcher(){
//                @Override
//                public Event dispatchEvent(Event event, EventDispatchChain tail) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//            });
            //Platform.exit();
        });

        minButton.setPrefWidth(30);
        minButton.setPrefHeight(27);
        minButton.setId("WindowMin");
        minButton.setOnAction((ActionEvent actionEvent) -> {
        	if (!iconifiedActionSet.isEmpty()) {
                for (EventHandler<ActionEvent> e : iconifiedActionSet) {
                    e.handle(actionEvent);
                }
            } else {
            	 stage.setIconified(true);
            }
        });

        maxButton.setPrefWidth(30);
        maxButton.setPrefHeight(27);
        maxButton.setId("WindowMax");
        maxButton.setOnAction((ActionEvent actionEvent) -> {
            toogleMaximized();
        });

        // this.setPrefSize(width, height);
        // this.setMaxSize(width, height);
        setTitlePaneStyle(style);
    }

    public void setTitlePaneStyle(int style) {
       // double width = 0;
       // double height = 27;
        getChildren().remove(minButton);
        getChildren().remove(maxButton);
        getChildren().remove(closeButton);
        if (style == 1) {
            getChildren().addAll(minButton, maxButton, closeButton);
            //width = minButton.getWidth() + maxButton.getWidth() + closeButton.getWidth();
        } else if (style == 2) {
            getChildren().addAll(minButton, closeButton);
            //width = minButton.getWidth() + closeButton.getWidth();
            isAllowMaximized = false;
        } else if (style == 3) {
            getChildren().addAll(closeButton);
            //width = closeButton.getWidth();
            isAllowMaximized = false;
        } else {
            getChildren().addAll(minButton, maxButton, closeButton);
           // width = minButton.getWidth() + maxButton.getWidth() + closeButton.getWidth();
        }
    }

    public void toogleMaximized() {
        if (isAllowMaximized) {
            move.toogleMaximized();
            if (move.isMaximized()) {
                maxButton.setId("WindowRestore");
            } else {
                maxButton.setId("WindowMax");
            }
        } else {

        }

    }
    
    public void addOnCloseAction(EventHandler<ActionEvent> e) {
    	closeActionSet.add(e);
    }
    
    public void addIconifiedAction(EventHandler<ActionEvent> e) {
    	iconifiedActionSet.add(e);
    }
}
