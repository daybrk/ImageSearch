package com.example.imagesearch.pojos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestedSearches {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("chips")
    @Expose
    private String chips;
    @SerializedName("serpapi_link")
    @Expose
    private String serpapiLink;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getChips() {
        return chips;
    }

    public void setChips(String chips) {
        this.chips = chips;
    }

    public String getSerpapiLink() {
        return serpapiLink;
    }

    public void setSerpapiLink(String serpapiLink) {
        this.serpapiLink = serpapiLink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
