package com.lostAndFind.project.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName tag_rel
 */
@Data
public class TagRel implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 0为lost，1为pick
     */
    private String type;

    /**
     * 物品id
     */
    private Long thingid;

    /**
     * 标签id
     */
    private Long tagid;

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
        TagRel other = (TagRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getThingid() == null ? other.getThingid() == null : this.getThingid().equals(other.getThingid()))
            && (this.getTagid() == null ? other.getTagid() == null : this.getTagid().equals(other.getTagid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getThingid() == null) ? 0 : getThingid().hashCode());
        result = prime * result + ((getTagid() == null) ? 0 : getTagid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", thingid=").append(thingid);
        sb.append(", tagid=").append(tagid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}