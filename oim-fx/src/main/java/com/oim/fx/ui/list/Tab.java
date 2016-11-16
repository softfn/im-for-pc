/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui.list;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;

/**
 *
 * @author XiaHui
 */
public class Tab extends StackPane {

    ImageView imageView = new ImageView();
    Button imageButton = new Button();

    private Image normalImage = null;
    private Image hoverImage = null;
    private Image selectedImage = null;
    private boolean selected = false;

    private boolean mouseEntered = false;

    public Tab() {
        initComponent();
        iniEvent();
    }

    public Tab(Image normalImage, Image hoverImage, Image selectedImage) {
        this.normalImage = normalImage;
        this.hoverImage = hoverImage;
        this.selectedImage = selectedImage;
        initComponent();
        iniEvent();
        updateImage();
    }

    private void initComponent() {
        this.getChildren().add(imageView);
        imageButton.setBackground(Background.EMPTY);
        imageButton.setBorder(Border.EMPTY);
        imageButton.setFocusTraversable(false);
        imageButton.setGraphic(imageView);
//        imageButton.setOnAction(new EventHandler() {
//            @Override
//            public void handle(Event event) {
//
//            }
//        });
    }

    public void setOnAction(EventHandler<ActionEvent> value) {
        imageButton.setOnAction(value);
    }

    private void iniEvent() {
        this.setOnMouseEntered((Event event) -> {
            mouseEntered = true;
            updateImage();
        });
        this.setOnMouseExited((Event event) -> {
            mouseEntered = false;
            updateImage();
        });
    }

//    private void mouseEntered() {
//        if (!this.isSelected()) {
//            imageView.setImage(hoverImage);
//        }
//    }
//
//    private void mouseExited() {
//        if (!this.isSelected()) {
//            imageView.setImage(normalImage);
//        }
//    }

    private void updateImage() {

        if (!this.isSelected()) {
            if (mouseEntered) {
                imageView.setImage(hoverImage);
            } else {
                imageView.setImage(normalImage);
            }

        } else {
            imageView.setImage(selectedImage);
        }
    }

    /**
     * @return the normalImage
     */
    public Image getNormalImage() {
        return normalImage;
    }

    /**
     * @param normalImage the normalImage to set
     */
    public void setNormalImage(Image normalImage) {
        this.normalImage = normalImage;
        updateImage();
    }

    /**
     * @return the hoverImage
     */
    public Image getHoverImage() {
        return hoverImage;
    }

    /**
     * @param hoverImage the hoverImage to set
     */
    public void setHoverImage(Image hoverImage) {
        this.hoverImage = hoverImage;
        updateImage();
    }

    /**
     * @return the selectedImage
     */
    public Image getSelectedImage() {
        return selectedImage;
    }

    /**
     * @param selectedImage the selectedImage to set
     */
    public void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
        updateImage();
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        updateImage();
    }

}
