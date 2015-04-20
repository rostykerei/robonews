package io.robonews.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "channel_picture", uniqueConstraints = @UniqueConstraint(columnNames = "channelId"))
public class ChannelPicture implements Serializable {


    @Id
    @OneToOne
    @JoinColumn(name = "channelId", referencedColumnName = "id", nullable = false)
    private Channel channel;

    @Lob
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
