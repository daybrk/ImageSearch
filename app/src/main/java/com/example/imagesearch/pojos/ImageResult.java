package com.example.imagesearch.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageResult {

    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("is_product")
    @Expose
    private String isProduct;
    @SerializedName("in_stock")
    @Expose
    private String inStock;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIsProduct() {
        return isProduct;
    }

    public void setIsProduct(String isProduct) {
        this.isProduct = isProduct;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

}
