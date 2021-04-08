package com.teaminversion.envisionbuddy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONProcessActivity {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("htmlID")
    @Expose
    private String htmlID;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("gltf_url")
    @Expose
    private String gltfUrl;
    @SerializedName("bin_url")
    @Expose
    private String binUrl;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("png_url")
    @Expose
    private String pngUrl;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHtmlID() {
        return htmlID;
    }

    public void setHtmlID(String htmlID) {
        this.htmlID = htmlID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getGltfUrl() {
        return gltfUrl;
    }

    public void setGltfUrl(String gltfUrl) {
        this.gltfUrl = gltfUrl;
    }

    public String getBinUrl() {
        return binUrl;
    }

    public void setBinUrl(String binUrl) {
        this.binUrl = binUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPngUrl() {
        return pngUrl;
    }

    public void setPngUrl(String pngUrl) {
        this.pngUrl = pngUrl;
    }
}