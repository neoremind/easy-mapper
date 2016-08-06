package com.baidu.unbiz.easymapper.vo;

/**
 * 创意
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-4-24 下午1:16:35
 */
public class Creative extends AbstractCreative<Long> {

    private static final long serialVersionUID = -6983033954812610929L;

    public Creative() {
        super();
        // 默认静态
        this.category = 0;
        this.adviewType = 0;
        this.interactiveStyle = 1;
        this.telNo = "";
        this.downloadUrl = "";
        this.appName = "";
        this.appDesc = "";
        this.appPackageSize = 0.0F;
        this.dataRate = 0;
        this.duration = 0;
        this.playTimeMonitorUrl = "";
        this.adTag = 0;
    }

    /**
     * ubmc物料mediaid
     */
    private long mcmediaid;

    /**
     * 业务端自己维护的version
     */
    private int version;

    /**
     * 物料下载URL，客户通过Api上传可以指定下载URL
     */
    private String creativeUrl;

    /**
     * 展现监测连接
     */
    private String monitorUrls;

    /**
     */
    private int category;

    /**
     */
    private Integer adviewType;

    /**
     */
    private Integer interactiveStyle;

    /**
     * 电话号码, 如400号、800号、手机号、座机号等,当互动样式选为电话直拨时必填
     */
    private String telNo;

    /**
     * 下载包地址,当互动样式选为下载地址时必填
     */
    private String downloadUrl;

    /**
     * 应用名称,当互动样式选为下载地址时必填
     */
    private String appName;

    /**
     * 应用介绍,当互动样式选为下载地址时必填
     */
    private String appDesc;

    /**
     * 应用大小，单位为MB,当互动样式选为下载地址时必填
     */
    private Float appPackageSize;

    /**
     * 视频广告的码流,单位是Kbps.
     */
    private int dataRate;

    /**
     * 广告的播放时长,单位为s.
     */
    private int duration;

    /**
     * 视频广告的播放时间监测.
     */
    private String playTimeMonitorUrl;

    /**
     * 创意标注
     */
    private int adTag;

    public long getMcmediaid() {
        return mcmediaid;
    }

    public void setMcmediaid(long mcmediaid) {
        this.mcmediaid = mcmediaid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCreativeUrl() {
        return creativeUrl;
    }

    public void setCreativeUrl(String creativeUrl) {
        this.creativeUrl = creativeUrl;
    }

    public String getMonitorUrls() {
        return monitorUrls;
    }

    public void setMonitorUrls(String monitorUrls) {
        this.monitorUrls = monitorUrls;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * @return the adviewType
     */
    public Integer getAdviewType() {
        return adviewType;
    }

    /**
     * @param adviewType the adviewType to set
     */
    public void setAdviewType(Integer adviewType) {
        this.adviewType = adviewType;
    }

    /**
     * @return the interactiveStyle
     */
    public Integer getInteractiveStyle() {
        return interactiveStyle;
    }

    /**
     * @param interactiveStyle the interactiveStyle to set
     */
    public void setInteractiveStyle(Integer interactiveStyle) {
        this.interactiveStyle = interactiveStyle;
    }

    /**
     * @return the telNo
     */
    public String getTelNo() {
        return telNo;
    }

    /**
     * @param telNo the telNo to set
     */
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    /**
     * @return the downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * @param downloadUrl the downloadUrl to set
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return the appDesc
     */
    public String getAppDesc() {
        return appDesc;
    }

    /**
     * @param appDesc the appDesc to set
     */
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    /**
     * @return the appPackageSize
     */
    public Float getAppPackageSize() {
        return appPackageSize;
    }

    /**
     * @param appPackageSize the appPackageSize to set
     */
    public void setAppPackageSize(Float appPackageSize) {
        this.appPackageSize = appPackageSize;
    }

    public int getDataRate() {
        return dataRate;
    }

    public void setDataRate(int dataRate) {
        this.dataRate = dataRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPlayTimeMonitorUrl() {
        return playTimeMonitorUrl;
    }

    public void setPlayTimeMonitorUrl(String playTimeMonitorUrl) {
        this.playTimeMonitorUrl = playTimeMonitorUrl;
    }

    public int getAdTag() {
        return adTag;
    }

    public void setAdTag(int adTag) {
        this.adTag = adTag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dspId;
        result = prime * result
                + (int) (oriCreativeId ^ (oriCreativeId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Creative)) {
            return false;
        }
        Creative other = (Creative) obj;
        if (dspId != other.dspId) {
            return false;
        }
        if (oriCreativeId != other.oriCreativeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Creative [mcmediaid=" + mcmediaid + ", version=" + version
                + ", creativeUrl=" + creativeUrl + ", monitorUrls="
                + monitorUrls + ", category=" + category + ", adviewType="
                + adviewType + ", interactiveStyle=" + (interactiveStyle == null ? "" : interactiveStyle)
                + ", telNo=" + telNo + ", downloadUrl=" + downloadUrl
                + ", appName=" + appName + ", appDesc=" + appDesc
                + ", appPackageSize=" + appPackageSize + ", mcid=" + mcid
                + ", mcversionid=" + mcversionid + ", state=" + state
                + ", dspId=" + dspId + ", type=" + type + ", targetUrl="
                + targetUrl + ", landingPage=" + landingPage + ", height="
                + height + ", width=" + width + ", oriCreativeId="
                + oriCreativeId + ", advertiserId=" + advertiserId
                + ", oriAdvertiserId=" + oriAdvertiserId + ", creativeTradeId="
                + creativeTradeId + ", oriCreativeTradeId="
                + oriCreativeTradeId + ", addtime=" + addtime + ", modtime="
                + modtime + ", frameAgreementNo=" + frameAgreementNo
                + ", confidenceLevel=" + confidenceLevel + ", beautyLevel="
                + beautyLevel + ", cheatLevel=" + cheatLevel + ", vulgarLevel="
                + vulgarLevel + ", refuseReason=" + refuseReason
                + ", auditTime=" + auditTime + ", auditPassNum=" + auditPassNum
                + ", auditRefuseNum=" + auditRefuseNum + ", id=" + id + ", dataRate=" + dataRate
                + ", duration=" + duration + ", playTimeMonitorUrl=" + playTimeMonitorUrl + ", adTag =" + adTag + "]";
    }
}
