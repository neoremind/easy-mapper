package com.baidu.unbiz.easymapper.vo;

import java.util.Date;

/**
 * @author zhangxu
 */
public class AbstractCreative<KEY extends Number> extends
        BaseObject<KEY> implements UBMCGetable {

    /**
     *
     */
    protected static final long serialVersionUID = -4025356052316263961L;

    public AbstractCreative() {
        // ignore
    }

    public AbstractCreative(KEY creativeId, long mcid, int mcversionid,
                            int state, int dspId, int type, String targetUrl,
                            String landingPage, int height, int width, long oriCreativeId,
                            long oriAdvertiserId, int creativeTradeId, int oriCreativeTradeId,
                            Date addtime, Date modtime, String frameAgreementNo) {
        super();
        this.id = creativeId;
        this.mcid = mcid;
        this.mcversionid = mcversionid;
        this.state = state;
        this.dspId = dspId;
        this.type = type;
        this.targetUrl = targetUrl;
        this.landingPage = landingPage;
        this.height = height;
        this.width = width;
        this.oriCreativeId = oriCreativeId;
        this.oriAdvertiserId = oriAdvertiserId;
        this.creativeTradeId = creativeTradeId;
        this.oriCreativeTradeId = oriCreativeTradeId;
        this.addtime = addtime;
        this.modtime = modtime;
        this.frameAgreementNo = frameAgreementNo;
    }

    /**
     * ubmc物料id
     */
    protected long mcid;

    /**
     * ubmc物料version
     */
    protected int mcversionid;

    /**
     * 状态
     */
    protected int state;

    /**
     * DSP ID
     */
    protected int dspId;

    /**
     * 类型
     */
    protected int type = -1;

    /**
     * 物料下载URL，客户通过Api上传可以指定下载URL
     */
    // @Column(CreativeColumns.creativeUrl)
    // protected String creativeUrl;

    /**
     * targetUrl
     */
    protected String targetUrl;

    /**
     * 目标连接
     */
    protected String landingPage;

    /**
     * 展现监测连接
     */
    // @Column(CreativeColumns.monitorUrls)
    // protected String monitorUrls;

    /**
     * 高
     */
    protected int height;

    /**
     * 宽
     */
    protected int width;

    /**
     * dsp指定的物料ID,唯一不重复
     */
    protected long oriCreativeId;

    /**
     * 广告主id
     */
    protected int advertiserId;

    /**
     * dsp指定的广告主id
     */
    protected long oriAdvertiserId;

    /**
     * 业务系统设置的物料广告行业
     */
    protected int creativeTradeId;

    /**
     * dsp指定的物料广告行业
     */
    protected int oriCreativeTradeId;

    /**
     * 添加时间
     */
    protected Date addtime;

    /**
     * 修改时间
     */
    protected Date modtime;

    protected String frameAgreementNo;

    protected Integer confidenceLevel = 0;

    protected Integer beautyLevel = 0;

    protected Integer cheatLevel = 0;

    protected Integer vulgarLevel = 0;

    protected String refuseReason;

    protected Date auditTime;

    protected int auditPassNum;

    protected int auditRefuseNum;

    public long getMcid() {
        return mcid;
    }

    public void setMcid(long mcid) {
        this.mcid = mcid;
    }

    public int getMcversionid() {
        return mcversionid;
    }

    public void setMcversionid(int mcversionid) {
        this.mcversionid = mcversionid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDspId() {
        return dspId;
    }

    public void setDspId(int dspId) {
        this.dspId = dspId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    // public String getMonitorUrls() {
    // return monitorUrls;
    // }
    //
    // public void setMonitorUrls(String monitorUrls) {
    // this.monitorUrls = monitorUrls;
    // }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public long getOriCreativeId() {
        return oriCreativeId;
    }

    public void setOriCreativeId(long oriCreativeId) {
        this.oriCreativeId = oriCreativeId;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public long getOriAdvertiserId() {
        return oriAdvertiserId;
    }

    public void setOriAdvertiserId(long oriAdvertiserId) {
        this.oriAdvertiserId = oriAdvertiserId;
    }

    public int getCreativeTradeId() {
        return creativeTradeId;
    }

    public void setCreativeTradeId(int creativeTradeId) {
        this.creativeTradeId = creativeTradeId;
    }

    public int getOriCreativeTradeId() {
        return oriCreativeTradeId;
    }

    public void setOriCreativeTradeId(int oriCreativeTradeId) {
        this.oriCreativeTradeId = oriCreativeTradeId;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Date getModtime() {
        return modtime;
    }

    public void setModtime(Date modtime) {
        this.modtime = modtime;
    }

    public String getFrameAgreementNo() {
        return frameAgreementNo;
    }

    public void setFrameAgreementNo(String frameAgreementNo) {
        this.frameAgreementNo = frameAgreementNo;
    }

    public Integer getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Integer confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Integer getBeautyLevel() {
        return beautyLevel;
    }

    public void setBeautyLevel(Integer beautyLevel) {
        this.beautyLevel = beautyLevel;
    }

    public Integer getCheatLevel() {
        return cheatLevel;
    }

    public void setCheatLevel(Integer cheatLevel) {
        this.cheatLevel = cheatLevel;
    }

    public Integer getVulgarLevel() {
        return vulgarLevel;
    }

    public void setVulgarLevel(Integer vulgarLevel) {
        this.vulgarLevel = vulgarLevel;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public int getAuditPassNum() {
        return auditPassNum;
    }

    public void setAuditPassNum(int auditPassNum) {
        this.auditPassNum = auditPassNum;
    }

    public int getAuditRefuseNum() {
        return auditRefuseNum;
    }

    public void setAuditRefuseNum(int auditRefuseNum) {
        this.auditRefuseNum = auditRefuseNum;
    }

}
