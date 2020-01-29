package com.yanytska.mobileapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fare {
    @SerializedName("fareId")
    @Expose
    private final int id;

    @SerializedName("uuid")
    @Expose
    private final String uuid;

    @SerializedName("status")
    @Expose
    private final String type;

    @SerializedName("imageUrl")
    @Expose
    private final String image;

    @SerializedName("createdAt")
    @Expose
    private final String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private final String updatedAt;

    public Fare(final int id, final String uuid, final String type, String image,
                final String createdAt, final String updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.type = type;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Fare(final String uuid,
                final String type, final String image) {
        this.id = 0;
        this.uuid = uuid;
        this.type = type;
        this.image = image;
        this.createdAt = "";
        this.updatedAt = "";
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
