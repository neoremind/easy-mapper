package com.baidu.unbiz.easymapper.vo;

import java.io.Serializable;

/**
 * 创意VO
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-5-2 下午10:18:13
 */
public class CreativeVo extends Creative implements Serializable {

    private static final long serialVersionUID = -5459301230182945960L;

    private byte[] binaryData;

    private String media; //UBMC对外的URL

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

}
