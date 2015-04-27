/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "channel_picture", uniqueConstraints = @UniqueConstraint(columnNames = "channelId"))
public class ChannelPicture implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "channelId", referencedColumnName = "id", nullable = false)
    private Channel channel;

    @NotNull
    @Column(name = "typeId")
    private int type;

    @Lob
    @NotNull
    @Column( name = "picture" )
    private byte[] picture;

    @Version
    @Column(name = "version")
    private long version;


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Image.Type getType() {
        return Image.Type.getType(type);
    }

    public void setType(Image.Type type) {
        this.type = type.getId();
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
