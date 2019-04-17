package com.eiisys.ipcc.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ipcc_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 是否是主账号(0非主账号 1主账号)
     */
    @Column(name = "is_admin")
    private Byte isAdmin;

    /**
     * 员工邮箱
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 分组id
     */
    @Column(name = "group_id")
    private Short groupId;

    /**
     * 公司id
     */
    @Column(name = "company_id")
    private Integer companyId;

    /**
     * 员工姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 性别(1男2女)
     */
    private Byte gender;

    /**
     * saas同步id
     */
    private Integer id6d;

    /**
     * 客服头像路径
     */
    @Column(name = "portrait_path")
    private String portraitPath;

    /**
     * 过期时间
     */
    @Column(name = "expiration_time")
    private Date expirationTime;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 坐席号码
     */
    @Column(name = "attendant_number")
    private String attendantNumber;

    /**
     * 坐席工号
     */
    @Column(name = "agent_id")
    private Integer agentId;

    /**
     * 坐席点好
     */
    @Column(name = "agent_phone")
    private String agentPhone;

    /**
     * 坐席密码
     */
    @Column(name = "agent_pwd")
    private String agentPwd;

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
     * 获取是否是主账号(0非主账号 1主账号)
     *
     * @return is_admin - 是否是主账号(0非主账号 1主账号)
     */
    public Byte getIsAdmin() {
        return isAdmin;
    }

    /**
     * 设置是否是主账号(0非主账号 1主账号)
     *
     * @param isAdmin 是否是主账号(0非主账号 1主账号)
     */
    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 获取员工邮箱
     *
     * @return user_id - 员工邮箱
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置员工邮箱
     *
     * @param userId 员工邮箱
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取分组id
     *
     * @return group_id - 分组id
     */
    public Short getGroupId() {
        return groupId;
    }

    /**
     * 设置分组id
     *
     * @param groupId 分组id
     */
    public void setGroupId(Short groupId) {
        this.groupId = groupId;
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
     * 获取员工姓名
     *
     * @return user_name - 员工姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置员工姓名
     *
     * @param userName 员工姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取性别(1男2女)
     *
     * @return gender - 性别(1男2女)
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * 设置性别(1男2女)
     *
     * @param gender 性别(1男2女)
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * 获取saas同步id
     *
     * @return id6d - saas同步id
     */
    public Integer getId6d() {
        return id6d;
    }

    /**
     * 设置saas同步id
     *
     * @param id6d saas同步id
     */
    public void setId6d(Integer id6d) {
        this.id6d = id6d;
    }

    /**
     * 获取客服头像路径
     *
     * @return portrait_path - 客服头像路径
     */
    public String getPortraitPath() {
        return portraitPath;
    }

    /**
     * 设置客服头像路径
     *
     * @param portraitPath 客服头像路径
     */
    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    /**
     * 获取过期时间
     *
     * @return expiration_time - 过期时间
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    /**
     * 设置过期时间
     *
     * @param expirationTime 过期时间
     */
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * 获取联系电话
     *
     * @return phone - 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取坐席号码
     *
     * @return attendant_number - 坐席号码
     */
    public String getAttendantNumber() {
        return attendantNumber;
    }

    /**
     * 设置坐席号码
     *
     * @param attendantNumber 坐席号码
     */
    public void setAttendantNumber(String attendantNumber) {
        this.attendantNumber = attendantNumber;
    }

    /**
     * 获取坐席工号
     *
     * @return agent_id - 坐席工号
     */
    public Integer getAgentId() {
        return agentId;
    }

    /**
     * 设置坐席工号
     *
     * @param agentId 坐席工号
     */
    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    /**
     * 获取坐席点好
     *
     * @return agent_phone - 坐席点好
     */
    public String getAgentPhone() {
        return agentPhone;
    }

    /**
     * 设置坐席点好
     *
     * @param agentPhone 坐席点好
     */
    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    /**
     * 获取坐席密码
     *
     * @return agent_pwd - 坐席密码
     */
    public String getAgentPwd() {
        return agentPwd;
    }

    /**
     * 设置坐席密码
     *
     * @param agentPwd 坐席密码
     */
    public void setAgentPwd(String agentPwd) {
        this.agentPwd = agentPwd;
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
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getIsAdmin() == null ? other.getIsAdmin() == null : this.getIsAdmin().equals(other.getIsAdmin()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getId6d() == null ? other.getId6d() == null : this.getId6d().equals(other.getId6d()))
            && (this.getPortraitPath() == null ? other.getPortraitPath() == null : this.getPortraitPath().equals(other.getPortraitPath()))
            && (this.getExpirationTime() == null ? other.getExpirationTime() == null : this.getExpirationTime().equals(other.getExpirationTime()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getAttendantNumber() == null ? other.getAttendantNumber() == null : this.getAttendantNumber().equals(other.getAttendantNumber()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getAgentPhone() == null ? other.getAgentPhone() == null : this.getAgentPhone().equals(other.getAgentPhone()))
            && (this.getAgentPwd() == null ? other.getAgentPwd() == null : this.getAgentPwd().equals(other.getAgentPwd()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getIsAdmin() == null) ? 0 : getIsAdmin().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getId6d() == null) ? 0 : getId6d().hashCode());
        result = prime * result + ((getPortraitPath() == null) ? 0 : getPortraitPath().hashCode());
        result = prime * result + ((getExpirationTime() == null) ? 0 : getExpirationTime().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getAttendantNumber() == null) ? 0 : getAttendantNumber().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getAgentPhone() == null) ? 0 : getAgentPhone().hashCode());
        result = prime * result + ((getAgentPwd() == null) ? 0 : getAgentPwd().hashCode());
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
        sb.append(", isAdmin=").append(isAdmin);
        sb.append(", userId=").append(userId);
        sb.append(", groupId=").append(groupId);
        sb.append(", companyId=").append(companyId);
        sb.append(", userName=").append(userName);
        sb.append(", gender=").append(gender);
        sb.append(", id6d=").append(id6d);
        sb.append(", portraitPath=").append(portraitPath);
        sb.append(", expirationTime=").append(expirationTime);
        sb.append(", phone=").append(phone);
        sb.append(", attendantNumber=").append(attendantNumber);
        sb.append(", agentId=").append(agentId);
        sb.append(", agentPhone=").append(agentPhone);
        sb.append(", agentPwd=").append(agentPwd);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}