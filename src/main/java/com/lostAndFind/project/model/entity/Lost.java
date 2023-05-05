package com.lostAndFind.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 丢失物品表
 * @TableName lost
 */
@TableName(value ="lost")
@Data
public class Lost implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * userId
     */
    private Long userId;

    /**
     * 报失名称
     */
    private String lostName;

    /**
     * 丢失时间
     */
    private Date lostDate;

    /**
     * 联系电话
     */
    private String contact;

    /**
     * 报失时间
     */
    private Date reportTime;

    /**
     * 物品描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 物品图片
     */
    private String img;

    /**
     * 赏金
     */
    private Double business;

    /**
     * 紧急程度 0-5
     */
    private Double urgent;

    /**
     * 订单状态（0进行 1完成）
     */
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        Lost other = (Lost) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getLostName() == null ? other.getLostName() == null : this.getLostName().equals(other.getLostName()))
            && (this.getLostDate() == null ? other.getLostDate() == null : this.getLostDate().equals(other.getLostDate()))
            && (this.getContact() == null ? other.getContact() == null : this.getContact().equals(other.getContact()))
            && (this.getReportTime() == null ? other.getReportTime() == null : this.getReportTime().equals(other.getReportTime()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getImg() == null ? other.getImg() == null : this.getImg().equals(other.getImg()))
            && (this.getBusiness() == null ? other.getBusiness() == null : this.getBusiness().equals(other.getBusiness()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getLostName() == null) ? 0 : getLostName().hashCode());
        result = prime * result + ((getLostDate() == null) ? 0 : getLostDate().hashCode());
        result = prime * result + ((getContact() == null) ? 0 : getContact().hashCode());
        result = prime * result + ((getReportTime() == null) ? 0 : getReportTime().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getImg() == null) ? 0 : getImg().hashCode());
        result = prime * result + ((getBusiness() == null) ? 0 : getBusiness().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", lostName=").append(lostName);
        sb.append(", lostDate=").append(lostDate);
        sb.append(", contact=").append(contact);
        sb.append(", reportTime=").append(reportTime);
        sb.append(", description=").append(description);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", img=").append(img);
        sb.append(", business=").append(business);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}