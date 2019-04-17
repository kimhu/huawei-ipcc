package com.eiisys.ipcc.entity;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "ipcc_demo")
public class IpccDemo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名字
     */
    @Column(name = "demo_name")
    private String demoName;

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
     * 获取名字
     *
     * @return demo_name - 名字
     */
    public String getDemoName() {
        return demoName;
    }

    /**
     * 设置名字
     *
     * @param demoName 名字
     */
    public void setDemoName(String demoName) {
        this.demoName = demoName;
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
        IpccDemo other = (IpccDemo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDemoName() == null ? other.getDemoName() == null : this.getDemoName().equals(other.getDemoName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDemoName() == null) ? 0 : getDemoName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", demoName=").append(demoName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}