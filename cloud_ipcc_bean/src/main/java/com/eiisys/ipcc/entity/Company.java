package com.eiisys.ipcc.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ipcc_company")
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 公司id
     */
    @Column(name = "company_id")
    private Integer companyId;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 绑定手机
     */
    private String phone;

    /**
     * 企业账号（华为）
     */
    @Column(name = "corp_id")
    private String corpId;

    /**
     * 总机号码
     */
    @Column(name = "switchboard_number")
    private String switchboardNumber;

    /**
     * 是否开通了短信
     */
    @Column(name = "is_sms")
    private Byte isSms;

    /**
     * 公司呼叫服务状态记录，（1：可用，0：不可用）
     */
    private Byte available;

    /**
     * 短信开关（0关 1开）
     */
    @Column(name = "sms_switch")
    private Byte smsSwitch;

    /**
     * 到期时间
     */
    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取公司id
     *
     * @return company_id - 公司id
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * 设置公司id
     *
     * @param companyId 公司id
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取公司名称
     *
     * @return company_name - 公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置公司名称
     *
     * @param companyName 公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取联系人
     *
     * @return contact - 联系人
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系人
     *
     * @param contact 联系人
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 获取绑定手机
     *
     * @return phone - 绑定手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置绑定手机
     *
     * @param phone 绑定手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取企业账号（华为）
     *
     * @return corp_id - 企业账号（华为）
     */
    public String getCorpId() {
        return corpId;
    }

    /**
     * 设置企业账号（华为）
     *
     * @param corpId 企业账号（华为）
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    /**
     * 获取总机号码
     *
     * @return switchboard_number - 总机号码
     */
    public String getSwitchboardNumber() {
        return switchboardNumber;
    }

    /**
     * 设置总机号码
     *
     * @param switchboardNumber 总机号码
     */
    public void setSwitchboardNumber(String switchboardNumber) {
        this.switchboardNumber = switchboardNumber;
    }

    /**
     * 获取是否开通了短信
     *
     * @return is_sms - 是否开通了短信
     */
    public Byte getIsSms() {
        return isSms;
    }

    /**
     * 设置是否开通了短信
     *
     * @param isSms 是否开通了短信
     */
    public void setIsSms(Byte isSms) {
        this.isSms = isSms;
    }

    /**
     * 获取公司呼叫服务状态记录，（1：可用，0：不可用）
     *
     * @return available - 公司呼叫服务状态记录，（1：可用，0：不可用）
     */
    public Byte getAvailable() {
        return available;
    }

    /**
     * 设置公司呼叫服务状态记录，（1：可用，0：不可用）
     *
     * @param available 公司呼叫服务状态记录，（1：可用，0：不可用）
     */
    public void setAvailable(Byte available) {
        this.available = available;
    }

    /**
     * 获取短信开关（0关 1开）
     *
     * @return sms_switch - 短信开关（0关 1开）
     */
    public Byte getSmsSwitch() {
        return smsSwitch;
    }

    /**
     * 设置短信开关（0关 1开）
     *
     * @param smsSwitch 短信开关（0关 1开）
     */
    public void setSmsSwitch(Byte smsSwitch) {
        this.smsSwitch = smsSwitch;
    }

    /**
     * 获取到期时间
     *
     * @return expiration_time - 到期时间
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    /**
     * 设置到期时间
     *
     * @param expirationTime 到期时间
     */
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * @return created_at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return updated_at
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return deleted_at
     */
    public Date getDeletedAt() {
        return deletedAt;
    }

    /**
     * @param deletedAt
     */
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Company other = (Company) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getContact() == null ? other.getContact() == null : this.getContact().equals(other.getContact()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getCorpId() == null ? other.getCorpId() == null : this.getCorpId().equals(other.getCorpId()))
            && (this.getSwitchboardNumber() == null ? other.getSwitchboardNumber() == null : this.getSwitchboardNumber().equals(other.getSwitchboardNumber()))
            && (this.getIsSms() == null ? other.getIsSms() == null : this.getIsSms().equals(other.getIsSms()))
            && (this.getAvailable() == null ? other.getAvailable() == null : this.getAvailable().equals(other.getAvailable()))
            && (this.getSmsSwitch() == null ? other.getSmsSwitch() == null : this.getSmsSwitch().equals(other.getSmsSwitch()))
            && (this.getExpirationTime() == null ? other.getExpirationTime() == null : this.getExpirationTime().equals(other.getExpirationTime()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getContact() == null) ? 0 : getContact().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getCorpId() == null) ? 0 : getCorpId().hashCode());
        result = prime * result + ((getSwitchboardNumber() == null) ? 0 : getSwitchboardNumber().hashCode());
        result = prime * result + ((getIsSms() == null) ? 0 : getIsSms().hashCode());
        result = prime * result + ((getAvailable() == null) ? 0 : getAvailable().hashCode());
        result = prime * result + ((getSmsSwitch() == null) ? 0 : getSmsSwitch().hashCode());
        result = prime * result + ((getExpirationTime() == null) ? 0 : getExpirationTime().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", companyId=").append(companyId);
        sb.append(", companyName=").append(companyName);
        sb.append(", contact=").append(contact);
        sb.append(", phone=").append(phone);
        sb.append(", corpId=").append(corpId);
        sb.append(", switchboardNumber=").append(switchboardNumber);
        sb.append(", isSms=").append(isSms);
        sb.append(", available=").append(available);
        sb.append(", smsSwitch=").append(smsSwitch);
        sb.append(", expirationTime=").append(expirationTime);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}